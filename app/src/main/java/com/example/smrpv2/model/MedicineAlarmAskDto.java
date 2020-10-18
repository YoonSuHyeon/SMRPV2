package com.example.smrpv2.model;

import java.util.ArrayList;

public class MedicineAlarmAskDto {
    private long id;
    private String userId;
    private ArrayList<Long> registerId;  //등록된 약 리스트
    private String alarmName; // 알람 이름
    private int dosingPeriod;  //복용기간
    private String startAlarm; //시작 시간
    private String finishAlarm; // 끝나는 시간
    private int oneTimeCapacity;  //1회 복용 횟수
    private String doseType;  //복용 타입

    public MedicineAlarmAskDto(long id, String userId, ArrayList<Long> registerId, String alarmName, int dosingPeriod, String startAlarm, String finishAlarm, int oneTimeCapacity, String doseType) {
        this.id = id;
        this.userId = userId;
        this.registerId = registerId;
        this.alarmName = alarmName;
        this.dosingPeriod = dosingPeriod;
        this.startAlarm = startAlarm;
        this.finishAlarm = finishAlarm;
        this.oneTimeCapacity = oneTimeCapacity;
        this.doseType = doseType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Long> getRegisterId() {
        return registerId;
    }

    public void setRegisterId(ArrayList<Long> registerId) {
        this.registerId = registerId;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public int getDosingPeriod() {
        return dosingPeriod;
    }

    public void setDosingPeriod(int dosingPeriod) {
        this.dosingPeriod = dosingPeriod;
    }

    public String getStartAlarm() {
        return startAlarm;
    }

    public void setStartAlarm(String startAlarm) {
        this.startAlarm = startAlarm;
    }

    public String getFinishAlarm() {
        return finishAlarm;
    }

    public void setFinishAlarm(String finishAlarm) {
        this.finishAlarm = finishAlarm;
    }

    public int getOneTimeCapacity() {
        return oneTimeCapacity;
    }

    public void setOneTimeCapacity(int oneTimeCapacity) {
        this.oneTimeCapacity = oneTimeCapacity;
    }

    public String getDoseType() {
        return doseType;
    }

    public void setDoseType(String doseType) {
        this.doseType = doseType;
    }
}
