package com.parking.reservation.service;

import com.parking.reservation.dto.request.CreateFloorRequest;
import com.parking.reservation.dto.response.FloorResponse;
import com.parking.reservation.entity.Floor;
import com.parking.reservation.exception.AlreadyExistsException;
import com.parking.reservation.repository.FloorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FloorServiceImpl implements FloorService {

    private final FloorRepository floorRepository;

    @Override
    public FloorResponse createFloor(CreateFloorRequest request) {
        log.info("Creating floor with name: {}", request.getName());

        if (floorRepository.existsByName(request.getName())) {
            log.warn("Floor with name '{}' already exists", request.getName());
            throw new AlreadyExistsException("Floor with name '" + request.getName() + "' already exists");
        }

        Floor floor = new Floor();
        floor.setName(request.getName());
        Floor saved = floorRepository.save(floor);

        log.info("Floor created successfully with ID: {}", saved.getId());
        return mapToResponse(saved);
    }

    private FloorResponse mapToResponse(Floor floor) {
        return new FloorResponse(floor.getId(), floor.getName());
    }
}
