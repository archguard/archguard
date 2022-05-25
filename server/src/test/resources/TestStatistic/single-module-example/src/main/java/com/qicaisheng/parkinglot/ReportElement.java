package com.qicaisheng.parkinglot;

public interface ReportElement {
    
    String accept(ReportVisitor reportVisitor);
}
