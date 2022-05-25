package com.qicaisheng.parkinglot;

import java.util.List;

public class ParkingBoy extends ParkingAgent {
    
    private int parkingLotIndex;
    
    public ParkingBoy(List<ParkingLot> parkingLots) {
        super(parkingLots);
    }
    
    @Override
    public ParkingLot selectParkingLot() {
        ParkingLot parkingLot = managedParkingLots.get(parkingLotIndex);
        parkingLotIndex = (parkingLotIndex + 1) % managedParkingLots.size();
        return parkingLot;
    }
}
