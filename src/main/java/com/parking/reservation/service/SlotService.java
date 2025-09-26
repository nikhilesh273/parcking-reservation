package com.parking.reservation.service;

import com.parking.reservation.dto.request.CreateSlotRequest;
import com.parking.reservation.dto.response.SlotResponse;
import jakarta.validation.Valid;

public interface SlotService {
    SlotResponse createSlot(@Valid CreateSlotRequest request);
}
