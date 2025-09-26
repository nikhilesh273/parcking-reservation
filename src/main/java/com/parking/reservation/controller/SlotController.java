package com.parking.reservation.controller;

import com.parking.reservation.dto.ApiResponse;
import com.parking.reservation.dto.request.CreateSlotRequest;
import com.parking.reservation.dto.response.SlotResponse;
import com.parking.reservation.service.SlotService;
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
public class SlotController {

    private final SlotService slotService;

    @PostMapping("/slots")
    public ResponseEntity<ApiResponse<SlotResponse>> createSlot(@Valid @RequestBody CreateSlotRequest request) {
        SlotResponse response = slotService.createSlot(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Slot created successfully"));
    }
}