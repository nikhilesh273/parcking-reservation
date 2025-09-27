package com.parking.reservation.service;

import com.parking.reservation.dto.request.CreateFloorRequest;
import com.parking.reservation.dto.response.FloorResponse;
import jakarta.validation.Valid;

public interface FloorService {
    FloorResponse createFloor(@Valid CreateFloorRequest request);
}
