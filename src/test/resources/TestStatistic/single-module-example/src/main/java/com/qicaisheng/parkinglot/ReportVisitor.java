package com.qicaisheng.parkinglot;

public abstract class ReportVisitor {
    private String from;

    public abstract String visitFromParkingManager(ParkingLot parkingLot);

    public abstract String visit(ParkingManager parkingManager);
    
    public abstract String visit(ParkingAgent parkingAgent);

    public abstract String visitFromParkingAgent(ParkingLot parkingLot);

    public String visit(ParkingLot parkingLot) {
        if (getFrom().equals("ParkingManager")) {
            return visitFromParkingManager(parkingLot);
        } else {
            return visitFromParkingAgent(parkingLot);
        }
    }
    
    public String contentOf(ParkingResource parkingResource) {
        return parkingResource.getShortName() + " " + parkingResource.getAvailableSpaces() + " " + parkingResource.getCapacity();
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFrom() {
        return from;
    }
}
