package com.parking.reservation.dto.request;

import com.parking.reservation.enums.VehicleType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
