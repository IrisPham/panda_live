package com.panda.live.pandalive.data.model;

/**
 * Created by Android Studio on 2/1/2018.
 */

public class ChannelModel {
    private int mIdIcon;
    private String mName;

    public ChannelModel(int mIdIcon, String mName) {
        this.mIdIcon = mIdIcon;
        this.mName = mName;
    }

    public int getmIdIcon() {
        return mIdIcon;
    }

    public String getmName() {
        return mName;
    }
}
