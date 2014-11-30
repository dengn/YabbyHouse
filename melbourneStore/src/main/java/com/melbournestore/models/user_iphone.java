package com.melbournestore.models;

public class user_iphone {

    private String phone_number;
    private String street;
    private int good_count;
    private String head_icon;
    private String unit_no;
    private Suburb suburb;

    public user_iphone() {

    }

    public user_iphone(String phone_number, String unit_no, String street,
                       int good_count, String head_icon, Suburb suburb) {
        this.phone_number = phone_number;
        this.unit_no = unit_no;
        this.suburb = suburb;
        this.street = street;
        this.head_icon = head_icon;
        this.good_count = good_count;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getUnitNo() {
        return unit_no;
    }

    public void setUnitNo(String unit_no) {
        this.unit_no = unit_no;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Suburb getSuburb() {
        return suburb;
    }

    public void setSuburb(Suburb suburb) {
        this.suburb = suburb;
    }

    public String getHead_icon() {
        return head_icon;
    }

    public void setHead_icon(String head_icon) {
        this.head_icon = head_icon;
    }

    public int getGood_count() {
        return good_count;
    }

    public void setGood_count(int good_count) {
        this.good_count = good_count;
    }
}
