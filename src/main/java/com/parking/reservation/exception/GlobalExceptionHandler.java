package com.parking.reservation.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.parking.reservation.dto.ApiResponse;
import com.parking.reservation.enums.VehicleType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SlotUnavailableException.class)
    public ResponseEntity<ApiResponse<String>> handleSlotUnavailable(SlotUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler({SlotNotFoundException.class, InvalidReservationException.class})
    public ResponseEntity<ApiResponse<String>> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(msg, HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleAlreadyExists(AlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException invalidFormat) {
            String fieldName = invalidFormat.getPath().isEmpty() ? "field" : invalidFormat.getPath().get(0).getFieldName();
            String targetType = invalidFormat.getTargetType() != null
                    ? invalidFormat.getTargetType().getSimpleName()
                    : "enum";

            if (targetType.equals("VehicleType")) {
                String allowedValues = Arrays.toString(VehicleType.values());
                String message = String.format("Invalid value for '%s'. Allowed values: %s", fieldName, allowedValues);
                return ResponseEntity.badRequest().body(ApiResponse.error(message, HttpStatus.BAD_REQUEST.value()));
            }
        }
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid JSON format", HttpStatus.BAD_REQUEST.value()));
    }
}
