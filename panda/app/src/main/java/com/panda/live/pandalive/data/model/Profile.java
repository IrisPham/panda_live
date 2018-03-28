package com.panda.live.pandalive.data.model;

/**
 * Created by levan on 26/03/2018.
 */

public class Profile{
    public String nickName;
    public String location;
    public String education;
    public String job;

    public Profile(String nickName, String location, String education, String job){
        this.nickName = nickName;
        this.location = location;
        this.education = education;
        this.job = job;
    }
}