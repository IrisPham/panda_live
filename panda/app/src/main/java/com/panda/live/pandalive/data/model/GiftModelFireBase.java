package com.panda.live.pandalive.data.model;

/**
 * Created by levan on 04/05/2018.
 */

public class GiftModelFireBase {

    public String name;
    public int value;

    public GiftModelFireBase(){}

    public GiftModelFireBase(String name, int value) {
        this.name = name;
        this.value = value;
    }
    public String getNameGift() {
        return name;
    }
    public int getValueGift() {
        return value;
    }

}
