package com.example.carparkservice.service;

import com.example.carparkservice.dto.CarParkingSlotsResponse;
import com.example.carparkservice.dto.ParkingChargeResponse;
import com.example.carparkservice.dto.ParkedCarResponse;
import com.example.carparkservice.request.CarType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class CarParkingService {

    private final int totalSlots = 2;
    private int availableSlots = totalSlots;
    private final Map<String, ParkedVehicle> parkedVehicles = new HashMap<>();

    public synchronized CarParkingSlotsResponse getCarParkSlots() {
        return new CarParkingSlotsResponse(availableSlots, totalSlots - availableSlots);
    }

    public synchronized ParkedCarResponse addCarToSlot(CarType carType) {
        if (availableSlots == 0) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "No parking spaces are available");
        }
        if (carType == null || carType.vehicleType() < 1 || carType.vehicleType() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle type must be 1, 2, or 3");
        }
        if (carType.vehicleReg() == null || carType.vehicleReg().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Vehicle registration is required");
        }
        if (parkedVehicles.containsKey(carType.vehicleReg())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is already parked");
        }

        LocalDateTime timeIn = LocalDateTime.now();
        parkedVehicles.put(carType.vehicleReg(), new ParkedVehicle(carType, timeIn));
        availableSlots--;
        return new ParkedCarResponse(carType.vehicleReg(), timeIn);
    }

    public synchronized ParkingChargeResponse removeCarFromSlot(String vehicleReg) {
        ParkedVehicle vehicle = parkedVehicles.get(vehicleReg);
        if (vehicle == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle is not currently parked");
        }

        LocalDateTime timeOut = LocalDateTime.now();
        LocalDateTime timeIn = vehicle.timeIn();
        int duration = (int) Duration.between(timeIn, timeOut).toMinutes();
        double finalCharge = calculateCharge1(vehicle.carType().vehicleType(), duration);

        parkedVehicles.remove(vehicleReg);
        availableSlots++;
        return new ParkingChargeResponse(
                vehicleReg,
                timeIn,
                timeOut,
                finalCharge);
    }

    public static double calculateCharge1(int vehicleType, int minutes) {

        double rate = switch (vehicleType) {
            case 1 -> 0.10;
            case 2 -> 0.20;
            case 3 -> 0.40;
            default -> throw new IllegalArgumentException(
                    "Vehicle type must be 1, 2 or 3"
            );
        };

        if (minutes < 0) {
            throw new IllegalArgumentException(
                    "Parking duration cannot be negative"
            );
        }

        double charge = rate * minutes;
        charge += ((double) minutes / 5);

        return charge;
    }



    private record ParkedVehicle(CarType carType, LocalDateTime timeIn) {
    }
}