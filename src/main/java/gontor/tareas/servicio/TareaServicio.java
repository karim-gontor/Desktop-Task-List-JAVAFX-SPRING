package gontor.tareas.servicio;

import gontor.tareas.modelo.Tarea;
import gontor.tareas.repositorio.TareaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TareaServicio implements ITareaServicio{

    @Autowired
    private TareaRepositorio tr;

    @Override
    public List<Tarea> listarTareas() {
        return tr.findAll();
    }

    @Override
    public Tarea buscarTareaPorId(Integer idTarea) {
        return tr.findById(idTarea).orElse(null);
    }

    @Override
    public void guardarTarea(Tarea tarea) {
        tr.save(tarea);
    }

    @Override
    public void eliminarTarea(Tarea tarea) {
        tr.delete(tarea);
    }
}
