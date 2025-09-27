package com.parking.reservation.service;

import com.parking.reservation.dto.request.ReserveRequest;
import com.parking.reservation.dto.response.ReservationResponse;
import com.parking.reservation.dto.response.SlotResponse;
import com.parking.reservation.enums.VehicleType;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ReservationService {

    ReservationResponse reserveSlot(@Valid ReserveRequest request);

    ReservationResponse getReservation(Long id);

    void cancelReservation(Long id);

    Page<SlotResponse> getAvailableSlots(LocalDateTime startTime, LocalDateTime endTime, VehicleType vehicleType, Pageable pageable);
}
