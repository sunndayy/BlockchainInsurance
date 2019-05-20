package com.example.insuranceadmin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInResponse {
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("token")
    @Expose
    private String token;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
