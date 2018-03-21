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
    public String nickname;
    public String location;
    public String education;
    public String job;
    public String rank;
    public int xu;
    public int houronl;
    public int exp;



    public User(String id, String pwd, String username,String nickname, String location,
                String education,String job, String rank,int xu, int houronl, int exp ) {
        this.id = id;
        this.pwd = pwd;
        this.username = username;
        this.nickname = nickname;
        this.location = location;
        this.education = education;
        this.job = job;
        this.rank = rank;
        this.xu = xu;
        this.houronl = houronl;
        this.exp = exp;

    }

    public class Profile{
        public String nickname;
        public String location;
        public String education;
        public String job;

    }

}

