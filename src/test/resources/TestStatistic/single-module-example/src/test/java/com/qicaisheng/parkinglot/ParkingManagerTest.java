package com.qicaisheng.parkinglot;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ParkingManagerTest {

    @Test
    public void should_be_able_to_pick_the_parked_car_when_parking_manager_park_the_car() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        ParkingManager parkingManager = new ParkingManager(Arrays.asList(parkingLot1, parkingLot2));
        
        Car car = new Car();
        
        parkingManager.park(car, parkingLot1);

        Assert.assertEquals(car, parkingManager.pick(car, parkingLot1));
    }

    @Test
    public void should_be_able_to_let_managed_parking_boy_park_and_pick_car() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        ParkingManager parkingManager = new ParkingManager(Arrays.asList(parkingLot1, parkingLot2));
        ParkingBoy parkingBoy = new ParkingBoy(Arrays.asList(parkingLot1, parkingLot2));
        parkingManager.manager(Arrays.asList(parkingBoy));
        Car car = new Car();

        parkingManager.park(car, parkingBoy);
        
        Assert.assertEquals(car, parkingManager.pick(car, parkingBoy));
    }

    @Test
    public void should_be_able_to_let_managed_smart_parking_boy_park_and_pick_car() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        ParkingManager parkingManager = new ParkingManager(Arrays.asList(parkingLot1, parkingLot2));
        ParkingAgent parkingBoy = new SmartParkingBoy(Arrays.asList(parkingLot1, parkingLot2));
        parkingManager.manager(Arrays.asList(parkingBoy));
        Car car = new Car();

        parkingManager.park(car, parkingBoy);

        Assert.assertEquals(1, parkingLot2.getAvailableSpaces());
        Assert.assertEquals(car, parkingManager.pick(car, parkingBoy));
    }

}
