package com.example.bishop;

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
}
