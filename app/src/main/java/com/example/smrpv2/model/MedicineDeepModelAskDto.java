package com.example.smrpv2.model;

public class MedicineDeepModelAskDto {
    String lineFront;
    String lineBack;
    String color;
    String printFront;
    String printBack;
    String drugShape;

    public MedicineDeepModelAskDto(String lineFront, String lineBack, String color, String printFront, String printBack, String drugShape) {
        this.lineFront = lineFront;
        this.lineBack = lineBack;
        this.color = color;
        this.printFront = printFront;
        this.printBack = printBack;
        this.drugShape = drugShape;
    }

    public String getLineFront() {
        return lineFront;
    }

    public void setLineFront(String lineFront) {
        this.lineFront = lineFront;
    }

    public String getLineBack() {
        return lineBack;
    }

    public void setLineBack(String lineBack) {
        this.lineBack = lineBack;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPrintFront() {
        return printFront;
    }

    public void setPrintFront(String printFront) {
        this.printFront = printFront;
    }

    public String getPrintBack() {
        return printBack;
    }

    public void setPrintBack(String printBack) {
        this.printBack = printBack;
    }

    public String getDrugShape() {
        return drugShape;
    }

    public void setDrugShape(String drugShape) {
        this.drugShape = drugShape;
    }
}
