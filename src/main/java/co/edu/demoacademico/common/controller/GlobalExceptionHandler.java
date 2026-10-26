package co.edu.demoacademico.common.controller;

import co.edu.demoacademico.common.api.ApiResponse;
import co.edu.demoacademico.common.api.ResponseBuilder;
import co.edu.demoacademico.common.exception.BusinessException;
import co.edu.demoacademico.common.exception.NotFoundException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - recurso no existe
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(NotFoundException ex) {
        return ResponseBuilder.error(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    // 409 - regla de negocio (email duplicado, matrícula duplicada, sin cupo...)
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusiness(BusinessException ex) {
        return ResponseBuilder.error(HttpStatus.CONFLICT, ex.getMessage(), null);
    }

    // 409 - restricción de BD (unique / FK), p. ej. borrar un programa con asignaturas
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleIntegridad(DataIntegrityViolationException ex) {
        return ResponseBuilder.error(HttpStatus.CONFLICT,
                "Operación rechazada por integridad de datos (valor duplicado o registros relacionados)", null);
    }

    // 400 - validación de DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> fields = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Validación inválida", fields);
    }

    // 400 - JSON mal formado o tipo de parámetro inválido
    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(Exception ex) {
        return ResponseBuilder.error(HttpStatus.BAD_REQUEST, "Solicitud inválida", null);
    }

    // Genérico
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        if (ex instanceof ErrorResponse er) { // errores propios de Spring (404 de ruta, 405, etc.)
            return ResponseBuilder.error(er.getStatusCode(), ex.getMessage(), null);
        }
        return ResponseBuilder.error(HttpStatus.INTERNAL_SERVER_ERROR, "Error inesperado", null);
    }
}
