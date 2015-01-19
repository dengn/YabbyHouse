package com.melbournestore.models;

/**
 * Created by dengn on 2015/1/19.
 */
public class ShowPage {

    private Show[] shows;
    private boolean hasNext;
    private Show[] deleteShows;
    private int nextNum;
    private int perPage;

    public ShowPage() {

    }

    public ShowPage(Show[] shows, boolean hasNext, Show[] deleteShows, int nextNum, int perPage) {
        this.shows = shows;
        this.hasNext = hasNext;
        this.deleteShows = deleteShows;
        this.nextNum = nextNum;
        this.perPage = perPage;
    }

    public Show[] getShows() {
        return shows;
    }

    public void setShows(Show[] shows) {
        this.shows = shows;
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public Show[] getDeleteShows() {
        return deleteShows;
    }

    public void setDeleteShows(Show[] deleteShows) {
        this.deleteShows = deleteShows;
    }

    public int getNextNum() {
        return nextNum;
    }

    public void setNextNum(int nextNum) {
        this.nextNum = nextNum;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }
}
