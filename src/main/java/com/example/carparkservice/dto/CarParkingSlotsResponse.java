package com.example.carparkservice.dto;

public record CarParkingSlotsResponse (
    int availableSlots,
    int occupiedSlots
) {}
