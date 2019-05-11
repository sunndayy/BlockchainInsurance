package com.example.insuranceadmin;

public class InsurancePackage {
    private String id;
    private String name;
    private String price;
    private String date;

    public InsurancePackage(String id, String name, String price, String date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
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
