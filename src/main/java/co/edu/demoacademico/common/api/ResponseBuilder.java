package co.edu.demoacademico.common.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public final class ResponseBuilder {

    private ResponseBuilder() {}

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, message, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(HttpStatusCode status, String message, T data) {
        return ResponseEntity.status(status).body(new ApiResponse<>(false, message, data));
    }
}
