package com.parking.reservation.dto.request;

import com.parking.reservation.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateSlotRequest {
    @NotNull(message = "Floor ID is required")
    private Long floorId;

    @NotBlank(message = "Slot number is required")
    private String slotNumber;

    @NotNull(message = "Vehicle type is required")
    private VehicleType vehicleType;
}
