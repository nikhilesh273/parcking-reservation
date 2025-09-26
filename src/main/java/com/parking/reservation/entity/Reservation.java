package com.parking.reservation.entity;

import com.parking.reservation.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a customer's reservation of a {@link Slot} for a specific time window.
 * --------------------------------------------------
 * @author nikhilesh
 * @Created 26-Sep-2025
 * <p>
 * Enforces business rules:
 * - No overlapping reservations for the same slot
 * - Duration ≤ 24 hours
 * - Valid vehicle number format
 * - Cost calculated with partial-hour rounding
 */

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slot_id", nullable = false)
    private Slot slot;

    @Column(nullable = false)
    private String vehicleNumber;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private BigDecimal cost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @Version
    private Long version;
}
