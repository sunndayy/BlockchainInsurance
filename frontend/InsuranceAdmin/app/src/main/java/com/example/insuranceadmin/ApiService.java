package com.example.insuranceadmin;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/sign-in")
    @FormUrlEncoded
    Call<LogInResponse> LogIn(@Field("username") String username,
                              @Field("password") String password);

    @GET("/plans")
    Call<List<InsurancePackage>> GetInsurances(@Header("AccessToken") String AccessToken);

    @POST("/tx/plan")
    Call<ResponseBody> CreateInsurance(@Header("AccessToken") String AccessToken,
                                       @Body InsurancePost insurancePost);

    @GET("/contracts")
    Call<List<Order>> GetContracts(@Header("AccessToken") String AccessToken);

    @GET("/txs")
    Call<List<Tx>> GetTxs(@Header("AccessToken") String AccessToken);

    @PUT("/tx/{id}")
    @FormUrlEncoded
    Call<ResponseBody> UpdateOrder(@Path("id") Integer id,
                                   @Header("AccessToken") String AccessToken,
                                   @Field("status") Boolean status);
}