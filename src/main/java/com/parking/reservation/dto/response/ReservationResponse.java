package com.parking.reservation.dto.response;

import com.parking.reservation.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponse(
          Long id,
          Long slotId,
          String vehicleNumber,
          LocalDateTime startTime,
          LocalDateTime endTime,
          BigDecimal cost,
          ReservationStatus status
) {
}
