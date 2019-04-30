package com.example.bishop;

public class Bike {
    private String mName;
    private int mPrice;
    private int mthumbnail;

    public Bike() {

    }

    public Bike(String name, int price, int thumbnail) {
        this.mName = name;
        this.mPrice = price;
        this.mthumbnail = thumbnail;
    }


    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public int getmPrice() {
        return mPrice;
    }

    public void setmPrice(int mPrice) {
        this.mPrice = mPrice;
    }

    public int getMthumbnail() {
        return mthumbnail;
    }

    public void setMthumbnail(int mthumbnail) {
        this.mthumbnail = mthumbnail;
    }
}
