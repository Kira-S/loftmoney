package com.ks.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {
    @SerializedName("status") String status;
    @SerializedName("id") int userId;
    @SerializedName("auth_token") String authToken;

    public String getStatus() {
        return status;
    }

    public int getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }
}
