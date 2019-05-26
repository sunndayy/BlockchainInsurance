package com.example.policeadmin;

public class ApiUtils {
    private ApiUtils() {
    }

    public static final String BASE_URL = "http://bcpolice.herokuapp.com";

    public static ApiService getApiService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
