package com.melbournestore.models;

import java.util.ArrayList;

/**
 * Created by dengn on 2014/11/23.
 */
public class Shop_iPhone {
    private int id;
    private String name;
    private String desc;
    private String addr;
    private String contact_number;
    private int seq;
    private int delivery_type;
    private String image;
    private String update_time;
    private Plate[] plates;

    public Shop_iPhone() {

    }

    public Shop_iPhone(int id, String name, String desc, String addr,
                       String contact_number, int seq, String image, String update_time,
                       Plate[] plates) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.addr = addr;
        this.contact_number = contact_number;
        this.seq = seq;
        this.image = image;
        this.update_time = update_time;
        this.plates = plates;
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

    public Plate[] getPlates() {
        return plates;
    }

    public void setPlates(Plate[] plates) {
        this.plates = plates;
    }

    public ArrayList<Plate> getArrayPlates() {
        ArrayList<Plate> plateList = new ArrayList<Plate>();
        for (int i = 0; i < plates.length; i++) {
            plateList.add(plates[i]);
        }
        return plateList;
    }
}