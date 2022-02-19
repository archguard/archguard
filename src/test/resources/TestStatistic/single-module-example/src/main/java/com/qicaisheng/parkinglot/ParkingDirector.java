package com.qicaisheng.parkinglot;

public class ParkingDirector {
    
    ParkingManager managedParkingManager;
    
    public void manage(ParkingManager parkingManager) {
        managedParkingManager = parkingManager;
    }

}
