package com.qicaisheng.parkinglot;

public class TextReportVisitor extends ReportVisitor {
    
    @Override
    public String visitFromParkingManager(ParkingLot parkingLot) {
        return visit("\t", parkingLot);
    }

    @Override
    public String visit(ParkingManager parkingManager) {
        return visit("", parkingManager);
    }
    
    @Override
    public String visit(ParkingAgent parkingAgent) {
        return visit("\t", parkingAgent);
    }

    @Override
    public String visitFromParkingAgent(ParkingLot parkingLot) {
        return visit("\t\t", parkingLot);
    }

    public String visit(String prefix, ParkingResource parkingResource) {
        return prefix + contentOf(parkingResource) + "\n";
    }
}
