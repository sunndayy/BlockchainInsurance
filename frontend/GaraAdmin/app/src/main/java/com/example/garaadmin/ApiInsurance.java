package com.example.garaadmin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface ApiInsurance {
    @GET("/plans")
    Call<List<InsuranceInfo>> GetInsuranceInfo(@Header("AccessToken") String AccessToken);
}
