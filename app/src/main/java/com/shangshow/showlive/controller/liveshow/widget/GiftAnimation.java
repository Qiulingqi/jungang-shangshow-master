package com.shangshow.showlive.controller.liveshow.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.nim.uikit.common.ui.imageview.HeadImageView;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.netease.nim.uikit.session.extension.GiftInfoAttachment;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 礼物动画
 * Created by hzxuwen on 2016/4/1.
 */
public class GiftAnimation {
    private final int SHOW_HIDE_ANIMATOR_DURATION = 500;
    private final int ANIMATION_STAY_DURATION = 1000;

    private boolean upFree = true;
    private boolean downFree = true;
    private boolean isSameUser = false;

    private ViewGroup upView;
    private ViewGroup downView;
    private AnimatorSet upAnimatorSet;
    private AnimatorSet downAnimatorSet;

    private Queue<ChatRoomMessage> cache = new LinkedList<>();

    public GiftAnimation(ViewGroup downView, ViewGroup upView) {
        this.upView = upView;
        this.downView = downView;
        this.upAnimatorSet = buildAnimationSet(upView);
        this.downAnimatorSet = buildAnimationSet(downView);
    }

    // 收到礼物，等待显示动画
    public void showGiftAnimation(final ChatRoomMessage message, boolean isSameUser) {
        cache.add(message);
        this.isSameUser = isSameUser;
        checkAndStart();
    }

    private void checkAndStart() {
        if(!upFree && !downFree) {
            return;
        }
        if (isSameUser) {
            if (!downFree) {
                startAnimation(downView, downAnimatorSet);
            } else {
                startAnimation(upView, upAnimatorSet);
            }
        } else {
            if (downFree) {
                startAnimation(downView, downAnimatorSet);
            } else {
                startAnimation(upView, upAnimatorSet);
            }
        }
    }

    // 开始礼物动画
    private void startAnimation(ViewGroup target, AnimatorSet set) {
        ChatRoomMessage message = cache.poll();
        if(message == null) {
            return;
        }

        // 更新状态
        onAnimationStart(target);

        // 更新礼物视图
        updateView(message, target);

        // 执行动画组
        target.setAlpha(1f);
        target.setVisibility(View.VISIBLE);
        set.start();
    }

    private void onAnimationStart(final ViewGroup target) {
        if(target == upView) {
            upFree = false;
        } else if(target == downView) {
            downFree = false;
        }
    }

    private void onAnimationCompleted(final ViewGroup target) {
        if(target == upView) {
            upFree = true;
        } else if(target == downView) {
            downFree = true;
        }

        checkAndStart();
    }

    /**
     * ********************* 属性动画 *********************
     */

    private AnimatorSet buildAnimationSet(final ViewGroup target){
        ObjectAnimator show = buildShowAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        ObjectAnimator hide = buildHideAnimator(target, SHOW_HIDE_ANIMATOR_DURATION);
        hide.setStartDelay(ANIMATION_STAY_DURATION);

        AnimatorSet set = new AnimatorSet();
        set.setTarget(target);
        set.playSequentially(show, hide);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                onAnimationCompleted(target);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        return set;
    }

    private ObjectAnimator buildShowAnimator(final View target, long duration) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(target, "translationX", -300.0F, 0.0F)
                .setDuration(duration);
        translationX.setInterpolator(new OvershootInterpolator());

        return translationX;
    }

    private ObjectAnimator buildHideAnimator(final View target, long duration) {
        return ObjectAnimator.ofFloat(target, View.ALPHA, 1f, 0.0f)
                .setDuration(duration);
    }

    /**
     * ********************* 更新礼物信息 *********************
     */

    private void updateView(final ChatRoomMessage message, ViewGroup root) {
        // senderName
        TextView audienceNameText = (TextView) root.findViewById(com.shangshow.showlive.R.id.audience_name);
        TextView giftText = (TextView) root.findViewById(com.shangshow.showlive.R.id.gift_name);
        ImageView giftImage = (ImageView) root.findViewById(com.shangshow.showlive.R.id.gift_image);
        HeadImageView userIcon = (HeadImageView) root.findViewById(com.shangshow.showlive.R.id.user_icon);
        ImageView userTypeIcon = (ImageView) root.findViewById(com.shangshow.showlive.R.id.user_type_icon);
        TextView giftNum = (TextView) root.findViewById(com.shangshow.showlive.R.id.gift_count_text_view);
        if (message != null) {
            GiftInfoAttachment attachment = (GiftInfoAttachment) message.getAttachment();
            audienceNameText.setText(attachment.getSendUser().userName);
            ImageLoaderKit.getInstance().displayImage(attachment.getSendUser().avatarUrl, userIcon);
            ShangXiuUtil.setUserTypeIcon(userTypeIcon, attachment.getSendUser().userType);
            // gift name & image
            giftText.setText(attachment.getGiftInfo().getProductName());

            ImageLoaderKit.getInstance().displayImage(attachment.getGiftInfo().getLogoUrl(), giftImage, false);
            giftNum.setText(attachment.getCount() + "");
        }


    }
}
