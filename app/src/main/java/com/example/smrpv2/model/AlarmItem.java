package com.example.smrpv2.model;

/**
 * AlarmItem : 알람 정보
 * 쓰는 곳 : AlarmFragment, AlarmListViewAdapter
 */
public class AlarmItem {


    private String alramName;
    private String startAlram;
    private String finishAlram;
    private String oneTimeDose;
    private Long alramGroupId;
    private String doseType;
    private String dosingPeriod;

    public AlarmItem(String alramName, String startAlram, String finishAlram, String oneTimeDose, Long alramGroupId, String doseType, String dosingPeriod) {
        this.alramName = alramName;
        this.startAlram = startAlram;
        this.finishAlram = finishAlram;
        this.oneTimeDose = oneTimeDose;
        this.alramGroupId = alramGroupId;
        this.doseType = doseType;
        this.dosingPeriod = dosingPeriod;
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

    public String getOneTimeDose() {
        return oneTimeDose;
    }

    public void setOneTimeDose(String oneTimeDose) {
        this.oneTimeDose = oneTimeDose;
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