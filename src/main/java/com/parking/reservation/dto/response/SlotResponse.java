package com.parking.reservation.dto.response;

public record SlotResponse(
        Long id,
        String slotNumber,
        String vehicleType,
        Long floorId,
        String floorName
) {}
