package com.shangshow.showlive.model;

import android.content.Context;

import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.model.port.ILiveModel;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.body.StartLiveBody;
import com.shangshow.showlive.network.service.models.body.VideoOffLiveBody;

import rx.Subscription;

public class LiveModel extends BaseModel implements ILiveModel {
    public LiveModel(Context context) {
        super(context);
    }

    @Override
    public void startLive(StartLiveBody startLiveBody, final Callback<LiveInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.startLive(startLiveBody).subscribe(new NewSubscriber<LiveInfo>(context, true) {
            @Override
            public void onNext(LiveInfo liveInfo) {
                callback.onSuccess(liveInfo);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void offLive(String closeType, VideoOffLiveBody videoOffLiveBody, final Callback<String> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.offLive(closeType, videoOffLiveBody).subscribe(new NewSubscriber<String>(context, true) {
            @Override
            public void onNext(String s) {
                callback.onSuccess(s);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getVideoRoomList(String videoType, final PageBody pageBody, final Callback<Pager<VideoRoom>> callback, boolean hasProgress) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getVideoRoomList(videoType, pageBody).subscribe(new NewSubscriber<Pager<VideoRoom>>(context, hasProgress) {
            @Override
            public void onNext(Pager<VideoRoom> pager) {
                callback.onSuccess(pager);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

}
