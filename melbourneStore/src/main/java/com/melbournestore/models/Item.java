package com.melbournestore.models;

public class Item {

    private int id;
    private String name;
    private String desc;
    private int price;
    private long stock;
    private int unit;
    private int group_buy;
    private int group_price;
    private String image;
    private int good;
    private int seq;
    private int shop_id;
    private String update_time;

    public Item() {

    }

    public Item(int id, String name, String desc, int price, long stock, int unit, int group_buy, int group_price, String image, int good, int seq, int shop_id, String update_time) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.price = price;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getStock() {
        return stock;
    }

    public void setStock(long stock) {
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

    public int getGroupPrice() {
        return group_price;
    }

    public void setGroupPrice(int group_price) {
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
