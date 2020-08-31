package com.qicaisheng.parkinglot;

public class MarkdownReportVisitor extends ReportVisitor {
    
    @Override
    public String visitFromParkingManager(ParkingLot parkingLot) {
        return visit("## ", parkingLot);
    }

    @Override
    public String visit(ParkingManager parkingManager) {
        return visit("# ", parkingManager);
    }
    
    @Override
    public String visit(ParkingAgent parkingAgent) {
        return visit("## ", parkingAgent);
    }

    @Override
    public String visitFromParkingAgent(ParkingLot parkingLot) {
        return visit("### ", parkingLot);
    }

    public String visit(String prefix, ParkingResource parkingResource) {
        return prefix + contentOf(parkingResource) + "\n";
    }
}
