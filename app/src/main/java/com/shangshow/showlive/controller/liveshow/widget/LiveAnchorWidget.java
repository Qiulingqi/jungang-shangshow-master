package com.shangshow.showlive.controller.liveshow.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nim.uikit.common.ui.listview.MessageListView;
import com.netease.nim.uikit.model.UserInfo;
import com.netease.nim.uikit.session.actions.BaseAction;
import com.netease.nim.uikit.session.extension.DanmuAttachment;
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
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomInfo;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomNotificationAttachment;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
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
import com.shangshow.showlive.common.utils.XmlDB;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.controller.adapter.MessageAdapter;
import com.shangshow.showlive.controller.adapter.P2PChatAdapter;
import com.shangshow.showlive.controller.liveshow.LiveAnchorActivity;
import com.shangshow.showlive.controller.liveshow.chatroom.helper.ChatRoomMemberCache;
import com.shangshow.showlive.controller.liveshow.chatroom.module.ChatRoomMsgListPanel;
import com.shangshow.showlive.controller.liveshow.goods.LiveAnchorGoodsActivity;
import com.shangshow.showlive.controller.liveshow.widget.opendanmaku.DanmakuItem;
import com.shangshow.showlive.controller.liveshow.widget.opendanmaku.DanmakuView;
import com.shangshow.showlive.controller.liveshow.widget.opendanmaku.IDanmakuItem;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.ApiWrapper;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shangshow.showlive.network.http.subscriber.NewSubscriber;
import com.shangshow.showlive.network.service.models.LiveInfo;
import com.shangshow.showlive.network.service.models.PrivateLetter;
import com.shangshow.showlive.network.service.models.VideoStatistic;
import com.shangshow.showlive.network.service.models.body.VideoOffLiveBody;
import com.shangshow.showlive.widget.SwipeListView;
import com.shaojun.utils.log.Logger;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import rx.Subscription;

/**
 * <直播-主播端widget管理类>
 * <功能详细描述>
 *
 * @auth:jeremy
 * @see: [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
//  主播开播的 直播的View   控件  所有的点击事件都在这里边进行
public class LiveAnchorWidget extends RelativeLayout implements ModuleProxy, View.OnClickListener {

    private UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
    /**
     * 顶部视图
     */
    public TextView masterNameText;//
    public HeadImageView masterHead;
    public ImageView userTypeIcon;
    public TextView onlineCountText; // 在线人数view
    /**
     * 控制按钮
     */
    public LinearLayout controlLayout; // 右下角几个image button布局
    /**
     * 礼物、点赞
     */
    public GridView giftView; // 礼物列表
    public ImageButton closeBtn;
    public ImageButton privateLetterBtn;
    public ImageButton switchCameraBtn;
    public ImageButton shareBtn;
    public ImageButton likeBtn;
    public View goodBtn; // 礼物按钮
    //    public ImageButton switchBtn;
    //    public ViewGroup giftLayout; // 礼物布局
    public TextView noGiftText;
    /**
     * 开始
     */
    public View startLayout;//开始直播遮罩层
    public TextView startText;
    public RelativeLayout giftAnimationViewDown; // 礼物动画布局1
    public RelativeLayout giftAnimationViewUp; // 礼物动画布局2
    public PeriscopeLayout periscopeLayout; // 点赞爱心布局
    // data
    protected GiftAdapter adapter;
    protected GiftAnimation giftAnimation; // 礼物动画
    protected InputPanel inputPanel, inputPanel2;
    protected ChatRoomMsgListPanel messageListPanel;
    protected MessageListPanel privateLetterMsgListPanel;
    List<IDanmakuItem> danmukuList = new ArrayList<>();
    /**
     * 输入框，消息组件
     */
//    private LinearLayout messageInput;
    private String currAccount = "";
    private RelativeLayout liveAnchorWidgetRl;
    private RecyclerView guestRecyclerView;
    /**
     * 布局
     */
    private LinearLayout liveHeaderLayout, liveGiftAnimationLayout, liveControlLayout, liveMessageAndLikeLayout;
    //    private RelativeLayout livePrivateLetterLayout;
    private TextView liveDiamandAmountText;
    /**
     * 结束
     */
    private FrameLayout liveFinishLayout;
    private TextView liveTimeText, liveIncomeDiamandText, audienceCountText, rewardMeAudienceCountText;
    private Button liveFinishBtn, saveLiveBtn;
    /**
     * 私信
     */
    //私信
    private PtrHTFrameLayout privateLetterPtrFramentlayout;
    private List<PrivateLetter> privateLetters = new ArrayList<PrivateLetter>();
    private LinearLayout replyPrivateLetterLayout;
    private View privateLetterLayout;
    private SwipeListView privateLetterRecyclerView;
    private View replyBackBtn;
    private ImageView replyCloseBtn;
    private MessageAdapter messageAdapter;
    //    private PrivateLetterAnchorRecyclerAdapter privateLetterAnchorRecyclerAdapter;
//    private CustomViewPager privateLetterTabViewPager;
//    private SlidingTabLayout privateLetterSlidingTabLayout;
//    private String[] privateLetterTitles = {"私信", "通讯录"};
//    private List<String> privateLetterTitleList = new ArrayList<>();
//    ArrayList<View> viewContainter2 = new ArrayList<View>();
    private TextView replyUserName;
    private MessageListView messageP2pListView;
    private List<PrivateLetter> replyPrivateLetters = new ArrayList<PrivateLetter>();
    private P2PChatAdapter p2PChatAdapter;
    private TextView buttonP2pSendMessage;
    private EditText editP2pTextMessage;
    private LiveWidgetOperate.LiveAnchorOperateInterface liveAnchorOperateInterface;
    private String roomId = "";
    private View rootView;
    private Bitmap bitmap;
    private LayoutInflater mInflater;
    private Context mContext;
    private LiveInfo liveInfo;
    private long startTime;
    private long endTime;
    private UserHeaderRecyclerAdapter userHeaderRecyclerAdapter;
    private List<UserInfo> guestList;
    private List<PrivateLetter> privateLetterList = new ArrayList<PrivateLetter>();
    private UserInfo imSender;
    private SessionTypeEnum sessionType = SessionTypeEnum.P2P;
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
    private ImageView end_zhibo_background;

    public LiveAnchorWidget(Context context) {
        super(context);
        mContext = context;
        initView();
    }


    public LiveAnchorWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public LiveAnchorWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public void setLiveAnchorOperateInterface(LiveWidgetOperate.LiveAnchorOperateInterface liveAnchorOperateInterface) {
        this.liveAnchorOperateInterface = liveAnchorOperateInterface;
    }

    private void initView() {
        rootView = LayoutInflater.from(getContext()).inflate(R.layout.layout_live_anchor_widget, this);
        liveAnchorWidgetRl = (RelativeLayout) findViewById(R.id.live_anchor_widget_rl);
        //布局
        liveHeaderLayout = (LinearLayout) findViewById(R.id.live_anchor_header_ll);
        liveGiftAnimationLayout = (LinearLayout) findViewById(R.id.live_anchor_gift_animation_ll);
        liveMessageAndLikeLayout = (LinearLayout) findViewById(R.id.live_anchor_message_and_like_ll);
        liveControlLayout = (LinearLayout) findViewById(R.id.live_anchor_control_ll);
//        livePrivateLetterLayout = (LinearLayout) findViewById(R.id.reply_private_letter_ll);

        guestRecyclerView = (RecyclerView) findViewById(R.id.in_room_audience_recycler);

        //设置布局管理器
        guestRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayout.HORIZONTAL, false));
        userHeaderRecyclerAdapter = new UserHeaderRecyclerAdapter(mContext, guestList, R.layout.item_recycler_user_header);
        guestRecyclerView.setAdapter(userHeaderRecyclerAdapter);

        //顶部视图
//        liveAnchorRl = (RelativeLayout) findViewById(R.id.live_anchor_widget_layout);
        masterHead = (HeadImageView) findViewById(R.id.user_icon);
        userTypeIcon = (ImageView) findViewById(R.id.user_type_icon);
        masterNameText = (TextView) findViewById(R.id.master_name);
        onlineCountText = (TextView) findViewById(R.id.online_count_text);
        liveDiamandAmountText = (TextView) findViewById(R.id.live_diamand_amount_text);
        getAccountBalance();
        //控制按钮
        controlLayout = (LinearLayout) findViewById(R.id.control_layout);
        switchCameraBtn = (ImageButton) findViewById(R.id.camera_switch_btn);
        closeBtn = (ImageButton) findViewById(R.id.close_live_btn);
        privateLetterBtn = (ImageButton) findViewById(R.id.private_letter_btn);
        shareBtn = (ImageButton) findViewById(R.id.share_btn);
        goodBtn = findViewById(R.id.live_good_btn);

        //私信
        initPrivateLetterLayout();

        // 点赞的爱心布局
        periscopeLayout = (PeriscopeLayout) findViewById(R.id.periscope);
        giftAnimationViewDown = (RelativeLayout) findViewById(R.id.gift_animation_view_down);
        giftAnimationViewUp = (RelativeLayout) findViewById(R.id.gift_animation_view_up);
//        giftAnimation = new GiftInfoAnimation(giftAnimationView);
        giftAnimation = new GiftAnimation(giftAnimationViewDown, giftAnimationViewUp);

//        giftAnimationViewDown = (RelativeLayout) findViewById(R.id.gift_animation_view);
//        giftAnimationViewUp = (RelativeLayout) findViewById(R.id.gift_animation_view_up);
//        giftAnimation = new GiftAnimation(giftAnimationViewDown, giftAnimationViewUp);
//        adapter = new GiftAdapter(new ArrayList<Gift>(), getContext());
//        giftView.setAdapter(adapter);


        //开始视图
        startLayout = findViewById(R.id.start_layout);
        startText = (TextView) findViewById(R.id.start_live_text);

        // 直播结束
        liveFinishLayout = (FrameLayout) findViewById(R.id.live_finish_layout);
        end_zhibo_background = (ImageView) findViewById(R.id.end_zhibo_background);
        liveTimeText = (TextView) findViewById(R.id.live_time_tv);
        liveIncomeDiamandText = (TextView) findViewById(R.id.live_income_diamand_tv);
        //  观看人数
        audienceCountText = (TextView) findViewById(R.id.live_audience_count_tv);
        rewardMeAudienceCountText = (TextView) findViewById(R.id.live_reward_me_audience_tv);
        liveFinishBtn = (Button) findViewById(R.id.finish_live_btn);
        saveLiveBtn = (Button) findViewById(R.id.save_live_btn);

        initDanmu();
        initGift();

        liveAnchorWidgetRl.setOnClickListener(this);
        closeBtn.setOnClickListener(this);
        switchCameraBtn.setOnClickListener(this);
        privateLetterBtn.setOnClickListener(this);
        shareBtn.setOnClickListener(this);
        buttonP2pSendMessage.setOnClickListener(this);
        messageAdapter.setOnItemListener(new MessageAdapter.OnItemListener() {
            @Override
            public void delete(int position) {

            }

            @Override
            public void onItemClick(int position) {
                liveAnchorOperateInterface.onClickReplyPrivateLetter(privateLetters.get(position).getUserInfo(), Long.parseLong(privateLetters.get(position).getContactId()));
            }
        });
        goodBtn.setOnClickListener(this);

//        giftLayout.setOnClickListener(this);
        saveLiveBtn.setOnClickListener(this);
        liveFinishBtn.setOnClickListener(this);
//        privateLetterBackBtn.setOnClickListener(this);
//        privateLetterCloseBtn.setOnClickListener(this);
        replyBackBtn.setOnClickListener(this);
        replyCloseBtn.setOnClickListener(this);
        changeLiveState(MConstants.STATE_LIVE_INITIAL);
    }

    private void initGift() {
        initAnimateImage();
    }

    private void initDanmu() {
        live_danmu_rl = findViewById(R.id.live_danmu_rl);
        mDanmakuView = (DanmakuView) findViewById(R.id.danmakuView);
//        Collections.shuffle(danmukuList);
//        mDanmakuView.addItem(danmukuList, true);
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

    void playCake() {
        iv_cake.setVisibility(View.VISIBLE);
        iv_cake.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_cake.setVisibility(View.GONE);
                if (animations.size() > 0) {
                    animations.remove(0);
                    if (animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4000);
    }

    void playWhiteChocolate() {
        iv_white_chocolate.setVisibility(View.VISIBLE);
        iv_white_chocolate.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_white_chocolate.setVisibility(View.GONE);
                if (animations.size() > 0) {
                    animations.remove(0);
                    if (animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4000);
    }

    void playBouquet() {
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
                if (animations.size() > 0) {
                    animations.remove(0);
                    if (animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4500);
    }

    void playBlackChocolate() {
        iv_black_chocolate.setVisibility(View.VISIBLE);
        iv_black_chocolate.postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_black_chocolate.setVisibility(View.GONE);
                if (animations.size() > 0) {
                    animations.remove(0);
                    if (animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4000);
    }

    void playChocolate() {
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
                if (animations.size() > 0) {
                    animations.remove(0);
                    if (animations.size() > 0) {
                        play();
                    }
                }
            }
        }, 4500);
    }

    void playCar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_car_before.getBackground();
            animationDrawable.start();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            AnimationDrawable animationDrawable = (AnimationDrawable) iv_car_behind.getBackground();
            animationDrawable.start();
        }
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation originalAnimation = new TranslateAnimation(-carWidth / 2, carWidth / 4, 0, (float) (carHeight * 1.5));
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
                TranslateAnimation endAnimation = new TranslateAnimation(carWidth / 4, carWidth * 2, (float) (carHeight * 1.5), (float) (carHeight * 1.5));
                endAnimation.setDuration(1500);
                endAnimation.setFillAfter(true);
                ll_live_car.startAnimation(endAnimation);
                endAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (animations.size() > 0) {
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

    private void playPlane() {
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
                                if (animations.size() > 0) {
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

    void play() {
        if (animations.get(0) == 15)
            playPlane();
        else if (animations.get(0) == 18 || animations.get(0) == 19 || animations.get(0) == 20)
            playCar();
        else if (animations.get(0) == 7)
            playWhiteChocolate();
        else if (animations.get(0) == 11)
            playCake();
        else if (animations.get(0) == 12)
            playBouquet();
        else if (animations.get(0) == 8)
            playBlackChocolate();
        else if (animations.get(0) == 9)
            playChocolate();
    }

   /* private List<IDanmakuItem> initItems() {
        List<IDanmakuItem> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            IDanmakuItem item = new DanmakuItem(mContext, i + " : plain text danmuku", mDanmakuView.getWidth());
            danmukuList.add(item);
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

    private void initPrivateLetterLayout() {
        privateLetterLayout = findViewById(R.id.private_letter_list_rl);
        //消息
//        messageInput = (LinearLayout) findViewById(R.id.messageActivityBottomLayout);
        privateLetterLayout.setVisibility(View.GONE);

        privateLetterPtrFramentlayout = (PtrHTFrameLayout) findViewById(R.id.private_letter_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, privateLetterPtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        privateLetterPtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getPrivateLetterMessages();
            }
        });

        privateLetterRecyclerView = (SwipeListView) findViewById(R.id.private_letter_recycler);
        /*/
        设置item不可点击
         */
        privateLetterRecyclerView.setEnabled(false);
        privateLetterRecyclerView.setRightViewWidth(80);
        messageAdapter = new MessageAdapter(privateLetterRecyclerView.getRightViewWidth(), getContext(), privateLetters, R.layout.message_layout_item);
        privateLetterRecyclerView.setAdapter(messageAdapter);

        replyPrivateLetterLayout = (LinearLayout) findViewById(R.id.reply_private_letter_ll);
//      privateLetterRecyclerView = (RecyclerView) findViewById(R.id.private_letter_recycler);
        replyBackBtn = findViewById(R.id.reply_private_letter_back_btn);
        replyCloseBtn = (ImageView) findViewById(R.id.reply_private_letter_close_btn);
        replyUserName = (TextView) findViewById(R.id.reply_private_letter_name_text);
        messageP2pListView = (MessageListView) findViewById(R.id.messageP2pListView);
        /**
         * 设置条目不可点击
         */
        messageP2pListView.setEnabled(false);
        buttonP2pSendMessage = (TextView) findViewById(R.id.buttonP2pSendMessage);
        editP2pTextMessage = (EditText) findViewById(R.id.editP2pTextMessage);


    }

    /**
     * 私信操作
     */
    boolean isRefreshPrivate;

    public void autoGetPrivates() {
        if (!isRefreshPrivate) {
            privateLetterPtrFramentlayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    privateLetterPtrFramentlayout.autoRefresh();
                    isRefreshPrivate = true;
                }
            }, MConstants.DELAYED);
        }
    }

    public void getPrivateLetterMessages() {
        liveAnchorOperateInterface.onClickShowPrivateLetter(privateLetters);
    }

    public void addPrivates() {
        privateLetterPtrFramentlayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageAdapter.addAll(privateLetters);
            }
        }, MConstants.DELAYED);
    }

    public void clearPrivates() {
        privateLetters.clear();
        messageAdapter.clear();
    }

    public void refreshPrivate() {
        privateLetterPtrFramentlayout.refreshComplete();
    }

    /**
     * 设置消息组件
     *
     * @param activity
     * @param roomId
     */
    public void setMessageModules(Activity activity, String roomId) {
        this.roomId = roomId;
        Container container = new Container(activity, roomId, SessionTypeEnum.ChatRoom, this);
        if (messageListPanel == null) {
            messageListPanel = new ChatRoomMsgListPanel(container, rootView);
        }

        if (inputPanel == null) {
            inputPanel = new InputPanel(container, rootView, getActionList(), false);
        } else {
            inputPanel.reload(container, null);
        }
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldCollapseInputPanel();
            }
        });
    }

    /**
     * 设置消息组件
     *
     * @param activity
     */
    public void setPrivateLetterModules(Activity activity) {
        this.currAccount = CacheCenter.getNimAccount();
        Container container2 = new Container(activity, currAccount, SessionTypeEnum.P2P, this);
        if (privateLetterMsgListPanel == null) {
            privateLetterMsgListPanel = new MessageListPanel(container2, rootView);
        }

        if (inputPanel2 == null) {
            inputPanel2 = new InputPanel(container2, rootView, getActionList(), false);
        } else {
            inputPanel2.reload(container2, null);
        }
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                shouldCollapseInputPanel();
            }
        });
    }

    public void showPrivateLetterLayout() {
        controlLayout.setVisibility(View.GONE);
        privateLetterLayout.setVisibility(View.VISIBLE);
    }

    public void setGiftData(List<Gift> giftList) {
        if (adapter == null) {
            adapter = new GiftAdapter(giftList, getContext());
            giftView.setAdapter(adapter);
        } else {
            adapter.changeGiftData(giftList);
        }
    }


    /**
     * 显示礼物布局
     */
    public void showGiftLayout(boolean isShow) {
//        if (isShow) {
//            inputPanel.collapse(true);// 收起软键盘
//            giftLayout.setVisibility(View.VISIBLE);
//            if (adapter == null) {
//                adapter.notifyDataSetChanged();
//                if (adapter.getCount() == 0) {
//                    // 暂无礼物
//                    noGiftText.setVisibility(View.VISIBLE);
//                } else {
//                    noGiftText.setVisibility(View.GONE);
//                }
//            }
//        } else {
//            giftLayout.setVisibility(View.GONE);
//        }

    }

    /**
     * 修改直播widget状态
     *
     * @param liveState
     */
    public void changeLiveState(int liveState) {

        switch (liveState) {
            case MConstants.STATE_LIVE_INITIAL:
                startLayout.setVisibility(View.VISIBLE);
                break;
            case MConstants.STATE_LIVE_START:
                startText.setText("准备中...稍等");
                break;
            case MConstants.STATE_LIVE_LIVING:
                startLayout.setVisibility(View.GONE);
                startTime = System.currentTimeMillis();
                break;
            case MConstants.STATE_LIVE_END:
                endTime = System.currentTimeMillis();
                long durTime = endTime - startTime;
                DateFormat df = new SimpleDateFormat("HH:mm:ss");
                df.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                String durStr = df.format(new Date(durTime));
                liveTimeText.setText(durStr);
                liveFinishLayout.setVisibility(View.VISIBLE);
                //  设置直播结束信息画面背景
                UserInfo object4XmlDB = XmlDB.getObject4XmlDB(MConstants.KEY_USER, UserInfo.class);
                //  Picasso.with(context).load(object4XmlDB.avatarUrl).into(end_zhibo_background);
                getVideoStatistics();
                break;
            case MConstants.STATE_LIVE_ERROR:
                startLayout.setVisibility(View.VISIBLE);
                startText.setText("服务链接异常");
                //  当服务器异常的时候自动跳回主界面
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((LiveAnchorActivity) mContext).getBackMainActivity();
                    }
                }, MConstants.WAIT_TIME);
                break;
            default:
                break;
        }
    }


    private void getVideoStatistics() {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getVideoStatistics(liveInfo.videoId + "").subscribe(new NewSubscriber<VideoStatistic>(mContext, true) {
            @Override
            public void onNext(VideoStatistic videoStatistic) {
                liveIncomeDiamandText.setText(videoStatistic.getTotalGaveAmount() + "");
                audienceCountText.setText(videoStatistic.getTotalWatchCount() + "");
                rewardMeAudienceCountText.setText(videoStatistic.getTotalGaveCount() + "");
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
            }
        });
        ((LiveAnchorActivity) mContext).addSubscrebe(subscription);
    }


    public void updateUI(ChatRoomInfo roomInfo, UserInfo currUser) {
        //设置头像
        if (currUser != null) {
            ImageLoaderKit.getInstance().displayImage(currUser.avatarUrl, masterHead);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, currUser.userType);
        }
        if (roomInfo == null) {
            Logger.e("updateUI(ChatRoomInfo roomInfo).. roomInfo is null !");
            return;
        }
//        bitmap = ImageLoaderKit.getBitmapFromCache(currUser.avatarUrl, liveAnchorRl.getWidth(), liveAnchorRl.getHeight());
//        liveAnchorRl.setBackground(new BitmapDrawable(bitmap));
        UserInfo userInfo = CacheCenter.getInstance().getCurrUser();
        masterNameText.setText(userInfo.userName);
//        liveFinishMasterName.setText(roomInfo.getCreator());
        onlineCountText.setText(String.format("%s人", String.valueOf(roomInfo.getOnlineUserCount())));
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
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(userIdList);

        //  轮询在线人数
        getGuestList(userIds);
    }

    /**
     * 发送消息
     *
     * @param userInfo
     */
    public void sendPrivateLetter(final UserInfo userInfo) {
        String content = editP2pTextMessage.getText().toString();
        MessageUtils.sendMessgeP2p(userInfo.userId + "", content, editP2pTextMessage, messageP2pListView, p2PChatAdapter, userInfo, currentUser, replyPrivateLetters);
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
        Log.d("ChatRoomMessage content", " " + message.getContent());
        if (message != null && message.getAttachment() instanceof GiftInfoAttachment) {
            // 收到礼物消息
//                GiftInfoAttachment giftInfoAttachment = ((GiftInfoAttachment) message.getAttachment());
            ((LiveAnchorActivity) mContext).receivGift(message);
            Log.d("ChatRoomMessage type", " " + ((GiftInfoAttachment) message.getAttachment()).getType());
        } else if (message != null && message.getAttachment() instanceof LikeAttachment) {
            // 收到点赞爱心
            ((LiveAnchorActivity) mContext).receivLove();
            Log.d("ChatRoomMessage type", " " + ((LikeAttachment) message.getAttachment()).getType());
        } else if (message != null && message.getAttachment() instanceof DanmuAttachment) {
            // 收到弹幕
            ((LiveAnchorActivity) mContext).receivDanmu(message);
            Log.d("ChatRoomMessage type", " " + ((DanmuAttachment) message.getAttachment()).getType());
        } else if (message != null && message.getAttachment() instanceof ChatRoomNotificationAttachment) {
            // 通知类消息
//            messageListPanel.onIncomingMessage(messages);
            Log.d("ChatRoomMessage type", " " + ((ChatRoomNotificationAttachment) message.getAttachment()).getType());
        } else {
            messageListPanel.onIncomingMessage(messages);
            if (message != null && message.getAttachment() != null) {
                Log.d("ChatRoomMessage type", " " + message.getAttachment().toString());
            }
        }
    }

    /**
     * 显示礼物动画
     *
     * @param message
     */
    public void showGiftAnimation(final ChatRoomMessage message, boolean isSameUser) {
        if (giftAnimation == null) {
            Logger.e("giftAnimation is null !");
            return;
        }
        GiftInfoAttachment giftInfoAttachment = (GiftInfoAttachment) message.getAttachment();
        if (giftInfoAttachment != null) {
            liveDiamandAmountText.setText((Long.valueOf(liveDiamandAmountText.getText().toString()) + giftInfoAttachment.getGiftInfo().getAmount()) + "");
        }
        giftAnimation.showGiftAnimation(message, isSameUser);
        long productId = giftInfoAttachment.getGiftInfo().getProductId();
        animateImage(productId);
    }

    private void animateImage(long productId) {
        switch ((int) productId) {
            case 15:
            case 18:
            case 19:
            case 20:
            case 7:
            case 11:
            case 12:
            case 8:
            case 9: {
                if (animations.size() == 0) {
                    animations.add(productId);
                    if (productId == 15) {
                        if (animations.size() <= 1) {
                            playPlane();
                        }
                    }
                    if (productId == 18 || productId == 19 || productId == 20) {
                        if (animations.size() <= 1) {
                            playCar();
                        }
                    }
                    if (productId == 7) {
                        if (animations.size() <= 1) {
                            playWhiteChocolate();
                        }
                    }
                    if (productId == 11) {
                        if (animations.size() <= 1) {
                            playCake();
                        }
                    }
                    if (productId == 12) {
                        if (animations.size() <= 1) {
                            playBouquet();
                        }
                    }
                    if (productId == 8) {
                        if (animations.size() <= 1) {
                            playBlackChocolate();
                        }
                    }
                    if (productId == 9) {
                        if (animations.size() <= 1) {
                            playChocolate();
                        }
                    }
                } else {
                    if (animations.get(animations.size() - 1) != productId) {
                        animations.add(productId);
                        if (productId == 15) {
                            if (animations.size() <= 1) {
                                playPlane();
                            }
                        }
                        if (productId == 18 || productId == 19 || productId == 20) {
                            if (animations.size() <= 1) {
                                playCar();
                            }
                        }
                        if (productId == 7) {
                            if (animations.size() <= 1) {
                                playWhiteChocolate();
                            }
                        }
                        if (productId == 11) {
                            if (animations.size() <= 1) {
                                playCake();
                            }
                        }
                        if (productId == 12) {
                            if (animations.size() <= 1) {
                                playBouquet();
                            }
                        }
                        if (productId == 8) {
                            if (animations.size() <= 1) {
                                playBlackChocolate();
                            }
                        }
                        if (productId == 9) {
                            if (animations.size() <= 1) {
                                playChocolate();
                            }
                        }
                    } else {
                        if (productId == 15) {
                            if (animations.size() < 1) {
                                playPlane();
                            }
                        }
                        if (productId == 18 || productId == 19 || productId == 20) {
                            if (animations.size() < 1) {
                                playCar();
                            }
                        }
                        if (productId == 7) {
                            if (animations.size() < 1) {
                                playWhiteChocolate();
                            }
                        }
                        if (productId == 11) {
                            if (animations.size() < 1) {
                                playCake();
                            }
                        }
                        if (productId == 12) {
                            if (animations.size() < 1) {
                                playBouquet();
                            }
                        }
                        if (productId == 8) {
                            if (animations.size() < 1) {
                                playBlackChocolate();
                            }
                        }
                        if (productId == 9) {
                            if (animations.size() < 1) {
                                playChocolate();
                            }
                        }
                    }
                }
            }
            break;
        }
    }

    /**
     * 收到点赞，添加点赞动画
     */
    public void addHeart() {
        if (periscopeLayout == null) {
            Logger.e("periscopeLayout is null !");
            return;
        }
        periscopeLayout.addHeart();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.gift_layout:
//                showGiftLayout(false);
//                break;
            case R.id.close_live_btn:
                if (liveAnchorOperateInterface != null) {
                    liveAnchorOperateInterface.onClickBackClose();
                }
                break;

            case R.id.camera_switch_btn:
                if (liveAnchorOperateInterface != null) {
                    liveAnchorOperateInterface.onClickSwitchCamera();
                }
                break;
            case R.id.private_letter_btn:
                if (liveAnchorOperateInterface != null) {
                    liveAnchorOperateInterface.onClickPrivateLetter();
                    sessionType = SessionTypeEnum.P2P;
//                    setPrivateLetterModules((LiveAnchorActivity) mContext);
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
                privateLetterLayout.setVisibility(VISIBLE);
                setMessageModules((LiveAnchorActivity) mContext, liveInfo.cid);
                break;
            case R.id.reply_private_letter_close_btn:
                replyPrivateLetterLayout.setVisibility(View.GONE);
                controlLayout.setVisibility(View.VISIBLE);
                sessionType = SessionTypeEnum.ChatRoom;
                sessionType = SessionTypeEnum.ChatRoom;
                setMessageModules((LiveAnchorActivity) mContext, liveInfo.cid);
                break;
            case R.id.share_btn:
                if (liveAnchorOperateInterface != null) {
                    liveAnchorOperateInterface.onClickShare();
                }
                break;
            case R.id.live_good_btn:
                if (liveAnchorOperateInterface != null) {
                    liveAnchorOperateInterface.onClickGood();
                }
                break;
            case R.id.save_live_btn: {
                if (liveAnchorOperateInterface != null) {
                    VideoOffLiveBody videoOffLiveBody = new VideoOffLiveBody();
                    videoOffLiveBody.totalProfit = Long.parseLong(liveIncomeDiamandText.getText().toString());
                    videoOffLiveBody.peakValue = Long.parseLong(audienceCountText.getText().toString());
                    liveAnchorOperateInterface.onClickSaveVideo(videoOffLiveBody);
                }
            }
            break;
            case R.id.finish_live_btn:
                if (liveAnchorOperateInterface != null) {
                    VideoOffLiveBody videoOffLiveBody = new VideoOffLiveBody();
                    videoOffLiveBody.totalProfit = Long.parseLong(liveIncomeDiamandText.getText().toString());
                    videoOffLiveBody.peakValue = Long.parseLong(audienceCountText.getText().toString());
                    liveAnchorOperateInterface.onClickFinish(videoOffLiveBody);
                }
                break;
            case R.id.live_anchor_widget_rl:
//                messageInput.setVisibility(GONE);
                replyPrivateLetterLayout.setVisibility(GONE);
                privateLetterLayout.setVisibility(GONE);
                controlLayout.setVisibility(VISIBLE);
                sessionType = SessionTypeEnum.ChatRoom;
                setMessageModules((LiveAnchorActivity) mContext, liveInfo.cid);
                break;
            case R.id.buttonP2pSendMessage: {
                /**
                 * 此处加了一个方法  强制隐藏键盘
                 */
                hideInput(getContext(), editP2pTextMessage);
                UserInfo userInfo = (UserInfo) buttonP2pSendMessage.getTag();
                /*
                * 此处做了判断，当输入框为空时   不得进行私信的发送
                * */
                if ((userInfo != null) && !"".equals(editP2pTextMessage.getText().toString())) {
                    liveAnchorOperateInterface.onClickSendPrivateLetter(userInfo);
                }
            }
            break;

        }
    }

    public void onResume() {
        if (messageListPanel != null) {
            messageListPanel.onResume();
        }
        if (privateLetterMsgListPanel != null) {
            privateLetterMsgListPanel.onResume();
        }
        mDanmakuView.show();
    }

    protected void onPause() {
        mDanmakuView.hide();
    }

    public void onDestroy() {
        adapter = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        mDanmakuView.clear();
    }

    /**************************
     * Module proxy回话窗口
     ***************************/
    @Override
    public boolean sendMessage(final IMMessage msg) {
        if (msg instanceof ChatRoomMessage) {
            ChatRoomMessage message = (ChatRoomMessage) msg;
            Map<String, Object> ext = new HashMap<>();
            ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId, CacheCenter.getNimAccount());
            if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                ext.put("type", chatRoomMember.getMemberType().getValue());
                message.setRemoteExtension(ext);
            }

            NIMClient.getService(ChatRoomService.class).sendMessage(message, false)
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

        } else {
            NIMClient.getService(MsgService.class).sendMessage(msg, false)
                    .setCallback(new RequestCallback<Void>() {
                        @Override
                        public void onSuccess(Void param) {
                            privateLetterMsgListPanel.onMsgSend(msg);
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

        }
        return true;
    }

    @Override
    public void onInputPanelExpand() {
        controlLayout.setVisibility(View.GONE);
    }

    @Override
    public void shouldCollapseInputPanel() {
//        inputPanel.collapse(false);
//        inputPanel2.collapse(false);
        controlLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isLongClickEnabled() {
        return false;
    }

    @Override
    public void showDanmu(IMMessage msg) {
        DanmuAttachment danmuAttachment = (DanmuAttachment) msg.getAttachment();
        if (TextUtils.isEmpty(danmuAttachment.getContent())) {
            Toast.makeText(mContext, "弹幕为空", Toast.LENGTH_SHORT).show();
        } else {

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
        Intent intent = new Intent(mContext, LiveAnchorGoodsActivity.class);
        intent.putExtra("live_anchor_user_id", liveInfo.userId);
        intent.putExtra("live_video_id", liveInfo.videoId);
        mContext.startActivity(intent);
    }

    public LiveInfo getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(LiveInfo liveInfo) {
        this.liveInfo = liveInfo;
    }

    private void getAccountBalance() {
        ApiWrapper apiWrapper = new ApiWrapper();
        Subscription subscription = apiWrapper.getAccountBalance().subscribe(new NewSubscriber<Long>(mContext, false) {
            @Override
            public void onNext(Long amount) {
                liveDiamandAmountText.setText(amount + "");
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
            }
        });
        ((LiveAnchorActivity) mContext).addSubscrebe(subscription);
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
        ((LiveAnchorActivity) mContext).addSubscrebe(subscription);
    }

    public void showReplyPrivateLetterLayout(final UserInfo userInfo) {
        privateLetterLayout.setVisibility(View.GONE);
        controlLayout.setVisibility(View.GONE);
        replyPrivateLetterLayout.setVisibility(VISIBLE);
        replyUserName.setText(userInfo.userName);
        sessionType = SessionTypeEnum.P2P;
        buttonP2pSendMessage.setTag(userInfo);
        if (p2PChatAdapter != null) {
            replyPrivateLetters.clear();
            p2PChatAdapter.clear();
        }
        IMMessage imMessage = MessageBuilder.createEmptyMessage(userInfo.userId + "", SessionTypeEnum.P2P, 0);
        MessageUtils.pullMessageHistory(imMessage, new RequestCallbackWrapper<List<IMMessage>>() {
            @Override
            public void onResult(int i, List<IMMessage> imMessages, Throwable throwable) {
                if (imMessages != null) {
                    Collections.reverse(imMessages);
                    p2PChatAdapter = new P2PChatAdapter(getContext(), replyPrivateLetters, CacheCenter.getInstance().getCurrUser(), userInfo);
                    messageP2pListView.setAdapter(p2PChatAdapter);
                    for (IMMessage imMessage : imMessages) {
                        PrivateLetter privateLetter = new PrivateLetter();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = format.format(new java.util.Date(imMessage.getTime()));
                        privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
                        privateLetter.setContactId(imMessage.getSessionId());
                        privateLetter.setAccount(imMessage.getFromAccount());
                        privateLetter.setMessage(imMessage.getContent());
                        replyPrivateLetters.add(privateLetter);
                    }
                    p2PChatAdapter.addAll(replyPrivateLetters);
                    messageP2pListView.setSelection(messageP2pListView.getBottom());
                }
            }
        });
    }

    public void onIncomingIMMessage(List<IMMessage> messages) {
//        for(IMMessage im:messages){
//            PrivateLetter pl=new PrivateLetter();
//            pl.setImMessage(im);
//            getUserInfo(Long.parseLong(im.getFromAccount()));
//            pl.setUserInfo(imSender);
//            if(!im.isRemoteRead()) {
//                privateLetterList.add(pl);
//            }
//        }
//
//        if (privateLetterMsgListPanel == null) {
//            Logger.e("privateLetterMsgListPanel is null !");
//            return;
//        }
//        privateLetterMsgListPanel.onIncomingMessage(messages);
    }

    /**
     * 接收私信
     *
     * @param messages
     */
    public void onPrivateMessage(List<IMMessage> messages) {
        if (messages != null) {
            UserInfo userInfo = (UserInfo) buttonP2pSendMessage.getTag();
            if (userInfo != null) {
                for (IMMessage im : messages) {
                    String contactId = im.getSessionId();
                    if ((userInfo.userId + "").equals(contactId)) {
                        PrivateLetter privateLetter = new PrivateLetter();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = format.format(new java.util.Date(im.getTime()));
                        privateLetter.setTime(DateUtils.formatDisplayTime(date, true));
                        privateLetter.setContactId(im.getSessionId());
                        privateLetter.setAccount(im.getFromAccount());
                        privateLetter.setMessage(im.getContent());
                        replyPrivateLetters.add(privateLetter);
                        p2PChatAdapter.add(privateLetter);
                    }
                }
                messageP2pListView.setSelection(messageP2pListView.getBottom());
            }
            isRefreshPrivate = false;
            autoGetPrivates();
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo(long userId) {
        UserModel userModel = new UserModel(mContext);
        userModel.getUserInfo(userId, new Callback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo userInfo) {
                imSender = userInfo;
            }

            @Override
            public void onFailure(int resultCode, String message) {
            }
        }, false);
    }

    public void recentContactList(List<RecentContact> messages) {

    }


    /**
     * 强制隐藏输入法键盘
     */
    private void hideInput(Context context, View view) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
