package co.edu.demoacademico.estudiantes.controller;

import co.edu.demoacademico.common.api.ApiResponse;
import co.edu.demoacademico.common.api.ResponseBuilder;
import co.edu.demoacademico.estudiantes.dto.EstudianteCreateDTO;
import co.edu.demoacademico.estudiantes.dto.EstudianteDTO;
import co.edu.demoacademico.estudiantes.dto.EstudianteUpdateDTO;
import co.edu.demoacademico.estudiantes.handler.EstudianteHandler;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteHandler handler;

    public EstudianteController(EstudianteHandler handler) {
        this.handler = handler;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EstudianteDTO>> crear(@Valid @RequestBody EstudianteCreateDTO in) {
        return ResponseBuilder.created("Estudiante creado", handler.crear(in));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<EstudianteDTO>>> listar() {
        return ResponseBuilder.ok("OK", handler.listar());
    }

    @GetMapping("/pagina")
    public ResponseEntity<ApiResponse<Page<EstudianteDTO>>> listarPaginado(@ParameterObject Pageable pageable) {
        return ResponseBuilder.ok("OK", handler.listar(pageable));
    }

    @GetMapping("/buscar")
    public ResponseEntity<ApiResponse<EstudianteDTO>> buscarPorEmail(@RequestParam("email") String email) {
        return ResponseBuilder.ok("OK", handler.buscarPorEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EstudianteDTO>> obtener(@PathVariable("id") Long id) {
        return ResponseBuilder.ok("OK", handler.obtener(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EstudianteDTO>> actualizar(@PathVariable("id") Long id,
                                                                 @Valid @RequestBody EstudianteUpdateDTO in) {
        return ResponseBuilder.ok("Estudiante actualizado", handler.actualizar(id, in));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminar(@PathVariable("id") Long id) {
        handler.eliminar(id);
        return ResponseBuilder.ok("Estudiante eliminado", null);
    }
}
