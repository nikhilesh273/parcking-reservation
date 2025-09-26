package com.parking.reservation.repository;

import com.parking.reservation.entity.Reservation;
import com.parking.reservation.enums.ReservationStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " +
            "WHERE r.slot.id = :slotId " +
            "AND r.status = :status " +
            "AND r.endTime > :startTime " +
            "AND r.startTime < :endTime")
    List<Reservation> findOverlapping(
            @Param("slotId") Long slotId,
            @Param("status") ReservationStatus status,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
