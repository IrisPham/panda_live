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
    public Map<String, Boolean> stars = new HashMap<>();


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

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pwd", pwd);
        result.put("username", username);
        result.put("rank", rank);
        result.put("uri", uri);
        result.put("stars", stars);

        return result;
    }

}

