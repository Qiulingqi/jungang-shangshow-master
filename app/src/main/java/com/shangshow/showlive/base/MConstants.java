package com.shangshow.showlive.base;

public class MConstants {


    // 正式网址
    public static final String BASE_URL = "http://120.55.82.96:8082/";
    // 测试网址
     //public static final String BASE_URL = "http://192.168.199.166:8089/";
    public static final String TEST_URL = "http://gank.io/api/data/";
    public static final int HTTP_CONNECT_TIMEOUT = 20; //
    public static final String SUCCESS_CODE = "0"; //接口返回:0-成，非0失败.
    public static final String SHANGXIU_PHONE = "";
    /**
     * 用户信息相关
     */
    public static final String KEY_USER_NIM_ACCOUNT = "nim_account";
    public static final String KEY_USER_NIM_TOKEN = "wytoken";
    public static final String KEY_USER_TOKEN = "token";//token
    public static final String KEY_USER = "userInfo";//用户信息
    public static final String key_IS_FRIST_START_APP = "isFristStartApp";//是否是第一次启动app 0:第一次启动， 1:非第一次
    /**
     * 首页tab切换
     */
    public static final int MENU_HOME = 0; //主页
    public static final int MENU_MERCHANT = 1; //星品
    public static final int MENU_SUPERSTAR = 2; //星咖
    public static final int MENU_FAVOURITE = 3; //星尚
    public static final int MENU_PERSONAL = 4; //个人中心

    /**
     * 分页信息
     */
    public static final int PAGE_INDEX = 1; //起始页
    public static final int PAGE_SIZE = 10; //每页显示多少数据


    /**
     * [1:普通用户,2:星尚,3:星咖,4:星品]
     */

    public static final String USER_TYPE_NULL_0 = "0"; //没有类型
    public static final String USER_TYPE_COMMONUSER = "1"; //普通用户
    public static final String USER_TYPE_FAVOURITE = "2"; // 网红
    public static final String USER_TYPE_SUPERSTAR = "3"; //星咖
    public static final String USER_TYPE_MERCHANT = "4"; //商家


    public static final String MY_REWARD = "5";//我的打赏
    public static final String REWARD_ME = "6";//打赏我的

    public static final String DIAMOND_EARN = "7";//钻石收益
    public static final String CASH_EARN = "8";//现金收益

    public static final String AUCTION_LAUNCH = "9";//发起竞拍
    public static final String AUCTION_PARTICIPATE = "10";//参与竞拍

    public static final String GIFT_TYPE_DIAMAND = "22"; //星咖
    public static final String GIFT_TYPE_GOOD = "23"; //星品

    /**
     * 控件高度
     */
    public static final double RATIO_POINT_BANNER = 1 / 3d;//【banner广告位】 宽度占满屏幕，高度为宽度的1/3
    public static final double RATIO_POINT_VOIDEIMAGE = 3 / 4d;//【直播列表图片高度】 宽度占满屏幕，高度为宽度的1/3
    public static final int MERCHANT_GRID_ADS_GAP = 10;//星品动态布局，图片间隙


    /**
     * 短信验证码类型
     */
    public static final String SMSCAPTCHA_OP_REGISTER = "register"; //表格显示
    public static final String SMSCAPTCHA_OP_LOGIN = "login"; //表格显示
    public static final String SMSCAPTCHA_OP_FORGET = "forget"; //表格显示
    public static final String SMSCAPTCHA_OP_BINDING = "binding"; //表格显示

    /**
     * 用户性别
     */
    public static final String User_Sex_Type_Male = "M"; // 男
    public static final String User_Sex_Type_Male_VALUE = "男"; //
    public static final String User_Sex_Type_Female = "F"; // 女
    public static final String User_Sex_Type_Female_VALUE = "女"; //

    public static final String ISDEFAULT_ADDRESS_YES = "1"; //默认收货地址
    public static final String ISDEFAULT_ADDRESS_NO = "2"; //非默认收货地址


    /**
     * 页面逻辑跳转intnet请求码
     */
    public static final int REQUESTCODE_UPDATE_NICKNAME = 1001; //更新昵称
    public static final int REQUESTCODE_UPDATE_ADDRESS_ADD = 1002; //新增收货地址
    public static final int REQUESTCODE_UPDATE_ADDRESS_UPDATE = 1003; //更新收货地址
    public static final int REQUESTCODE_UPDATE_SEX_UPDATE = 1004; //编辑性别
    /**
     * 头像名称
     */
    public static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    public static final int PHOTO_REQUEST_SELECT = 1005;// 选择图片
    public static final int PHOTO_REQUEST_CAMERA = 1006;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 1007;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 1008;// 裁剪
    public static final int REQUESTCODE_UPLOAD = 1009;// 文件上传
    public static final int FILE_REQUEST_SELECT = 1010;// 选择文件by类型
    /**
     * 下载渠道号
     */
    public final static String CHANNEL_MYAPP = "D0000001_MYAPP"; // 应用宝
    public final static String CHANNEL_360 = "D0000002_360"; // 360
    public final static String CHANNEL_BAIDU = "D0000003_BAIDU"; // 91&百度
    public final static String CHANNEL_XIAOMI = "D0000004_XIAOMI"; // 小米应用商店
    public final static String CHANNEL_WANDOUJIA = "D0000005_WANDOUJIA"; // 豌豆荚
    public final static String CHANNEL_ANZHI = "D0000006_ANZHI"; // 安智


    public static final String UPLOAD_FILE_TYPE_PICTURE = "file_picture";//
    public static final String UPLOAD_FILE_TYPE_PICTURE_SUFFIX = ".jpg";//
    public static final String UPLOAD_FILE_TYPE_VIDEO = "file_video";//
    public static final String UPLOAD_FILE_TYPE_VIDEO_SUFFIX = ".3gp";//
    /**
     * 文件上传类型
     */
    public static final String UPLOAD_TYPE_AVATAR = "avatar";// 个人头像
    public static final String UPLOAD_TYPE_VIDEO_IMAGE = "video_image";// 个人头像
    public static final String UPLOAD_TYPE_APPLY = "apply";// 申请
    public static final String UPLOAD_TYPE_GOODS = "goods";// 商品
    public static final String UPLOAD_TYPE_VIDEO = "video";// 直播
    public static final String UPLOAD_TYPE_COVER = "cover";// 直播

    public static final int DATA_4_REFRESH = 0; //刷新
    public static final int DATA_4_LOADMORE = 1; //加载更多


    /**
     * recycler显示样式
     */
    public static final int RECYCLER_LINEAR = 0; //列表显示
    public static final int RECYCLER_GRID = 1; //表格显示
    public static final int RECYCLER_STAGGERED_GRID = 2; //瀑布流
    /**
     * 下拉刷新header样式{黑、白}
     */
    public static final String REFRESH_HEADER_BLACK = "black";
    public static final String REFRESH_HEADER_WHITE = "white";


    /**
     * 拍照、选择相片返回的uri类型
     */
    public static final String MEDIAFROM_TYPE_CONTENT = "content://media";
    public static final String MEDIAFROM_TYPE_FILE = "file:///";


    /**
     * 广告跳转类型【网页、nation页面】
     */
    public static final int AD_ACTIONTYPE_WEB = 1;
    public static final int AD_ACTIONTYPE_NATIVE = 2;

    public static final int DELAYED = 300;
    public static final int WAIT_TIME = 2000;

    /**
     * 正负符号
     */
    public static final String SymbolPlus = "＋";
    public static final String SymbolMinus = "－";

    /**
     * 广告位区块编号
     */
    public static final int ADS_NO_SPLASH = 1;//启动页广告
    public static final int ADS_NO_HOMEHOT = 2;//首页热门广告
    public static final int ADS_NO_FAVOURITE = 3;//星尚直播页广告
    public static final int ADS_NO_SUPERSTAR = 4;//星咖直播页广告
    public static final int ADS_NO_MERCHANT = 5;//星品直播页广告
    public static final int ADS_NO_MERCHANT_GRID = 6;//星品直播页宫格位广告

    /**
     * 直播-房间在线人数轮询时间
     */
    public final static int FETCH_ONLINE_PEOPLE_COUNTS_DELTA = 10 * 1000;
    /**
     * 直播端状态
     */
    public final static int STATE_LIVE_INITIAL = 10000;//直播初始状态
    public final static int STATE_LIVE_START = 10001;//直播开始
    public final static int STATE_LIVE_LIVING = 10002;//直播中
    public final static int STATE_LIVE_END = 10003;//直播结束
    public final static int STATE_LIVE_ERROR = 10004;//直播结束
    /**
     * 客户端状态
     */
    public final static int STATE_VIDEO_INITIAL = 20000;//观看初始状态
    public final static int STATE_VIDEO_START = 20001;//观看开始状态
    public final static int STATE_VIDEO_LIVING = 20002;//观看中状态
    public final static int STATE_VIDEO_COMPLETE = 20003;//观看结束状态
    public final static int STATE_VIDEO_ERROR = 20004;//观看错误
    public final static int STATE_VIDEO_STOP = 20005;//观看结束状态

    /**
     * 关闭直播间【参数】
     */
    public static final String CLOSE_TYPE_SAVE = "VIDEO-SAVE";
    public static final String CLOSE_TYPE_CLOSE = "VIDEO-CLOSE";


    /**
     * 直播间类型【上传视频】|【直播视频】
     */
    public static final String VIDEOROOM_TYPE_LIVE = "LIVE";
    public static final String VIDEOROOM_TYPE_VIDEO = "VIDEO";


    public final static String[] imageUrls = new String[]{
            "http://img4.duitang.com/uploads/blog/201311/04/20131104193715_NCexN.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201408/09/20140809210610_iTSJx.thumb.jpeg",
            "http://cdn.duitang.com/uploads/blog/201401/07/20140107223310_LH3Uy.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201405/09/20140509222156_kVexJ.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201306/14/20130614185903_raNR3.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201112/11/20111211191621_HU4Bj.thumb.jpg",
            "http://cdn.duitang.com/uploads/item/201408/07/20140807224553_VXaUc.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/29/20140729105929_GQLwC.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201408/04/20140804160432_LnFeB.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201408/04/20140804161101_JVJea.thumb.jpeg",
            "http://cdn.duitang.com/uploads/blog/201408/04/20140804093210_FxFNd.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201408/04/20140804160314_hRKtv.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201408/01/20140801080524_SnGfE.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/23/20140723140958_NSWfE.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201407/22/20140722153305_WHejQ.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/21/20140721010148_ZBQwe.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201407/23/20140723085122_cmteu.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/23/20140723130620_Z2yJB.thumb.jpeg",
            "http://cdn.duitang.com/uploads/blog/201407/20/20140720204738_NXxLE.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201407/20/20140720134916_VGfyh.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/17/20140717113117_mUssJ.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/17/20140717121501_wfFEm.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/17/20140717121431_w4AV8.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/17/20140717121918_TtJjY.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/11/20140711234806_FNLBQ.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/18/20140718121437_UyiAS.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/17/20140717114829_RiCXR.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201407/17/20140717120301_wGFYL.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/11/20140511121106_JXS4B.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/10/20140510094144_kfLji.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/09/20140509201906_kERjy.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/08/20140508193226_UaSGB.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201405/05/20140505201747_aPNtf.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/06/20140506104824_jPWQj.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/05/20140505201105_MkXdn.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/03/20140503205822_GCzta.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/03/20140503205535_cCHPB.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/03/20140503204354_xxSQX.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201404/06/20140406191307_GTxFd.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201404/06/20140406191247_BG2cU.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201404/06/20140406191114_MzYtw.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201404/06/20140406191127_fazJA.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/10/20140710081204_vYnCi.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/15/20140715133758_M2Y3J.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201407/13/20140713190806_TGJHm.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201407/05/20140705223413_5r3ze.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/13/20140713012526_tcy5u.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/13/20140713121424_Gy43T.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201407/15/20140715033844_tcvrY.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/10/20140710181106_4HHay.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/06/20140706122850_8PER3.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/07/20140707192042_8xKXF.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/07/20140707063954_mVa3y.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201407/08/20140708093733_AFFhc.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201407/07/20140707161220_unvzn.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201407/07/20140707113856_hBf3R.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201308/26/20130826090203_NtuYA.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201407/07/20140707145925_dHeKV.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625101534_xiZxN.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201406/30/20140630150534_EWUVY.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625121626_ZmT5n.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201307/31/20130731231806_4yGxV.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/28/20140628122218_fLQyP.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201406/26/20140626131831_MrdKP.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201406/16/20140616165201_nuKWj.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625140308_KP4rn.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625121604_2auuA.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201406/25/20140625131625_LmmLZ.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625132851_mPmKY.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625133312_ZtmW4.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625164858_AuafS.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/24/20140624114145_e4iVw.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/25/20140625100427_Hkxj5.thumb.jpeg",
            "http://cdn.duitang.com/uploads/blog/201406/25/20140625213455_VHHcL.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/25/20140625132659_UuES4.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/24/20140624020050_zCE4U.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/18/20140618152533_dJjtW.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/20/20140620075216_twZE4.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/22/20140622162247_Z4WK4.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/20/20140620075158_TnyKU.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201406/18/20140618235506_5QJwc.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/18/20140618152515_AFcLy.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/03/20140603001954_NjKfX.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201405/31/20140531232042_4FkHQ.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201406/13/20140613002234_LHXcT.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201405/31/20140531231843_J5Euh.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/11/20140611220941_xBeyi.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/13/20140613114809_yuHRV.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/13/20140613120109_yL8hk.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201406/01/20140601185856_Q5jZr.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/06/20140606004724_GxQHQ.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201406/08/20140608003809_3JnEK.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/10/20140610085447_zeXJU.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201406/08/20140608193617_HyFrY.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201405/30/20140530190040_KQdsM.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/09/20140609101937_UBfJJ.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201406/10/20140610170410_cFhwW.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/09/20140609225334_PdGwG.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201406/09/20140609184438_e33i2.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201405/29/20140529200010_AfL8f.thumb.jpeg",
            "http://img4.duitang.com/uploads/blog/201406/08/20140608104649_KVtMx.thumb.png",
            "http://img5.duitang.com/uploads/item/201406/01/20140601215152_wi4wf.thumb.jpeg",
            "http://cdn.duitang.com/uploads/blog/201406/08/20140608194234_FEGkW.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201405/31/20140531221002_Awtv8.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/08/20140608091030_TJ3Cc.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201405/31/20140531221355_cSCTt.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/08/20140608005415_arBdK.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/08/20140608000002_2MTjn.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/03/20140603012613_z88sn.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201405/31/20140531221745_rnAzU.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201405/31/20140531220735_XBxFP.thumb.jpeg",
            "http://cdn.duitang.com/uploads/blog/201406/08/20140608194112_uEYf5.thumb.jpeg",
            "http://img5.duitang.com/uploads/blog/201406/08/20140608225626_xc2QT.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/07/20140607235759_sNS5Z.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201405/31/20140531220635_Lrw3w.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/08/20140608004245_jmBmP.thumb.jpeg",
            "http://img4.duitang.com/uploads/item/201406/08/20140608020213_SBfGH.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/08/20140608214105_kvVVY.thumb.jpeg",
            "http://img5.duitang.com/uploads/item/201406/03/20140603001556_XsMEv.thumb.jpeg",
            "http://cdn.duitang.com/uploads/item/201406/08/20140608024120_XjjGB.thumb.jpeg",
    };

}
