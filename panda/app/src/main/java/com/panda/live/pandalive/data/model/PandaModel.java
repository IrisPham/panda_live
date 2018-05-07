package com.panda.live.pandalive.data.model;

/**
 * Created by Android Studio on 3/12/2018.
 */

public class PandaModel {
    private String idRoom;
    private boolean isLock;
    private String pwdRoom;
    private String title;
    private DataRoom data;

    public PandaModel() {
    }

    public PandaModel(String idRoom, boolean isLock, String pwdRoom, String title, DataRoom data) {
        this.idRoom = idRoom;
        this.isLock = isLock;
        this.pwdRoom = pwdRoom;
        this.title = title;
        this.data = data;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    public String getPwdRoom() {
        return pwdRoom;
    }

    public void setPwdRoom(String pwdRoom) {
        this.pwdRoom = pwdRoom;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataRoom getData() {
        return data;
    }

    public void setData(DataRoom data) {
        this.data = data;
    }

}
