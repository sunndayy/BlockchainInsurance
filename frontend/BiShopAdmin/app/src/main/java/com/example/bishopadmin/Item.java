package com.example.bishopadmin;

public class Item {
    private String name;
    private long price;
    private int image;

    public Item() {

    }

    public Item(String name, long price, int image) {
        this.name = name;
        this.price = price;
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
}
