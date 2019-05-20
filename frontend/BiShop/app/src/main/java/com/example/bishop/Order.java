package com.example.bishop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("price")
    @Expose
    private Long price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Order() {

    }

    public Order(String id, Long price) {
        this.id = id;
        this.price = price;
    }
}
