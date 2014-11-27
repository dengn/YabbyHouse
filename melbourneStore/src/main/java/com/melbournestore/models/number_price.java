package com.melbournestore.models;

/**
 * Created by OLEDCOMM on 27/11/2014.
 */
public class number_price {

    int number;
    int price;

    public number_price(){

    }

    public number_price(int number, int price){
        this.number = number;
        this.price = price;
    }

    public int getNumber(){
        return number;
    }

    public int getPrice(){
        return price;
    }
    public void setNumber(){
        this.number = number;
    }
    public void setPrice(){
        this.price = price;
    }
}
