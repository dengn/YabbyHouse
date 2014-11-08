package com.melbournestore.models;

public class OrderItem {

    private int id;
    private int item_id;
    private String name;
    private String desc;
    private int price;
    private float count;

    public OrderItem() {

    }

    public OrderItem(int id, int item_id, String name, String desc, int price, float count) {
        this.id = id;
        this.item_id = item_id;
        this.name = name;
        this.desc = desc;
        this.price = price;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return item_id;
    }

    public void setItemId(int item_id) {
        this.item_id = item_id;
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

    public float getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
