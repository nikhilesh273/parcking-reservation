package com.parking.reservation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateFloorRequest {
    @NotBlank(message = "Floor name is required")
    private String name;
}
