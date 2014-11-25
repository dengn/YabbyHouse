package com.melbournestore.models;

/**
 * Created by dengn on 2014/11/25.
 */
public class categories {

    String update_time;
    String image;
    int id;
    int seq;
    String name;

    public categories() {

    }

    public categories(String update_time, String image, int id, int seq, String name) {
        this.update_time = update_time;
        this.image = image;
        this.id = id;
        this.seq = seq;
        this.name = name;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getImage() {
        return image;
    }

    public int getId() {
        return id;
    }

    public int getSeq() {
        return seq;
    }

    public String getName() {
        return name;
    }
}
