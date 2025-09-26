package com.parking.reservation.entity;

import com.parking.reservation.enums.VehicleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a single parking slot on a {@link Floor}.
 * --------------------------------------------------
 * @author nikhilesh
 * @Created 26-Sep-2025
 * <p>
 * Each slot is associated with exactly one floor and supports one {@link VehicleType}.
 */

@Entity
@Table(name = "slots", uniqueConstraints = @UniqueConstraint(columnNames = {"floor_id", "slot_number"}))
@Getter
@Setter
@NoArgsConstructor
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id", nullable = false)
    private Floor floor;

    @Column(nullable = false)
    private String slotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;
}
