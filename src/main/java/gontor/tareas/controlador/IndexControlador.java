package gontor.tareas.controlador;

import gontor.tareas.modelo.Tarea;
import gontor.tareas.servicio.TareaServicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class IndexControlador implements Initializable {
    //Enviar informacion a la consola
    private static final Logger logger = LoggerFactory.getLogger(IndexControlador.class);

    //Inyeccion service
    @Autowired
    private TareaServicio tareaServicio;

    //Obtenemos la tabla y las columnas
    @FXML
    private TableView<Tarea> tareaTabla;
    @FXML
    private TableColumn<Tarea, Integer> idTareaColumna;
    @FXML
    private TableColumn<Tarea, String> nombreTareaColumna;
    @FXML
    private TableColumn<Tarea, String> responsableColumna;
    @FXML
    private TableColumn<Tarea, String> estatusColumna;

    @FXML
    private TextField nombreTareaInput;
    @FXML
    private TextField responsableInput;
    @FXML
    private TextField estatusInput;

    private Integer idTareaInterno;

    //Para refrescar la informacion de la tabla, usamos un Observable
    private final ObservableList<Tarea> tareaList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Para que solo se pueda seleccionar un elemento de la tabla
        tareaTabla.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        configurarColumnas();
        listarTareas();

    }
    //Configurar columnas de la tabla para cargar info de la DB
    private void configurarColumnas(){
        idTareaColumna.setCellValueFactory(new PropertyValueFactory<>("idTarea"));
        nombreTareaColumna.setCellValueFactory(new PropertyValueFactory<>("nombreTarea"));
        responsableColumna.setCellValueFactory(new PropertyValueFactory<>("responsable"));
        estatusColumna.setCellValueFactory(new PropertyValueFactory<>("estatus"));
    }

    //Para separar funciones
    private void listarTareas(){
        logger.info("Listar tareas");
        //limpiamos la lista
        tareaList.clear();
        //Obtenemos todos los objetos <Tarea> de la DB
        tareaList.addAll(tareaServicio.listarTareas());
        //Relacionar tabla con la lista
        tareaTabla.setItems(tareaList);
    }

    //Funcion para el boton de agregar
    public void agregarTarea(){
        //validacion si el input esta vacio
        if(nombreTareaInput.getText().isEmpty()){
            mostrarMensaje("Error de validacion", "Debe proporcionar una tarea");
            nombreTareaInput.requestFocus();
            return;
        }
        else {
            Tarea tarea = new Tarea();
            //Obtenemos los datos de los inputs
            recolectarDatosFormulario(tarea); //Esto puede establecer la id de la tarea ¡¡!!
            //Seteamos a null la id de la tarea para evitar conflictos en la DB por el AutoInc
            tarea.setIdTarea(null);
            //Guardamos la tarea
            tareaServicio.guardarTarea(tarea);
            mostrarMensaje("Informacion", "Tarea agregada");
            limpiarFormulario();
            listarTareas();
        }
    }

    public void modificarTarea(){
        if(idTareaInterno == null){
            mostrarMensaje("Informacion", "Debe seleccionar una tarea");
            return;
        }
        if(nombreTareaInput.getText().isEmpty()){
            mostrarMensaje("Error validacion", "Debe proporcionar una tarea");
            nombreTareaInput.requestFocus();
            return;
        }
        Tarea tarea = new Tarea();
        recolectarDatosFormulario(tarea);
        tareaServicio.guardarTarea(tarea);
        mostrarMensaje("Informacion", "Tarea modificada");
        limpiarFormulario();
        listarTareas();
    }

    public void eliminarTarea(){
        Tarea tarea = tareaTabla.getSelectionModel().getSelectedItem();

        if(idTareaInterno == null){
            mostrarMensaje("Informacion", "Debe seleccionar una tarea");
            return;
        }
        if(tarea != null){
            tareaServicio.eliminarTarea(tarea);
            mostrarMensaje("Informacion", "Tarea eliminada");
            limpiarFormulario();
            listarTareas();
        }
    }

    public void cargarTareaFormulario(){
        Tarea tarea = tareaTabla.getSelectionModel().getSelectedItem();
        if(tarea != null){
            idTareaInterno = tarea.getIdTarea();
            nombreTareaInput.setText(tarea.getNombreTarea());
            responsableInput.setText(tarea.getResponsable());
            estatusInput.setText(tarea.getEstatus());
        }
    }

    //Obtener valores de los inputs del formulario
    private void recolectarDatosFormulario(Tarea tarea){

        //Si este valor es diferente de nulo se hace una actualizacion y no una insercion
        if(idTareaInterno != null){
            tarea.setIdTarea(idTareaInterno);
        }
        tarea.setNombreTarea(nombreTareaInput.getText());
        tarea.setResponsable(responsableInput.getText());
        tarea.setEstatus(estatusInput.getText());
    }

    //Limpiar formulario
    public void limpiarFormulario(){
        idTareaInterno = null;
        nombreTareaInput.clear();
        responsableInput.clear();
        estatusInput.clear();
    }

    private void mostrarMensaje(String titulo, String mensaje){
        //MOSTRAR ALERTAS EN JAVAFX
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
