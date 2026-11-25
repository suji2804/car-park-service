package com.example.carparkservice.dto;

import java.time.LocalDateTime;

public record ParkingChargeResponse(
        String vehicleReg,
        LocalDateTime timeIn,
        LocalDateTime timeOut,
        double finalCharge
) {
}
