package com.panda.live.pandalive.data.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by levan on 23/04/2018.
 */

@IgnoreExtraProperties
public class Post {

    public String id;
    public String pwd;
    public String username;
    public String rank;
    public int coin;
    public int hourOnl;
    public int exp;
    public Profile profile;


    public Post() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Post(String id, String pwd, String username,
                String rank, int coin, int hourOnl, int exp, Profile profile ) {
        this.id = id;
        this.pwd = pwd;
        this.username = username;
        this.rank = rank;
        this.coin = coin;
        this.hourOnl = hourOnl;
        this.exp = exp;
        this.profile = profile;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("pwd", pwd);
        result.put("username", username);
        result.put("rank", rank);
        result.put("coin", coin);
        result.put("hourOnl", hourOnl);
        result.put("exp", exp);
        result.put("profile", profile);



        return result;
    }

}
