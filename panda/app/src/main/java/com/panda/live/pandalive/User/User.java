package com.panda.live.pandalive.User;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    public int houronl;
    public int exp;


    public User(String id, String pwd, String username,
                String rank, String uri, int xu, int houronl, int exp ) {
        this.id = id;
        this.pwd = pwd;
        this.username = username;
        this.rank = rank;
        this.uri = uri;
        this.xu = xu;
        this.houronl = houronl;
        this.exp = exp;

    }


}

