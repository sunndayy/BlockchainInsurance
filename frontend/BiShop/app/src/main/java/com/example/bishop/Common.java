package com.example.bishop;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Common {

    public static String beautifyPrice(long price) {
        String str = "";
        while (price / 1000 != 0) {
            str += ".000";
            price = price / 1000;
        }
        str = Long.toString(price) + str + " vnđ";
        return  str;
    }

    public static List<Item> cart = new ArrayList<>();

    public static User user = new User(
            "Duong",
            "adsadsa",
            "251096839",
            "Duong",
            null,
            "TP HCM",
            "0919670339",
            "duong@gmail.com",
            "123123",
            ""
    );
}
