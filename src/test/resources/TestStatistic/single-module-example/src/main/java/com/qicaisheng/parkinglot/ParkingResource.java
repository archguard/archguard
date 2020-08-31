package com.qicaisheng.parkinglot;

public interface ParkingResource extends ReportElement {

    String getShortName();

    int getAvailableSpaces();

    int getCapacity();

}
