package com.melbournestore.models;

/**
 * Created by dengn on 2015/1/19.
 */
public class Show {

    private int status;
    private String create_time;
    private int user_id;
    private int id;
    private String show_image;

    public Show() {

    }

    public Show(int status, String create_time, int user_id, int id, String show_image) {
        status = this.status;
        create_time = this.create_time;
        user_id = this.user_id;
        id = this.id;
        show_image = this.show_image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShow_image() {
        return show_image;
    }

    public void setShow_image(String show_image) {
        this.show_image = show_image;
    }
}
