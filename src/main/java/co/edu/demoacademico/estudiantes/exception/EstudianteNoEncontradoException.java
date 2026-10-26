package co.edu.demoacademico.estudiantes.exception;

import co.edu.demoacademico.common.exception.NotFoundException;

public class EstudianteNoEncontradoException extends NotFoundException {
    public EstudianteNoEncontradoException(String email) {
        super("No se encontró estudiante con el email: " + email);
    }
}
