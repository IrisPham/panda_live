package com.panda.live.pandalive.data.model;

/**
 * Created by levan on 04/05/2018.
 */

public class GiftModel {

    private String name;
    private int coin;
    private int image;

    public GiftModel(){}

    public GiftModel(String name, int coin, int image) {
        this.name = name;
        this.coin = coin;
        this.image = image;
    }
    public String getNameGift() {
        return name;
    }

    public int getCoinGift() {
        return coin;
    }

    public int getImageGift() {
        return image;
    }
}
