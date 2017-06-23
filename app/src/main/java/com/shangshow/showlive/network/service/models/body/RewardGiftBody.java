package com.shangshow.showlive.network.service.models.body;

import java.io.Serializable;

/**
 * Created by lorntao on 8/18/16.
 */
public class RewardGiftBody implements Serializable {

    private long videoUserId;
    private long videoId;
    private long gaveProductId;

    public RewardGiftBody(long videoUserId, long videoId, long gaveProductId) {
        this.videoUserId = videoUserId;
        this.videoId = videoId;
        this.gaveProductId = gaveProductId;
    }

    public long getVideoUserId() {

        return videoUserId;
    }

    public void setVideoUserId(long videoUserId) {
        this.videoUserId = videoUserId;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public long getGaveProductId() {
        return gaveProductId;
    }

    public void setGaveProductId(long gaveProductId) {
        this.gaveProductId = gaveProductId;
    }
}
