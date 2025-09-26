package com.parking.reservation.dto.request;

import com.parking.reservation.enums.VehicleType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Request DTO for creating a new reservation.
 * <p>
 * Validation rules:
 * - {@code slotId} must not be null
 * - {@code vehicleNumber} must match format XX00XX0000 (validated in service)
 * - {@code startTime} must be present or future
 * - {@code endTime} must be after {@code startTime} and within 24 hours
 * - {@code vehicleType} must not be null
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRequest {

    @NotNull(message = "Slot ID is required")
    private Long slotId;

    private String vehicleNumber;

    @FutureOrPresent(message = "Start time must be present or future")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;


}
