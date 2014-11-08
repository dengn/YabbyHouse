package com.melbournestore.models;

public class Courier {

    private int id;
    private String login_name;
    private String display_name;
    private String phone_number;
    private String head_icon;
    private String duty_area;
    private String status;
    private String device_token;

    public Courier() {

    }

    public Courier(int id, String login_name, String display_name, String phone_number, String head_icon, String duty_area, String status, String device_token) {
        this.id = id;
        this.login_name = login_name;
        this.display_name = display_name;
        this.phone_number = phone_number;
        this.head_icon = head_icon;
        this.duty_area = duty_area;
        this.status = status;
        this.device_token = device_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginName() {
        return login_name;
    }

    public void setLoginName(String login_name) {
        this.login_name = login_name;
    }

    public String getDisplayName() {
        return display_name;
    }

    public void setDisplayName(String display_name) {
        this.display_name = display_name;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getHeadIcon() {
        return head_icon;
    }

    public void setHeadIcon(String head_icon) {
        this.head_icon = head_icon;
    }

    public String getDutyArea() {
        return duty_area;
    }

    public void setDutyArea(String duty_area) {
        this.duty_area = duty_area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceToken() {
        return device_token;
    }

    public void setDeviceToken(String device_token) {
        this.device_token = device_token;
    }

}
