package com.parking.reservation.repository;

import com.parking.reservation.entity.Reservation;
import com.parking.reservation.entity.Slot;
import com.parking.reservation.enums.ReservationStatus;
import com.parking.reservation.enums.VehicleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r " + "WHERE r.slot.id = :slotId " + "AND r.status = :status " + "AND r.endTime > :startTime " + "AND r.startTime < :endTime")
    List<Reservation> findOverlapping(@Param("slotId") Long slotId, @Param("status") ReservationStatus status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("""
            SELECT s FROM Slot s
            LEFT JOIN FETCH s.floor
            WHERE s.vehicleType = :vehicleType
              AND s.id NOT IN (
                SELECT r.slot.id FROM Reservation r
                WHERE r.status = 'ACTIVE'
                  AND r.endTime > :startTime
                  AND r.startTime < :endTime
              )
            """)
    Page<Slot> findAvailableSlots(
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("vehicleType") VehicleType vehicleType,
            Pageable pageable
    );
}
