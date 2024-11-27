package gontor.tareas.presentacion;

import gontor.tareas.TareasApplication;
import gontor.tareas.modelo.Tarea;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

public class SistemaTareasFx extends Application {

    private ConfigurableApplicationContext applicationContext;

    //Inicializar fabrica de Spring
    @Override
    public void init(){
        this.applicationContext =
                new SpringApplicationBuilder(TareasApplication.class).run();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //Cargamos en memoria la interfaz grafica de JavaFX
        FXMLLoader loader =
                new FXMLLoader(TareasApplication.class.getResource("/templates/index.fxml"));

        loader.setControllerFactory(applicationContext::getBean);

        Scene escena = new Scene(loader.load());
        stage.setScene(escena);
        stage.show();
    }

    @Override
    public void stop(){
        //Cerramos conexion
        applicationContext.close();
        //Cerramos JavaFx
        Platform.exit();
    }

}
