package com.example.bishopadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("describe")
    @Expose
    private String describe;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("price")
    @Expose
    private Long price;
    @SerializedName("producer")
    @Expose
    private String producer;
    @SerializedName("image")
    @Expose
    private String image;

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Item() {

    }

    public Item(String id, String name, int type, String describe, long price, int amount, String producer, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.describe = describe;
        this.price = price;
        this.amount = amount;
        this.producer = producer;
        this.image = image;
    }
}
