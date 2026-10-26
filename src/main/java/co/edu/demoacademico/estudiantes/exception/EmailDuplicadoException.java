package co.edu.demoacademico.estudiantes.exception;

import co.edu.demoacademico.common.exception.BusinessException;

public class EmailDuplicadoException extends BusinessException {
    public EmailDuplicadoException(String email) {
        super("Ya existe un estudiante con el email: " + email);
    }
}
