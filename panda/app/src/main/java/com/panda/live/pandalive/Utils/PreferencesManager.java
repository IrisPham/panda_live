package com.panda.live.pandalive.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {

    private static final String SHARED_PREFERENCES_NAME = "PREFERENCE_FILE_KEY";

    private SharedPreferences mSharedPreferences;

    private static PreferencesManager sInstance;

    public static PreferencesManager getInstance() {
        if (sInstance == null) {
            sInstance = new PreferencesManager();
        }
        return sInstance;
    }

    private PreferencesManager() {

    }

    public void init(Context context) {
        mSharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void clearData() {
        mSharedPreferences.edit().clear().apply();
    }

    /**
     * Save authentication
     */
    public void saveStringData(String key, String data) {
        mSharedPreferences.edit()
                .putString(key, data).apply();
    }

    /**
     * Get authentication
     */
    public String getStringData(String key) {
        return mSharedPreferences.getString(key, "");
    }
}
