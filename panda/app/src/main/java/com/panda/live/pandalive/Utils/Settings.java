package com.panda.live.pandalive.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.bambuser.broadcaster.Broadcaster.AudioSetting;
import com.panda.live.pandalive.BuildConfig;

public class Settings {
    static final String KEY_AUDIO_HIGH = "audio";
    static final String KEY_AUTHOR = "author";
    static final String KEY_CAPSOUND = "capsound";
    static final String KEY_CAPSOUND_SUPPORTED = "capsound_supported";
    static final String KEY_GEOTAG = "geotag";
    private static final String KEY_IRIS_LOGIN = "iris_login";
    private static final String KEY_IRIS_TOKEN = "iris_token";
    static final String KEY_LIVE_TITLE = "live_title";
    static final String KEY_RECORD = "record";
    static final String KEY_RECORD_SUPPORTED = "record_supported";
    static final String KEY_TALKBACK_MIXIN = "talkback_mixin";
    static final String KEY_TALKBACK_SUPPORTED = "talkback_supported";
    static final String KEY_VIDEO_HIGH = "video_high";
    private static Settings sInstance;
    private final SharedPreferences mPrefs;

    public static Settings getInstance(Context ctx) {
        if (sInstance == null) {
            sInstance = new Settings(ctx.getApplicationContext());
        }
        return sInstance;
    }

    private Settings(Context ctx) {
        this.mPrefs = PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public String getAuthor() {
        return this.mPrefs.getString(KEY_AUTHOR, BuildConfig.FLAVOR);
    }

    public void setAuthor(String author) {
        this.mPrefs.edit().putString(KEY_AUTHOR, author).apply();
    }

    public String getLiveTitle() {
        return this.mPrefs.getString(KEY_LIVE_TITLE, BuildConfig.FLAVOR);
    }

    public void setLiveTitle(String title) {
        this.mPrefs.edit().putString(KEY_LIVE_TITLE, title).apply();
    }

    public boolean getVideoQuality() {
        return this.mPrefs.getBoolean(KEY_VIDEO_HIGH, false);
    }

    public AudioSetting getAudioQuality() {
        return this.mPrefs.getBoolean(KEY_AUDIO_HIGH, true) ? AudioSetting.HIGH_QUALITY : AudioSetting.NORMAL_QUALITY;
    }

    public void setTalkbackCapability(boolean capable) {
        this.mPrefs.edit().putBoolean(KEY_TALKBACK_SUPPORTED, capable).apply();
    }

    public boolean getTalkbackCapability() {
        return this.mPrefs.getBoolean(KEY_TALKBACK_SUPPORTED, false);
    }

    public void setRecordLocalCapability(boolean capable) {
        this.mPrefs.edit().putBoolean(KEY_RECORD_SUPPORTED, capable).apply();
    }

    public boolean getRecordLocalCapability() {
        return this.mPrefs.getBoolean(KEY_RECORD_SUPPORTED, false);
    }

    public boolean getRecordLocalCopy() {
        return this.mPrefs.getBoolean(KEY_RECORD, false);
    }

    public void setRecordLocalCopy(boolean record) {
        this.mPrefs.edit().putBoolean(KEY_RECORD, record).apply();
    }

    public boolean getGeotag() {
        return this.mPrefs.getBoolean(KEY_GEOTAG, false);
    }

    public void setGeotag(boolean geotag) {
        this.mPrefs.edit().putBoolean(KEY_GEOTAG, geotag).apply();
    }

    public boolean getCapSound() {
        return this.mPrefs.getBoolean(KEY_CAPSOUND, true);
    }

    public void setCapSound(boolean capsound) {
        this.mPrefs.edit().putBoolean(KEY_CAPSOUND, capsound).apply();
    }

    public void setCapSoundCapability(boolean capsound_supp) {
        this.mPrefs.edit().putBoolean(KEY_CAPSOUND_SUPPORTED, capsound_supp).apply();
    }

    public boolean getCaptureSoundCapability() {
        return this.mPrefs.getBoolean(KEY_CAPSOUND_SUPPORTED, false);
    }

    public String getIrisLogin() {
        return this.mPrefs.getString(KEY_IRIS_LOGIN, BuildConfig.FLAVOR);
    }

    public String getIrisToken() {
        return this.mPrefs.getString(KEY_IRIS_TOKEN, BuildConfig.FLAVOR);
    }

    public void storeIrisCredentials(String login, String token) {
        this.mPrefs.edit().putString(KEY_IRIS_LOGIN, login).putString(KEY_IRIS_TOKEN, token).apply();
    }

    public boolean getTalkbackMixin() {
        return this.mPrefs.getBoolean(KEY_TALKBACK_MIXIN, false);
    }
}
