package com.parking.reservation.controller;

import com.parking.reservation.dto.ApiResponse;
import com.parking.reservation.dto.request.ReserveRequest;
import com.parking.reservation.dto.response.ReservationResponse;
import com.parking.reservation.dto.response.SlotResponse;
import com.parking.reservation.enums.VehicleType;
import com.parking.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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

    @GetMapping("/reservations/{id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservation(@PathVariable Long id) {
        ReservationResponse response = reservationService.getReservation(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Reservation fetched successfully"));
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<ApiResponse<Void>> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Reservation cancelled successfully"));
    }

    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<Page<SlotResponse>>> getAvailableSlots(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam VehicleType vehicleType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "slotNumber") String sortProperty,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));

        Page<SlotResponse> availableSlots = reservationService.getAvailableSlots(startTime, endTime, vehicleType, pageable);
        return ResponseEntity.ok(ApiResponse.success(availableSlots, "Available slots retrieved"));
    }
}
