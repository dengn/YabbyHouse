package com.melbournestore.models;

import java.util.ArrayList;

public class Area {

    private int id;
    private String name;
    private int fee;
    private int status;
    private ArrayList<Suburb> suburbs;
    private String update_time;

    public Area() {

    }

    public Area(int id, String name, int fee, int status, ArrayList<Suburb> suburbs, String update_time) {
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

    public ArrayList<Suburb> getSuburbs() {
        ArrayList<Suburb> suburbList = new ArrayList<Suburb>();
        for (int i = 0; i < suburbs.size(); i++) {
            suburbList.add(suburbs.get(i));
        }
        return suburbList;
    }

    public void setSuburbs(ArrayList<Suburb> suburbs) {
        this.suburbs.clear();
        this.suburbs.addAll(suburbs);
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
        this.update_time = update_time;
    }

}
