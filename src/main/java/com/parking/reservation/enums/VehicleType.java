package com.parking.reservation.enums;

import java.math.BigDecimal;

public enum VehicleType {
    TWO_WHEELER(20),
    FOUR_WHEELER(30);
    private final int hourlyRate;

    VehicleType(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public BigDecimal getHourlyRate() {
        return BigDecimal.valueOf(hourlyRate);
    }
}
