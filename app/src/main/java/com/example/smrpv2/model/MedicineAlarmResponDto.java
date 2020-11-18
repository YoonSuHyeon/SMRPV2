package com.example.smrpv2.model;

import com.example.smrpv2.model.SumMedInfo;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

//유저가 등록란 알람 정보
public class MedicineAlarmResponDto {
    private Long id;

    private String userInfo;

    private ArrayList<SumMedInfo> regMedicineArrayList;


    private String alarmName;
    private int dosingPeriod;
    private String startAlarm;
    private String finishAlarm;
    private DoseTime doseTime;
    private String doseType;
    private List<AlarmListDto> alarmListList;

    public List<AlarmListDto> getAlarmListList() {
        return alarmListList;
    }

    public void setAlarmListList(List<AlarmListDto> alarmListList) {
        this.alarmListList = alarmListList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public ArrayList<SumMedInfo> getRegMedicineArrayList() {
        return regMedicineArrayList;
    }

    public void setRegMedicineArrayList(ArrayList<SumMedInfo> regMedicineArrayList) {
        this.regMedicineArrayList = regMedicineArrayList;
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

    public DoseTime getDoseTime() {
        return doseTime;
    }

    public void setDoseTime(DoseTime doseTime) {
        this.doseTime = doseTime;
    }

    public String getDoseType() {
        return doseType;
    }

    public void setDoseType(String doseType) {
        this.doseType = doseType;
    }
}
