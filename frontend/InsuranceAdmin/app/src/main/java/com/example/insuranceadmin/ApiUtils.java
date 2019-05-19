package com.example.insuranceadmin;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://192.168.1.105:3000";

    public static ApiService getApiService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
