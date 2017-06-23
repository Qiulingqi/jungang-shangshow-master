package com.shangshow.showlive.network.service;


import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.shangshow.showlive.common.model.ImageInfo;
import com.shangshow.showlive.network.http.Response;
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
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Sunflower on 2015/11/4.
 */
public interface ApiService {

    /**
     * 注册
     *
     * @param userBody
     * @return
     */
    @POST("service/register/0")
    Observable<Response<UserInfo>> register(@Body UserBody userBody);

    /**
     * 登陆
     *
     * @param userBody
     * @return
     */
    @POST("service/login/0")
    Observable<Response<UserInfo>> login(@Body UserBody userBody);

    /**
     * 登陆
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("service/login/0")
    Call<Response<Object>> login(@Body RequestBody body);

    /**
     * 短信验证码
     *
     * @param userBody
     * @return
     */
    @POST("service/smscaptcha/0")
    Observable<Response<String>> smsCaptcha(@Body UserBody userBody);


    /**
     * 获取用户信息
     *
     * @return
     */
    @GET("service/getUserInfo/{userId}/0")
    Observable<Response<UserInfo>> getUserInfo(@Path("userId") long userId);

    /**
     * 编辑用户信息
     *
     * @param userInfo
     * @return
     */
    @POST("user/updateUserInfo/0")
    Observable<Response<UserInfo>> updateUserInfo(@Body UserInfo userInfo);

    /**
     * 重置密码
     *
     * @return
     */
    @POST("service/restPwd/0")
    Observable<Response<String>> restPwd(@Body UserBody userBody);

    /**
     * 绑定手机
     *
     * @return
     */
    @POST("user/bindingMobile/0")
    Observable<Response<String>> bindingMobile(@Body UserBody userBody);

    /**
     * 关注列表
     *
     * @return
     */
    @POST("user/friends/0")
    Observable<Response<Pager<UserInfo>>> friends(@Body PageBody pageBody);

    /**
     * 粉丝列表
     *
     * @return
     */
    @POST("user/follwers/0")
    Observable<Response<Pager<UserInfo>>> follwers(@Body PageBody pageBody);

    /**
     * 添加关注
     *
     * @return
     */
    @POST("user/addFriend/{userId}/0")
    Observable<Response<EditFriendBody>> addFriend(@Path("userId") long userId);

    /**
     * 取消关注
     *
     * @return
     */
    @POST("user/cancelFriend/{userId}/0")
    Observable<Response<EditFriendBody>> cancelFriend(@Path("userId") long userId);

    /**
     * 收货地址
     *
     * @return
     */
    @POST("user/addressList/0")
    Observable<Response<Pager<UserAddress>>> addressList(@Body PageBody pageBody);

    /**
     * 添加收货地址
     *
     * @return
     */
    @POST("user/addressAdd/0")
    Observable<Response<UserAddress>> addressAdd(@Body AddressBody addressBody);

    /**
     * 编辑收货地址
     *
     * @return
     */
    @POST("user/addressUpdate/0")
    Observable<Response<UserAddress>> addressUpdate(@Body AddressBody addressBody);

    /**
     * 删除收货信息
     *
     * @return
     */
    @POST("user/addressDelete/{addressId}/0")
    Call<Response<Object>> addressDelete(@Path("addressId") String addressId);

    /**
     * 申请星品
     *
     * @return
     */
    @POST("user/businessApply/0")
    Observable<Response<MerchantApplyBody>> businessApply(@Body MerchantApplyBody favouriteApplyBody);

    /**
     * 申请星尚
     *
     * @return
     */
    @POST("user/favouriteApply/0")
    Observable<Response<FavouriteApplyBody>> favouriteApply(@Body FavouriteApplyBody favouriteApplyBody);


    /**
     * 申请星咖
     *
     * @return
     */
    @POST("user/starApply/0")
    Observable<Response<StarApplyBody>> superstarApply(@Body StarApplyBody starApplyBody);

    /**
     * 申请网红认证资料详情
     *
     * @return
     */
    @POST("user/favouriteApplyDetail/0")
    Observable<Response<UserInfo>> favouriteApplyDetail(@Body UserInfo userInfo);

    /**
     * 申请星咖认证资料详情
     *
     * @return
     */
    @POST("user/starApplyDetail/0")
    Observable<Response<UserInfo>> starApplyDetail(@Body UserInfo userInfo);

    /**
     * 申请商家认证资料详情
     *
     * @return
     */
    @POST("user/businessApplyDetail/0")
    Observable<Response<UserInfo>> businessApplyDetail(@Body UserInfo userInfo);

    /**
     * 首页推荐用户接口
     *
     * @return
     */
    @POST("service/getRecomUserList/0")
    Observable<Response<Pager<UserInfo>>> getRecomUserList(@Body PageBody pageBody);

    @POST("PGCVideo/getPGCVideoType/0")
    Observable<Response<List<ImageInfo>>> getPGCVideoType();

    /**
     * 星咔直播计划列表查询
     *
     * @return
     */
    @POST("service/getVSInfoList/0")
    Observable<Response<List<VideoRemind>>> getVSInfoList();

    /**
     * 用户订阅星咔直播计划
     *
     * @return
     */
    @POST("video/subscriberStar/{userId}/0")
    Observable<Response<Object>> subscriberStar(@Path("userId") long userId);

    /**
     * 直播间-合作列表查询
     *
     * @return
     */
    @POST("user/cooperationBusinessList/{userId}/0")
    Observable<Response<Pager<UserInfo>>> cooperationBusinessList(@Path("userId") long userId, @Body PageBody pageBody);

    /**
     * 购买商品->下订单
     *
     * @param productId      产品编号
     * @param buyProductInfo 购买信息
     * @return
     */
    @POST("orders/videoBuy/{productId}/0")
    Observable<Response<BuyProductResponse>> videoBuy(@Path("productId") long productId, @Body BuyProductRequest buyProductInfo);

    /**
     * 购买商品->下订单
     *
     * @param rechargeInfoId 产品编号
     * @return
     */
    @POST("orders/rechargeBuy/{rechargeInfoId}/0")
    Observable<Response<BuyProductResponse>> rechargeBuy(@Path("rechargeInfoId") long rechargeInfoId);


    /**
     * 充值商秀币->下订单
     *
     * @param rechargeInfoId 产品编号
     * @return
     */
    @POST("orders/videoBuy/{productId}/0")
    Observable<Response<BuyProductResponse>> chongzhixiubiBuy(@Path("productId") long rechargeInfoId, @Body BuyProductRequest buyProductInfo);


    /**
     * 合作列表查询
     *
     * @return
     */
    @POST("user/getUserCooperationList/0")
    Observable<Response<Pager<UserInfo>>> getUserCooperationList(@Body PageBody pageBody);

//    /**
//     * 同意/拒绝合作
//     *
//     * @param userBody
//     * @return
//     */
//    @POST("user/agreeCooperation/0")
//    Observable<Response<Object>> agreeCooperation(@Body UserBusinessCooperationBody userBody);

    /**
     * 同意/拒绝合作
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user/agreeCooperation/0")
    Call<Response<Object>> agreeCooperation(@Body RequestBody body);


    /**
     * 用户视频列表
     *
     * @return
     */
    @POST("user/getVideoListByUser/{userId}/0")
    Observable<Response<Pager<VideoRoom>>> getVideoListByUser(@Path("userId") long userId, @Body PageBody pageBody);

    /**
     * 删除多个视频
     *
     * @return
     */
    @POST("user/removeVideoListByUser/0")
    Call<Response<Object>> removeVideoListByUser(@Body List<Long> userIdList);

    ///////////////////////////////////////////////////////////////////
    /////////////////直播相关////////////////////////

    /**
     * 开启直播
     *
     * @return
     */
    @POST("video/startLive/0")
    Observable<Response<LiveInfo>> startLive(@Body StartLiveBody startLiveBody);

    /**
     * 关闭直播【closeType:VIDEO-SAVE 保存视屏】|【VIDEO-CLOSE : 直接关闭不保存】
     *
     * @return
     */
    @POST("video/offLive/{closeType}/0")
    Observable<Response<String>> offLive(@Path("closeType") String closeType, @Body VideoOffLiveBody videoOffLiveBody);


    /**
     * 直播间列表查询
     *
     * @return
     */
    @POST("service/getVideoRoomList/{videoType}/0")
    Observable<Response<Pager<VideoRoom>>> getVideoRoomList(@Path("videoType") String videoType, @Body PageBody pageBody);


    /**
     * 用户关注的网红列表接口
     *
     * @return
     */
    @POST("user/getUserFriedsVideoList/0")
    Observable<Response<List<VideoRoom>>> getUserFriedsVideoList(@Body PageBody pageBody);

    ///////////////////////////////////////////////////////////////////
    /////////////////商品////////////////////////

    /**
     * 商品列表
     *
     * @return
     */
    @POST("products/getProducts/0")
    Observable<Response<List<Goods>>> getProducts(@Body GoodsPageBody pageBody);

    /**
     * 商品列表
     *
     * @return
     */
    @POST("products/getProducts/0")
    Observable<Response<List<Goods>>> getProducts2(@Body GoodsPageBody pageBody);

    /**
     * 添加商品
     *
     * @return
     */
    @POST("products/addProduct/0")
    Observable<Response<Goods>> addProduct(@Body Goods goods);

    /**
     * 编辑商品
     *
     * @return
     */
    @POST("products/updateProduct/0")
    Observable<Response<Goods>> updateProduct(@Body Goods goods);

    /**
     * 删除商品
     *
     * @return
     */
    @POST("products/delProducts/{productId}/0")
    Call<Response<Object>> delProducts(@Path("productId") long productId);

    /**
     * 商品详情
     *
     * @return
     */
    @GET("products/getProductsInfo/{productsId}/0")
    Observable<Response<Goods>> getProductsInfo(@Path("productId") long productId);

    @GET("/products/videoProductList/{videoId}/0")
    Observable<Response<List<Goods>>> videoProductList(@Path("videoId") long videoId);

    /**
     * 直播上架商品
     *
     * @param videoId
     * @param requestBody
     * @return
     */
    @POST("products/productShelves/{videoId}/0")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Response<Object>> productShelves(@Path("videoId") long videoId, @Body RequestBody requestBody);

    /**
     * 直播下架商品
     *
     * @param videoId
     * @param requestBody
     * @return
     */
    @POST("products/productOffShelves/{videoId}/0")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Call<Response<Object>> productOffShelves(@Path("videoId") long videoId, @Body RequestBody requestBody);

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
    Observable<Response<List<AdsInfo>>> adsInfoList(@Path("adsNo") long adsNo);

    /**
     * 文件上传-认证
     *
     * @return
     */
    @GET("oss/auth/{mediaType}/0")
    Observable<Response<OssAuth>> ossAuth(@Path("mediaType") String mediaType);


    /**
     * 钻石充值价格列表
     *
     * @return
     */
    @GET("recharge/list/0")
    Observable<Response<List<Recharge>>> rechargeList();

    /**
     * 获取热门标签列表
     *
     * @return
     */
    @GET("labels/hotList/0")
    Observable<Response<Pager<LabelInfo>>> getHotLabelList();

    /**
     * 获取热门标签列表
     *
     * @return
     */
    @POST("labels/list/0")
    Observable<Response<Pager<LabelInfo>>> getHotLabelList(@Body BasePageBody pageBody);

    /**
     * 获取合作列表
     *
     * @return
     */
    @POST("user/getUserCooperationList/0")
    Observable<Response<Pager<UserInfo>>> getCooperateList(@Body PageBody pageBody);

    /**
     * 当前用户的钻石收益详情
     *
     * @return
     */
    @POST("user/getDiamondEarn/0")
    Observable<Response<DiamondEarnModel>> getDiamondEarn();


    /**
     * 当前用户的现金收益总额
     *
     * @return
     */
    @POST("user/getCashBalance/0")
    Observable<Response<Long>> getCashBalance();

    /**
     * 收益总额
     *
     * @return
     */
    @POST("user/getCashEarnAmounts/0")
    Observable<Response<Long>> getCashEarnAmounts();

    /**
     * 当前用户的现金收益详情
     *
     * @return
     */
    @POST("user/getCashEarnInfo/0")
    Observable<Response<Pager<Goods>>> getCashEarnInfo(@Body PageBody pageBody);

    /**
     * 查询用户当前订单信息
     *
     * @return
     */
    @POST("orders/list/0")
    Observable<Response<Pager<OrderInfo>>> getOrderList(@Body OrderPageBody pageBody);

    /**
     * 操作询户当前订单信息  去支付/取消支付
     *
     * @return
     */
    @GET("orders/cancelOrder/{orderNo}/0")
    Observable<Response<Object>> getOrderListSetStatue(@Path("orderNo") String orderNo);

    /**
     * 被打赏的用户获取打赏的用户和金额
     *
     * @return
     */
    @POST("user/getRewardMeList/0")
    Observable<Response<Pager<RewardInfo>>> getRewardMeList(@Body PageBody pageBody);

    /**
     * 当前用户打赏的用户
     *
     * @return
     */
    @POST("user/getMyRewardList/0")
    Observable<Response<Pager<RewardInfo>>> getMyRewardList(@Body PageBody pageBody);

    /**
     * 当前用户获取到被打赏的总金额
     *
     * @return
     */
    @POST("user/getRewardMeAmounts/0")
    Observable<Response<Long>> getRewardMeAmounts();



    /**
     * 当前用户打赏的总额
     *
     * @return
     */
    @POST("user/getMyRewardAmounts/0")
    Observable<Response<Long>> getMyRewardAmounts();

    /**
     * 当前用户的账户钻石余额
     *
     * @return
     */
    @POST("user/getAccountBalance/0")
    Observable<Response<Long>> getAccountBalance();

    /**
     * 礼物列表
     *
     * @return
     */
    @POST("products/gaves/{giftType}/0")
    Observable<Response<Pager<GiftInfo>>> getLiveGaves(@Body PageBody pageBody, @Path("giftType") String giftType);

    /**
     * 打赏主播
     *
     * @return
     */
    @POST("payment/gave/0")
    Observable<Response<Long>> rewardAnchor(@Body RewardGiftBody rewardGiftBody);

    /**
     * 获取直播统计相关信息（本场收益，观看人数，送礼人数）
     *
     * @return
     */
    @GET("service/getVideoStatistics/{videoId}/0")
    Observable<Response<VideoStatistic>> getVideoStatistics(@Path("videoId") String videoId);

    /**
     * 根据用户Ids批量查询用户详情
     *
     * @return
     */
    @POST("service/getUserInfoByUserIds/0")
    Observable<Response<List<UserInfo>>> getUserInfoByUserIds(@Body List<Long> userIdList);

    @POST("wx/login/{code}/0")
    Observable<Response<UserInfo>> wechatLogin(@Path("code") String code);

    //微信用户调用，会根据用户状态创建或者返回用户信息。
    @POST("wx/register/0")
    Observable<Response<UserInfo>> wechatLogin2(@Body WeChatUserInfo weChatUserInfo);
//
//    @POST("products/getProducts/0")
//    Observable<Response<List<Goods>>> getProducts(@Body GoodsPageBody pageBody);

    @POST("user/checkIsFriend/{userid}/0")
    Observable<Response<Boolean>> checkIsFriend(@Path("userid") String userid);

    /**
     * 用户，商家合作详情查询
     *
     * @param cooperationApplyInfo
     * @return
     */
    @POST("user/cooperationApplyDetail/0")
    Observable<Response<CooperationApplyDetail>> cooperationApplyDetail(@Body CooperationApplyInfo cooperationApplyInfo);

    /**
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("user/cooperationApply/0")
    Call<Response<Object>> cooperationApply(@Body RequestBody body);

    /**
     * 用户上传视屏申请提交
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("video/applyVideo/0")
    Call<Response<Object>> applyVideo(@Body RequestBody body);

    /**
     * 获取商品列表
     *
     * @param body
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("products/getProducts/0")
    Call<Response<Object>> getProducts(@Body RequestBody body);

    @GET("products/getProductsInfo/{productsId}/0")
    Observable<Response<Goods>> getProductsInfo(@Path("productsId") String productsId);

    /**
     * 新增商品服务
     *
     * @return
     */
    @POST("products/addProduct/0")
    Observable<Response<Goods>> addProductsInfo(@Body Goods goods);

    /**
     * @param cooperationPageBody
     * @return
     */
    @POST("user/getUserCooperationList/0")
    Observable<Response<Pager<UserInfo>>> getUserCooperationList(@Body CooperationPageBody cooperationPageBody);

    /**
     * 用户搜索接口，用户可根据用户编号，用户昵称搜索
     *
     * @return
     */
    @POST("service/getHotUserList/0")
    Observable<Response<Pager<UserInfo>>> getHotUserList(@Body PageBody pageBody);

    /**
     * 获取推荐列表视频
     *
     * @return
     */
    @POST("PGCVideo/getPGCVideoInfo/0")
    Observable<Response<Pager<PGCVideoInfo>>> gettuijianList(@Body PGCVideoInfo pgcVideoInfo);


    /**
     * 根据roomID获取主播的开播信息 VideoRoom
     *
     * @return
     */
    @POST("/service/getVideoRoomByVideoRoomId/{roomId}/0")
    Observable<Response<VideoRoom>> getSingeoVideoRoom(@Path("roomId") Long roomId);

    /**
     * @return
     */
    @POST("orders/rechargeOrder/{orderNo}/0")
    Observable<Response<Object>> getPaySuccessEnd(@Path("orderNo") String dingdanhao);

    /**
     * 获取支付所需参数
     */
    @POST("/payment/crash/0")
    Observable<Response<PayRecBody>> getPayStringBody(@Body PayOrderDto payOrderDto);

    /**
     * 获取主页热播封皮信息
     */
    @POST("/PGCVideo/getPGCVideoYouTube/0")
    Observable<Response<HomeHotFirstBody>> getHomeHot();

    /**
     * 获取YouTube的视频列表
     */
    @POST("/PGCVideo/getPGCVideoInfo/{pgcVideoTypesId}/0")
    Observable<Response<YoutubeListBody>> getYoutubeList(@Path("pgcVideoTypesId") String dingdanhao);
    /**
     * 获取更多热门type
     */
    @POST("/PGCVideo/getPGCVideoList/0")
    Observable<Response<HotMoreListBody>> getHotMoreList(@Body PageBody pageBody);
}
