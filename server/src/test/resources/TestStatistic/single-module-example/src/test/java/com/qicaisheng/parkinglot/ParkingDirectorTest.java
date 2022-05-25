package com.qicaisheng.parkinglot;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ParkingDirectorTest {

    @Test
    public void should_report_manager_managed_parking_lots() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        parkingLot2.park(new Car());
        ParkingManager parkingManager = new ParkingManager(Arrays.asList(parkingLot1, parkingLot2));
        ParkingDirector parkingDirector = new ParkingDirector();
        parkingDirector.manage(parkingManager);

        String report = parkingDirector.managedParkingManager.accept(new TextReportVisitor());
        
        Assert.assertEquals("M 2 3\n\tP 1 1\n\tP 1 2\n", report);
    }
    
    @Test
    public void should_report_manager_managed_parking_lots_and_parking_boys() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        parkingLot2.park(new Car());
        ParkingManager parkingManager = new ParkingManager(Arrays.asList(parkingLot1, parkingLot2));
        
        ParkingAgent parkingAgent = new ParkingBoy(Arrays.asList(parkingLot1));
        parkingAgent.park(new Car());
        
        parkingManager.manager(Arrays.asList(parkingAgent));
        ParkingDirector parkingDirector = new ParkingDirector();
        parkingDirector.manage(parkingManager);

        String report = parkingDirector.managedParkingManager.accept(new TextReportVisitor());

        Assert.assertEquals("M 1 3\n\tP 0 1\n\tP 1 2\n\tB 0 1\n\t\tP 0 1\n", report);
    }

    @Test
    public void should_get_markdown_report_with_manager_managed_parking_lots_and_parking_boys() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        parkingLot2.park(new Car());
        ParkingManager parkingManager = new ParkingManager(Arrays.asList(parkingLot1, parkingLot2));

        ParkingAgent parkingAgent = new ParkingBoy(Arrays.asList(parkingLot1));
        parkingAgent.park(new Car());

        parkingManager.manager(Arrays.asList(parkingAgent));
        ParkingDirector parkingDirector = new ParkingDirector();
        parkingDirector.manage(parkingManager);

        String report = parkingDirector.managedParkingManager.accept(new MarkdownReportVisitor());

        Assert.assertEquals("# M 1 3\n## P 0 1\n## P 1 2\n## B 0 1\n### P 0 1\n", report);
    }

    @Test
    public void should_get_html_report_with_manager_managed_parking_lots_and_parking_boys() {
        ParkingLot parkingLot1 = new ParkingLot(1);
        ParkingLot parkingLot2 = new ParkingLot(2);
        parkingLot2.park(new Car());
        ParkingManager parkingManager = new ParkingManager(Arrays.asList(parkingLot1, parkingLot2));

        ParkingAgent parkingAgent = new ParkingBoy(Arrays.asList(parkingLot1));
        parkingAgent.park(new Car());

        parkingManager.manager(Arrays.asList(parkingAgent));
        ParkingDirector parkingDirector = new ParkingDirector();
        parkingDirector.manage(parkingManager);

        String report = parkingDirector.managedParkingManager.accept(new HTMLReportVisitor());

        Assert.assertEquals("<h2>M 1 3</h2><h3>P 0 1</h3><h3>P 1 2</h3><h3>B 0 1</h3><p>P 0 1</p>", report);
    }
}
