package com.mobatia.bskl.fragment.socialmedia.model;

import java.io.Serializable;

/**
 * Created by mobatia on 04/09/18.
 */

public class YoutubeModel implements Serializable {
    String title;
    String publishedAt;
    String thumbnailsDefault;
    String thumbnailsMedium;
    String thumbnailsHigh;
    String videoId;
    String channelId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getThumbnailsDefault() {
        return thumbnailsDefault;
    }

    public void setThumbnailsDefault(String thumbnailsDefault) {
        this.thumbnailsDefault = thumbnailsDefault;
    }

    public String getThumbnailsMedium() {
        return thumbnailsMedium;
    }

    public void setThumbnailsMedium(String thumbnailsMedium) {
        this.thumbnailsMedium = thumbnailsMedium;
    }

    public String getThumbnailsHigh() {
        return thumbnailsHigh;
    }

    public void setThumbnailsHigh(String thumbnailsHigh) {
        this.thumbnailsHigh = thumbnailsHigh;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
