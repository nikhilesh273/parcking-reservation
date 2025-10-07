package com.parking.reservation.service;

import com.parking.reservation.dto.request.ReserveRequest;
import com.parking.reservation.dto.response.ReservationResponse;
import com.parking.reservation.dto.response.SlotResponse;
import com.parking.reservation.entity.Reservation;
import com.parking.reservation.entity.Slot;
import com.parking.reservation.enums.ReservationStatus;
import com.parking.reservation.enums.VehicleType;
import com.parking.reservation.exception.InvalidReservationException;
import com.parking.reservation.exception.SlotNotFoundException;
import com.parking.reservation.exception.SlotUnavailableException;
import com.parking.reservation.repository.ReservationRepository;
import com.parking.reservation.repository.SlotRepository;
import com.parking.reservation.util.PricingUtil;
import com.parking.reservation.util.SlotConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService {

    private static final Pattern VEHICLE_PATTERN = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$");

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;

    @Override
    public ReservationResponse reserveSlot(ReserveRequest request) {
        log.info("Processing reservation request for slot ID: {}, vehicle: {}, time: {} to {}",
                request.getSlotId(), request.getVehicleNumber(), request.getStartTime(), request.getEndTime());

        validateRequest(request);

        Slot slot = slotRepository.findByIdWithLock(request.getSlotId())
                .orElseThrow(() -> new SlotNotFoundException("Slot not found"));

        if (!slot.getVehicleType().equals(request.getVehicleType())) {
            String msg = String.format("Vehicle type mismatch: slot supports %s, but %s was requested",
                    slot.getVehicleType(), request.getVehicleType());
            log.warn(msg);
            throw new InvalidReservationException(msg);
        }

        List<Reservation> overlaps = reservationRepository.findOverlapping(
                request.getSlotId(),
                ReservationStatus.ACTIVE,
                request.getStartTime(),
                request.getEndTime()
        );

        if (!overlaps.isEmpty()) {
            String msg = String.format("Slot ID %d is already reserved between %s and %s",
                    request.getSlotId(), request.getStartTime(), request.getEndTime());
            log.warn(msg);
            throw new SlotUnavailableException(msg);
        }

        BigDecimal cost = PricingUtil.calculateCost(
                request.getVehicleType(),
                request.getStartTime(),
                request.getEndTime()
        );

        Reservation reservation = new Reservation();
        reservation.setSlot(slot);
        reservation.setVehicleNumber(request.getVehicleNumber());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setCost(cost);
        reservation.setStatus(ReservationStatus.ACTIVE);

        Reservation saved = reservationRepository.save(reservation);
        log.info("Reservation created successfully with ID: {}", saved.getId());

        return mapToResponse(saved);
    }

    private void validateRequest(ReserveRequest req) {
        if (req.getStartTime() == null || req.getEndTime() == null) {
            throw new InvalidReservationException("Start and end time are required");
        }
        if (!req.getStartTime().isBefore(req.getEndTime())) {
            throw new InvalidReservationException("Start time must be before end time");
        }
        if (java.time.Duration.between(req.getStartTime(), req.getEndTime()).toHours() > 24) {
            throw new InvalidReservationException("Reservation duration cannot exceed 24 hours");
        }
        if (req.getVehicleNumber() == null || !VEHICLE_PATTERN.matcher(req.getVehicleNumber()).matches()) {
            throw new InvalidReservationException("Invalid vehicle number format. Expected: XX00XX0000 (e.g., KA05MH1234)");
        }
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSlot().getId(),
                reservation.getVehicleNumber(),
                reservation.getStartTime(),
                reservation.getEndTime(),
                reservation.getCost(),
                reservation.getStatus()
        );
    }

    @Override
    public ReservationResponse getReservation(Long id) {
        log.info("Fetching reservation with ID: {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationException("Reservation not found"));
        return mapToResponse(reservation);
    }

    @Override
    public void cancelReservation(Long id) {
        log.info("Cancelling reservation with ID: {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new InvalidReservationException("Reservation not found"));
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }

    @Override
    public Page<SlotResponse> getAvailableSlots(
            LocalDateTime startTime,
            LocalDateTime endTime,
            VehicleType vehicleType,
            Pageable pageable) {

        log.info("Fetching available slots for vehicle type: {}, time range: {} to {}, page: {}",
                vehicleType, startTime, endTime, pageable);

        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                String property = order.getProperty();
                if (!SlotConstants.ALLOWED_SORT_PROPERTIES.contains(property)) {
                    throw new InvalidReservationException(
                            "Sorting by '" + property + "' is not allowed. Allowed: " +
                                    SlotConstants.ALLOWED_SORT_PROPERTIES);
                }
            }
        }

        if (!startTime.isBefore(endTime)) {
            throw new InvalidReservationException("Start time must be before end time");
        }
        if (Duration.between(startTime, endTime).toHours() > 24) {
            throw new InvalidReservationException("Time range cannot exceed 24 hours");
        }

        Page<Slot> availableSlots = reservationRepository.findAvailableSlots(startTime, endTime, vehicleType, pageable);

       return availableSlots.map(this::mapToSlotResponse);
    }

    private SlotResponse mapToSlotResponse(Slot slot) {
        return new SlotResponse(
                slot.getId(),
                slot.getSlotNumber(),
                slot.getVehicleType().name(),
                slot.getFloor().getId(),
                slot.getFloor().getName()
        );
    }
}
