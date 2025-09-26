package com.parking.reservation.service;

import com.parking.reservation.dto.request.CreateFloorRequest;
import com.parking.reservation.dto.response.FloorResponse;
import com.parking.reservation.entity.Floor;
import jakarta.validation.Valid;

public interface FloorService {
    FloorResponse createFloor(@Valid CreateFloorRequest request);
}
