package com.melbournestore.models;

/**
 * Created by dengn on 01/12/2014.
 */
public class Coupon {

    private int count;
    private String discount;
    private String update_time;
    private String goods;
    private int status;
    private String name;
    private String valid_date;
    private int type;
    private int id;

    public Coupon() {

    }

    public Coupon(int count, String discount, String update_time, String goods, int status, String name, String valid_date, int type, int id) {
        this.count = count;
        this.discount = discount;
        this.update_time = update_time;
        this.goods = goods;
        this.status = status;
        this.name = name;
        this.valid_date = valid_date;
        this.type = type;
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValid_date() {
        return valid_date;
    }

    public void setValid_date(String valid_date) {
        this.valid_date = valid_date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
