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

/**
 * Global exception handler that converts exceptions into standardized {@link ApiResponse} responses.
 * ---------------------------------------------------------------
 * @author nikhilesh
 * @Created 26-Sep-2025
 * <p>
 * Handles:
 * - Validation errors ({@link MethodArgumentNotValidException})
 * - Business rule violations (custom exceptions)
 * - Unexpected server errors
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles SlotUnavailableException and returns a standardized error response.
     *
     * @param ex the SlotUnavailableException
     * @return ResponseEntity with error details and HTTP status 409 Conflict
     */
    @ExceptionHandler(SlotUnavailableException.class)
    public ResponseEntity<ApiResponse<String>> handleSlotUnavailable(SlotUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    /**
     * Handles SlotNotFoundException and InvalidReservationException, returning a standardized error response.
     *
     * @param ex the exception (either SlotNotFoundException or InvalidReservationException)
     * @return ResponseEntity with error details and HTTP status 400 Bad Request
     */
    @ExceptionHandler({SlotNotFoundException.class, InvalidReservationException.class})
    public ResponseEntity<ApiResponse<String>> handleNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Handles validation errors and returns a standardized error response.
     *
     * @param ex the MethodArgumentNotValidException
     * @return ResponseEntity with error details and HTTP status 400 Bad Request
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(msg, HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Handles all other uncaught exceptions and returns a standardized error response.
     *
     * @param ex the Exception
     * @return ResponseEntity with error details and HTTP status 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    /**
     * Handles AlreadyExistsException (e.g., duplicate floor name) and returns a 409 Conflict.
     *
     * @param ex the AlreadyExistsException
     * @return ResponseEntity with custom message and HTTP 409
     */
    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiResponse<String>> handleAlreadyExists(AlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ex.getMessage(), HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<String>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        // Check if it's caused by an invalid enum value
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

        // Fallback for other JSON parse errors
        return ResponseEntity.badRequest()
                .body(ApiResponse.error("Invalid JSON format", HttpStatus.BAD_REQUEST.value()));
    }
}
