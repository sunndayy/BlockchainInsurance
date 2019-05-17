package com.example.bishopadmin;

public class Item {
    private String id;
    private String name;
    private String describe;
    private int type;
    private long price;
    private int amount;
    private String producer;
    private int image;

    public Item() {

    }

    public Item(String id, String name, String describe, int type,
                long price, int amount, String producer, int image) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.type = type;
        this.price = price;
        this.amount = amount;
        this.producer = producer;
        this.image = image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }
}
