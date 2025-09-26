package com.parking.reservation.controller;

import com.parking.reservation.dto.ApiResponse;
import com.parking.reservation.dto.request.ReserveRequest;
import com.parking.reservation.dto.response.ReservationResponse;
import com.parking.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/reserve")
    public ResponseEntity<ApiResponse<ReservationResponse>> reserve(@Valid @RequestBody ReserveRequest request) {
        ReservationResponse response = reservationService.reserveSlot(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Reservation created successfully"));
    }


}
