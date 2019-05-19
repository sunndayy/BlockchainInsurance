package com.example.bishop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("items")
    @Expose
    private List<Order> items = null;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("error")
    @Expose
    private String error;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Order> getItems() {
        return items;
    }

    public void setItems(List<Order> items) {
        this.items = items;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Orders (String time, List<Order> Orders, Boolean Status, Integer id, String Error) {
        this.time = time;
        this.items = Orders;
        this.status = Status;
        this.id = id;
        this.error = Error;
    }

    public Orders() {

    }
}
