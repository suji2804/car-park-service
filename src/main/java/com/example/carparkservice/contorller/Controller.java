package com.example.carparkservice.contorller;

import com.example.carparkservice.dto.CarParkingSlotsResponse;
import com.example.carparkservice.dto.ParkingChargeResponse;
import com.example.carparkservice.dto.ParkedCarResponse;
import com.example.carparkservice.request.CarType;
import com.example.carparkservice.service.CarParkingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller{

    private final CarParkingService carParkingService;

    public Controller(CarParkingService carParkingService) {
        this.carParkingService = carParkingService;
    }

    @GetMapping("/parking")
    @Operation(summary = "Get available and occupied parking spaces", description = "Returns the number of available and occupied parking spaces.")
    public CarParkingSlotsResponse getCarParkSlots() {
        return carParkingService.getCarParkSlots();
    }

    @PostMapping(path = "/parkCar", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Park a vehicle", description = "Allocates an available parking space.")
    public ParkedCarResponse addCarToSlot(
            @RequestBody CarType carType) {
        return carParkingService.addCarToSlot(carType);
    }

    @PostMapping("/parking/bill")
    @Operation(summary = "Free up a parking space and return the final charge", description = "Frees up the parking space for the vehicle with the given registration number and returns the final charge based on the parking duration.")
    public ParkingChargeResponse removeCarFromSlot(@RequestParam String vehicleReg) {
        return carParkingService.removeCarFromSlot(vehicleReg);
    }
}
