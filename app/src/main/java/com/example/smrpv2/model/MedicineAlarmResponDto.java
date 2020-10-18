package com.example.smrpv2.model;

import com.example.smrpv2.model.SumMedInfo;

import java.util.ArrayList;

public class MedicineAlarmResponDto {
    private Long id;
    private String userInfo;

    private ArrayList<SumMedInfo> regMedicineArrayList;
    private String alarmName;
    private int dosingPeriod;
    private String startAlarm;
    private String finishAlarm;
    private int oneTimeCapacity;
    private String doseType;
}
