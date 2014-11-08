package com.melbournestore.models;

public class DeliveryTime {

    private String begin_time;
    private String end_time;
    private int rest_day;
    private int prepare_time;
    private String update_time;

    public DeliveryTime() {

    }

    public DeliveryTime(String begin_time, String end_time, int rest_day, int prepare_time, String update_time) {
        this.begin_time = begin_time;
        this.end_time = end_time;
        this.rest_day = rest_day;
        this.prepare_time = prepare_time;
        this.update_time = update_time;
    }

    public String getBeginTime() {
        return begin_time;
    }

    public void setBeginTime(String begin_time) {
        this.begin_time = begin_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public void setEndTime(String end_time) {
        this.end_time = end_time;
    }

    public int getRestDay() {
        return rest_day;
    }

    public void setRestDay(int rest_day) {
        this.rest_day = rest_day;
    }

    public int getPrepareTime() {
        return prepare_time;
    }

    public void setPrepareTime(int prepare_time) {
        this.prepare_time = prepare_time;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
        this.update_time = update_time;
    }

}
