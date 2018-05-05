package com.panda.live.pandalive.data.model;

import android.net.Uri;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by levan on 25/04/2018.
 */

public class RankModel {
    public String numeric;
    public Uri uri;
    public String name;
    public int coin;

    public RankModel(){

    }

    public void setNumericRank(String numeric) {
        this.numeric = numeric;
    }

    public void setUriRank(Uri uri) {
        this.uri = uri;
    }

    public void setNameRank(String name) {
        this.name = name;
    }

    public void setCoinRank(int coin) {
        this.coin = coin;
    }

    public RankModel(String numeric, Uri uri, String name, int coin){
        this.numeric = numeric;
        this.uri = uri;
        this.name = name;
        this.coin = coin;
    }

    public String getNumeric(){
        return numeric;
    }

    public Uri getUri(){
        return uri;
    }

    public String getName(){
        return name;
    }

    public int getCoin(){
        return coin;
    }
}
