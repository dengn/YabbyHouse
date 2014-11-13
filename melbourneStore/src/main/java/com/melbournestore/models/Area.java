package com.melbournestore.models;

public class Area {

    private int id;
    private String name;
    private int fee;
    private int status;
    private Suburb[] suburbs;
    private String update_time;

    public Area() {

    }

    public Area(int id, String name, int fee, int status, Suburb[] suburbs, String update_time) {
        this.id = id;
        this.name = name;
        this.fee = fee;
        this.status = status;
        this.suburbs = suburbs;
        this.update_time = update_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Suburb[] getSuburbs() {
        return suburbs;
    }

    public void setSuburbs(Suburb[] suburbs) {
        this.suburbs = suburbs;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
        this.update_time = update_time;
    }

}
