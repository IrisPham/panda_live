package com.panda.live.pandalive.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IrisModel {
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("next")
    @Expose
    private String next;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public class Result {
        @SerializedName("author")
        @Expose
        private String author;
        @SerializedName("created")
        @Expose
        private Long created;
        @SerializedName("height")
        @Expose
        private Long height;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("ingestChannel")
        @Expose
        private String ingestChannel;
        @SerializedName("length")
        @Expose
        private Long length;
        @SerializedName("preview")
        @Expose
        private String preview;
        @SerializedName("resourceUri")
        @Expose
        private String resourceUri;
        @SerializedName("tags")
        @Expose
        private List<Object> tags = null;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("width")
        @Expose
        private Long width;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public Long getCreated() {
            return created;
        }

        public void setCreated(Long created) {
            this.created = created;
        }

        public Long getHeight() {
            return height;
        }

        public void setHeight(Long height) {
            this.height = height;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIngestChannel() {
            return ingestChannel;
        }

        public void setIngestChannel(String ingestChannel) {
            this.ingestChannel = ingestChannel;
        }

        public Long getLength() {
            return length;
        }

        public void setLength(Long length) {
            this.length = length;
        }

        public String getPreview() {
            return preview;
        }

        public void setPreview(String preview) {
            this.preview = preview;
        }

        public String getResourceUri() {
            return resourceUri;
        }

        public void setResourceUri(String resourceUri) {
            this.resourceUri = resourceUri;
        }

        public List<Object> getTags() {
            return tags;
        }

        public void setTags(List<Object> tags) {
            this.tags = tags;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Long getWidth() {
            return width;
        }

        public void setWidth(Long width) {
            this.width = width;
        }

    }
}
