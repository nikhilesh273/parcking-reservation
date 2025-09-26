package com.parking.reservation.exception;

/**
 * Thrown when a requested parking slot is already reserved for the given time window.
 * <p>
 * Translates to HTTP 409 Conflict in {@link GlobalExceptionHandler}.
 */
public class SlotUnavailableException extends RuntimeException{
    public SlotUnavailableException(String message) {
        super(message);
    }
}
