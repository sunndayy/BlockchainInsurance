package com.example.bishop;

public class ItemPopup {
    private String image;
    private String name;
    private Long price;
    private String bienSo;

    public ItemPopup(String image, String name, Long price, String bienSo) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.bienSo = bienSo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }
}
