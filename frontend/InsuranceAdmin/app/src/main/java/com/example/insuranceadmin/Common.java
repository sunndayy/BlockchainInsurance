package com.example.insuranceadmin;

public class Common {
    public static String AccessToken = "";

    public static String beautifyPrice(long price) {

        long temp = price;
        String str = "";

        while (temp >= 1000) {
            long i = temp % 1000;
            if (i == 0) {
                str = ".000" + str;
            } else {
                if (i < 10) {
                    str = ".00" + i + str;
                } else {
                    if (i < 100) {
                        str = ".0" + i + str;
                    } else {
                        if (i > 100) {
                            str = "." + i + str;
                        }
                    }
                }
            }

            temp = temp / 1000;
        }

        return String.valueOf(temp) + str + " VNĐ";
    }

    public static String beautifyPercent(double x) {
        double x1 = x * 100;
        return Integer.toString((int) x1) + "%";
    }
}
