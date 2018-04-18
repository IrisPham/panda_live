package com.panda.live.pandalive.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by levan on 26/03/2018.
 */

public class Profile{
    public String nickName;
    public String address;
    public String education;
    public String job;
    public String gender;
    public String birthday;

    public Profile(){

    }

    public Profile(String nickName, String address, String education,
                   String job, String gender, String birthday){
        this.nickName = nickName;
        this.address = address;
        this.education = education;
        this.job = job;
        this.gender = gender;
        this.birthday = birthday;
    }
}