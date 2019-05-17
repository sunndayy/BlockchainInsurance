package com.example.garaadmin;

public class Item {
    private String bienSo;
    private String name;
    private Long money;

    public Item(String bienSo, String name, Long money) {
        this.bienSo = bienSo;
        this.name = name;
        this.money = money;
    }

    public String getBienSo() {
        return bienSo;
    }

    public void setBienSo(String bienSo) {
        this.bienSo = bienSo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }
}
