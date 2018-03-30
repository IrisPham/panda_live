package com.panda.live.pandalive.data.model;

/**
 * Created by levan on 28/03/2018.
 */

public class DataRoom {
    public int channelId;
    public String avatarLink;
    public String resourceUri;

    public DataRoom() {
    }

    public DataRoom(int channelId, String avatarLink, String resourceUri) {
        this.channelId = channelId;
        this.avatarLink = avatarLink;
        this.resourceUri = resourceUri;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getAvatarLink() {
        return avatarLink;
    }

    public void setAvatarLink(String avatarLink) {
        this.avatarLink = avatarLink;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }
}
