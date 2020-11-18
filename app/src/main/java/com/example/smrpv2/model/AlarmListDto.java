package com.example.smrpv2.model;

public class AlarmListDto {
    Long id;
    String time;
    Bool checkDose;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bool getCheckDose() {
        return checkDose;
    }

    public void setCheckDose(Bool checkDose) {
        this.checkDose = checkDose;
    }
}
