package co.edu.demoacademico.estudiantes.service;

import co.edu.demoacademico.common.exception.NotFoundException;
import co.edu.demoacademico.estudiantes.exception.EmailDuplicadoException;
import co.edu.demoacademico.estudiantes.exception.EstudianteNoEncontradoException;
import co.edu.demoacademico.estudiantes.model.Estudiante;
import co.edu.demoacademico.estudiantes.port.EstudianteQueryPort;
import co.edu.demoacademico.estudiantes.repository.EstudianteRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService, EstudianteQueryPort {

    private final EstudianteRepository repo;

    public EstudianteServiceImpl(EstudianteRepository repo) {
        this.repo = repo;
    }

    @Override
    public Estudiante crear(Estudiante e) {
        if (repo.existsByEmail(e.getEmail())) {
            throw new EmailDuplicadoException(e.getEmail());
        }
        return repo.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante obtenerPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Estudiante buscarPorEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new EstudianteNoEncontradoException(email));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Estudiante> listar() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Estudiante> listar(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    public Estudiante actualizar(Long id, Estudiante e) {
        Estudiante actual = obtenerPorId(id);
        if (!actual.getEmail().equalsIgnoreCase(e.getEmail()) && repo.existsByEmail(e.getEmail())) {
            throw new EmailDuplicadoException(e.getEmail());
        }
        actual.setNombre(e.getNombre());
        actual.setEmail(e.getEmail());
        return repo.save(actual);
    }

    @Override
    public void eliminar(Long id) {
        repo.delete(obtenerPorId(id));
        repo.flush();
    }
}
