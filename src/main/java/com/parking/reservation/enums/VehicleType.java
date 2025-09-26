package com.parking.reservation.enums;

import java.math.BigDecimal;

/**
 * Enum representing supported vehicle types and their hourly parking rates.
 * <p>
 * Designed to be extensible: new vehicle types can be added by defining new constants.
 */
public enum VehicleType {
    TWO_WHEELER(20),
    FOUR_WHEELER(30);
    private final int hourlyRate;

    /**
     * Constructs a vehicle type with its hourly rate.
     *---------------------------------------------
     * @param hourlyRate rate in INR per hour
     */
    VehicleType(int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    /**
     * Returns the hourly parking rate as a {@link BigDecimal}.
     *---------------------------------------------
     * @return hourly rate
     */
    public BigDecimal getHourlyRate() {
        return BigDecimal.valueOf(hourlyRate);
    }
}
