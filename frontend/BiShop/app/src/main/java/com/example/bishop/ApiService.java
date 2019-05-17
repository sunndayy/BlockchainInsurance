package com.example.bishop;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/sign-in")
    @FormUrlEncoded
    Call<User> SignIn(@Field("username") String username,
                      @Field("password") String password);

    @POST("/sign-up")
    @FormUrlEncoded
    Call<User> SignUp(@Field("username") String username,
                      @Field("password") String password,
                      @Field("identityCard") String identityCard,
                      @Field("birthDay") BirthDay birthDay,
                      @Field("address") String address,
                      @Field("phoneNumber") String phoneNumber,
                      @Field("email") String email);
}
