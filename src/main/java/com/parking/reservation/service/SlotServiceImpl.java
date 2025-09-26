package com.parking.reservation.service;

import com.parking.reservation.dto.request.CreateSlotRequest;
import com.parking.reservation.dto.response.SlotResponse;
import com.parking.reservation.entity.Floor;
import com.parking.reservation.entity.Slot;
import com.parking.reservation.exception.AlreadyExistsException;
import com.parking.reservation.exception.SlotNotFoundException;
import com.parking.reservation.repository.FloorRepository;
import com.parking.reservation.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlotServiceImpl implements SlotService{

    private final SlotRepository slotRepository;
    private final FloorRepository floorRepository;

    @Override
    public SlotResponse createSlot(CreateSlotRequest request) {
        log.info("Creating slot for floor ID: {}, slotNumber: {}, vehicleType: {}",
                request.getFloorId(), request.getSlotNumber(), request.getVehicleType());

        Floor floor = floorRepository.findById(request.getFloorId())
                .orElseThrow(() -> {
                    log.warn("Floor not found with ID: {}", request.getFloorId());
                    return new SlotNotFoundException("Floor not found with ID: " + request.getFloorId());
                });

        Slot slot = new Slot();
        slot.setFloor(floor);
        slot.setSlotNumber(request.getSlotNumber());
        slot.setVehicleType(request.getVehicleType());

        try {
            Slot savedSlot = slotRepository.save(slot);
            log.info("Slot created successfully with ID: {}", savedSlot.getId());
            return mapToResponse(savedSlot);
        } catch (DataIntegrityViolationException ex) {
            String message = String.format("Slot with number '%s' already exists on floor ID %d",
                    request.getSlotNumber(), request.getFloorId());
            log.warn(message);
            throw new AlreadyExistsException(message);
        }
    }

    private SlotResponse mapToResponse(Slot slot) {
        return new SlotResponse(
                slot.getId(),
                slot.getSlotNumber(),
                slot.getVehicleType().name(),
                slot.getFloor().getId(),
                slot.getFloor().getName()
        );
    }
}
