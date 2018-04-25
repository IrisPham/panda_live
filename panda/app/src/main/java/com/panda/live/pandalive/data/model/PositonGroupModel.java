package com.panda.live.pandalive.data.model;

public class PositonGroupModel {
    private int position;
    private boolean state;
    private Data data;

    public PositonGroupModel() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        private String memberId;
        private String resourceUrl;

        public Data() {
        }

        public Data(String memberId, String resourceUrl) {
            this.memberId = memberId;
            this.resourceUrl = resourceUrl;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getResourceUrl() {
            return resourceUrl;
        }

        public void setResourceUrl(String resourceUrl) {
            this.resourceUrl = resourceUrl;
        }
    }



}
