package com.example.bishopadmin;

import android.content.Intent;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/sign-in")
    @FormUrlEncoded
    Call<LogInResponse> LogIn(@Field("username") String username,
                              @Field("password") String password);

    @GET("/products")
    Call<List<Item>> GetProducts();

    @GET("/orders")
    Call<List<Order>> GetOrders(@Header("AccessToken") String AccessToken);

    @Multipart
    @POST("/create-product")
    Call<Item> CreateProduct(@Header("AccessToken") String token,
                             @Part MultipartBody.Part image,
                             @Part("id") RequestBody id,
                             @Part("name") RequestBody name,
                             @Part("describe") RequestBody describe,
                             @Part("type") RequestBody type,
                             @Part("price") RequestBody price,
                             @Part("amount") RequestBody amount,
                             @Part("producer") RequestBody producer);

    @PUT("/update-order/{id}")
    @FormUrlEncoded
    Call<ResponseBody> UpdateOrders(@Path("id") Integer idOrder,
                                    @Header("AccessToken") String token,
                                    @Field("status") Boolean status,
                                    @Field("licensePlate") String licensePlate,
                                    @Field("company") String company,
                                    @Field("contractId") String contractId,
                                    @Field("duration") Integer duration);

}
