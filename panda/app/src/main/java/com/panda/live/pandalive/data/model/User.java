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
    public String uri;
    public int xu;
    public int hourOnl;
    public int exp;



    public User(){

    }

    public User(String id, String pwd, String username,
                String rank, String uri, int xu, int hourOnl, int exp ) {
        this.id = id;
        this.pwd = pwd;
        this.username = username;
        this.rank = rank;
        this.uri = uri;
        this.xu = xu;
        this.hourOnl = hourOnl;
        this.exp = exp;

    }


}

