package com.panda.live.pandalive.data.remote;

import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Android Studio on 3/11/2018.
 */

public class Command {
    private Command(){

    }

    private static Command mCommand = null;

    public static Command getInstance(){
        if (mCommand == null) {
            mCommand = new Command();
        }
        return mCommand;
    };

//    public void updateProfile(MultipartBody.Part part, Callback<ResponseObject> callback) {
//        MobisciLabService linkBuilder = RetrofitConfig.createLinkBuilder(MobisciLabService.class);
//        Call<ResponseObject> call = linkBuilder.addProfileResource(part);
//        call.enqueue(callback);
//    }
//
//    public void postImage(String token, MultipartBody.Part mull, RequestBody body, Callback<ResponseObject> callback) {
//        MobisciLabService linkBuilder = RetrofitConfig.createLinkBuilder(MobisciLabService.class);
//        Call<ResponseObject> call = linkBuilder.postImage(token, mull, body);
//        call.enqueue(callback);
//    }
//
//    public void getAllUniversity(String token, Callback<UniversityModel> callback) {
//        MobisciLabService linkBuilder = RetrofitConfig.createLinkBuilder(MobisciLabService.class);
//        JsonObject universitiesObject = new JsonObject();
//        universitiesObject.addProperty("access_token", token);
//        Call<UniversityModel> call = linkBuilder.getUniversity(universitiesObject);
//        call.enqueue(callback);
//    }
//
//    public void signIn(String username, String password, String pushyID, Callback<SignInModel> callback) {
//        MobisciLabService linkBuilder = RetrofitConfig.createLinkBuilder(MobisciLabService.class);
//        JsonObject signInObject = new JsonObject();
//        signInObject.addProperty("username", username);
//        signInObject.addProperty("password", password);
//        signInObject.addProperty("pushy_id", pushyID);
//        Call<SignInModel> call = linkBuilder.signIn(signInObject);
//        call.enqueue(callback);
//    }
}
