package com.melbournestore.models;

/**
 * Created by dengn on 2014/11/23.
 */
public class advertisements {
    private String update_time;
    private String contact_number;
    private String name;
    private int seq;
    private String address;
    private String pic;
    private int id;
    private String desc;

    public advertisements() {

    }

    public advertisements(String update_time, String contact_number, String name, int seq, String address, String pic, int id, String desc) {
        this.update_time = update_time;
        this.contact_number = contact_number;
        this.name = name;
        this.seq = seq;
        this.address = address;
        this.pic = pic;
        this.id = id;
        this.desc = desc;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getContact_number() {
        return contact_number;
    }

    public String getName() {
        return name;
    }

    public int getSeq() {
        return seq;
    }

    public String getAddress() {
        return address;
    }

    public String getPic() {
        return pic;
    }

    public int getId() {
        return id;
    }

    public String getDesc() {
        return desc;
    }
}
