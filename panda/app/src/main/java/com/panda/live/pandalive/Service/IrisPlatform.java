package com.panda.live.pandalive.Service;

import com.google.gson.JsonObject;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.model.IrisModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface IrisPlatform {
    @Headers({"Content-Type: application/json",
            "Authorization: Bearer "+ PreferencesManager.API_KEY,
            "Accept: application/vnd.bambuser.v1+json"})
    @GET("broadcasts")
    Call<IrisModel> irisApi();
}
