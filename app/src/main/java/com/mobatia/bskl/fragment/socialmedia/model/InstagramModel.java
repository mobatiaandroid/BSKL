package com.mobatia.bskl.fragment.socialmedia.model;

import java.io.Serializable;

/**
 * Created by mobatia on 04/09/18.
 */

public class InstagramModel implements Serializable {
   String type;
   String username;
   String link;
   String text;
   String imagesthumbnail;
   String imageslowres;
   String imageshighres;
   String videosstandard_resolution;
   String videoslow_bandwidth;
   String videoslow_resolution;
   String carousel_mediastandard_resolution;
   String carousel_medialow_bandwidth;
   String carousel_medialow_resolution;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImagesthumbnail() {
        return imagesthumbnail;
    }

    public void setImagesthumbnail(String imagesthumbnail) {
        this.imagesthumbnail = imagesthumbnail;
    }

    public String getImageslowres() {
        return imageslowres;
    }

    public void setImageslowres(String imageslowres) {
        this.imageslowres = imageslowres;
    }

    public String getImageshighres() {
        return imageshighres;
    }

    public void setImageshighres(String imageshighres) {
        this.imageshighres = imageshighres;
    }

    public String getVideosstandard_resolution() {
        return videosstandard_resolution;
    }

    public void setVideosstandard_resolution(String videosstandard_resolution) {
        this.videosstandard_resolution = videosstandard_resolution;
    }

    public String getVideoslow_bandwidth() {
        return videoslow_bandwidth;
    }

    public void setVideoslow_bandwidth(String videoslow_bandwidth) {
        this.videoslow_bandwidth = videoslow_bandwidth;
    }

    public String getVideoslow_resolution() {
        return videoslow_resolution;
    }

    public void setVideoslow_resolution(String videoslow_resolution) {
        this.videoslow_resolution = videoslow_resolution;
    }

    public String getCarousel_mediastandard_resolution() {
        return carousel_mediastandard_resolution;
    }

    public void setCarousel_mediastandard_resolution(String carousel_mediastandard_resolution) {
        this.carousel_mediastandard_resolution = carousel_mediastandard_resolution;
    }

    public String getCarousel_medialow_bandwidth() {
        return carousel_medialow_bandwidth;
    }

    public void setCarousel_medialow_bandwidth(String carousel_medialow_bandwidth) {
        this.carousel_medialow_bandwidth = carousel_medialow_bandwidth;
    }

    public String getCarousel_medialow_resolution() {
        return carousel_medialow_resolution;
    }

    public void setCarousel_medialow_resolution(String carousel_medialow_resolution) {
        this.carousel_medialow_resolution = carousel_medialow_resolution;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
