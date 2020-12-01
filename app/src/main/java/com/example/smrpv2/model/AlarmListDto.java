package com.example.smrpv2.model;

public class AlarmListDto {
    Long id; //알람 id
    String time; //알람 설정시간
    Bool checkDose; // 지났는지 안지났는지 확인

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
