package com.parking.reservation.util;

import com.parking.reservation.enums.VehicleType;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

public class PricingUtil {

    public static BigDecimal calculateCost(VehicleType type, LocalDateTime start, LocalDateTime end) {
        long minutes = Duration.between(start, end).toMinutes();
        long hours = (long) Math.ceil(minutes / 60.0);
        return type.getHourlyRate().multiply(BigDecimal.valueOf(hours));
    }
}
