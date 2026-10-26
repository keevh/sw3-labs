package co.edu.demoacademico.estudiantes.handler;

import co.edu.demoacademico.estudiantes.dto.EstudianteCreateDTO;
import co.edu.demoacademico.estudiantes.dto.EstudianteDTO;
import co.edu.demoacademico.estudiantes.dto.EstudianteUpdateDTO;
import co.edu.demoacademico.estudiantes.model.Estudiante;
import co.edu.demoacademico.estudiantes.service.EstudianteService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class EstudianteHandler {

    private final EstudianteService service;

    public EstudianteHandler(EstudianteService service) {
        this.service = service;
    }

    public EstudianteDTO crear(EstudianteCreateDTO in) {
        Estudiante e = new Estudiante();
        e.setNombre(in.getNombre());
        e.setEmail(in.getEmail());
        return toDto(service.crear(e));
    }

    public EstudianteDTO obtener(Long id) {
        return toDto(service.obtenerPorId(id));
    }

    public EstudianteDTO buscarPorEmail(String email) {
        return toDto(service.buscarPorEmail(email));
    }

    public List<EstudianteDTO> listar() {
        return service.listar().stream().map(this::toDto).toList();
    }

    public Page<EstudianteDTO> listar(Pageable pageable) {
        return service.listar(pageable).map(this::toDto);
    }

    public EstudianteDTO actualizar(Long id, EstudianteUpdateDTO in) {
        Estudiante e = new Estudiante();
        e.setNombre(in.getNombre());
        e.setEmail(in.getEmail());
        return toDto(service.actualizar(id, e));
    }

    public void eliminar(Long id) {
        service.eliminar(id);
    }

    private EstudianteDTO toDto(Estudiante e) {
        EstudianteDTO dto = new EstudianteDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setEmail(e.getEmail());
        return dto;
    }
}
