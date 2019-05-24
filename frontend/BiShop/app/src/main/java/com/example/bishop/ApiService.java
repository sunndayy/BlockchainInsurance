package com.example.bishop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/sign-in")
    @FormUrlEncoded
    Call<User> SignIn(@Field("username") String username,
                      @Field("password") String password);

    @POST("/sign-up")
    Call<User> SignUp(@Body User user);

    @GET("/products")
    Call<List<Item>> GetProducts();

    @GET("/user-info")
    Call<User> GetUserInfo(@Header("AccessToken") String AccessToken);

    @POST("/create-order")
    Call<Orders> CreateOrder(@Header("AccessToken") String AccessToken,
                           @Body Orders orders);
    @GET("orders-by-user/{username}")
    Call<List<History>> GetOrders(@Header("AccessToken") String AccessToken,
                                  @Path("username") String username);



}
