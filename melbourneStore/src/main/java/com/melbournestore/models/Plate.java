package com.melbournestore.models;

public class Plate {

    private int mPrice;
    private String mName;
    private int mNumber;
    private int mStockMax;
    private int mLikeNum;
    private int mImageId;
    private int mShopId;
    private int mPlateId;


    public Plate() {

    }

    public Plate(int price, String name, int number, int stockMax,
                 int likeNum, int imageId, int shopId, int plateId) {
        mPrice = price;
        mName = name;
        mNumber = number;
        mStockMax = stockMax;
        mLikeNum = likeNum;
        mImageId = imageId;
        mShopId = shopId;
        mPlateId = plateId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getNumber() {
        return mNumber;
    }

    public void setNumber(int number) {
        mNumber = number;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    public int getStockMax() {
        return mStockMax;
    }

    public void setStockMax(int stockMax) {
        mStockMax = stockMax;
    }

    public int getImageId() {
        return mImageId;
    }

    public void setImageId(int imageId) {
        mImageId = imageId;
    }

    public int getLikeNum() {
        return mLikeNum;
    }

    public void setLikeNum(int likeNum) {
        mLikeNum = likeNum;
    }

    public int getShopId() {
        return mShopId;
    }

    public void setShopId(int shopId) {
        mShopId = shopId;
    }

    public int getPlateId() {
        return mPlateId;
    }

    public void setPlateId(int plateId) {
        mPlateId = plateId;
    }

}
