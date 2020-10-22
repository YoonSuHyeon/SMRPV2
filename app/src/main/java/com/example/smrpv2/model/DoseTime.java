package com.example.smrpv2.model;

public class DoseTime {
    private String morning;
    private String lunch;
    private String dinner;

    public DoseTime(String morning, String lunch, String dinner) {
        this.morning = morning;
        this.lunch = lunch;
        this.dinner = dinner;
    }

    public String getMorning() {
        return morning;
    }

    public void setMorning(String morning) {
        this.morning = morning;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLunch(String lunch) {
        this.lunch = lunch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public int[] getDoseTime(){
        int [] doseTimes = new int[3];
        doseTimes[0]= this.morning.equals("Y") ? 1:0;
        doseTimes[1]= this.lunch.equals("Y") ? 1:0;
        doseTimes[2]= this.dinner.equals("Y") ? 1:0;
        return doseTimes;
    }
}
