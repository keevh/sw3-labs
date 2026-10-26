package co.edu.demoacademico.grupos.port;

import co.edu.demoacademico.grupos.model.Grupo;

/**
 * Contrato público del módulo grupos.
 * Otros módulos (p. ej. matriculas) consultan grupos SOLO a través de esta interfaz.
 */
public interface GrupoQueryPort {
    Grupo obtenerPorId(Long id);
}
