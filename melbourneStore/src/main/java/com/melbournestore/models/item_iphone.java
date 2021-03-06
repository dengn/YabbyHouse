package com.melbournestore.models;

public class item_iphone {

    private int id;
    private String name;
    private String desc;
    private String price;
    private int category_id;
    private int stock;
    private int unit;
    private int group_buy;
    private Float group_price;
    private String image;
    private int good;
    private int seq;
    private int shop_id;
    private String update_time;

    public item_iphone() {

    }

    public item_iphone(int id, String name, String desc, String price, int category_id, int stock, int unit, int group_buy, Float group_price, String image, int good, int seq, int shop_id, String update_time) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.category_id = category_id;
        this.stock = stock;
        this.unit = unit;
        this.group_buy = group_buy;
        this.group_price = group_price;
        this.image = image;
        this.good = good;
        this.seq = seq;
        this.shop_id = shop_id;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public int getGroupBuy() {
        return group_buy;
    }

    public void setGroupBuy(int group_buy) {
        this.group_buy = group_buy;
    }

    public Float getGroupPrice() {
        return group_price;
    }

    public void setGroupPrice(Float group_price) {
        this.group_price = group_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGood() {
        return good;
    }

    public void setGood(int good) {
        this.good = good;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getShopId() {
        return shop_id;
    }

    public void setShopId(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
        this.update_time = update_time;
    }

}
