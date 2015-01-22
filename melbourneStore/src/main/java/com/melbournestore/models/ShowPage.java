package com.melbournestore.models;

/**
 * Created by dengn on 2015/1/19.
 */
public class ShowPage {

    private Show[] user_show_list;
    private boolean has_next;
    private Show[] delete_show_list;
    private int next_num;
    private int per_page;

    public ShowPage() {

    }

    public ShowPage(Show[] user_show_list, boolean has_next, Show[] delete_show_list, int next_num, int per_page) {
        this.user_show_list = user_show_list;
        this.has_next = has_next;
        this.delete_show_list = delete_show_list;
        this.next_num = next_num;
        this.per_page = per_page;
    }

    public Show[] getShows() {
        return user_show_list;
    }

    public void setShows(Show[] user_show_list) {
        this.user_show_list = user_show_list;
    }

    public boolean getHasNext() {
        return has_next;
    }

    public void setHasNext(boolean hasNext) {
        this.has_next = has_next;
    }

    public Show[] getDeleteShows() {
        return delete_show_list;
    }

    public void setDeleteShows(Show[] delete_show_list) {
        this.delete_show_list = delete_show_list;
    }

    public int getNextNum() {
        return next_num;
    }

    public void setNextNum(int nextNum) {
        this.next_num = next_num;
    }

    public int getPerPage() {
        return per_page;
    }

    public void setPerPage(int perPage) {
        this.per_page = per_page;
    }
}
