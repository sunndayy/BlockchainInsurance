package com.example.bishop;

public class History {
    private String id;
    private String date;
    private int totalPrice;


    public  History(String id, String date, int totalPrice) {
        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
