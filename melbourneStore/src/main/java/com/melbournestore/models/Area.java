package com.melbournestore.models;

import java.util.ArrayList;

public class Area {

    private String name;
    private int fee;
    private int status;
    private Suburb[] suburbs;
    private String update_time;

    public Area() {

    }

    public Area(String name, int fee, int status, Suburb[] suburbs, String update_time) {
        this.name = name;
        this.fee = fee;
        this.status = status;
        this.suburbs = suburbs;
        this.update_time = update_time;
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

    public ArrayList<Suburb> getSuburbs() {
        ArrayList<Suburb> suburbList = new ArrayList<Suburb>();
        for (int i = 0; i < suburbs.length; i++) {
            suburbList.add(suburbs[i]);
        }
        return suburbList;
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
