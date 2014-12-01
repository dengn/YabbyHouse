package com.melbournestore.models;

/**
 * Created by OLEDCOMM on 01/12/2014.
 */
public class user_coupon {

    private int id;
    private int user_id;
    private int status;
    private String update_time;
    private Coupon coupon;

    public user_coupon(){

    }

    public user_coupon(int id, int user_id, int status, String update_time, Coupon coupon){
        this.id = id;
        this.user_id = user_id;
        this.status = status;
        this.update_time = update_time;
        this.coupon = coupon;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public int getUser_id(){
        return user_id;
    }
    public void setUser_id(int user_id){
        this.user_id = user_id;
    }
    public int getStatus(){
        return status;
    }
    public void setStatus(){
        this.status = status;
    }
    public String getUpdate_time(){
        return update_time;
    }
    public void setUpdate_time(String update_time){
        this.update_time = update_time;
    }
    public Coupon getCoupon(){
        return coupon;
    }
    public void setCoupon(Coupon coupon){
        this.coupon = coupon;
    }
}
