package com.panda.live.pandalive.data.model;

/**
 * Created by levan on 28/03/2018.
 */

public class Room {
    public String idRoom;
    public String pwdRoom;
    public boolean isClock;


    public Room(String idRoom, String pwdRoom, boolean isClock){
        this.idRoom = idRoom;
        this.pwdRoom = pwdRoom;
        this.isClock = isClock;
    }
}
