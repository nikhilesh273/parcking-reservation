package com.parking.reservation.service;

import com.parking.reservation.dto.request.ReserveRequest;
import com.parking.reservation.dto.response.ReservationResponse;
import jakarta.validation.Valid;

public interface ReservationService {
    ReservationResponse reserveSlot(@Valid ReserveRequest request);
}
