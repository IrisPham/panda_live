package com.panda.live.pandalive.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.panda.live.pandalive.User.User;

public class PreferencesManager {

    private static final String SHARED_PREFERENCES_NAME = "Panda-live";

    public static final String EXTRA_USER_NAME = "userName";
    public static final String EXTRA_USER_ID = "userId";
    public static final String EXTRA_USER_JOB = "userJob";
    public static final String EXTRA_USER_ADDRESS = "userAddress";
    public static final String EXTRA_USER_AVATAR_LINK = "userAvatarLink";
    public static final String EXTRA_USER_GENDER = "Gender";
    public static final String EXTRA_HOUR_ONLINE = "hourOnline";
    public static final String EXTRA_USER_RANK = "rank";
    public static final String EXTRA_USER_EXPERIENCE = "experience";
    public static final String EXTRA_USER_EDUCATION = "education";
    public static final String EXTRA_USER_COIN = "coin";

    public static final String EXTRA_ACCESS_TOKEN = "accessToken";
    public static final String APPLICATION_ID = "s2pycOnPUVt59AyitWjSKw";
    public static final String API_KEY = "2tmjht1a8zdz36ob7nkprwuz8";
    /**
     * The function is called to set the login accessToken Facebook, Google, Phone.
     * @param context Transfers the current context.
     * @param accessToken Is a code that allows access to the system.
     *
     */
    public static void setAccessToken(Context context, String accessToken){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(EXTRA_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    /**
     * The function is called to get AccessToken
     * @param context Transfers the current context.
     *
     */
    public static String getAccessToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(EXTRA_ACCESS_TOKEN,"");
    }

    /*
    *
    * Save user information
    *
    * */
    public static void saveUserInfo(Context ctx,
                                    String userName,
                                    String id,
                                    String job,
                                    String gender,
                                    String address,
                                    String avatarLink,
                                    String hourOnline,
                                    String rank,
                                    String experience,
                                    String education,
                                    String coin) {
        SharedPreferences sharedPref = ctx.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(EXTRA_USER_NAME, userName);
        editor.putString(EXTRA_USER_ID, id);
        editor.putString(EXTRA_USER_JOB, job);
        editor.putString(EXTRA_USER_GENDER, gender);
        editor.putString(EXTRA_USER_ADDRESS, address);
        editor.putString(EXTRA_USER_AVATAR_LINK, avatarLink);
        editor.putString(EXTRA_HOUR_ONLINE, hourOnline);
        editor.putString(EXTRA_USER_RANK, rank);
        editor.putString(EXTRA_USER_EXPERIENCE, experience);
        editor.putString(EXTRA_USER_EDUCATION, education);
        editor.putString(EXTRA_USER_COIN, coin);
        editor.apply();
    }

    /*
   *
   * Get user information
   *
   * */
    public static User getUserInfo(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);

//        User user = new User(
//                sharedPref.getString(EXTRA_USER_ID,""),
//                "",
//                sharedPref.getString(EXTRA_USER_NAME,""),
//                "",
//                sharedPref.getString(EXTRA_USER_ADDRESS,""),
//                sharedPref.getString(EXTRA_USER_EDUCATION,""),
//                sharedPref.getString(EXTRA_USER_JOB,""),
//                sharedPref.getString(EXTRA_USER_RANK,""),
//                Integer.parseInt(sharedPref.getString(EXTRA_USER_COIN,"")),
//                Integer.parseInt(sharedPref.getString(EXTRA_HOUR_ONLINE,"")),
//                Integer.parseInt(sharedPref.getString(EXTRA_USER_EXPERIENCE,"")));
        return null;
    }


}
