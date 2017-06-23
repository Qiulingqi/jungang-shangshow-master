package com.shangshow.showlive.controller.liveshow.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.listview.MessageListView;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.extension.DanmuAttachment;
import com.netease.nim.uikit.session.extension.GiftInfo;
import com.netease.nim.uikit.session.extension.GiftInfoAttachment;
import com.netease.nim.uikit.session.extension.LikeAttachment;
import com.netease.nim.uikit.session.module.Container;
import com.netease.nim.uikit.session.module.ModuleProxy;
import com.netease.nim.uikit.session.module.input.InputPanel;
import com.netease.nim.uikit.session.module.list.MessageListPanel;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.DateUtils;
import com.shangshow.showlive.common.utils.MessageUtils;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.viewpager.CustomViewPager;
import com.shangshow.showlive.controller.adapter.P2PChatAdapter;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.liveshow.chatroom.helper.ChatRoomMemberCache;
import com.shangshow.showlive.controller.liveshow.chatroom.module.ChatRoomMsgListPanel;
import com.shangshow.showlive.controller.liveshow.widget.opendanmaku.DanmakuItem;
import com.shangshow.showlive.controller.liveshow.widget.opendanmaku.DanmakuView;
import com.shangshow.showlive.controller.liveshow.widget.opendanmaku.IDanmakuItem;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;
import com.shaojun.utils.log.Logger;
import com.shaojun.widget.tablayout.SlidingTabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import rx.Subscription;

/**
 * <直播-客户端widget管理类>
 * <功能详细描述>
 *
 * @auth:jeremy
 * @see: [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */

// 客户端视图  所有的界面操作

/**
 * 点赞布局设置 OK
 */
public class LiveAudienceWidget extends RelativeLayout implements ModuleProxy, View.OnClickListener {
    private UserInfo currentUser = new UserInfo();
    private List<PrivateLetter> privateLetters = new ArrayList<PrivateLetter>();
    public LinearLayout liveAudienceLl;
    public HeadImageView masterHead;
    public ImageView userTypeIcon;
    public ImageView stop_live_user_icon;
    public ImageView stop_live_user_type_icon;
    public TextView tv_stop_live_name;
    public View rl_stop_live_parent;
    public BaseButton bb_stop_live_close;
    public TextView masterNameText;
    public TextView onlineCountText; // 在线人数view
    // view
    public RecyclerView giftRecyclerView; // 礼物列表
    public RelativeLayout giftAnimationViewDown; // 礼物动画布局1
    public RelativeLayout giftAnimationViewUp; // 礼物动画布局2
    public PeriscopeLayout periscopeLayout; // 点赞爱心布局
    public View giftLayout; // 礼物布局
    public ViewGroup controlLayout; // 右下角几个image button布局
    /**
     * 结束
     */
    public ViewGroup liveFinishLayout;
    /*public TextView liveFinishMasterName;*/
//    public RelativeLayout liveFinishBtn;
    protected GiftRecyclerAdapter giftRecycleradapter;
    protected GiftAnimation giftAnimation; // 礼物动画
    protected ChatRoomInputPanel inputPanel;
    protected ChatRoomMsgListPanel messageListPanel;
    protected MessageListPanel privateLetterMsgListPanel;
    Bitmap bitmap;
    ArrayList<View> viewContainter = new ArrayList<View>();
    ArrayList<View> viewContainter2 = new ArrayList<View>();
    PrivateLetterWidget view1;
    List<IDanmakuItem> danmukuList = new ArrayList<>();
    private TextView attentionText;
    // modules
    private LinearLayout messageAndLikeLl;
    private InputPanel inputPanel2;
    private RecyclerView guestRecyclerView;
    /**
     * 消息
     */
    private RelativeLayout messageInput;
    private View reply_private_letter_ll;
    private LinearLayout privateLetterInput;
    private TextView liveDiamandAmountText;
    private CustomViewPager giftTabViewPager;
    private SlidingTabLayout giftSlidingTabLayout;
    private LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface;
    private String currRoomId = "";
    private String currAccount = "";
    private View rootView;
    private ImageButton closeBtn, giftBtn, shopBtn, messageBtn, privateLetterBtn, shareBtn; // 礼物按钮
    //    private Button sendGiftBtn;
    private TextView preparedText;
//    private TextView finishTipText;
    //    public ImageView liveFinishHeadImage;
    // 选中的礼物
    private int giftPosition = -1;
    private List<Gift> giftList;

    //私信
    private LinearLayout replyPrivateLetterLayout;
    private RelativeLayout privateLetterLayout;
    private RecyclerView privateLetterRecyclerView;
    private CustomViewPager privateLetterTabViewPager;
    private SlidingTabLayout privateLetterSlidingTabLayout;
    private String[] privateLetterTitles = {"私信", "通讯录"};
    private List<String> privateLetterTitleList = new ArrayList<>();
    //礼物
    private View rl_live_audience_addmoney;
    private TextView giftChargeDiamandNumber;
    private ImageView giftChargeDiamandNev;
    private Button sendGiftBtn;
    private View replyBackBtn;
    private ImageView replyCloseBtn;
    private ImageView privateLetterBackBtn;
    private ImageView privateLetterCloseBtn;
    private TextView replyUserName;
    private ListView messageListView;
    private MessageListView messageP2pListView;
    private P2PChatAdapter p2PChatAdapter;
    private String[] giftTitles = {"虚拟打赏", "实物打赏"};
    private List<String> titleList = new ArrayList<>();
    private LayoutInflater mInflater;
    private Context mContext;
    private DanmakuContext mDanmuContext;
    private VideoRoom videoRoom;
    private UserHeaderRecyclerAdapter userHeaderRecyclerAdapter;
    private List<UserInfo> guestList;
    private List<PrivateLetter> privateLetterList=new ArrayList<>();
    private UserInfo imSender;
    private UserInfo sendUser;
    private List<Long> userIdList = new ArrayList<>();
    private SessionTypeEnum sessionType = SessionTypeEnum.ChatRoom;
    private TextView buttonP2pSendMessage;
    private EditText editP2pTextMessage;
    private ImageView danmuImageView;
    private DanmakuView mDanmakuView;
    private View live_danmu_rl;

    /**
     * 礼物动画效果
     */
    private List<Long> animations = new ArrayList<Long>();
    ImageView iv_plane_screw, iv_plane_head, iv_car_behind, iv_car_before, iv_car_lambo, iv_love_chocolate_animate,
            iv_black_chocolate, iv_bouquet_animate, iv_white_chocolate, iv_cake;
    private View ll_plane, ll_live_car;
    int planeWidth, planeHeight, carWidth, carHeight;

    public LiveAudienceWidget(Context context) {
        super(context);
        mContext = context;
    }

    public LiveAudienceWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public LiveAudienceWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    public void setLiveAudienceInterface(LiveWidgetOperate.LiveAudienceInterface liveAudienceInterface) {
        this.liveAudienceInterface = liveAudienceInterface;
        initView();
    }

    private void initView() {

        rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_live_audience_widget, this);
        liveAudienceLl = (LinearLayout) findViewById(R.id.live_audience_widget_layout);
        //顶部视图
//        roomText = (TextView) findViewById(R.id.room_name);
        masterHead = (HeadImageView) findViewById(R.id.user_icon);
        userTypeIcon = (ImageView) findViewById(R.id.user_type_icon);
        stop_live_user_icon = (ImageView) findViewById(R.id.stop_live_user_icon);
        stop_live_user_type_icon = (ImageView) findViewById(R.id.stop_live_user_type_icon);
        tv_stop_live_name = (TextView) findViewById(R.id.tv_stop_live_name);
        rl_stop_live_parent = findViewById(R.id.rl_stop_live_parent);
        bb_stop_live_close = (BaseButton) findViewById(R.id.bb_stop_live_close);
        masterNameText = (TextView) findViewById(R.id.master_name);
        onlineCountText = (TextView) findViewById(R.id.online_count_text);
        liveDiamandAmountText = (TextView) findViewById(R.id.live_diamand_amount_text);
        attentionText = (TextView) findViewById(R.id.attention_btn);
        getAccountBalance();
        guestRecyclerView = (RecyclerView) findViewById(R.id.in_room_audience_recycler);

        //设置布局管理器
        guestRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        userHeaderRecyclerAdapter = new UserHeaderRecyclerAdapter(mContext, guestList, R.layout.item_recycler_user_header);
        guestRecyclerView.setAdapter(userHeaderRecyclerAdapter);

        //控制按钮
        controlLayout = (ViewGroup) findViewById(R.id.control_layout);
//        switchBtn = (ImageButton) findViewById(R.id.switch_btn);
        closeBtn = (ImageButton) findViewById(R.id.close_live_btn);
        giftBtn = (ImageButton) findViewById(R.id.gift_btn);
        messageBtn = (ImageButton) findViewById(R.id.message_btn);
        shopBtn = (ImageButton) findViewById(R.id.shop_btn);
        privateLetterBtn = (ImageButton) findViewById(R.id.private_letter_btn);
        shareBtn = (ImageButton) findViewById(R.id.share_btn);
        //私信
        initPrivateLetterLayout();
        // 初始化礼物布局
        initGiftLayout();
        // 点赞的爱心布局  观众端的点赞爱心
        messageAndLikeLl = (LinearLayout) findViewById(R.id.live_audience_message_and_like_ll);
        //点击发送爱心布局
        messageAndLikeLl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLike();
            }
        });
        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
//        sendGiftBtn = (Button) findViewById(R.id.send_gift_btn);
//        giftRecycleradapter = new GiftRecyclerAdapter(getContext(),giftList,R.layout.item_recycler_gift);
//        giftRecyclerView.setAdapter(giftRecycleradapter);


        //消息
        messageInput = (RelativeLayout) findViewById(R.id.textMessageLayout);
        reply_private_letter_ll = findViewById(R.id.reply_private_letter_ll);
        buttonP2pSendMessage = (TextView) findViewById(R.id.buttonP2pSendMessage);
        editP2pTextMessage = (EditText) findViewById(R.id.editP2pTextMessage);
        preparedText = (TextView) findViewById(R.id.prepared_text);

        // 直播结束布局
        liveFinishLayout = (ViewGroup) findViewById(R.id.live_finish_layout);
//        liveFinishBtn = (RelativeLayout) findViewById(R.id.finish_close_btn);
//        finishTipText = (TextView) findViewById(R.id.finish_tip_text);
//        liveFinishMasterName = (TextView) findViewById(R.id.finish_master_name);
//        finishTipText.setText(R.string.loading);

        //礼物
        rl_live_audience_addmoney = findViewById(R.id.rl_live_audience_addmoney);
        giftChargeDiamandNumber = (TextView) findViewById(R.id.live_charge_count_tv);
        giftChargeDiamandNev = (ImageView) findViewById(R.id.nev_charge_diamand_iv);
//        sendGiftBtn=(Button)findViewById(R.id.live_send_gift_btn) ;
        initDanmu();
//        sendGiftBtn.setOnClickListener(this);
//        giftBtn.setOnClickListener(this);
//        giftLayout.setOnClickListener(this);
//        liveFinishBtn.setOnClickListener(this);

        closeBtn.setOnClickListener(this);
//        switchBtn.setOnClickListener(this);
        attentionText.setOnClickListener(this);
        giftBtn.setOnClickListener(this);
        privateLetterBtn.setOnClickListener(this);
        messageBtn.setOnClickListener(this);
        shopBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        replyBackBtn.setOnClickListener(this);
        replyCloseBtn.setOnClickListener(this);
        buttonP2pSendMessage.setOnClickListener(this);
        rl_live_audience_addmoney.setOnClickListener(this);
//        privateLetterBackBtn.setOnClickListener(this);
//        privateLetterCloseBtn.setOnClickListener(this);

//        liveFinishBtn.setOnClickListener(this);
        liveAudienceLl.setOnClickListener(this);
        bb_stop_live_close.setOnClickListener(this);
        changeVideoState(MConstants.STATE_VIDEO_INITIAL);

    }

    private void initDanmu() {
        live_danmu_rl = findViewById(R.id.live_danmu_rl);
        mDanmakuView = (DanmakuView) findViewById(R.id.danmakuView);
    }

    /*private List<IDanmakuItem> initItems() {
        List<IDanmakuItem> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            IDanmakuItem item = new DanmakuItem(mContext, i + " : plain text danmuku", mDanmakuView.getWidth());
            list.add(item);
        }

        String msg = " : text with image   ";
        for (int i = 0; i < 100; i++) {
            ImageSpan imageSpan = new ImageSpan(mContext, R.drawable.em);
            SpannableString spannableString = new SpannableString(i + msg);
            spannableString.setSpan(imageSpan, spannableString.length() - 2, spannableString.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            IDanmakuItem item = new DanmakuItem(mContext, spannableString, mDanmakuView.getWidth(), 0, 0, 0, 1.5f);
            list.add(item);
        }
        return list;
    }*/


    //私信
    private void initPrivateLetterLayout() {
        privateLetterLayout = (RelativeLayout) findViewById(R.id.private_letter_list_rl);
        privateLetterLayout.setVisibility(View.GONE);
        privateLetterTabViewPager = (CustomViewPager) findViewById(R.id.private_letter_tab_view_pager);
        privateLetterSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.private_letter_sliding_tab_layout);
        viewContainter2 = new ArrayList<>();
        privateLetterTitleList = new ArrayList<>();
        for (String title : privateLetterTitles) {
            privateLetterTitleList.add(title);
        }
        mInflater = LayoutInflater.from(mContext);
        view1 = new PrivateLetterWidget(mContext, liveAudienceInterface,privateLetterList);
        final PrivateLetterFriendsWidget view2 = new PrivateLetterFriendsWidget(mContext, liveAudienceInterface);

        viewContainter2.add(view1);
        viewContainter2.add(view2);

        PrivateLetterViewPagerAdapter privateLetterViewPagerAdapter = new PrivateLetterViewPagerAdapter(viewContainter2, privateLetterTitleList);
        privateLetterTabViewPager.setAdapter(privateLetterViewPagerAdapter);
        privateLetterTabViewPager.setOffscreenPageLimit(2);
        privateLetterSlidingTabLayout.setViewPager(privateLetterTabViewPager);

        replyPrivateLetterLayout = (LinearLayout) findViewById(R.id.reply_private_letter_ll);

        replyBackBtn = (View) findViewById(R.id.reply_private_letter_back_btn);
        replyCloseBtn = (ImageView) findViewById(R.id.reply_private_letter_close_btn);
        replyUserName = (TextView) findViewById(R.id.reply_private_letter_name_text);
        messageP2pListView = (MessageListView) findViewById(R.id.messageP2pListView);
        messageListView = (ListView) findViewById(R.id.messageListView);

        privateLetterTabViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            boolean isRefresh;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(!isRefresh)
                if(position == 1){
                    view2.update();
                    isRefresh = true;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void initGiftLayout() {
        giftLayout = findViewById(R.id.live_audience_gift_rl);
        giftLayout.setVisibility(View.GONE);
        giftTabViewPager = (CustomViewPager) findViewById(R.id.gift_tab_view_pager);

        giftSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.gift_sliding_tab_layout);

        giftAnimationViewDown = (RelativeLayout) findViewById(R.id.gift_animation_view_down);
        giftAnimationViewUp = (RelativeLayout) findViewById(R.id.gift_animation_view_up);
        giftAnimation = new GiftAnimation(giftAnimationViewDown, giftAnimationViewUp);

        viewContainter = new ArrayList<>();
        titleList = new ArrayList<>();
        for (String title : giftTitles) {
            titleList.add(title);
        }
        mInflater = LayoutInflater.from(mContext);

        LiveGiftWidget view1 = new LiveGiftWidget(mContext, "1", liveAudienceInterface, videoRoom);
        LiveGiftWidget view2 = new LiveGiftWidget(mContext, "2", liveAudienceInterface, videoRoom);

        viewContainter.add(view1);
        viewContainter.add(view2);
        GiftViewPagerAdapter giftViewPagerAdapter = new GiftViewPagerAdapter(viewContainter, titleList);
        giftTabViewPager.setAdapter(giftViewPagerAdapter);
        giftTabViewPager.setOffscreenPageLimit(2);
        giftSlidingTabLayout.setViewPager(giftTabViewPager);

        initAnimateImage();
    }

    private void initAnimateImage() {
        ll_plane = findViewById(R.id.ll_plane);
        iv_plane_screw = (ImageView) findViewById(R.id.iv_plane_screw);
        iv_plane_head = (ImageView) findViewById(R.id.iv_plane_head);
        ll_live_car = findViewById(R.id.ll_live_car);
        iv_love_chocolate_animate = (ImageView) findViewById(R.id.iv_love_chocolate_animate);
        iv_cake = (ImageView) findViewById(R.id.iv_cake);
        iv_black_chocolate = (ImageView) findViewById(R.id.iv_black_chocolate);
        iv_bouquet_animate = (ImageView) findViewById(R.id.iv_bouquet_animate);
        iv_white_chocolate = (ImageView) findViewById(R.id.iv_white_chocolate);
        iv_car_behind = (ImageView) findViewById(R.id.iv_car_behind);
        iv_car_before = (ImageView) findViewById(R.id.iv_car_before);
        iv_car_lambo = (ImageView) findViewById(R.id.iv_car_lambo);
        iv_plane_head.post(new Runnable() {
            @Override
            public void run() {
                planeWidth = ll_plane.getWidth();
                planeHeight = ll_plane.getHeight();
                carWidth = ll_live_car.getWidth();
                carHeight = ll_live_car.getHeight();
                TranslateAnimation planeAnimation = new TranslateAnimation(0, planeWidth, 0, 0);
                planeAnimation.setDuration(0);
                planeAnimation.setFillAfter(true);
                ll_plane.startAnimation(planeAnimation);
                TranslateAnimation carAnimation = new TranslateAnimation(0, -carWidth, 0, 0);
                carAnimation.setDuration(0);
                carAnimation.setFillAfter(true);
                ll_live_car.startAnimation(carAnimation);
            }
        });
    }

    void playCake(){
        iv_cake.setVisibility(View.VISIBLE);
        iv_cake.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_cake.setVisibility(View.GONE);
                if(animations.size() > 0) {
                    animations.remove(0);
                    if(animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4000);
    }

    void playWhiteChocolate(){
        iv_white_chocolate.setVisibility(View.VISIBLE);
        iv_white_chocolate.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_white_chocolate.setVisibility(View.GONE);
                if(animations.size() > 0) {
                    animations.remove(0);
                    if(animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4000);
    }

    void playBouquet(){
        iv_bouquet_animate.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_bouquet_animate.getDrawable();
            animationDrawable.setOneShot(true);
            animationDrawable.start();
        }
        iv_bouquet_animate.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_bouquet_animate.setVisibility(View.GONE);
                if(animations.size() > 0) {
                    animations.remove(0);
                    if(animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4500);
    }

    void playBlackChocolate(){
        iv_black_chocolate.setVisibility(View.VISIBLE);
        iv_black_chocolate.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_black_chocolate.setVisibility(View.GONE);
                if(animations.size() > 0) {
                    animations.remove(0);
                    if(animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4000);
    }

    void playChocolate(){
        iv_love_chocolate_animate.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_love_chocolate_animate.getDrawable();
            animationDrawable.setOneShot(true);
            animationDrawable.start();
        }
        iv_love_chocolate_animate.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_love_chocolate_animate.setVisibility(View.GONE);
                if(animations.size() > 0) {
                    animations.remove(0);
                    if(animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4500);
    }

    void playCar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_car_before.getBackground();
            animationDrawable.start();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_car_behind.getBackground();
            animationDrawable.start();
        }
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation originalAnimation = new TranslateAnimation(-carWidth / 2, carWidth / 4, 0, (float)(carHeight * 1.5));
        originalAnimation.setDuration(1500);
        originalAnimation.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1, 0.5f, 1);
        scaleAnimation.setDuration(1500);
        animationSet.addAnimation(originalAnimation);
        animationSet.addAnimation(scaleAnimation);
        ll_live_car.startAnimation(animationSet);
        originalAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation endAnimation = new TranslateAnimation(carWidth / 4, carWidth * 2, (float)(carHeight * 1.5), (float)(carHeight * 1.5));
                endAnimation.setDuration(1500);
                endAnimation.setFillAfter(true);
                ll_live_car.startAnimation(endAnimation);
                endAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(animations.size() > 0) {
                            animations.remove(0);
                            if (animations.size() > 0) {
                                play();
                            } else {
                                AnimationDrawable animationDrawable = (AnimationDrawable) iv_car_before.getBackground();
                                animationDrawable.stop();
                                animationDrawable = (AnimationDrawable) iv_car_behind.getBackground();
                                animationDrawable.stop();
                            }
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void playPlane(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_plane_screw.getDrawable();
            animationDrawable.start();
        }
        TranslateAnimation originalAnimation = new TranslateAnimation(planeWidth / 2, planeWidth / 8, 0, planeHeight);
        originalAnimation.setDuration(1500);
        originalAnimation.setFillAfter(true);
        ll_plane.startAnimation(originalAnimation);
        originalAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TranslateAnimation changeAnimation = new TranslateAnimation(planeWidth / 8, -planeWidth / 8, planeHeight, planeHeight + planeHeight / 4);
                changeAnimation.setDuration(1500);
                changeAnimation.setFillAfter(true);
                ll_plane.startAnimation(changeAnimation);
                changeAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        TranslateAnimation endAnimation = new TranslateAnimation(-planeWidth / 8, -planeWidth, planeHeight + planeHeight / 4, planeHeight + planeHeight / 4);
                        endAnimation.setDuration(2000);
                        endAnimation.setFillAfter(true);
                        ll_plane.startAnimation(endAnimation);
                        endAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if(animations.size() > 0) {
                                    animations.remove(0);
                                    if (animations.size() > 0) {
                                        play();
                                    } else {
                                        AnimationDrawable animationDrawable = (AnimationDrawable) iv_plane_screw.getDrawable();
                                        animationDrawable.stop();
                                    }
                                }
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    void play(){
        if(animations.get(0) == 15)
            playPlane();
        else if(animations.get(0) == 18 || animations.get(0) == 19 || animations.get(0) == 20)
            playCar();
        else if(animations.get(0) == 7)
            playWhiteChocolate();
        else if(animations.get(0) == 11)
            playCake();
        else if(animations.get(0) == 12)
            playBouquet();
        else if(animations.get(0) == 8)
            playBlackChocolate();
        else if(animations.get(0) == 9)
            playChocolate();
    }

    /**
     * 设置消息组件
     *
     * @param activity
     * @param roomId
     */
    public void setMessageModules(Activity activity, String roomId) {
        this.currRoomId = roomId;
        Container container = new Container(activity, roomId, SessionTypeEnum.ChatRoom, this);
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, messageAndLikeLl);
        }

        if (inputPanel == null) {
            inputPanel = new ChatRoomInputPanel(container, messageInput, getActionList(), false, roomId);
        } else {
            inputPanel.reload(container, null);
        }
        messageInput.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldCollapseInputPanel();
            }
        });

    }

    /**
     * 设置消息组件
     *
     */
//    public void setPrivateLetterModules(Activity activity) {
//        this.currAccount = CacheCenter.getNimAccount();
//        Container container2 = new Container(activity, currAccount, SessionTypeEnum.P2P, this);
//        if (privateLetterMsgListPanel == null) {
//            privateLetterMsgListPanel = new MessageListPanel(container2, rootView);
//        }
//
//        if (inputPanel2 == null) {
//            inputPanel2 = new InputPanel(container2, rootView, getActionList(), true);
//        } else {
//            inputPanel2.reload(container2, null);
//        }
//        rootView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shouldCollapseInputPanel();
//            }
//        });
//    }


    public void updateUI(ChatRoomInfo roomInfo, VideoRoom videoRoom) {
//        if (roomInfo == null) {
//            Logger.e("updateUI(ChatRoomInfo roomInfo).. roomInfo is null !");
//            return;
//        }
//
//        if (videoRoom != null) {
//            bitmap = ImageLoaderKit.getBitmapFromCache(videoRoom.avatarUrl, liveAudienceRl.getWidth(), liveAudienceRl.getHeight());
//            liveAudienceRl.setBackground(new BitmapDrawable(bitmap));
//            ImageLoaderKit.getInstance().displayImage(videoRoom.avatarUrl, masterHead);
//            ShangXiuUtil.setUserTypeIcon(userTypeIcon, videoRoom.userType);
//        }
//        masterNameText.setText(roomInfo.getName());
//        liveFinishMasterName.setText(roomInfo.getCreator());
//        onlineCountText.setText(String.format("%s人", String.valueOf(roomInfo.getOnlineUserCount())));

        //设置头像
        if (videoRoom != null) {
            currentUser.avatarUrl = videoRoom.avatarUrl;
            currentUser.userId = videoRoom.userId;
            currentUser.userName = videoRoom.userName;
            ImageLoaderKit.getInstance().displayImage(videoRoom.avatarUrl, masterHead);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, videoRoom.userType);
            masterNameText.setText(videoRoom.userName);
            ImageLoaderKit.getInstance().displayImage(videoRoom.avatarUrl, stop_live_user_icon);
            ShangXiuUtil.setUserTypeIcon(stop_live_user_type_icon, videoRoom.userType);
            tv_stop_live_name.setText(videoRoom.userName);
            checkIsFriend(videoRoom.userId);
        }
        if (roomInfo == null) {
            Logger.e("updateUI(ChatRoomInfo roomInfo).. roomInfo is null !");
            return;
        }
//        bitmap = ImageLoaderKit.getBitmapFromCache(videoRoom.avatarUrl, liveAnchorRl.getWidth(), liveAnchorRl.getHeight());
//        liveAnchorRl.setBackground(new BitmapDrawable(bitmap));
//        liveFinishMasterName.setText(roomInfo.getCreator());
        onlineCountText.setText(String.format("%s人", String.valueOf(roomInfo.getOnlineUserCount() - 1)));
    }

    public void updateUINickName(String nickName) {
//        masterNameText.setText(nickName);
//        liveFinishMasterName.setText(nickName);
    }

    public void updateUIGuest(List<ChatRoomMember> chatRoomMemberList) {
        Set<Long> userIdList = new HashSet<Long>();
        for (ChatRoomMember crm : chatRoomMemberList) {
            userIdList.add(Long.valueOf(crm.getAccount()));
        }
//        UserIds userIds = new UserIds();
//        userIds.setUserIdList(userIdList);
        List<Long> userIds=new ArrayList<>();
        userIds.addAll(userIdList);
        getGuestList(userIds);
        getAccountBalance();
    }


    /**
     * 显示礼物布局
     */
    public void showGiftLayout(boolean isShow) {
        if (isShow) {
            inputPanel.collapse(true);// 收起软键盘
            controlLayout.setVisibility(View.INVISIBLE);
            giftLayout.setVisibility(View.VISIBLE);
        } else {
            giftLayout.setVisibility(View.GONE);
            controlLayout.setVisibility(View.VISIBLE);
            giftPosition = -1;
        }
    }

    /***********************
     * 发送礼物
     ******************************/
    // 发送礼物
    public void sendGift(GiftInfo giftInfo, int count) {
//        if (giftPosition == -1) {
//            Toast.makeText(getContext(), "请选择礼物", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        giftLayout.setVisibility(View.GONE);
        sendUser = CacheCenter.getInstance().getCurrUser();
        GiftInfoAttachment attachment = new GiftInfoAttachment(giftInfo, count, sendUser);
        ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(currRoomId, attachment);
        setMemberType(message);
        NIMClient.getService(ChatRoomService.class).sendMessage(message, false);
        giftAnimation.showGiftAnimation(message, true);
        liveDiamandAmountText.setText((Long.valueOf(liveDiamandAmountText.getText().toString()) - giftInfo.getAmount()) + "");
        giftChargeDiamandNumber.setText(liveDiamandAmountText.getText().toString() + "");
//        giftPosition = -1; // 发送完毕，置空
        long productId = giftInfo.getProductId();
        animateImage(productId);
    }

    public void showGiftAnimation(ChatRoomMessage message, boolean isSameUser) {
        long productId = ((GiftInfoAttachment)message.getAttachment()).getGiftInfo().getProductId();
        animateImage(productId);
        giftAnimation.showGiftAnimation(message, isSameUser);
    }

    private void animateImage(long productId) {
        switch ((int)productId){
            case 15:
            case 18:
            case 19:
            case 20:
            case 7:
            case 11:
            case 12:
            case 8:
            case 9:{
                if(animations.size() == 0){
                    animations.add(productId);
                    if(productId == 15){
                        if(animations.size() <= 1){
                            playPlane();
                        }
                    }
                    if(productId == 18 || productId == 19 || productId == 20){
                        if(animations.size() <= 1){
                            playCar();
                        }
                    }
                    if(productId == 7){
                        if(animations.size() <= 1){
                            playWhiteChocolate();
                        }
                    }
                    if(productId == 11){
                        if(animations.size() <= 1){
                            playCake();
                        }
                    }
                    if(productId == 12){
                        if(animations.size() <= 1){
                            playBouquet();
                        }
                    }
                    if(productId == 8){
                        if(animations.size() <= 1){
                            playBlackChocolate();
                        }
                    }
                    if(productId == 9){
                        if(animations.size() <= 1){
                            playChocolate();
                        }
                    }
                }else{
                    if(animations.get(animations.size() - 1) != productId){
                        animations.add(productId);
                        if(productId == 15){
                            if(animations.size() <= 1){
                                playPlane();
                            }
                        }
                        if(productId == 18 || productId == 19 || productId == 20){
                            if(animations.size() <= 1){
                                playCar();
                            }
                        }
                        if(productId == 7){
                            if(animations.size() <= 1){
                                playWhiteChocolate();
                            }
                        }
                        if(productId == 11){
                            if(animations.size() <= 1){
                                playCake();
                            }
                        }
                        if(productId == 12){
                            if(animations.size() <= 1){
                                playBouquet();
                            }
                        }
                        if(productId == 8){
                            if(animations.size() <= 1){
                                playBlackChocolate();
                            }
                        }
                        if(productId == 9){
                            if(animations.size() <= 1){
                                playChocolate();
                            }
                        }
                    }else{
                        if(productId == 15){
                            if(animations.size() < 1){
                                playPlane();
                            }
                        }
                        if(productId == 18 || productId == 19 || productId == 20){
                            if(animations.size() < 1){
                                playCar();
                            }
                        }
                        if(productId == 7){
                            if(animations.size() < 1){
                                playWhiteChocolate();
                            }
                        }
                        if(productId == 11){
                            if(animations.size() < 1){
                                playCake();
                            }
                        }
                        if(productId == 12){
                            if(animations.size() < 1){
                                playBouquet();
                            }
                        }
                        if(productId == 8){
                            if(animations.size() < 1){
                                playBlackChocolate();
                            }
                        }
                        if(productId == 9){
                            if(animations.size() < 1){
                                playChocolate();
                            }
                        }
                    }
                }
            }
            break;
        }
    }

    private void setMemberType(ChatRoomMessage message) {
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(currRoomId, CacheCenter.getInstance().getNimAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            message.setRemoteExtension(ext);
        }
    }

    /*************************
     * 点赞爱心
     ********************************/

    /**
     * 点赞动画
     */
    public void addHeart() {
        if (periscopeLayout == null) {
            Logger.e("periscopeLayout is null !");
            return;
        }
        periscopeLayout.addHeart();
    }

    /**
     * 发送点赞爱心
     */
    public void sendLike() {
        addHeart();
        // 发送爱心频率控制
        if (!CommonUtil.isClickSoFast(1000)) {
            LikeAttachment attachment = new LikeAttachment();
            attachment.setSendUser(sendUser);
            ChatRoomMessage message = ChatRoomMessageBuilder.createChatRoomCustomMessage(currRoomId, attachment);
            setMemberType(message);
            NIMClient.getService(ChatRoomService.class).sendMessage(message, false);
        }
    }

    /**
     * 修改播放状态
     *
     * @param state
     */
    public void changeVideoState(int state) {
        switch (state) {
            case MConstants.STATE_VIDEO_INITIAL:
//                preparedText.setVisibility(View.GONE);
//                liveFinishLayout.setVisibility(View.GONE);
                break;
            case MConstants.STATE_VIDEO_START:
                rl_stop_live_parent.setVisibility(GONE);
//                preparedText.setVisibility(View.GONE);
//                liveFinishLayout.setVisibility(View.GONE);
                break;
            case MConstants.STATE_VIDEO_LIVING:
                rl_stop_live_parent.setVisibility(View.GONE);
                preparedText.setVisibility(View.GONE);
                liveFinishLayout.setVisibility(View.GONE);
                break;
            case MConstants.STATE_VIDEO_COMPLETE:
                showFinishLayout(false);
                break;
            case MConstants.STATE_VIDEO_ERROR:
                preparedText.setVisibility(View.VISIBLE);
                showFinishLayout(true);
                break;
            case MConstants.STATE_VIDEO_STOP:{
                rl_stop_live_parent.setVisibility(View.VISIBLE);
                liveFinishLayout.setVisibility(View.GONE);
            }
            break;
        }
    }

    // 显示直播已结束布局
    private void showFinishLayout(boolean isError) {
        liveFinishLayout.setVisibility(View.VISIBLE);

//        if (isError) {
//            finishTipText.setText("进入聊天室失败");
//        } else {
//            finishTipText.setText(R.string.live_finish);
//        }

        inputPanel.collapse(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.attention_btn:
                addFriends(mContext, videoRoom.userId);
                break;

            case R.id.close_live_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickBackClose();
                }
                break;
            case R.id.shop_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickShop();
                }
                break;

//            case R.id.switch_btn:
//                if (liveAudienceInterface != null) {
//                    liveAudienceInterface.onClickSwitchCamera();
//                }
//                break;
            case R.id.gift_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickGift();
                }
                break;
            case R.id.message_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickMessage();
                }
                break;
            case R.id.private_letter_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickPrivateLetter();
                }
                break;
//            case R.id.private_letter_back_btn:
//                privateLetterLayout.setVisibility(View.GONE);
//                controlLayout.setVisibility(View.VISIBLE);
//                break;
//            case R.id.private_letter_close_btn:
//                privateLetterLayout.setVisibility(View.GONE);
//                controlLayout.setVisibility(View.VISIBLE);
//                break;
            case R.id.reply_private_letter_back_btn:
                replyPrivateLetterLayout.setVisibility(View.GONE);
                sessionType = SessionTypeEnum.ChatRoom;
                setMessageModules((LiveAudienceActivity) mContext, currRoomId);
                privateLetterLayout.setVisibility(VISIBLE);
                break;
            case R.id.reply_private_letter_close_btn:
                replyPrivateLetterLayout.setVisibility(View.GONE);
                sessionType = SessionTypeEnum.ChatRoom;
                setMessageModules((LiveAudienceActivity) mContext, currRoomId);
                controlLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.share_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickShare();
                }
                break;
            case R.id.live_good_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickGood();
                }
                break;
            case R.id.finish_live_btn:
                if (liveAudienceInterface != null) {
                    liveAudienceInterface.onClickFinish();
                }
                break;
            case R.id.live_audience_widget_layout:
                messageInput.setVisibility(GONE);
                replyPrivateLetterLayout.setVisibility(GONE);
                privateLetterLayout.setVisibility(GONE);
                giftLayout.setVisibility(GONE);
                controlLayout.setVisibility(VISIBLE);
                sessionType = SessionTypeEnum.ChatRoom;
                setMessageModules((LiveAudienceActivity) mContext, currRoomId);
                break;
            case R.id.bb_stop_live_close:{
                liveAudienceInterface.onClickFinish();
            }
            break;
            case R.id.buttonP2pSendMessage:{
                UserInfo userInfo = (UserInfo) buttonP2pSendMessage.getTag();
                if(userInfo != null) {
                    liveAudienceInterface.onClickSendPrivateLetter(userInfo);
                }
            }
            break;
            /*case R.id.finish_close_btn:{
                liveAudienceInterface.onClickFinish();
            }
            break;*/
            case R.id.rl_live_audience_addmoney:{
                liveAudienceInterface.onClickAddMoney();
            }
            break;
        }
    }

    private void addFriends(final Context context, final long userId) {
        UserModel userModel = new UserModel(mContext);
        userModel.addFriend(userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                attentionText.setVisibility(GONE);
                ToastUtils.show("关注成功");
//                ShangXiuUtil.doAddFriend(context, videoRoom.userName + "", "请求添加好友", false);
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    private void checkIsFriend(final long userId){
        UserModel userModel = new UserModel(mContext);
        userModel.checkIsFriend(userId + "", new Callback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    attentionText.setVisibility(GONE);
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        });
    }

    public void onPause() {
        if (inputPanel != null) {
            inputPanel.onPause();
        }
        if (inputPanel2 != null) {
            inputPanel2.onPause();
        }
        if (messageListPanel != null) {
            messageListPanel.onPause();
        }
        if (privateLetterMsgListPanel != null) {
            privateLetterMsgListPanel.onPause();
        }
        mDanmakuView.hide();
        live_danmu_rl.setVisibility(GONE);
    }

    public void onResume() {
        if (messageListPanel != null) {
            messageListPanel.onResume();
        }
        if (privateLetterMsgListPanel != null) {
            privateLetterMsgListPanel.onResume();
        }
        mDanmakuView.show();
        live_danmu_rl.setVisibility(VISIBLE);
    }

    public void onDestroy() {
        giftRecycleradapter = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        mDanmakuView.clear();
        live_danmu_rl.setVisibility(GONE);
    }


    /**************************
     * Module proxy回话窗口
     ***************************/
    public boolean SendChatRoomMessage(final ChatRoomMessage msg) {
        Map<String, Object> ext = new HashMap<>();
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(currRoomId, CacheCenter.getNimAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            msg.setRemoteExtension(ext);
        }

        NIMClient.getService(ChatRoomService.class).sendMessage(msg, false)
                .setCallback(new RequestCallback<Void>() {
                    @Override
                    public void onSuccess(Void param) {
                        messageListPanel.onMsgSend(msg);
                    }

                    @Override
                    public void onFailed(int code) {
                        if (code == ResponseCode.RES_CHATROOM_MUTED) {
                            Toast.makeText(CacheCenter.getInstance().getContext(), "用户被禁言", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CacheCenter.getInstance().getContext(), "消息发送失败：code:" + code, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onException(Throwable exception) {
                        Toast.makeText(CacheCenter.getInstance().getInstance().getContext(), "消息发送失败！", Toast.LENGTH_SHORT).show();
                    }
                });

        return true;
    }

    @Override
    public boolean sendMessage(final IMMessage msg) {
        NIMClient.getService(ChatRoomService.class).sendMessage((ChatRoomMessage) msg, false);
        messageListPanel.addMessage(msg);
//        if (msg != null && msg instanceof ChatRoomMessage) {
//            ChatRoomMsgAttachment attachment = new ChatRoomMsgAttachment();
//            attachment.setSendUser(sendUser);
//            msg.setAttachment(attachment);
//            SendChatRoomMessage((ChatRoomMessage) msg);
//        } else {
//            NIMClient.getService(MsgService.class).sendMessage(msg, false)
//                    .setCallback(new RequestCallback<Void>() {
//                        @Override
//                        public void onSuccess(Void param) {
//                            privateLetterMsgListPanel.onMsgSend(msg);
//                        }
//
//                        @Override
//                        public void onFailed(int code) {
//                            if (code == ResponseCode.RES_CHATROOM_MUTED) {
//                                Toast.makeText(CacheCenter.getInstance().getContext(), "用户被禁言", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(CacheCenter.getInstance().getContext(), "消息发送失败：code:" + code, Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onException(Throwable exception) {
//                            Toast.makeText(CacheCenter.getInstance().getInstance().getContext(), "消息发送失败！", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//        }

        return true;
    }

    @Override
    public void onInputPanelExpand() {
        controlLayout.setVisibility(View.GONE);
    }

    @Override
    public void shouldCollapseInputPanel() {
        inputPanel.collapse(false);
        controlLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }


    @Override
    public void showDanmu(IMMessage msg) {
        DanmuAttachment danmuAttachment=(DanmuAttachment) msg.getAttachment();
        if (TextUtils.isEmpty(danmuAttachment.getContent())) {
            Toast.makeText(mContext, "弹幕为空", Toast.LENGTH_SHORT).show();
        } else {
//            Bitmap bitmap=ImageLoaderKit.getBitmapFromCache(danmuAttachment.getSendUser().avatarUrl,100,100);
//            ImageSpan imageSpan = new ImageSpan(mContext, bitmap);
            danmukuList.clear();
            SpannableString spannableString = new SpannableString(danmuAttachment.getContent());
//            spannableString.setSpan(imageSpan, -1, 0, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            final DanmakuItem item = new DanmakuItem(mContext, spannableString, mDanmakuView.getWidth(), 0, 0, 0, 1.5f);
            danmukuList.add(item);
            Collections.shuffle(danmukuList);
            mDanmakuView.addItem(danmukuList, true);
            mDanmakuView.show();
            live_danmu_rl.setVisibility(VISIBLE);
        }

    }

    // 操作面板集合
    protected List<BaseAction> getActionList() {
        List<BaseAction> actions = new ArrayList<>();
        return actions;
    }

    public void showGoodLayout() {

    }

    boolean isRefresh;
    public void showPrivateLetterLayout() {
        controlLayout.setVisibility(View.GONE);
        privateLetterLayout.setVisibility(View.VISIBLE);
        reply_private_letter_ll.setVisibility(VISIBLE);
        if(!isRefresh){
            view1.update();
            isRefresh = true;
        }
    }

    /**
     * 接收消息
     *
     * @param messages
     */
    public void onIncomingMessage(List<ChatRoomMessage> messages) {
        if (messageListPanel == null) {
            Logger.e("messageListPanel is null !");
            return;
        }
        ChatRoomMessage message = messages.get(0);
        if (message != null && message.getAttachment() instanceof GiftInfoAttachment) {
            // 收到礼物消息
//                GiftInfoAttachment giftInfoAttachment = ((GiftInfoAttachment) message.getAttachment());
            ((LiveAudienceActivity) mContext).receivGift(message);
        } else if (message != null && message.getAttachment() instanceof LikeAttachment) {
            // 收到点赞爱心
            ((LiveAudienceActivity) mContext).receivLove();
        } else if (message != null && message.getAttachment() instanceof DanmuAttachment) {
            // 收到弹幕
            ((LiveAudienceActivity) mContext).receivDanmu(message);
        } else if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
            // 通知类消息
//            messageListPanel.onIncomingMessage(messages);
        } else {
            messageListPanel.onIncomingMessage(messages);
        }

    }

    public void showMessageLayout() {
        controlLayout.setVisibility(GONE);
        messageInput.setVisibility(VISIBLE);
    }

    public VideoRoom getVideoRoom() {
        return videoRoom;
    }

    public void setVideoRoom(VideoRoom videoRoom) {
        this.videoRoom = videoRoom;
    }

    private void getAccountBalance() {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getAccountBalance().subscribe(new NewSubscriber<Long>(mContext, false) {
            @Override
            public void onNext(Long amount) {
                liveDiamandAmountText.setText(amount + "");
                giftChargeDiamandNumber.setText(amount + "");
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
            }
        });
        ((LiveAudienceActivity) mContext).addSubscrebe(subscription);
    }

    private void getGuestList(List<Long> userIdList) {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getUserInfoByUserIds(userIdList).subscribe(new NewSubscriber<List<UserInfo>>(mContext, false) {
            @Override
            public void onNext(List<UserInfo> userInfoList) {
                guestList = userInfoList;
                userHeaderRecyclerAdapter.replaceAll(guestList);
                userHeaderRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
            }
        });
        ((LiveAudienceActivity) mContext).addSubscrebe(subscription);
    }

    public void showReplyPrivateLetterLayout(final UserInfo userInfo) {
        privateLetterLayout.setVisibility(View.GONE);
        controlLayout.setVisibility(View.GONE);
        replyPrivateLetterLayout.setVisibility(VISIBLE);
        replyUserName.setText(userInfo.userName);
        sessionType = SessionTypeEnum.P2P;
        buttonP2pSendMessage.setTag(userInfo);
        if(p2PChatAdapter != null){
            privateLetters.clear();
            p2PChatAdapter.clear();
        }
        IMMessage imMessage = MessageBuilder.createEmptyMessage(userInfo.userId + "", SessionTypeEnum.P2P, 0);
        MessageUtils.pullMessageHistory(imMessage, new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
                if (imMessages != null) {
                    Collections.reverse(imMessages);
                    p2PChatAdapter = new P2PChatAdapter(getContext(), privateLetters, CacheCenter.getInstance().getCurrUser(), userInfo);
                    messageP2pListView.setAdapter(p2PChatAdapter);
                    for (IMMessage imMessage : imMessages) {
                        PrivateLetter privateLetter = new PrivateLetter();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = format.format(new Date(imMessage.getTime()));
                        privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
                        privateLetter.setContactId(imMessage.getSessionId());
                        privateLetter.setAccount(imMessage.getFromAccount());
                        privateLetter.setMessage(imMessage.getContent());
                        privateLetters.add(privateLetter);
                    }
                    p2PChatAdapter.addAll(privateLetters);
                    messageP2pListView.setSelection(messageP2pListView.getBottom());
                }
            }
        });
    }

    public void sendPrivateLetter(final UserInfo userInfo){
        String content = editP2pTextMessage.getText().toString();
        MessageUtils.sendMessgeP2p(userInfo.userId + "", content, editP2pTextMessage, messageP2pListView, p2PChatAdapter, userInfo, CacheCenter.getInstance().getCurrUser(), privateLetters);
        view1.update();
    }

    public void onIncomingIMMessage(List<IMMessage> messages) {
        if(messages != null) {
            for (IMMessage im : messages) {
                PrivateLetter pl = new PrivateLetter();
                pl.setImMessage(im);
                getUserInfo(Long.parseLong(im.getFromAccount()));
                pl.setUserInfo(imSender);
                if (!im.isRemoteRead()) {
                    privateLetterList.add(pl);
                }
            }
            if (privateLetterMsgListPanel == null) {
                Logger.e("privateLetterMsgListPanel is null !");
                return;
            }
            privateLetterMsgListPanel.onIncomingMessage(messages);
        }
    }

    public void onPrivateMessage(List<IMMessage> messages) {
        if(messages != null) {
            UserInfo userInfo = (UserInfo) buttonP2pSendMessage.getTag();
            if(userInfo != null) {
                for (IMMessage im : messages) {
                    String contactId = im.getSessionId();
                    if ((userInfo.userId + "").equals(contactId)) {
                        PrivateLetter privateLetter = new PrivateLetter();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = format.format(new Date(im.getTime()));
                        privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
                        privateLetter.setContactId(im.getSessionId());
                        privateLetter.setAccount(im.getFromAccount());
                        privateLetter.setMessage(im.getContent());
                        privateLetters.add(privateLetter);
                        p2PChatAdapter.add(privateLetter);
                    }
                }
                messageP2pListView.setSelection(messageP2pListView.getBottom());
            }
            view1.update();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(long userId) {
        UserModel userModel=new UserModel(mContext);
        userModel.getUserInfo(userId, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                imSender=userInfo;
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        }, false);
    }

    public void recentContactList(List<RecentContact> messages) {

    }

    public SessionTypeEnum getSessionType() {
        return sessionType;
    }

    public void setSessionType(SessionTypeEnum sessionType) {
        this.sessionType = sessionType;
    }
}
