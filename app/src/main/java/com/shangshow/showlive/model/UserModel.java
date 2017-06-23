package com.shangshow.showlive.model;

import android.content.Context;

import com.google.gson.Gson;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.model.ImageInfo;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.model.port.IUserModel;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.CreateSubscriber;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.AdsInfo;
import com.shangshow.showlive.network.service.models.CooperationApplyDetail;
import com.shangshow.showlive.network.service.models.CooperationApplyInfo;
import com.shangshow.showlive.network.service.models.DiamondEarnModel;
import com.shangshow.showlive.network.service.models.Goods;
import com.shangshow.showlive.network.service.models.LabelInfo;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.OrderInfo;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.Recharge;
import com.shangshow.showlive.network.service.models.RewardInfo;
import com.shangshow.showlive.network.service.models.UserAddress;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.VideoStatistic;
import com.shangshow.showlive.network.service.models.body.AddressBody;
import com.shangshow.showlive.network.service.models.body.BasePageBody;
import com.shangshow.showlive.network.service.models.body.CooperationPageBody;
import com.shangshow.showlive.network.service.models.body.FavouriteApplyBody;
import com.shangshow.showlive.network.service.models.body.GoodsPageBody;
import com.shangshow.showlive.network.service.models.body.HomeHotFirstBody;
import com.shangshow.showlive.network.service.models.body.HotMoreListBody;
import com.shangshow.showlive.network.service.models.body.MerchantApplyBody;
import com.shangshow.showlive.network.service.models.body.OrderPageBody;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.body.PayOrderDto;
import com.shangshow.showlive.network.service.models.body.PayRecBody;
import com.shangshow.showlive.network.service.models.body.RewardGiftBody;
import com.shangshow.showlive.network.service.models.body.StarApplyBody;
import com.shangshow.showlive.network.service.models.body.StartLiveBody;
import com.shangshow.showlive.network.service.models.body.UserBody;
import com.shangshow.showlive.network.service.models.body.UserBusinessCooperationBody;
import com.shangshow.showlive.network.service.models.body.VideoOffLiveBody;
import com.shangshow.showlive.network.service.models.body.WeChatUserInfo;
import com.shangshow.showlive.network.service.models.body.YoutubeListBody;
import com.shangshow.showlive.network.service.models.requestJson.BuyProductRequest;
import com.shangshow.showlive.network.service.models.responseBody.BuyProductResponse;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;
import com.shangshow.showlive.network.service.models.responseBody.PGCVideoInfo;
import com.shangshow.showlive.network.service.models.responseBody.VideoRemind;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import rx.Subscription;

public class UserModel extends BaseModel implements IUserModel {
    public UserModel(Context context) {
        super(context);
    }

    @Override
    public void register(UserBody userBody, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.register(userBody)
                .subscribe(new NewSubscriber<UserInfo>(context, false) {
                    @Override
                    public void onNext(UserInfo user) {
                        CacheCenter.getInstance().setCurrUser(user);
                        //登陆云信
                        ShangXiuUtil.loginNim(user.userId);
                        callback.onSuccess(user);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public void login(UserBody userBody, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.login(userBody)
                .subscribe(new NewSubscriber<UserInfo>(context, true) {
                    @Override
                    public void onNext(UserInfo user) {
                        CacheCenter.getInstance().setCurrUser(user);
                        //登陆云信
                        ShangXiuUtil.loginNim(user.userId);
                        callback.onSuccess(user);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
//        Gson gson = new Gson();
//        String json = gson.toJson(userBody);
//        ApiWrapper apiWrapper = new ApiWrapper();
//        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
//        final Call<Response<Object>> call = apiWrapper.login(body);
//        call.enqueue(new retrofit2.Callback<Response<Object>>() {
//            @Override
//            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
//                try {
//                    LinkedTreeMap<String, Object> object = (LinkedTreeMap<String, Object>) response.body().result;
//                    UserInfo user = new UserInfo();
//                    user.avatarUrl = object.get("avatarUrl") + "";
//                    user.gender = object.get("gender") + "";
//                    user.address = object.get("address") + "";
//                    user.applyTime = object.get("applyTime") + "";
//                    user.createAt = DateUtils.getTime(object.get("createAt") + "") + "";
//                    user.createBy = Long.parseLong(object.get("createBy") + "") + "";
//                    user.favourites = object.get("favourites") + "";
//                    user.follwers = object.get("follwers") + "";
//                    user.friends = object.get("friends") + "";
//                    user.labelName = object.get("labelName") + "";
//                    user.modifyAt = DateUtils.getTime(object.get("modifyAt") + "") + "";
//                    user.modifyBy = Long.parseLong(object.get("modifyBy") + "") + "";
//                    user.status = object.get("status") + "";
//                    double u = Double.parseDouble(object.get("userId") + "");
//                    user.userId = (long) u;
//                    user.userName = object.get("userName") + "";
//                    user.userType = object.get("userType") + "";
//                    user.videos = object.get("videos") + "";
//                    CacheCenter.getInstance().setCurrUser(user);
//                    //登陆云信
//                    ShangXiuUtil.loginNim(user.userId);
//                    callback.onSuccess(user);
//                } catch (NumberFormatException e) {
//                    e.printStackTrace();
//                } catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Response<Object>> call, Throwable t) {
//                callback.onFailure(10004, t.getMessage());
//            }
//        });
    }

    @Override
    public void wechatLogin(String code, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.wechatLogin(code)
                .subscribe(new NewSubscriber<UserInfo>(context, true) {
                    @Override
                    public void onNext(UserInfo user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void wechatLogin2(WeChatUserInfo weChatUserInfo, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.wechatLogin2(weChatUserInfo)
                .subscribe(new NewSubscriber<UserInfo>(context, true) {
                    @Override
                    public void onNext(UserInfo user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void smsCaptcha(UserBody userBody, final Callback<String> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.smsCaptcha(userBody)
                .subscribe(new NewSubscriber<String>(context, false) {
                    @Override
                    public void onNext(String smsCaptcha) {
                        callback.onSuccess(smsCaptcha);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getUserInfo(long userId, final Callback<UserInfo> callback, boolean hasProgress, final boolean show) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getUserInfo(userId)
                .subscribe(new NewSubscriber<UserInfo>(context, hasProgress) {
                    @Override
                    public void onNext(UserInfo user) {
                        CacheCenter.getInstance().setCurrUser(user);
                        callback.onSuccess(user);
                    }

                    @Override
                    public void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    public void getUserInfo(long userId, final Callback<UserInfo> callback, boolean hasProgress) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getUserInfo(userId)
                .subscribe(new NewSubscriber<UserInfo>(context, hasProgress) {
                    @Override
                    public void onNext(UserInfo user) {
                        callback.onSuccess(user);
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
    public void updateUserInfo(UserInfo userInfo, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.updateUserInfo(userInfo)
                .subscribe(new NewSubscriber<UserInfo>(context, true) {
                    @Override
                    public void onNext(UserInfo userInfo) {
                        //更新用户信息，修改缓存
                        CacheCenter.getInstance().setCurrUser(userInfo);
                        callback.onSuccess(userInfo);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void restPwd(UserBody userBod, final Callback<String> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.restPwd(userBod)
                .subscribe(new NewSubscriber<String>(context, true) {
                    @Override
                    public void onNext(String string) {
                        callback.onSuccess(string);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void bindingMobile(UserBody userBod, final Callback<String> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.bindingMobile(userBod)
                .subscribe(new NewSubscriber<String>(context, true) {
                    @Override
                    public void onNext(String string) {
                        callback.onSuccess(string);
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
    public void friends(PageBody pageBody, final Callback<Pager<UserInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.friends(pageBody).subscribe(new NewSubscriber<Pager<UserInfo>>(context, true) {
            @Override
            public void onNext(Pager<UserInfo> userInfoPager) {
                callback.onSuccess(userInfoPager);
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
    public void follwers(PageBody pageBody, final Callback<Pager<UserInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.follwers(pageBody).subscribe(new NewSubscriber<Pager<UserInfo>>(context, true) {
            @Override
            public void onNext(Pager<UserInfo> userInfoPager) {
                callback.onSuccess(userInfoPager);
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
    public void addFriend(long userId, final Callback<EditFriendBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.addFriend(userId).subscribe(new NewSubscriber<EditFriendBody>(context, true) {
            @Override
            public void onNext(EditFriendBody editFriendBody) {
                callback.onSuccess(editFriendBody);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void cancelFriend(long userId, final Callback<EditFriendBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.cancelFriend(userId).subscribe(new NewSubscriber<EditFriendBody>(context, true) {
            @Override
            public void onNext(EditFriendBody editFriendBody) {
                callback.onSuccess(editFriendBody);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void addressList(PageBody pageBody, final Callback<Pager<UserAddress>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.addressList(pageBody).subscribe(new NewSubscriber<Pager<UserAddress>>(context, true) {
            @Override
            public void onNext(Pager<UserAddress> userInfoPager) {
                callback.onSuccess(userInfoPager);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void addressAdd(AddressBody addressBody, final Callback<UserAddress> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.addressAdd(addressBody).subscribe(new NewSubscriber<UserAddress>(context, true) {
            @Override
            public void onNext(UserAddress userAddress) {
                callback.onSuccess(userAddress);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void addressUpdate(AddressBody addressBody, final Callback<UserAddress> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.addressUpdate(addressBody).subscribe(new NewSubscriber<UserAddress>(context, true) {
            @Override
            public void onNext(UserAddress userAddress) {
                callback.onSuccess(userAddress);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void addressDelete(String addressId, final Callback<Object> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Call<Response<Object>> call = apiWrapper.addressDelete(addressId);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    @Override
    public void businessApply(MerchantApplyBody merchantApplyBody, final Callback<MerchantApplyBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.businessApply(merchantApplyBody)
                .subscribe(new NewSubscriber<MerchantApplyBody>(context, true) {
                    public void onNext(MerchantApplyBody merchantApplyBody) {
                        callback.onSuccess(merchantApplyBody);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void favouriteApply(FavouriteApplyBody favouriteApplyBody, final Callback<FavouriteApplyBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.favouriteApply(favouriteApplyBody)
                .subscribe(new NewSubscriber<FavouriteApplyBody>(context, true) {
                    public void onNext(FavouriteApplyBody favouriteApplyBody) {
                        callback.onSuccess(favouriteApplyBody);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void superstarApply(StarApplyBody starApplyBody, final Callback<StarApplyBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.superstarApply(starApplyBody)
                .subscribe(new NewSubscriber<StarApplyBody>(context, true) {
                    public void onNext(StarApplyBody starApplyBody) {
                        callback.onSuccess(starApplyBody);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void starApplyDetail(long userId, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        UserInfo userInfo = new UserInfo();
        userInfo.userId = userId;
        Subscription subscription = apiWrapper.starApplyDetail(userInfo)
                .subscribe(new NewSubscriber<UserInfo>(context, true) {
                    public void onNext(UserInfo userInfo) {
                        callback.onSuccess(userInfo);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void businessApplyDetail(long userId, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        UserInfo userInfo = new UserInfo();
        userInfo.userId = userId;
        Subscription subscription = apiWrapper.businessApplyDetail(userInfo)
                .subscribe(new NewSubscriber<UserInfo>(context, true) {
                    public void onNext(UserInfo userInfo) {
                        callback.onSuccess(userInfo);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());

                    }
                });
        addSubscrebe(subscription);
    }


    @Override
    public void favouriteApplyDetail(long userId, final Callback<UserInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        UserInfo userInfo = new UserInfo();
        userInfo.userId = userId;
        Subscription subscription = apiWrapper.favouriteApplyDetail(userInfo)
                .subscribe(new NewSubscriber<UserInfo>(context, true) {
                    public void onNext(UserInfo userInfo) {
                        callback.onSuccess(userInfo);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());

                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getRecomUserList(PageBody pageBody, final Callback<Pager<UserInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getRecomUserList(pageBody).subscribe(new NewSubscriber<Pager<UserInfo>>(context, false) {
            @Override
            public void onNext(Pager<UserInfo> recommendPage) {
                callback.onSuccess(recommendPage);
            }

            @Override
            public void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    /**
     * 首页
     *
     * @param callback
     */
    @Override
    public void getPGCVideoType(final Callback<List<ImageInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getPGCVideoType().subscribe(new NewSubscriber<List<ImageInfo>>(context, false) {
            @Override
            public void onNext(List<ImageInfo> imageInfos) {
                callback.onSuccess(imageInfos);
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
    public void adsInfoList(long adsNo, final Callback<List<AdsInfo>> callback, boolean hasProgress) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.adsInfoList(adsNo).subscribe(new NewSubscriber<List<AdsInfo>>(context, hasProgress) {
            @Override
            public void onNext(List<AdsInfo> adsInfos) {
                callback.onSuccess(adsInfos);
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
    public void ossAuth(String mediaType, final Callback<OssAuth> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.ossAuth(mediaType)
                .subscribe(new NewSubscriber<OssAuth>(context, true) {
                    @Override
                    public void onNext(OssAuth ossAuth) {
                        callback.onSuccess(ossAuth);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }


    @Override
    public void cooperationBusinessList(long userId, PageBody pageBody, final Callback<Pager<UserInfo>> callback, boolean hasProgress) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.cooperationBusinessList(userId, pageBody)
                .subscribe(new NewSubscriber<Pager<UserInfo>>(context, true) {
                    @Override
                    public void onNext(Pager<UserInfo> pager) {
                        callback.onSuccess(pager);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 购买商品->下订单
     *
     * @param productId      产品编号
     * @param buyProductInfo 购买信息
     * @param callback
     */
    @Override
    public void videoBuy(long productId, BuyProductRequest buyProductInfo, final Callback<BuyProductResponse> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.videoBuy(productId, buyProductInfo)
                .subscribe(new NewSubscriber<BuyProductResponse>(context, true) {
                    @Override
                    public void onNext(BuyProductResponse buyProductResponse) {
                        callback.onSuccess(buyProductResponse);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 充值商秀币->下订单
     *
     * @param productId      产品编号
     * @param buyProductInfo 购买信息
     * @param callback
     */
    @Override
    public void chongzhixiubiBuy(long productId, BuyProductRequest buyProductInfo, final Callback<BuyProductResponse> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.chongzhixiubiBuy(productId, buyProductInfo)
                .subscribe(new NewSubscriber<BuyProductResponse>(context, true) {
                    @Override
                    public void onNext(BuyProductResponse buyProductResponse) {
                        callback.onSuccess(buyProductResponse);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getUserCooperationList(PageBody pageBody, final Callback<Pager<UserInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getUserCooperationList(pageBody)
                .subscribe(new NewSubscriber<Pager<UserInfo>>(context, true) {
                    @Override
                    public void onNext(Pager<UserInfo> pager) {
                        callback.onSuccess(pager);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getVideoListByUser(long userId, PageBody pageBody, final Callback<Pager<VideoRoom>> callback, boolean hasProgress) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getVideoListByUser(userId, pageBody)
                .subscribe(new NewSubscriber<Pager<VideoRoom>>(context, true) {
                    @Override
                    public void onNext(Pager<VideoRoom> pager) {
                        callback.onSuccess(pager);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public void rechargeList(final Callback<List<Recharge>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.rechargeList().subscribe(new NewSubscriber<List<Recharge>>(context, true) {
            @Override
            public void onNext(List<Recharge> rechargeList) {
                callback.onSuccess(rechargeList);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getHotLabelList(BasePageBody pageBody, final Callback<Pager<LabelInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getHotLabelList(pageBody).subscribe(new NewSubscriber<Pager<LabelInfo>>(context, true) {
            @Override
            public void onNext(Pager<LabelInfo> labelModelPager) {
                callback.onSuccess(labelModelPager);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getVSInfoList(final Callback<List<VideoRemind>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getVSInfoList().subscribe(new NewSubscriber<List<VideoRemind>>(context, false) {
            @Override
            public void onNext(List<VideoRemind> object) {
                callback.onSuccess(object);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void subscriberStar(long userId, final Callback<Object> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.subscriberStar(userId).subscribe(new NewSubscriber<Object>(context, true) {
            @Override
            public void onNext(Object object) {
                callback.onSuccess(object);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());

            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getCooperateList(PageBody pageBody, final Callback<Pager<UserInfo>> callback) {
        try {
            ApiWrapper apiWrapper = new ApiWrapper();
            Subscription subscription = apiWrapper.getCooperateList(pageBody).subscribe(new NewSubscriber<Pager<UserInfo>>(context, true) {
                @Override
                public void onNext(Pager<UserInfo> cooperateModel) {
                    callback.onSuccess(cooperateModel);
                }

                @Override
                protected void onError(ApiException ex) {
                    super.onError(ex);
                    callback.onFailure(ex.getCode(), ex.getErrMessage());
                }
            });
            addSubscrebe(subscription);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 当前用户的钻石收益详情
     *
     * @return
     */
    @Override
    public void getDiamondEarn(final Callback<DiamondEarnModel> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getDiamondEarn().subscribe(new NewSubscriber<DiamondEarnModel>(context, true) {
            @Override
            public void onNext(DiamondEarnModel diamondEarnModel) {
                callback.onSuccess(diamondEarnModel);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getCashEarnAmounts(final Callback<Long> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getCashBalance().subscribe(new NewSubscriber<Long>(context, true) {
            @Override
            public void onNext(Long amount) {
                callback.onSuccess(amount);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getCashEarnInfo(PageBody pageBody, final Callback<Pager<Goods>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getCashEarnInfo(pageBody).subscribe(new NewSubscriber<Pager<Goods>>(context, true) {
            @Override
            public void onNext(Pager<Goods> goodsPager) {
                callback.onSuccess(goodsPager);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    /**
     * 查询用户当前订单信息
     *
     * @param pageBody
     * @param callback
     * @return
     */
    @Override
    public void getOrderList(OrderPageBody pageBody, final Callback<Pager<OrderInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getOrderList(pageBody).subscribe(new NewSubscriber<Pager<OrderInfo>>(context, true) {
            @Override
            public void onNext(Pager<OrderInfo> orderInfoPager) {
                callback.onSuccess(orderInfoPager);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    /**
     * 操作用户当前订单信息 去支付 或  取消
     *
     * @param
     * @param callback
     * @return
     */

    @Override
    public void getOrderListSetStatue(String orderNo, final Callback<Object> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getOrderListSetStatue(orderNo).subscribe(new NewSubscriber<Object>(context, true) {
            @Override
            public void onNext(Object orderNo) {
                callback.onSuccess(orderNo);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getRewardMeList(PageBody pageBody, final Callback<Pager<RewardInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getRewardMeList(pageBody).subscribe(new NewSubscriber<Pager<RewardInfo>>(context, true) {
            @Override
            public void onNext(Pager<RewardInfo> rewardModel) {
                callback.onSuccess(rewardModel);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getMyRewardList(PageBody pageBody, final Callback<Pager<RewardInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getMyRewardList(pageBody).subscribe(new NewSubscriber<Pager<RewardInfo>>(context, true) {
            @Override
            public void onNext(Pager<RewardInfo> rewardInfoPager) {
                callback.onSuccess(rewardInfoPager);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getRewardMeAmounts(final Callback<Long> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getRewardMeAmounts().subscribe(new NewSubscriber<Long>(context, true) {
            @Override
            public void onNext(Long amount) {
                callback.onSuccess(amount);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getMyRewardAmounts(final Callback<Long> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getMyRewardAmounts().subscribe(new NewSubscriber<Long>(context, true) {
            @Override
            public void onNext(Long amount) {
                callback.onSuccess(amount);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getAccountBalance(final Callback<Long> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getAccountBalance().subscribe(new NewSubscriber<Long>(context, true) {
            @Override
            public void onNext(Long amount) {
                callback.onSuccess(amount);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getLiveGaves(PageBody pageBody, String giftType, final Callback<Pager<GiftInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getLiveGaves(pageBody, giftType).subscribe(new NewSubscriber<Pager<GiftInfo>>(context, true) {
            @Override
            public void onNext(Pager<GiftInfo> giftInfoPager) {
                super.onNext(giftInfoPager);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void rewardAnchor(RewardGiftBody rewardGiftBody, final Callback<Long> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.rewardAnchor(rewardGiftBody).subscribe(new NewSubscriber<Long>(context, true) {
            @Override
            public void onNext(Long amount) {
                super.onNext(amount);
                callback.onSuccess(amount);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getVideoStatistics(String videoId, final Callback<VideoStatistic> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getVideoStatistics(videoId).subscribe(new NewSubscriber<VideoStatistic>(context, true) {
            @Override
            public void onNext(VideoStatistic videoStatistic) {
                super.onNext(videoStatistic);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getUserInfoByUserIds(List<Long> userIdList, final Callback<List<UserInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getUserInfoByUserIds(userIdList).subscribe(new NewSubscriber<List<UserInfo>>(context, false) {
            @Override
            public void onNext(List<UserInfo> userInfoList) {
                super.onNext(userInfoList);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void checkIsFriend(String userid, final Callback<Boolean> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.checkIsFriend(userid).subscribe(new NewSubscriber<Boolean>(context, true) {
            @Override
            public void onNext(Boolean amount) {
                callback.onSuccess(amount);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void cooperationApplyDetail(CooperationApplyInfo cooperationApplyInfo, final Callback<CooperationApplyDetail> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.cooperationApplyDetail(cooperationApplyInfo).subscribe(new NewSubscriber<CooperationApplyDetail>(context, true) {
            @Override
            public void onNext(CooperationApplyDetail amount) {
                callback.onSuccess(amount);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void cooperationApply(String cooperationApplyInfo, final Callback<Response<Object>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), cooperationApplyInfo);
        final Call<Response<Object>> call = apiWrapper.cooperationApply(body);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    @Override
    public void applyVideo(Object applyVideo, final Callback<Response<Object>> callback) {
        Gson gson = new Gson();
        String video = gson.toJson(applyVideo);
        ApiWrapper apiWrapper = new ApiWrapper();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), video);
        final Call<Response<Object>> call = apiWrapper.applyVideo(body);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    @Override
    public void getProducts(Object commonObject, final Callback<Response<Object>> callback) {
        Gson gson = new Gson();
        String json = gson.toJson(commonObject);
        ApiWrapper apiWrapper = new ApiWrapper();
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        final Call<Response<Object>> call = apiWrapper.getProducts(body);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    @Override
    public void getProductsInfo(String productsId, final Callback<Goods> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getProductsInfo(productsId).subscribe(new NewSubscriber<Goods>(context, false) {
            @Override
            public void onNext(Goods goods) {
                callback.onSuccess(goods);
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
    public void addProductsInfo(Goods goods, final Callback<Object> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.addProductsInfo(goods)
                .subscribe(new NewSubscriber<Goods>(context, true) {
                    @Override
                    public void onNext(Goods goods) {
                        callback.onSuccess(goods);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void updateProductsInfo(Goods goods, final Callback<Goods> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.updateProductsInfo(goods)
                .subscribe(new NewSubscriber<Goods>(context, true) {
                    @Override
                    public void onNext(Goods goods) {
                        callback.onSuccess(goods);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void delProducts(long productId, final Callback<Object> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Call<Response<Object>> call = apiWrapper.delProducts(productId);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    @Override
    public void getUserCooperationList(CooperationPageBody cooperationPageBody, final Callback<Pager<UserInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getUserCooperationList(cooperationPageBody)
                .subscribe(new NewSubscriber<Pager<UserInfo>>(context, true) {
                    @Override
                    public void onNext(Pager<UserInfo> pager) {
                        callback.onSuccess(pager);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void gettuijianList(PGCVideoInfo pgcVideoInfo, final Callback<Pager<PGCVideoInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.gettuijianList(pgcVideoInfo)
                .subscribe(new NewSubscriber<Pager<PGCVideoInfo>>(context, true) {
                    @Override
                    public void onNext(Pager<PGCVideoInfo> pager) {
                        callback.onSuccess(pager);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 根据roomId 获取主播的videoRoom 信息
     *
     * @param
     * @param callback
     */
    @Override
    public void getSingeoVideoRoom(Long roomId, final Callback<VideoRoom> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getSingeoVideoRoom(roomId)
                .subscribe(new NewSubscriber<VideoRoom>(context, true) {
                    @Override
                    public void onNext(VideoRoom pager) {
                        callback.onSuccess(pager);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 支付成功后上传订单号
     *
     * @param
     * @param callback
     */
    @Override
    public void getPaySuccessEnd(String dingdanhao, final Callback<Object> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getPaySuccessEnd(dingdanhao)
                .subscribe(new NewSubscriber<Object>(context, true) {
                    @Override
                    public void onNext(Object object) {
                        callback.onSuccess(object);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void agreeCooperation(UserBusinessCooperationBody userBusinessCooperationBody, final Callback<Object> callback) {
        Gson gson = new Gson();
        String userBsinessCooperation = gson.toJson(userBusinessCooperationBody);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), userBsinessCooperation);
        ApiWrapper apiWrapper = new ApiWrapper();
        final Call<Response<Object>> call = apiWrapper.agreeCooperation(body);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content.result);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    /**
     * 开启直播
     *
     * @param startLiveBody
     * @param callback
     */
    @Override
    public void startLive(StartLiveBody startLiveBody, final Callback<LiveInfo> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.startLive(startLiveBody)
                .subscribe(new NewSubscriber<LiveInfo>(context, true) {
                    @Override
                    public void onNext(LiveInfo liveInfo) {
                        callback.onSuccess(liveInfo);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 网红关闭直播间，申请保存视频或关闭
     *
     * @param closeType
     * @param videoOffLiveBody
     * @param callback
     */
    @Override
    public void offLive(String closeType, VideoOffLiveBody videoOffLiveBody, final Callback<String> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.offLive(closeType, videoOffLiveBody)
                .subscribe(new NewSubscriber<String>(context, true) {
                    @Override
                    public void onNext(String value) {
                        callback.onSuccess(value);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 用户关注的网红列表接口
     *
     * @param pageBody
     * @param callback
     */
    @Override
    public void getUserFriedsVideoList(PageBody pageBody, final Callback<List<VideoRoom>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getUserFriedsVideoList(pageBody)
                .subscribe(new CreateSubscriber<List<VideoRoom>>(context, false) {
                    @Override
                    public void onNext(List<VideoRoom> userInfoExpands) {
                        callback.onSuccess(userInfoExpands);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void removeVideoListByUser(List<Long> userIdList, final Callback<Object> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Call<Response<Object>> call = apiWrapper.removeVideoListByUser(userIdList);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    //商品
    @Override
    public void getProducts(GoodsPageBody pageBody, final Callback<List<Goods>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getProducts(pageBody).subscribe(new NewSubscriber<List<Goods>>(context, true) {
            @Override
            public void onNext(List<Goods> goodsList) {
                super.onNext(goodsList);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void addProduct(Goods goods, final Callback<Goods> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.addProduct(goods).subscribe(new NewSubscriber<Goods>(context, true) {
            @Override
            public void onNext(Goods goods) {
                super.onNext(goods);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void updateProduct(Goods goods, final Callback<Goods> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.updateProduct(goods).subscribe(new NewSubscriber<Goods>(context, true) {
            @Override
            public void onNext(Goods goods) {
                super.onNext(goods);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void getProductsInfo(long productId, final Callback<Goods> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getProductsInfo(productId).subscribe(new NewSubscriber<Goods>(context, true) {
            @Override
            public void onNext(Goods goods) {
                super.onNext(goods);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void videoProductList(long videoId, final Callback<List<Goods>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.videoProductList(videoId).subscribe(new NewSubscriber<List<Goods>>(context, true) {
            @Override
            public void onNext(List<Goods> goodsList) {
                super.onNext(goodsList);
                callback.onSuccess(goodsList);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    @Override
    public void productShelves(long videoId, String json, final Callback<Object> callback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        ApiWrapper apiWrapper = new ApiWrapper();
        final Call<Response<Object>> call = apiWrapper.productShelves(videoId, body);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content.result);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    @Override
    public void productOffShelves(long videoId, String json, final Callback<Object> callback) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), json);
        ApiWrapper apiWrapper = new ApiWrapper();
        final Call<Response<Object>> call = apiWrapper.productOffShelves(videoId, body);
        call.enqueue(new retrofit2.Callback<Response<Object>>() {
            @Override
            public void onResponse(Call<Response<Object>> call, retrofit2.Response<Response<Object>> response) {
                Response<Object> content = response.body();
                callback.onSuccess(content.result);
            }

            @Override
            public void onFailure(Call<Response<Object>> call, Throwable t) {
                callback.onFailure(10004, t.getMessage());
            }
        });
    }

    @Override
    public void getHotUserList(PageBody pageBody, final Callback<Pager<UserInfo>> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getHotUserList(pageBody).subscribe(new NewSubscriber<Pager<UserInfo>>(context, true) {
            @Override
            public void onNext(Pager<UserInfo> userInfoPager) {
                super.onNext(userInfoPager);
                callback.onSuccess(userInfoPager);
            }

            @Override
            protected void onError(ApiException ex) {
                super.onError(ex);
                callback.onFailure(ex.getCode(), ex.getErrMessage());
            }
        });
        addSubscrebe(subscription);
    }

    /**
     * 获取支付所需要的参数信息
     */

    @Override
    public void getPayStringBody(PayOrderDto payOrderDto, final Callback<PayRecBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getPayStringBody(payOrderDto)
                .subscribe(new NewSubscriber<PayRecBody>(context, true) {
                    @Override
                    public void onNext(PayRecBody user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 获取封面列表信息
     */

    @Override
    public void getHomeHot(final Callback<HomeHotFirstBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getHomeHot()
                .subscribe(new NewSubscriber<HomeHotFirstBody>(context, true) {
                    @Override
                    public void onNext(HomeHotFirstBody user) {
                        callback.onSuccess(user);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 获取Youtube信息
     */

    @Override
    public void getYoutubeList(String dingdanhao, final Callback<YoutubeListBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getYoutubeList(dingdanhao)
                .subscribe(new NewSubscriber<YoutubeListBody>(context, true) {
                    @Override
                    public void onNext(YoutubeListBody object) {
                        callback.onSuccess(object);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }
    @Override
    public void getHotMoreList(PageBody pageBody, final Callback<HotMoreListBody> callback) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getHotMoreList(pageBody)
                .subscribe(new NewSubscriber<HotMoreListBody>(context, true) {
                    @Override
                    public void onNext(HotMoreListBody object) {
                        callback.onSuccess(object);
                    }

                    @Override
                    protected void onError(ApiException ex) {
                        super.onError(ex);
                        callback.onFailure(ex.getCode(), ex.getErrMessage());
                    }
                });
        addSubscrebe(subscription);
    }
}
