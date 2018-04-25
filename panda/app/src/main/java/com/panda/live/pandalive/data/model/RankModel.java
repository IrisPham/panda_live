package com.panda.live.pandalive.data.model;

import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by levan on 25/04/2018.
 */

public class RankModel {
    public String numeric;
    public RoundedImageView img;
    public String name;
    public int coin;

    public RankModel(){

    }

    public RankModel(String numeric, RoundedImageView img , String name, int coin){
        this.numeric = numeric;
        this.img = img;
        this.name = name;
        this.coin = coin;
    }

    public String getNumeric(){
        return numeric;
    }

    public RoundedImageView getImg(){
        return img;
    }

    public String getName(){
        return name;
    }

    public int getCoin(){
        return coin;
    }
}
