package com.parking.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Generic wrapper for all API responses to ensure consistent structure across endpoints.
 * -------------------------------------------------------------------------------------
 * @author nikhilesh
 * @Created 26-Sep-2025
 * <p>
 * Example success response:
 * <pre>
 * {
 *   "success": true,
 *   "message": "Operation completed successfully",
 *   "data": { ... },
 *   "timestamp": "2025-04-05T10:30:00.123456",
 *   "statusCode": 200
 * }
 * </pre>
 *
 * @param <T> the type of the response payload
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private String timestamp;
    private int statusCode;

    /**
     * Private constructor to enforce use of static factory methods.
     *-------------------------------------------------------------
     * @param success     whether the operation succeeded
     * @param message     descriptive message
     * @param data        response payload
     * @param statusCode  HTTP status code
     */
    private ApiResponse(boolean success, String message, T data, int statusCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Creates a successful API response with custom message.
     *-----------------------------------------------------
     * @param data    the response payload
     * @param message success message
     * @param <T>     type of the payload
     * @return a new {@link ApiResponse} instance
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(true);
        response.setMessage(message);
        response.setData(data);
        response.setStatusCode(200);
        response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }

    /**
     * Creates a successful API response with default message.
     *-------------------------------------------------------
     * @param data the response payload
     * @param <T>  type of the payload
     * @return a new {@link ApiResponse} instance
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(data, "Operation completed successfully");
    }

    /**
     * Creates an error API response.
     *------------------------------------------------------
     * @param message    error description
     * @param statusCode HTTP error code (e.g., 400, 404, 500)
     * @param <T>        payload type (always null for errors)
     * @return a new {@link ApiResponse} instance
     */
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setSuccess(false);
        response.setMessage(message);
        response.setData(null);
        response.setStatusCode(statusCode);
        response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return response;
    }
}

