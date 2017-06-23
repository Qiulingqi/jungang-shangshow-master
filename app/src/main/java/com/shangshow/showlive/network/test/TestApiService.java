package com.shangshow.showlive.network.test;


import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.service.models.AdsInfo;
import com.shangshow.showlive.network.service.models.DiamondEarnModel;
import com.shangshow.showlive.network.service.models.LabelInfo;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.OrderInfo;
import com.shangshow.showlive.network.service.models.OssAuth;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.Recharge;
import com.shangshow.showlive.network.service.models.RewardInfo;
import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.FavouriteApplyBody;
import com.shangshow.showlive.network.service.models.body.OrderPageBody;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.body.StarApplyBody;
import com.shangshow.showlive.network.service.models.body.StartLiveBody;
import com.shangshow.showlive.network.service.models.body.UserBody;
import com.shangshow.showlive.network.service.models.body.VideoOffLiveBody;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sunflower on 2015/11/4.
 */
public interface TestApiService {
    /**
     * 获取妹纸图
     *
     * @return
     */
    @GET("http://gank.io/api/data/福利/10/{PageBody}")
    Observable<Response<List<MeiZhi>>> getMeiZhiList(@Path("PageBody") long page);

    @GET("http://gank.io/api/data/福利/10/{PageBody}")
    Call<Response<List<MeiZhi>>> testGetMeiZhiList(@Path("PageBody") int page);

    //商秀接口测试
    @POST("service/register/0")
    Call<Response<UserInfo>> register(@Body UserBody register);

    @POST("service/login/0")
    Call<Response<UserInfo>> login(@Body UserBody register);

    @POST("service/smscaptcha/0")
    Call<Response<String>> smsCaptcha(@Body UserBody register);

    @GET("service/getCurrUser/{userId}/0")
    Call<Response<UserInfo>> getUserInfo(@Header("token") String token, @Path("userId") long userId);

    @POST("user/updateUserInfo/0")
    Call<Response<UserInfo>> updateUserInfo(@Header("token") String token, @Body UserInfo userInfo);

    /**
     * 添加关注
     *
     * @return
     */
    @POST("user/addFriend/{userId}/0")
    Call<Response<EditFriendBody>> addFriend(@Header("token") String token, @Path("userId") long userId);

    /**
     * 取消关注
     *
     * @return
     */
    @POST("user/cancelFriend/{userId}/0")
    Call<Response<EditFriendBody>> cancelFriend(@Header("token") String token, @Path("userId") long userId);

    /**
     * 申请星咖
     *
     * @return
     */
    @POST("user/starApply/0")
    Call<Response<StarApplyBody>> superstarApply(@Header("token") String token, @Body StarApplyBody starApplyBody);

    /**
     * 申请星尚
     *
     * @param token
     * @param favouriteApplyBody
     * @return
     */
    @POST("user/favouriteApply/0")
    Call<Response<FavouriteApplyBody>> favouriteApply(@Header("token") String token, @Body FavouriteApplyBody favouriteApplyBody);

    /**
     * 广告位接口
     * 1-启动页广告
     * 2-首页热门广告
     * 3-星尚直播页广告
     * 4-星咖直播页广告
     * 5-星品直播页广告
     * 6-星品直播页宫格位广告
     *
     * @return
     */
    @POST("service/adsInfoList/{adsNo}/0")
    Call<Response<List<AdsInfo>>> adsInfoList(@Header("token") String token, @Path("adsNo") long adsNo);

    /**
     * 首页推荐用户接口
     *
     * @return
     */
    @POST("service/getRecomUserList/0")
    Call<Response<Pager<UserInfo>>> getRecomUserList(@Body PageBody pageBody);

    /**
     * 开启直播
     *
     * @param token
     * @param startLiveBody
     * @return
     */
    @POST("video/startLive/0")
    Call<Response<LiveInfo>> startLive(@Header("token") String token, @Body StartLiveBody startLiveBody);

    /**
     * 关闭直播【closeType:VIDEO-SAVE 保存视屏】|【VIDEO-CLOSE : 直接关闭不保存】
     *
     * @return
     */
    @POST("video/offLive/{closeType}/0")
    Call<Response<String>> offLive(@Path("closeType") String closeType, @Body VideoOffLiveBody videoOffLiveBody);


    /**
     * 直播间列表查询
     *
     * @return
     */
    @POST("service/getVideoRoomList/{userType}/0")
    Call<Response<Pager<VideoRoom>>> getVideoRoomList(@Path("userType") String userType, @Body PageBody pageBody);


    /**
     * 文件上传-认证
     *
     * @return
     */
    @GET("oss/auth/{mediaType}/0")
    Call<Response<OssAuth>> ossAuth(@Header("token") String token, @Path("mediaType") String mediaType);

    /**
     * 钻石充值价格列表
     *
     * @return
     */
    @GET("recharge/list/0")
    Call<Response<List<Recharge>>> rechargeList(@Header("token") String token);

    /**
     * 获取热门标签列表
     *
     * @return
     */
    @GET("labels/hotList/0")
    Call<Response<Pager<LabelInfo>>> getHotLabelList(@Header("token") String token);
    /**
     * 获取合作列表
     *
     * @return
     */
    @POST("user/getUserCooperationList/0")
    Call<Response<Pager<UserInfo>>> getCooperateList(@Header("token") String token, @Body PageBody pageBody);

    /**
     * 当前用户的钻石收益总额
     * @return
     */
    @POST("user/getDiamondEarn/0")
    Call<Response<DiamondEarnModel>> getDiamondEarn(@Header("token") String token);

    /**
     * 当前用户的现金收益总额
     * @return
     */
    @POST("user/getCashEarnAmounts/0")
    Call<Response<Long>> getCashEarnAmounts(@Header("token") String token);

    /**
     * 当前用户的现金收益详情
     * @return
     */
    @POST("user/getCashEarnInfo/0")
    Call<Response<UserInfo>> getCashEarnInfo(@Header("token") String token, @Body PageBody pageBody);

    /**
     * 查询用户当前订单信息
     * @return
     */
    @POST("orders/list/0")
    Call<Response<Pager<OrderInfo>>> getOrderList(@Header("token") String token, @Body OrderPageBody pageBody);

    /**
     * 被打赏的用户获取打赏的用户和金额
     * @return
     */
    @POST("user/getRewardMeList/0")
    Call<Response<Pager<RewardInfo>>> getRewardMeList(@Header("token") String token, @Body PageBody pageBody);

    /**
     * 当前用户打赏的用户
     * @return
     */
    @POST("user/getMyRewardList/0")
    Call<Response<Pager<RewardInfo>>> getMyRewardList(@Header("token") String token, @Body PageBody pageBody);

    /**
     * 当前用户获取到被打赏的总金额
     * @return
     */
    @POST("user/getRewardMeAmounts/0")
    Call<Response<Long>> getRewardMeAmounts(@Header("token") String token);

    /**
     * 当前用户打赏的总额
     * @return
     */
    @POST("user/getMyRewardAmounts/0")
    Call<Response<Long>> getMyRewardAmounts(@Header("token") String token);

    /**
     * 当前用户的账户钻石余额
     * @return
     */
    @POST("user/getAccountBalance/0")
    Call<Response<Long>> getAccountBalance(@Header("token") String token);
}



