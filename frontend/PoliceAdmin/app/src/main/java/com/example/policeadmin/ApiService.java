package com.example.policeadmin;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
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

    @GET("/orders")
    Call<List<Item>> GetOrders(@Header("AccessToken") String AccessToken);

    @PUT("/order/{uid}")
    @FormUrlEncoded
    Call<ResponseBody> CreateLicensePlate(@Path("uid") String uid,
                                          @Header("AccessToken") String AccessToken,
                                          @Field("policeInfo.licensePlate") String licnesePlate);

}