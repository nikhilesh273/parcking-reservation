package com.parking.reservation.service;

import com.parking.reservation.dto.request.ReserveRequest;
import com.parking.reservation.dto.response.ReservationResponse;
import com.parking.reservation.entity.Reservation;
import com.parking.reservation.entity.Slot;
import com.parking.reservation.enums.ReservationStatus;
import com.parking.reservation.exception.InvalidReservationException;
import com.parking.reservation.exception.SlotNotFoundException;
import com.parking.reservation.exception.SlotUnavailableException;
import com.parking.reservation.repository.ReservationRepository;
import com.parking.reservation.repository.SlotRepository;
import com.parking.reservation.util.PricingUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ReservationServiceImpl implements ReservationService{

    private static final Pattern VEHICLE_PATTERN = Pattern.compile("^[A-Z]{2}\\d{2}[A-Z]{2}\\d{4}$");

    private final ReservationRepository reservationRepository;
    private final SlotRepository slotRepository;

    @Override
    public ReservationResponse reserveSlot(ReserveRequest request) {
        log.info("Processing reservation request for slot ID: {}, vehicle: {}, time: {} to {}",
                request.getSlotId(), request.getVehicleNumber(), request.getStartTime(), request.getEndTime());

        validateRequest(request);

        Slot slot = slotRepository.findById(request.getSlotId())
                .orElseThrow(() -> {
                    log.warn("Slot not found with ID: {}", request.getSlotId());
                    return new SlotNotFoundException("Slot not found with ID: " + request.getSlotId());
                });

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

    /**
     * Validates the reservation request against business rules.
     *
     * @param req the reservation request
     * @throws InvalidReservationException if any rule is violated
     */
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

    /**
     * Maps a {@link Reservation} entity to its response DTO.
     */
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


}
