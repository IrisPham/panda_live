package com.panda.live.pandalive.Service;

import com.google.gson.JsonObject;
import com.panda.live.pandalive.Utils.PreferencesManager;
import com.panda.live.pandalive.data.model.IrisModel;

import retrofit2.Call;
import retrofit2.Callback;

public class Command {

    private static Command mCommand = null;

    private Command() {

    }

    public static Command getInstance() {
        if (mCommand == null) {
            mCommand = new Command();
        }
        return mCommand;
    }

    public void irisApi(Callback<IrisModel> callback) {
        IrisPlatform linkBuilder = RetrofitConfig.createLinkBuilder(IrisPlatform.class);
        JsonObject irisObject = new JsonObject();

        irisObject.addProperty("Content-Type", "application/json");
        irisObject.addProperty("Authorization", "Bearer "+ PreferencesManager.API_KEY);
        irisObject.addProperty("Acceptn", "application/vnd.bambuser.v1+json");

        Call<IrisModel> call = linkBuilder.irisApi();
        call.enqueue(callback);
    }

}
