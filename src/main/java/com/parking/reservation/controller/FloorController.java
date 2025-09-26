package com.parking.reservation.controller;

import com.parking.reservation.dto.ApiResponse;
import com.parking.reservation.dto.request.CreateFloorRequest;
import com.parking.reservation.dto.response.FloorResponse;
import com.parking.reservation.entity.Floor;
import com.parking.reservation.service.FloorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/")
@RequiredArgsConstructor
public class FloorController {

    private final FloorService floorService;

    @PostMapping("/floors")
    public ResponseEntity<ApiResponse<FloorResponse>> createFloor(@Valid @RequestBody CreateFloorRequest request) {
        FloorResponse response = floorService.createFloor(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Floor created successfully"));
    }
}
