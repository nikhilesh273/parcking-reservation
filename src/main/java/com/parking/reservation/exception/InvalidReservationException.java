package com.parking.reservation.exception;

/**
 * Thrown when a reservation request is invalid, such as when the end time is before the start time.
 * <p>
 * Translates to HTTP 400 Bad Request in {@link GlobalExceptionHandler}.
 */
public class InvalidReservationException extends RuntimeException{
    public InvalidReservationException(String message) { super(message); }
}
