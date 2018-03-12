package com.panda.live.pandalive.data.model;

/**
 * Created by Android Studio on 3/12/2018.
 */

public class PandaModel {
    private int mIdIcon;
    private String mName;

    public PandaModel(int mIdIcon, String mName) {
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
