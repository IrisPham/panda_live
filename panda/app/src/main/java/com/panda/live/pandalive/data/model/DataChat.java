package com.panda.live.pandalive.data.model;

/**
 * Created by levan on 29/03/2018.
 */

public class DataChat {


    public String name;
    public String message;

    public  DataChat(){

    }

    public DataChat(String name, String message){

        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

}
