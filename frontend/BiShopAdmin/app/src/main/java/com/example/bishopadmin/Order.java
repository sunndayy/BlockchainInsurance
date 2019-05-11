package com.example.bishopadmin;

public class Order {
    private String id;
    private String price;
    private String date;

    public Order(String id, String price, String date) {
        this.id = id;
        this.price = price;
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
