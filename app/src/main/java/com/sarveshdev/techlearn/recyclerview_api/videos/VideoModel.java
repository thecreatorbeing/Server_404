package com.sarveshdev.techlearn.recyclerview_api.videos;

public class VideoModel {


    //TODO: add 'id' property!
    private String videoUrl, title, thumbnailUrl, desc;

    public VideoModel() {
    }

    public VideoModel(String videoUrl, String title, String thumbnailUrl, String desc) {
        this.videoUrl = videoUrl;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
