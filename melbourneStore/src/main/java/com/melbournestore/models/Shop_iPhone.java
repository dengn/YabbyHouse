package com.melbournestore.models;

/**
 * Created by dengn on 2014/11/23.
 */
public class Shop_iPhone {
    private int id;
    private String name;
    private String addr;
    private String contact_number;
    private int seq;
    private int delivery_type;
    private String image;
    private String update_time;
    private categories[] categorieses;
    private String desc;

    public Shop_iPhone() {

    }

    public Shop_iPhone(int id, String name, String addr,
                       String contact_number, int seq, String image, String update_time,
                       categories[] categorieses, String desc) {
        this.id = id;
        this.name = name;
        this.addr = addr;
        this.contact_number = contact_number;
        this.seq = seq;
        this.image = image;
        this.update_time = update_time;
        this.categorieses = categorieses;
        this.desc = desc;
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

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getContactNumber() {
        return contact_number;
    }

    public void setContactNumber(String contact_number) {
        this.contact_number = contact_number;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
        this.update_time = update_time;
    }

    public categories[] getCategories() {
        return categorieses;
    }

    public void setCategories(categories[] categorieses) {
        this.categorieses = categorieses;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}