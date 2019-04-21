package com.example.bishop;

public class Bike {
    private String id;
    private String name;
    private String describe;
    private int image;
    private int price;
    private String producer;

    public Bike() {

    }

    public Bike(String id, String name, String describe, int image, int price, String producer) {
        this.id = id;
        this.name = name;
        this.describe = describe;
        this.image = image;
        this.price = price;
        this.producer = producer;
    }
}
