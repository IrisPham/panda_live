package com.panda.live.pandalive.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by levan on 15/03/2018.
 */
@IgnoreExtraProperties
public class User {

    public String id;
    public String pwd;
    public String username;
    public String rank;
    public int coin;
    public int hourOnl;
    public int exp;



    public User(){

    }

    public User(String id, String pwd, String username,
                String rank, int coin, int hourOnl, int exp ) {
        this.id = id;
        this.pwd = pwd;
        this.username = username;
        this.rank = rank;
        this.coin = coin;
        this.hourOnl = hourOnl;
        this.exp = exp;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    public int getHourOnl() {
        return hourOnl;
    }

    public void setHourOnl(int hourOnl) {
        this.hourOnl = hourOnl;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}

