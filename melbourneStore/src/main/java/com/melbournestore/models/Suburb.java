package com.melbournestore.models;

public class Suburb {

    private int id;
    private String name;
    private String post_code;
    private String update_time;

    public Suburb() {

    }

    public Suburb(int id, String name, String post_code, String update_time) {
        this.id = id;
        this.name = name;
        this.post_code = post_code;
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

    public String getPostCode() {
        return post_code;
    }

    public void setPostCode(String post_code) {
        this.post_code = post_code;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
        this.update_time = update_time;
    }
}
