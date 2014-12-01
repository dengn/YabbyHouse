package com.melbournestore.models;

/**
 * Created by OLEDCOMM on 01/12/2014.
 */
public class Coupon {

    private int count;
    private String discount;
    private String update_time;
    private String goods;
    private int status;
    private String name;
    private String valid_type;
    private int type;
    private int id;

    public Coupon(){

    }

    public Coupon(int count, String discount, String update_time, String goods, int status, String name, String valid_type, int type, int id){
        this.count =count;
        this.discount = discount;
        this.update_time = update_time;
        this.goods =goods;
        this.status = status;
        this.name = name;
        this.valid_type = valid_type;
        this.type = type;
        this.id = id;
    }

    public int getCount(){
        return count;
    }

    public String getDiscount(){
        return discount;
    }

    public String getUpdate_time(){
        return update_time;
    }

    public String getGoods(){
        return goods;
    }

    public int getStatus(){
        return status;
    }

    public String getName(){
        return name;
    }

    public String getValid_type(){
        return valid_type;
    }

    public int getType(){
        return type;
    }

    public int getId(){
        return id;
    }

    public void setCount(int count){
        this.count = count;
    }

    public void setDiscount(String discount){
        this.discount = discount;
    }

    public void setUpdate_time(String update_time){
        this.update_time = update_time;
    }

    public void setGoods(String goods){
        this.goods = goods;
    }

    public void setStatus(int status){
        this.status = status;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setValid_type(String valid_type){
        this.valid_type = valid_type;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setId(int id){
        this.id = id;
    }
}
