package com.panda.live.pandalive.User;

/**
 * Created by levan on 26/03/2018.
 */

public class Profile{
    public String nickname;
    public String location;
    public String education;
    public String job;

    public Profile(String nickname, String location, String education, String job){
        this.nickname = nickname;
        this.location = location;
        this.education = education;
        this.job = job;
    }
}