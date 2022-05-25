package com.qicaisheng.parkinglot;

import java.util.Comparator;
import java.util.List;

public class SuperParkingBoy extends ParkingAgent {
    public SuperParkingBoy(List<ParkingLot> parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingLot selectParkingLot() {
        return this.managedParkingLots.stream().max(Comparator.comparing(ParkingLot::availableSpacesRate)).orElse(null);
    }
}
