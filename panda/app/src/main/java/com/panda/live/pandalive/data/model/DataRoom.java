package com.panda.live.pandalive.data.model;

/**
 * Created by levan on 28/03/2018.
 */

public class DataRoom {
    public int channelId;
    public String avatarLink;
    public String resourceUri;

    public DataRoom(int channelId, String avatarLink, String resourceUri){
        this.channelId = channelId;
        this.avatarLink = avatarLink;
        this.resourceUri = resourceUri;
    }
}
