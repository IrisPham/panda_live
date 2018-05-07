package com.panda.live.pandalive.data.model;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by levan on 25/04/2018.
 */

public class RankModel implements Comparable<RankModel> {
    public Uri uri;
    public String name;
    public int coin;
    private String id;

    public RankModel() {
    }

    public RankModel(Uri uri, String name, int coin, String id) {
        this.uri = uri;
        this.name = name;
        this.coin = coin;
        this.id = id;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int compareTo(RankModel comparestu) {
        int compareage=((RankModel)comparestu).getCoin();
//        /* For Ascending order*/
//        return this.coin-compareage;

        /* For Descending order do like this */
        return compareage-this.coin;
    }
}
