package com.shangshow.showlive.network.test;

import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.http.RetrofitManager;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * api包装类
 */
public class TestApiWrapper {
//    数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
//    请求个数： 数字，大于0
//    第几页：数字，大于0
//    例：
//    http://gank.io/api/data/Android/10/1
//    http://gank.io/api/data/福利/10/1
//    http://gank.io/api/data/iOS/20/2
//    http://gank.io/api/data/all/20/2

    /**
     * 获取妹纸图
     *
     * @return
     */
    public Observable<List<MeiZhi>> getMeiZhiList(long page) {
        return RetrofitManager.getInstance().getTestApiService().getMeiZhiList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Response<List<MeiZhi>>, Observable<List<MeiZhi>>>() {
                    @Override
                    public Observable<List<MeiZhi>> call(final Response<List<MeiZhi>> listResponse) {
                        return Observable.create(new Observable.OnSubscribe<List<MeiZhi>>() {
                            @Override
                            public void call(Subscriber<? super List<MeiZhi>> subscriber) {
                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onNext(listResponse.results);
                                }

                                if (!subscriber.isUnsubscribed()) {
                                    subscriber.onCompleted();
                                }
                            }
                        });
                    }
                });
    }


}
