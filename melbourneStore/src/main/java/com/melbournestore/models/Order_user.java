package com.melbournestore.models;

public class Order_user {


    private String delivery_time;
    private int delivery_fee;
    private String remark;
    private String create_time;
    private String update_time;
    private String confirm_time;
    private String distributing_time;
    private String deliverying_time;
    private String complete_time;
    private int status;
    private Plate[] plates;


    public Order_user() {

    }

    public Order_user(String delivery_time
            , int delivery_fee, String remark, String create_time, String update_time, String confirm_time, String distributing_time
            , String deliverying_time, String complete_time, int status, Plate[] plates) {

        this.delivery_time = delivery_time;
        this.delivery_fee = delivery_fee;
        this.remark = remark;
        this.create_time = create_time;
        this.update_time = update_time;
        this.confirm_time = confirm_time;
        this.distributing_time = distributing_time;
        this.deliverying_time = deliverying_time;
        this.complete_time = complete_time;
        this.status = status;
        this.plates = plates;

    }


    public String getDeliveryTime() {
        return delivery_time;
    }

    public void setDeliveryTime(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public int getDeliveryFee() {
        return delivery_fee;
    }

    public void setDeliveryFee(int delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return create_time;
    }

    public void setCreateTime(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdateTime() {
        return update_time;
    }

    public void setUpdateTime(String update_time) {
        this.update_time = update_time;
    }

    public String getConfirmTime() {
        return confirm_time;
    }

    public void setConfirmTime(String confirm_time) {
        this.confirm_time = confirm_time;
    }

    public String getDistributingTime() {
        return distributing_time;
    }

    public void setDistributingTime(String distributing_time) {
        this.distributing_time = distributing_time;
    }

    public String getDeliveryingTime() {
        return deliverying_time;
    }

    public void setDeliveryingTime(String deliverying_time) {
        this.deliverying_time = deliverying_time;
    }

    public String getCompleteTime() {
        return complete_time;
    }

    public void setCompleteTime(String complete_time) {
        this.complete_time = complete_time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Plate[] getPlates() {
        return plates;
    }

    public void setPlates(Plate[] plates) {
        this.plates = plates;
    }

}
