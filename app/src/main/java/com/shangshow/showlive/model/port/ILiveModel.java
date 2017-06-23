package com.shangshow.showlive.model.port;


import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.body.StartLiveBody;
import com.shangshow.showlive.network.service.models.body.VideoOffLiveBody;

/**
 * 直播相关
 */
public interface ILiveModel {
    /**
    * 开启直播
    */
    void startLive(StartLiveBody startLiveBody, Callback<LiveInfo> callback);

    /**
     * 关闭直播【closeType:VIDEO-SAVE 保存视屏】|【VIDEO-CLOSE : 直接关闭不保存】
     */
    void offLive(String closeType, VideoOffLiveBody videoOffLiveBody, Callback<String> callback);

    /**
     * 直播间列表查询
     */
    void getVideoRoomList(String videoType, PageBody pageBody, Callback<Pager<VideoRoom>> callback, boolean hasProgress);

}
