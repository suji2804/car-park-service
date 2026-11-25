package com.example.carparkservice.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Vehicle details to allocate a parking space.")
public record CarType(
        String vehicleReg,
        int vehicleType
) {
}
