package com.example.smrpv2.model.alarm_model;

import com.example.smrpv2.model.DoseTime;

/**
 * AlarmItem : 알람 정보
 * 쓰는 곳 : AlarmFragment, AlarmListViewAdapter
 */
public class AlarmItem {


    private String alramName;
    private String startAlram;
    private String finishAlram;
    private DoseTime doseTime;
    private Long alramGroupId; // id 알람 id 와 같다 .
    private String doseType;
    private String dosingPeriod;
    private String RemainTime;
    public AlarmItem(String alramName, String startAlram, String finishAlram, DoseTime doseTime, Long alramGroupId, String doseType, String dosingPeriod) {
        this.alramName = alramName;
        this.startAlram = startAlram;
        this.finishAlram = finishAlram;
        this.doseTime = doseTime;
        this.alramGroupId = alramGroupId;
        this.doseType = doseType;
        this.dosingPeriod = dosingPeriod;
    }

    public String getRemainTime() {
        return RemainTime;
    }

    public void setRemainTime(String remainTime) {
        RemainTime = remainTime;
    }

    public String getAlramName() {
        return alramName;
    }

    public void setAlramName(String alramName) {
        this.alramName = alramName;
    }

    public String getStartAlram() {
        return startAlram;
    }

    public void setStartAlram(String startAlram) {
        this.startAlram = startAlram;
    }

    public String getFinishAlram() {
        return finishAlram;
    }

    public void setFinishAlram(String finishAlram) {
        this.finishAlram = finishAlram;
    }

    public DoseTime getDoseTime() {
        return doseTime;
    }

    public void setDoseTime(DoseTime doseTime) {
        this.doseTime = doseTime;
    }

    public Long getAlramGroupId() {
        return alramGroupId;
    }

    public void setAlramGroupId(Long alramGroupId) {
        this.alramGroupId = alramGroupId;
    }

    public String getDoseType() {
        return doseType;
    }

    public void setDoseType(String doseType) {
        this.doseType = doseType;
    }

    public String getDosingPeriod() {
        return dosingPeriod;
    }

    public void setDosingPeriod(String dosingPeriod) {
        this.dosingPeriod = dosingPeriod;
    }
}