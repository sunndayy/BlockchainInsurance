package com.example.bishop;

import java.util.ArrayList;
import java.util.List;

public class Application {

    public static List<Bike> orderList = new ArrayList<>();

    public static String beaufityPrice(int price) {
        String str = "";
        while (price / 1000 != 0) {
            str += ".000";
            price = price / 1000;
        }
        str = Integer.toString(price) + str + " vnđ";
        return  str;
    }
}
