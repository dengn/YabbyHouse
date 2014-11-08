package com.melbournestore.models;

public class ShoppingCart {

    Order[] order;

    public ShoppingCart() {

    }

    public ShoppingCart(Order[] order) {
        this.order = order;
    }

    public Order[] getOrder() {
        return order;
    }

    public void setOrder(Order[] order) {
        this.order = order;
    }

}
