package com.example.insuranceadmin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/sign-in")
    @FormUrlEncoded
    Call<LogInResponse> LogIn(@Field("username") String username,
                              @Field("password") String password);

    @GET("/plans")
    Call<List<InsurancePackage>> GetInsurances(@Header("AccessToken") String AccessToken);
}
