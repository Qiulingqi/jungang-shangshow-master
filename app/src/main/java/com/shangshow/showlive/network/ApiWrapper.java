package com.shangshow.showlive.network;

import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.shangshow.showlive.common.model.ImageInfo;
import com.shangshow.showlive.network.http.Response;
import com.shangshow.showlive.network.http.RetrofitManager;
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
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * api包装类
 */
public class ApiWrapper {


    final Observable.Transformer transformer = new Observable.Transformer() {
        @Override
        public Object call(Object observable) {
            return ((Observable) observable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap(new Func1() {
                        @Override
                        public Object call(Object response) {
                            return flatResponse((Response<Object>) response);
                        }
                    });
        }
    };

    /**
     * 登陆
     *
     * @param userBody
     * @return UserInfo
     */
    public Observable<UserInfo> login(UserBody userBody) {
        return RetrofitManager.getInstance().getUserService().login(userBody)
                .compose(this.<UserInfo>applySchedulers());

    }

    /**
     * 注册
     *
     * @param userBody
     * @return UserInfo
     */
    public Observable<UserInfo> register(UserBody userBody) {
        return RetrofitManager.getInstance().getUserService().register(userBody)
                .compose(this.<UserInfo>applySchedulers());
    }

    /**
     * 短信验证码
     *
     * @param userBody
     * @return captchaToken
     */
    public Observable<String> smsCaptcha(UserBody userBody) {
        return RetrofitManager.getInstance().getUserService().smsCaptcha(userBody)
                .compose(this.<String>applySchedulers());
    }

    /**
     * 文件上传-认证
     *
     * @param mediaType
     * @return captchaToken
     */
    public Observable<OssAuth> ossAuth(String mediaType) {
        return RetrofitManager.getInstance().getUserService().ossAuth(mediaType)
                .compose(this.<OssAuth>applySchedulers());
    }

    /**
     * 获取用户信息
     *
     * @return captchaToken
     */
    public Observable<UserInfo> getUserInfo(long userId) {
        return RetrofitManager.getInstance().getUserService().getUserInfo(userId)
                .compose(this.<UserInfo>applySchedulers());
    }

    /**
     * 编辑用户信息
     *
     * @return captchaToken
     */
    public Observable<UserInfo> updateUserInfo(UserInfo userinfo) {
        return RetrofitManager.getInstance().getUserService().updateUserInfo(userinfo)
                .compose(this.<UserInfo>applySchedulers());
    }

    /**
     * 重置密码
     *
     * @return captchaToken
     */
    public Observable<String> restPwd(UserBody userBody) {
        return RetrofitManager.getInstance().getUserService().restPwd(userBody)
                .compose(this.<String>applySchedulers());
    }

    /**
     * 绑定手机
     *
     * @param userBody
     * @return
     */
    public Observable<String> bindingMobile(UserBody userBody) {
        return RetrofitManager.getInstance().getUserService().bindingMobile(userBody)
                .compose(this.<String>applySchedulers());
    }

    /**
     * 关注列表
     *
     * @param pageBody
     * @return
     */
    public Observable<Pager<UserInfo>> friends(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().friends(pageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }

    /**
     * 粉丝列表
     *
     * @param pageBody
     * @return
     */
    public Observable<Pager<UserInfo>> follwers(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().follwers(pageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }

    /**
     * 添加关注
     *
     * @param userId
     * @return
     */
    public Observable<EditFriendBody> addFriend(long userId) {
        return RetrofitManager.getInstance().getUserService().addFriend(userId)
                .compose(this.<EditFriendBody>applySchedulers());
    }

    /**
     * 取消关注
     *
     * @param userId
     * @return
     */
    public Observable<EditFriendBody> cancelFriend(long userId) {
        return RetrofitManager.getInstance().getUserService().cancelFriend(userId)
                .compose(this.<EditFriendBody>applySchedulers());
    }

    /**
     * 收货地址
     *
     * @param pageBody
     * @return
     */
    public Observable<Pager<UserAddress>> addressList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().addressList(pageBody)
                .compose(this.<Pager<UserAddress>>applySchedulers());
    }

    /**
     * 添加收货地址
     *
     * @param addressBody
     * @return
     */
    public Observable<UserAddress> addressAdd(AddressBody addressBody) {
        return RetrofitManager.getInstance().getUserService().addressAdd(addressBody)
                .compose(this.<UserAddress>applySchedulers());
    }

    /**
     * 编辑收货地址
     *
     * @param addressBody
     * @return
     */
    public Observable<UserAddress> addressUpdate(AddressBody addressBody) {
        return RetrofitManager.getInstance().getUserService().addressUpdate(addressBody)
                .compose(this.<UserAddress>applySchedulers());
    }

    /**
     * 编辑收货地址
     *
     * @param addressId
     * @return
     */
    public Call<Response<Object>> addressDelete(String addressId) {
        return RetrofitManager.getInstance().getUserService().addressDelete(addressId);
    }

    /**
     * 钻石充值价格列表
     *
     * @return
     */
    public Observable<List<Recharge>> rechargeList() {
        return RetrofitManager.getInstance().getUserService().rechargeList()
                .compose(this.<List<Recharge>>applySchedulers());
    }

    /**
     * 申请星咖
     *
     * @param starApplyBody
     * @return
     */
    public Observable<StarApplyBody> superstarApply(StarApplyBody starApplyBody) {
        return RetrofitManager.getInstance().getUserService().superstarApply(starApplyBody)
                .compose(this.<StarApplyBody>applySchedulers());
    }

    /**
     * 申请星尚
     *
     * @param favouriteApplyBody
     * @return
     */
    public Observable<FavouriteApplyBody> favouriteApply(FavouriteApplyBody favouriteApplyBody) {
        return RetrofitManager.getInstance().getUserService().favouriteApply(favouriteApplyBody)
                .compose(this.<FavouriteApplyBody>applySchedulers());
    }

    /**
     * 申请星品
     *
     * @param merchantApplyBody
     * @return
     */
    public Observable<MerchantApplyBody> businessApply(MerchantApplyBody merchantApplyBody) {
        return RetrofitManager.getInstance().getUserService().businessApply(merchantApplyBody)
                .compose(this.<MerchantApplyBody>applySchedulers());
    }

    /**
     * 申请网红认证资料详情
     *
     * @param userInfo
     * @return
     */
    public Observable<UserInfo> favouriteApplyDetail(UserInfo userInfo) {
        return RetrofitManager.getInstance().getUserService().favouriteApplyDetail(userInfo)
                .compose(this.<UserInfo>applySchedulers());
    }

    /**
     * 申请星咖认证资料详情
     *
     * @param userInfo
     * @return
     */
    public Observable<UserInfo> starApplyDetail(UserInfo userInfo) {
        return RetrofitManager.getInstance().getUserService().starApplyDetail(userInfo)
                .compose(this.<UserInfo>applySchedulers());
    }

    /**
     * 申请商家认证资料详情
     *
     * @param userInfo
     * @return
     */
    public Observable<UserInfo> businessApplyDetail(UserInfo userInfo) {
        return RetrofitManager.getInstance().getUserService().businessApplyDetail(userInfo)
                .compose(this.<UserInfo>applySchedulers());
    }

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
    public Observable<List<AdsInfo>> adsInfoList(long adsNo) {
        return RetrofitManager.getInstance().getUserService().adsInfoList(adsNo)
                .compose(this.<List<AdsInfo>>applySchedulers());
    }

    /**
     * 首页推荐用户接口
     *
     * @return
     */
    public Observable<Pager<UserInfo>> getRecomUserList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getRecomUserList(pageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }

    /**
     *
     *
     * @return
     */
    public Observable<List<ImageInfo>> getPGCVideoType() {
        return RetrofitManager.getInstance().getUserService().getPGCVideoType()
                .compose(this.<List<ImageInfo>>applySchedulers());
    }

    /**
     * 合作星品列表
     *
     * @return
     */
    public Observable<Pager<UserInfo>> cooperationBusinessList(long userId, PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().cooperationBusinessList(userId, pageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }

    /**
     * 购买商品->下订单
     *
     * @return
     */
    public Observable<BuyProductResponse> videoBuy(long productId, BuyProductRequest buyProductInfo) {
        return RetrofitManager.getInstance().getUserService().videoBuy(productId, buyProductInfo)
                .compose(this.<BuyProductResponse>applySchedulers());
    }

    /**
     *充值商秀币->下订单
     *
     * @return
     */
    public Observable<BuyProductResponse> chongzhixiubiBuy(long rechargeInfoId ,BuyProductRequest buyProductInfo) {
        return RetrofitManager.getInstance().getUserService().chongzhixiubiBuy(rechargeInfoId,buyProductInfo)
                .compose(this.<BuyProductResponse>applySchedulers());
    }

    /**
     * 购买商品->下订单
     *
     * @return
     */
    public Observable<BuyProductResponse> rechargeBuy(long rechargeInfoId) {
        return RetrofitManager.getInstance().getUserService().rechargeBuy(rechargeInfoId)
                .compose(this.<BuyProductResponse>applySchedulers());
    }



    /**
     * 合作星品列表
     *
     * @return
     */
    public Observable<Pager<UserInfo>> getUserCooperationList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getUserCooperationList(pageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }

    /**
     * ////////////////////////////////////////////////////////////////
     * /////////////////////////直播相关////////////////////////////////
     * ////////////////////////////////////////////////////////////////
     */

    /**
     * 用户视频列表
     *
     * @return
     */
    public Observable<Pager<VideoRoom>> getVideoListByUser(long userId, PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getVideoListByUser(userId, pageBody)
                .compose(this.<Pager<VideoRoom>>applySchedulers());
    }

    /**
     * 删除多个视频
     *
     * @return
     */
    public Call<Response<Object>> removeVideoListByUser(List<Long> userIdList) {
        return RetrofitManager.getInstance().getUserService().removeVideoListByUser(userIdList);
    }

    /**
     * 开启直播
     *
     * @return
     */
    public Observable<LiveInfo> startLive(StartLiveBody startLiveBody) {
        return RetrofitManager.getInstance().getUserService().startLive(startLiveBody)
                .compose(this.<LiveInfo>applySchedulers());
    }

    /**
     * 关闭直播【closeType:VIDEO-SAVE 保存视屏】|【VIDEO-CLOSE : 直接关闭不保存】
     *
     * @return
     */
    public Observable<String> offLive(String closeType, VideoOffLiveBody videoOffLiveBody) {
        return RetrofitManager.getInstance().getUserService().offLive(closeType, videoOffLiveBody)
                .compose(this.<String>applySchedulers());
    }

    /**
     * ////////////////////////////////////////////////////////////////
     * /////////////////////////商品相关////////////////////////////////
     * ////////////////////////////////////////////////////////////////
     */


///////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 直播间列表查询
     *
     * @return
     */
    public Observable<Pager<VideoRoom>> getVideoRoomList(String videoType, PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getVideoRoomList(videoType, pageBody)
                .compose(this.<Pager<VideoRoom>>applySchedulers());
    }

    /**
     * 直播间列表查询
     *
     * @return
     */
    public Observable<List<VideoRoom>> getUserFriedsVideoList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getUserFriedsVideoList(pageBody)
                .compose(this.<List<VideoRoom>>applySchedulers());
    }

    /**
     * http://www.jianshu.com/p/e9e03194199e
     * <p/>
     * Transformer实际上就是一个Func1<Observable<T>, Observable<R>>，
     * 换言之就是：可以通过它将一种类型的Observable转换成另一种类型的Observable，
     * 和调用一系列的内联操作符是一模一样的。
     *
     * @param <T>
     * @return
     */
    protected <T> Observable.Transformer<Response<T>, T> applySchedulers() {
        return (Observable.Transformer<Response<T>, T>) transformer;
    }

    /**
     * 对网络接口返回的Response进行分割操作
     *
     * @param response
     * @param <T>
     * @return
     */
    public <T> Observable<T> flatResponse(final Response<T> response) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onNext(response.result);
                }

                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }

    /**
     * 首页推荐用户接口
     *
     * @return
     */
    public Observable<Pager<LabelInfo>> getHotLabelList() {
        return RetrofitManager.getInstance().getUserService().getHotLabelList()
                .compose(this.<Pager<LabelInfo>>applySchedulers());
    }

    /**
     * 首页推荐用户接口
     *
     * @return
     */
    public Observable<Pager<LabelInfo>> getHotLabelList(BasePageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getHotLabelList(pageBody)
                .compose(this.<Pager<LabelInfo>>applySchedulers());
    }

    /**
     * 星咔直播计划列表查询
     *
     * @return
     */
    public Observable<List<VideoRemind>> getVSInfoList() {
        return RetrofitManager.getInstance().getUserService().getVSInfoList()
                .compose(this.<List<VideoRemind>>applySchedulers());
    }

    /**
     * 用户订阅星咔直播计划
     *
     * @return
     */
    public Observable<Object> subscriberStar(long userId) {
        return RetrofitManager.getInstance().getUserService().subscriberStar(userId)
                .compose(this.<Object>applySchedulers());
    }

    /**
     * 获取合作列表
     *
     * @return
     */
    public Observable<Pager<UserInfo>> getCooperateList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getCooperateList(pageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }

//    /**
//     * 拒绝/同意
//     *
//     * @return
//     */
//    public Observable<Object> agreeCooperation(UserBusinessCooperationBody userBusinessCooperationBody) {
//        return RetrofitManager.getInstance().getUserService().agreeCooperation(userBusinessCooperationBody)
//                .compose(this.<Object>applySchedulers());
//    }

    /**
     * 拒绝/同意
     *
     * @return
     */
    public Call<Response<Object>> agreeCooperation(RequestBody body) {
        return RetrofitManager.getInstance().getUserService().agreeCooperation(body);
    }

    /**
     * 当前用户的钻石收益总额
     *
     * @return
     */
    public Observable<DiamondEarnModel> getDiamondEarn() {
        return RetrofitManager.getInstance().getUserService().getDiamondEarn()
                .compose(this.<DiamondEarnModel>applySchedulers());
    }

    /**
     * 当前用户的现金收益总额
     *
     * @return
     */
    public Observable<Long> getCashBalance() {
        return RetrofitManager.getInstance().getUserService().getCashBalance()
                .compose(this.<Long>applySchedulers());
    }

    /**
     * 收益总额
     *
     * @return
     */
    public Observable<Long> getCashEarnAmounts() {
        return RetrofitManager.getInstance().getUserService().getCashEarnAmounts()
                .compose(this.<Long>applySchedulers());
    }

    /**
     * 当前用户的现金收益详情
     *
     * @return
     */
    public Observable<Pager<Goods>> getCashEarnInfo(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getCashEarnInfo(pageBody)
                .compose(this.<Pager<Goods>>applySchedulers());
    }

    /**
     * 查询用户当前订单信息
     *
     * @return
     */
    public Observable<Pager<OrderInfo>> getOrderList(OrderPageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getOrderList(pageBody)
                .compose(this.<Pager<OrderInfo>>applySchedulers());
    }

    /**
     * 操作用户当前订单信息  去支付或取消支付
     *
     * @return
     */
    public Observable<Object> getOrderListSetStatue(String oederNo) {
        return RetrofitManager.getInstance().getUserService().getOrderListSetStatue(oederNo)
                .compose(this.<Object>applySchedulers());
    }

    /**
     * 被打赏的用户获取打赏的用户和金额
     *
     * @return
     */
    public Observable<Pager<RewardInfo>> getRewardMeList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getRewardMeList(pageBody)
                .compose(this.<Pager<RewardInfo>>applySchedulers());
    }

    /**
     * 当前用户打赏的用户
     *
     * @return
     */
    public Observable<Pager<RewardInfo>> getMyRewardList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getMyRewardList(pageBody)
                .compose(this.<Pager<RewardInfo>>applySchedulers());
    }

    /**
     * 当前用户获取到被打赏的总金额
     *
     * @return
     */
    public Observable<Long> getRewardMeAmounts() {
        return RetrofitManager.getInstance().getUserService().getRewardMeAmounts()
                .compose(this.<Long>applySchedulers());
    }

    /**
     * 当前用户打赏的总额
     *
     * @return
     */
    public Observable<Long> getMyRewardAmounts() {
        return RetrofitManager.getInstance().getUserService().getMyRewardAmounts()
                .compose(this.<Long>applySchedulers());
    }

    /**
     * 当前用户的账户钻石余额
     *
     * @return
     */
    public Observable<Long> getAccountBalance() {
        return RetrofitManager.getInstance().getUserService().getAccountBalance()
                .compose(this.<Long>applySchedulers());
    }

    public Observable<Pager<GiftInfo>> getLiveGaves(PageBody pageBody, String giftType) {
        return RetrofitManager.getInstance().getUserService().getLiveGaves(pageBody, giftType)
                .compose(this.<Pager<GiftInfo>>applySchedulers());
    }

    public Observable<Long> rewardAnchor(RewardGiftBody rewardGiftBody) {
        return RetrofitManager.getInstance().getUserService().rewardAnchor(rewardGiftBody)
                .compose(this.<Long>applySchedulers());
    }

    public Observable<VideoStatistic> getVideoStatistics(String videoId) {
        return RetrofitManager.getInstance().getUserService().getVideoStatistics(videoId)
                .compose(this.<VideoStatistic>applySchedulers());
    }

    public Observable<List<UserInfo>> getUserInfoByUserIds(List<Long> userIds) {
        return RetrofitManager.getInstance().getUserService().getUserInfoByUserIds(userIds)
                .compose(this.<List<UserInfo>>applySchedulers());
    }

    public Observable<UserInfo> wechatLogin(String code) {
        return RetrofitManager.getInstance().getUserService().wechatLogin(code)
                .compose(this.<UserInfo>applySchedulers());
    }

    public Observable<UserInfo> wechatLogin2(WeChatUserInfo weChatUserInfo) {
        return RetrofitManager.getInstance().getUserService().wechatLogin2(weChatUserInfo)
                .compose(this.<UserInfo>applySchedulers());
    }

    public Observable<List<Goods>> getProducts(GoodsPageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getProducts(pageBody)
                .compose(this.<List<Goods>>applySchedulers());
    }

    /**
     * 检查是否好友关系
     *
     * @return
     */
    public Observable<Boolean> checkIsFriend(String userid) {
        return RetrofitManager.getInstance().getUserService().checkIsFriend(userid)
                .compose(this.<Boolean>applySchedulers());
    }

    /**
     * 检查是否好友关系
     *
     * @return
     */
    public Observable<CooperationApplyDetail> cooperationApplyDetail(CooperationApplyInfo cooperationApplyInfo) {
        return RetrofitManager.getInstance().getUserService().cooperationApplyDetail(cooperationApplyInfo)
                .compose(this.<CooperationApplyDetail>applySchedulers());
    }

    /**
     * 申请合作
     *
     * @return
     */
    public Call<Response<Object>> cooperationApply(RequestBody cooperationApplyInfo) {
        return RetrofitManager.getInstance().getUserService().cooperationApply(cooperationApplyInfo);
    }

    /**
     * 用户上传视屏申请提交
     *
     * @return
     */
    public Call<Response<Object>> applyVideo(RequestBody videoBody) {
        return RetrofitManager.getInstance().getUserService().applyVideo(videoBody);
    }

    /**
     * 获取商品列表
     *
     * @return
     */
    public Call<Response<Object>> getProducts(RequestBody videoBody) {
        return RetrofitManager.getInstance().getUserService().getProducts(videoBody);
    }

    /**
     * 获取商品详情
     *
     * @return
     */
    public Observable<Goods> getProductsInfo(String productsId) {
        return RetrofitManager.getInstance().getUserService().getProductsInfo(productsId)
                .compose(this.<Goods>applySchedulers());
    }

    /**
     * 添加商品
     *
     * @return
     */
    public Observable<Goods> addProductsInfo(Goods goods) {
        return RetrofitManager.getInstance().getUserService().addProductsInfo(goods).compose(this.<Goods>applySchedulers());
    }

    /**
     * 修改商品
     *
     * @return
     */
    public Observable<Goods> updateProductsInfo(Goods goods) {
        return RetrofitManager.getInstance().getUserService().updateProduct(goods).compose(this.<Goods>applySchedulers());
    }

    /**
     * 修改商品
     *
     * @return
     */
//    public Call<Response<Object>> delProducts(long goodId) {
//        return RetrofitManager.getInstance().getUserService().delProducts(goodId);
//    }

    /**
     * 合作星品列表
     *
     * @return
     */
    public Observable<Pager<UserInfo>> getUserCooperationList(CooperationPageBody cooperationPageBody) {
        return RetrofitManager.getInstance().getUserService().getUserCooperationList(cooperationPageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }
    /**
     * 合作星品列表
     *
     * @return
     */
    public Observable<Pager<PGCVideoInfo>> gettuijianList(PGCVideoInfo pgcVideoInfo) {
        return RetrofitManager.getInstance().getUserService().gettuijianList(pgcVideoInfo)
                .compose(this.<Pager<PGCVideoInfo>>applySchedulers());
    }
    /**
     * 合作星品列表
     *
     * @return
     */
    public Observable<VideoRoom> getSingeoVideoRoom(Long roomId) {
        return RetrofitManager.getInstance().getUserService().getSingeoVideoRoom(roomId)
                .compose(this.<VideoRoom>applySchedulers());
    }

    /**
     *
     *
     * @return
     */
    public Observable<Object> getPaySuccessEnd(String dingdanhao) {
        return RetrofitManager.getInstance().getUserService().getPaySuccessEnd(dingdanhao)
                .compose(this.<Object>applySchedulers());
    }

    public Observable<Goods> addProduct(Goods goods) {
        return RetrofitManager.getInstance().getUserService().addProduct(goods)
                .compose(this.<Goods>applySchedulers());
    }

    public Observable<Goods> updateProduct(Goods goods) {
        return RetrofitManager.getInstance().getUserService().updateProduct(goods)
                .compose(this.<Goods>applySchedulers());
    }

    public Call<Response<Object>> delProducts(long productId) {
        return RetrofitManager.getInstance().getUserService().delProducts(productId);
    }

    public Observable<Goods> getProductsInfo(long productId) {
        return RetrofitManager.getInstance().getUserService().getProductsInfo(productId)
                .compose(this.<Goods>applySchedulers());
    }

    public Observable<List<Goods>> videoProductList(long videoId) {
        return RetrofitManager.getInstance().getUserService().videoProductList(videoId)
                .compose(this.<List<Goods>>applySchedulers());
    }

    public Call<Response<Object>> productShelves(long videoId, RequestBody requestBody) {
        return RetrofitManager.getInstance().getUserService().productShelves(videoId, requestBody);
    }

    public Call<Response<Object>> productOffShelves(long videoId, RequestBody requestBody) {
        return RetrofitManager.getInstance().getUserService().productOffShelves(videoId, requestBody);
    }

    public Observable<Pager<UserInfo>> getHotUserList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getHotUserList(pageBody)
                .compose(this.<Pager<UserInfo>>applySchedulers());
    }

    /**
     * 获取支付所需要的参数信息
     */
    public Observable<PayRecBody> getPayStringBody(PayOrderDto payOrderDto) {
        return RetrofitManager.getInstance().getUserService().getPayStringBody(payOrderDto)
                .compose(this.<PayRecBody>applySchedulers());
    }
    /**
     * 获取封面参数
     */
    public Observable<HomeHotFirstBody> getHomeHot() {
        return RetrofitManager.getInstance().getUserService().getHomeHot()
                .compose(this.<HomeHotFirstBody>applySchedulers());
    }
    /**
     * youtube
     */
    public Observable<YoutubeListBody> getYoutubeList(String dingdanhao) {
        return RetrofitManager.getInstance().getUserService().getYoutubeList(dingdanhao)
                .compose(this.<YoutubeListBody>applySchedulers());
    }
    /**
     * 获取热门视频更多type
     *
     */

    public Observable<HotMoreListBody> getHotMoreList(PageBody pageBody) {
        return RetrofitManager.getInstance().getUserService().getHotMoreList(pageBody)
                .compose(this.<HotMoreListBody>applySchedulers());
    }
}
