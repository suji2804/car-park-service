package com.example.carparkservice.service;

import com.example.carparkservice.dto.ParkedCarResponse;
import com.example.carparkservice.request.CarType;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CarParkingServiceTest {

    @Test
    void allocateAndReject() {
        CarParkingService service = new CarParkingService();

        ParkedCarResponse response = service.addCarToSlot(new CarType("car1", 1));

        assertEquals("car1", response.vehicleReg());
        assertNotNull(response.timeIn());
        assertEquals(1, service.getCarParkSlots().availableSlots());
        assertEquals(1, service.getCarParkSlots().occupiedSlots());

        service.addCarToSlot(new CarType("car2", 1));

        assertEquals(0, service.getCarParkSlots().availableSlots());
        assertEquals(2, service.getCarParkSlots().occupiedSlots());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.addCarToSlot(new CarType("car3", 1)));
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals(0, service.getCarParkSlots().availableSlots());
    }



    @Test
    void rejectsInvalidVehicleTypes() {
        CarParkingService service = new CarParkingService();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> service.addCarToSlot(new CarType("car1", 4)));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals(2, service.getCarParkSlots().availableSlots());
    }


}
