package com.example.garaadmin;

public class ApiUtils {
    private ApiUtils() {
    }

    public static final String BASE_URL = "http://hdshop.herokuapp.com";

    public static ApiService getApiService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
