package com.shangshow.showlive.model.port;


import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.shangshow.showlive.common.model.ImageInfo;
import com.shangshow.showlive.model.callback.Callback;
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

/**
 * 用户相关
 */
public interface IUserModel {

    /**
     * 用户注册
     *
     * @param userBody 请求实体
     */
    void register(UserBody userBody, Callback<UserInfo> callback);

    /**
     * 用户登录
     */
    void login(UserBody userBody, Callback<UserInfo> callback);

    /**
     * 用户登录
     */
    void wechatLogin(String code, Callback<UserInfo> callback);

    /**
     * 用户登录
     */
    void wechatLogin2(WeChatUserInfo weChatUserInfo, Callback<UserInfo> callback);


    /**
     * 短信验证码
     */
    void smsCaptcha(UserBody userBody, Callback<String> callback);


    /**
     * 获取用户信息
     */
    void getUserInfo(long userId, Callback<UserInfo> callback, boolean hasProgress, boolean show);


    /**
     * 编辑用户信息
     */
    void updateUserInfo(UserInfo userInfo, Callback<UserInfo> callback);

    /**
     * 重置密码
     */
    void restPwd(UserBody userBod, Callback<String> callback);

    /**
     * 绑定手机
     */
    void bindingMobile(UserBody userBod, Callback<String> callback);

    /**
     * 关注列表
     */
    void friends(PageBody pageBody, Callback<Pager<UserInfo>> callback);


    /**
     * 粉丝列表
     */
    void follwers(PageBody pageBody, Callback<Pager<UserInfo>> callback);


    /**
     * 添加关注
     */
    void addFriend(long userId, Callback<EditFriendBody> callback);

    /**
     * 取消关注
     */
    void cancelFriend(long userId, Callback<EditFriendBody> callback);

    /**
     * 收货地址
     */
    void addressList(PageBody pageBody, Callback<Pager<UserAddress>> callback);

    /**
     * 添加收货地址
     */
    void addressAdd(AddressBody addressBody, Callback<UserAddress> callback);

    /**
     * 编辑收货地址
     */
    void addressUpdate(AddressBody addressBody, Callback<UserAddress> callback);

    /**
     * 删除收货地址
     */
    void addressDelete(String addressId, Callback<Object> callback);


    /**
     * 申请星品
     */
    void businessApply(MerchantApplyBody favouriteApplyBody, Callback<MerchantApplyBody> callback);


    /**
     * 申请星尚
     */
    void favouriteApply(FavouriteApplyBody favouriteApplyBody, Callback<FavouriteApplyBody> callback);


    /**
     * 申请星咖
     */
    void superstarApply(StarApplyBody starApplyBody, Callback<StarApplyBody> callback);

    /**
     * 申请网红认证资料详情
     * @param userId
     * @param callback
     */
    void favouriteApplyDetail(long userId, final Callback<UserInfo> callback);

    /**
     * 申请星咖认证资料详情
     * @param userId
     * @param callback
     */
    void starApplyDetail(long userId, final Callback<UserInfo> callback);

    /**
     * 申请商家认证资料详情
     * @param userId
     * @param callback
     */
    void businessApplyDetail(long userId, final Callback<UserInfo> callback);

    /**
     * 首页推荐用户接口
     */
    void getRecomUserList(PageBody pageBody, Callback<Pager<UserInfo>> callback);

    void getPGCVideoType(Callback<List<ImageInfo>> callback);

    /**
     * 广告位接口
     * 1-启动页广告
     * 2-首页热门广告
     * 3-星尚直播页广告
     * 4-星咖直播页广告
     * 5-星品直播页广告
     * 6-星品直播页宫格位广告
     */
    void adsInfoList(long adsNo, Callback<List<AdsInfo>> callback, boolean hasProgress);

    /**
     * 文件上传-认证
     */
    void ossAuth(String mediaType, Callback<OssAuth> callback);

    /**
     * 用户合作列表
     *
     * @param userId
     * @param pageBody
     * @param callback
     * @param hasProgress
     */
    void cooperationBusinessList(long userId, PageBody pageBody, final Callback<Pager<UserInfo>> callback, boolean hasProgress);


    void videoBuy(long productId, BuyProductRequest buyProductInfo, Callback<BuyProductResponse> callback);

    void chongzhixiubiBuy(long productId, BuyProductRequest buyProductInfo, Callback<BuyProductResponse> callback);

    /**
     * 用户合作列表
     *
     * @param pageBody
     * @param callback
     */
    void getUserCooperationList(PageBody pageBody, final Callback<Pager<UserInfo>> callback);

    /**
     * 用户视频列表
     *
     * @param userId
     * @param pageBody
     * @param callback
     * @param hasProgress
     */
    void getVideoListByUser(long userId, PageBody pageBody, Callback<Pager<VideoRoom>> callback, boolean hasProgress);

    /**
     * 删除多个视频
     * @param userIdList
     * @param callback
     */
    void removeVideoListByUser(List<Long> userIdList, final Callback<Object> callback);


    /**
     * 钻石充值价格列表
     */
    void rechargeList(Callback<List<Recharge>> callback);


    /**
     * 获取热门标签列表
     */
    void getHotLabelList(BasePageBody pageBody, Callback<Pager<LabelInfo>> callback);

    void getVSInfoList(Callback<List<VideoRemind>> callback);

    void subscriberStar(long userId, Callback<Object> callback);

    /**
     * 获取合作列表
     *
     * @return
     */
    void getCooperateList(PageBody pageBody, Callback<Pager<UserInfo>> callback);


    /**
     * 当前用户的钻石收益总额
     *
     * @return
     */
    void getDiamondEarn(Callback<DiamondEarnModel> callback);

    /**
     * 当前用户的现金收益总额
     *
     * @return
     */
    void getCashEarnAmounts(Callback<Long> callback);

    /**
     * 当前用户的现金收益详情
     *
     * @return
     */
    void getCashEarnInfo(PageBody pageBody, Callback<Pager<Goods>> callback);

    /**
     * 查询用户当前订单信息
     *
     * @return
     */
    void getOrderList(OrderPageBody pageBody, Callback<Pager<OrderInfo>> callback);

    void getOrderListSetStatue(String orderNo, Callback<Object> callback);

    /**
     * 被打赏的用户获取打赏的用户和金额
     *
     * @return
     */
    void getRewardMeList(PageBody pageBody, Callback<Pager<RewardInfo>> callback);

    /**
     * 当前用户打赏的用户
     *
     * @return
     */
    void getMyRewardList(PageBody pageBody, Callback<Pager<RewardInfo>> callback);


    /**
     * 当前用户获取到被打赏的总金额
     *
     * @return
     */
    void getRewardMeAmounts(Callback<Long> callback);

    /**
     * 当前用户打赏的总额
     *
     * @return
     */
    void getMyRewardAmounts(Callback<Long> callback);

    /**
     * 当前用户的账户钻石余额
     *
     * @return
     */
    void getAccountBalance(Callback<Long> callback);


    /**
     * 礼物列表
     *
     * @return
     */
    void getLiveGaves(PageBody pageBody, String giftType, Callback<Pager<GiftInfo>> callback);

    /**
     * 打赏主播
     *
     * @return
     */
    void rewardAnchor(RewardGiftBody rewardGiftBody, Callback<Long> callback);


    /**
     * 获取直播统计相关信息（本场收益，观看人数，送礼人数）
     *
     * @return
     */
    void getVideoStatistics(String videoId, Callback<VideoStatistic> callback);

    /**
     * 根据用户Ids批量查询用户详情
     *
     * @return
     */
    void getUserInfoByUserIds(List<Long> userIdList, Callback<List<UserInfo>> callback);


    void getProducts(GoodsPageBody pageBody, Callback<List<Goods>> callback);

    void checkIsFriend(String userid, Callback<Boolean> callback);

    void cooperationApplyDetail(CooperationApplyInfo cooperationApplyInfo, final Callback<CooperationApplyDetail> callback);

    void cooperationApply(String cooperationApplyInfo, Callback<Response<Object>> callback);

    void applyVideo(Object applyVideo, Callback<Response<Object>> callback);

    // 我的商品
    void getProducts(Object applyVideo, Callback<Response<Object>> callback);

    void getProductsInfo(String productsId, Callback<Goods> callback);

    void addProductsInfo(Goods goods, Callback<Object> callback);

    void updateProductsInfo(Goods goods, Callback<Goods> callback);

//    void delProducts(long productId, final Callback<Object> callback);

    // 我的合作
    void getUserCooperationList(CooperationPageBody cooperationPageBody, Callback<Pager<UserInfo>> callback);

    void gettuijianList(PGCVideoInfo pgcVideoInfo, Callback<Pager<PGCVideoInfo>> callback);


    void getSingeoVideoRoom(Long roomId, Callback<VideoRoom> callback);

    void getPaySuccessEnd(String dingdanhao, Callback<Object> callback);

    // 我的合作
    void agreeCooperation(UserBusinessCooperationBody userBusinessCooperationBody, Callback<Object> callback);

    void startLive(StartLiveBody startLiveBody, Callback<LiveInfo> callback);

    void offLive(String closeType, VideoOffLiveBody videoOffLiveBody, Callback<String> callback);

    /**
     * 用户关注的网红列表接口
     *
     * @param pageBody
     * @param callback
     */
    void getUserFriedsVideoList(PageBody pageBody, final Callback<List<VideoRoom>> callback);

    /**
     * 添加商品
     *
     * @return
     */
    void addProduct(Goods goods, Callback<Goods> callback);

    /**
     * 编辑商品
     *
     * @return
     */
    void updateProduct(Goods goods, Callback<Goods> callback);


    /**
     * 删除商品
     *
     * @return
     */
    void delProducts(long productId, Callback<Object> callback);

    /**
     * 商品详情
     *
     * @return
     */
    void getProductsInfo(long productId, Callback<Goods> callback);


    void videoProductList(long videoId, Callback<List<Goods>> callback);

    /**
     * 直播上架商品
     * @param videoId
     * @param json
     * @param callback
     */
    public abstract void productShelves(long videoId, String json, Callback<Object> callback);

    void productOffShelves(long videoId, String json, Callback<Object> callback);

    void getHotUserList(PageBody pageBody, Callback<Pager<UserInfo>> callback);

    void getPayStringBody(PayOrderDto payOrderDto, Callback<PayRecBody> callback);

    void getHomeHot(Callback<HomeHotFirstBody> callback);

    void getYoutubeList(String dingdanhao, Callback<YoutubeListBody> callback);

    void getHotMoreList(PageBody pageBody, Callback<HotMoreListBody> callback);
}
