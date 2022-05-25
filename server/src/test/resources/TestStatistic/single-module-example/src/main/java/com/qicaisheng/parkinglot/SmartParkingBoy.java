package com.qicaisheng.parkinglot;

import java.util.Comparator;
import java.util.List;

public class SmartParkingBoy extends ParkingAgent {
    
    public SmartParkingBoy(List<ParkingLot> parkingLots) {
        super(parkingLots);
    }
    
    @Override
    public ParkingLot selectParkingLot() {
        return this.managedParkingLots.stream().max(Comparator.comparing(ParkingLot::getAvailableSpaces)).orElse(null);
    }
}
