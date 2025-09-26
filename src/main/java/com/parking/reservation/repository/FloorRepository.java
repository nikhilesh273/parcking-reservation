package com.parking.reservation.repository;

import com.parking.reservation.entity.Floor;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FloorRepository extends JpaRepository<Floor, Long> {
    boolean existsByName(@NotBlank(message = "Floor name is required") String name);
}
