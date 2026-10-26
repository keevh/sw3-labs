package co.edu.demoacademico.estudiantes.port;

import co.edu.demoacademico.estudiantes.model.Estudiante;

/**
 * Contrato público del módulo estudiantes.
 * Otros módulos (p. ej. matriculas) consultan estudiantes SOLO a través de esta interfaz.
 */
public interface EstudianteQueryPort {
    Estudiante obtenerPorId(Long id);
}
