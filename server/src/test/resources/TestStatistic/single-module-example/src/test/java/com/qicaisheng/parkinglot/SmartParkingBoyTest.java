package com.qicaisheng.parkinglot;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SmartParkingBoyTest {
    
    @Test
    public void should_be_picked_up_from_managed_parking_lot_when_smart_park_boy_park_card() {
        ParkingLot parkingLot = new ParkingLot(1);
        ParkingAgent parkingBoy = new SmartParkingBoy(Arrays.asList(parkingLot));
        Car car = new Car();
        
        parkingBoy.park(car);
        
        Car pickedCar = parkingLot.pick(car);
        Assert.assertEquals(car, pickedCar);
    }

    @Test
    public void should_be_picked_up_from_parking_boy_when_smart_park_boy_park_card() {
        ParkingLot parkingLot = new ParkingLot(2);
        ParkingAgent parkingBoy = new SmartParkingBoy(Arrays.asList(parkingLot));
        Car car = new Car();

        parkingBoy.park(car);

        Car pickedCar = parkingBoy.pick(car);
        Assert.assertEquals(car, pickedCar);
    }

    @Test(expected = ParkingLotWithoutTheCar.class)
    public void should_not_be_picked_up_from_parking_boy_when_the_parked_car_has_been_picked_from_managed_parking_lot() {
        ParkingLot parkingLot = new ParkingLot(2);
        ParkingAgent parkingBoy = new SmartParkingBoy(Arrays.asList(parkingLot));
        Car car = new Car();
        parkingBoy.park(car);

        parkingLot.pick(car);

        parkingBoy.pick(car);
    }

    @Test(expected = ParkingLotWithoutTheCar.class)
    public void should_not_be_picked_up_from_parking_boy_again_when_the_parked_car_has_been_picked_from_parking_boy() {
        ParkingLot parkingLot = new ParkingLot(1);
        ParkingAgent parkingBoy = new SmartParkingBoy(Arrays.asList(parkingLot));
        Car car = new Car();
        parkingBoy.park(car);

        parkingBoy.pick(car);

        parkingBoy.pick(car);
    }

    @Test(expected = ParkingLotFullException.class)
    public void should_not_be_parked_from_smart_parking_boy_when_the_managed_parking_lot_is_full() {
        ParkingLot parkingLot = new ParkingLot(1);
        ParkingAgent parkingBoy = new SmartParkingBoy(Arrays.asList(parkingLot));
        Car car = new Car();
        parkingBoy.park(car);

        Car cannotBeParkedCar = new Car();
        parkingBoy.park(cannotBeParkedCar);
    }

    @Test
    public void should_be_picked_from_parking_lot_with_max_available_space_when_smart_parking_boy_park_the_car() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        ParkingAgent parkingBoy = new SmartParkingBoy(Arrays.asList(parkingLot1, parkingLot2));
        Car car = new Car();

        parkingBoy.park(car);


        Assert.assertEquals(car, parkingLot2.pick(car));
    }
}
