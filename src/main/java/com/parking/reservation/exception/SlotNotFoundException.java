package com.parking.reservation.exception;

/**
 * Thrown when a requested parking slot does not exist.
 * <p>
 * Translates to HTTP 404 Not Found in {@link GlobalExceptionHandler}.
 */

public class SlotNotFoundException extends RuntimeException{
    public SlotNotFoundException(String message) { super(message); }
}
