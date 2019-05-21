package com.example.insuranceadmin;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://bcinsurence.herokuapp.com";

    public static ApiService getApiService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
