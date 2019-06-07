package com.example.bishop;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInsuranceService {
    @GET("/plans")
    Call<List<InsuranceInfo>> GetInsuranceInfo(@Header("AccessToken") String AccessToken);

    @POST("/tx/contract")
    Call<ResponseBody> CreateInsuranceContract(@Header("AccessToken") String AccessToken,
                                               @Body ContractInfo contractInfo);

    @GET("/contracts-by-license-plate/{licenseplate}")
    Call<List<InsuranceOrder>> GetInsuranceByLicensePlate(@Path("licenseplate") String licenseplate,
                                                          @Header("AccessToken") String Accesstoken);
}
