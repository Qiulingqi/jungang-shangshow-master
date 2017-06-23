package com.shangshow.showlive.controller.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.base.cache.ImageLoaderKit;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.custom.DynamicHeightImageView;
import com.shangshow.showlive.controller.common.CommonBrowserActivity;
import com.shangshow.showlive.controller.common.LoginActivity;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.network.service.models.AdsInfo;
import com.shangshow.showlive.network.service.models.VideoRoom;

import java.util.List;

/**
 * 广告适配器
 *
 * @author JeremyAiYt
 */
public class BannerViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<AdsInfo> banners;

    public BannerViewPagerAdapter() {

    }

    public BannerViewPagerAdapter(Context context, List<AdsInfo> Banners) {
        this.context = context;
        this.banners = Banners;

    }

    public void changeData(List<AdsInfo> Banners) {
        this.banners = Banners;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return banners.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public Object instantiateItem(View container, final int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_viewpager_banner, null);

        final DynamicHeightImageView photoView = (DynamicHeightImageView) view
                .findViewById(R.id.item_room_image);
        if (banners.size() > 0) {
            //广告比例1:3
            photoView.setHeightRatio(MConstants.RATIO_POINT_BANNER);
            final AdsInfo adsInfo = banners.get(position);
            ImageLoaderKit.getInstance().displayImage(adsInfo.resource, photoView);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (1 == adsInfo.actionType) {
                        Intent intent = new Intent(context, CommonBrowserActivity.class);
                        intent.putExtra(CommonBrowserActivity.TITLE_KEY, adsInfo.name);
                        intent.putExtra(CommonBrowserActivity.URL_KEY, adsInfo.action);
                        context.startActivity(intent);
                    }
                    if (2 == adsInfo.actionType) {

                        if (!"".equals(adsInfo.action) && adsInfo.action.length() > 16) {
                            //  截取字符串判断是哪个类型
                            String substring = adsInfo.action.substring(0, 16);
                            String substring1 = substring.substring(12, 16);
                            if ("user".equals(substring1)) {

                                //  截取参数  用作跳转下个界面使用
                               // String substring2 = adsInfo.action.substring(24, 31);
                              //  String substring3 = adsInfo.action.substring(39, 42);
                                ToastUtils.show("查看用户信息");
                                /**
                                 * 信息不全   先注释掉
                                 */
                                /*Intent intent = new Intent(context, UserInfoActivity.class);
                                intent.putExtra(UserInfoActivity.key_USERINFO, "");
                                context.startActivity(intent);*/
                            }
                            if ("vide".equals(substring1)) {
                                ToastUtils.show("查看用户视频");
                            }
                            if ("live".equals(substring1)) {
                                if (adsInfo.action.length() >= 42){
                                    String substring2 = adsInfo.action.substring(24, 31);
                                    String substring3 = adsInfo.action.substring(39, 42);
                                    // 说明当前有主播正在直播
                                    if (!CommonUtil.isClickSoFast(MConstants.DELAYED)) {
                                        if (CacheCenter.getInstance().isLogin()) {
                                            Intent intent = new Intent(context, LiveAudienceActivity.class);
                                            VideoRoom videoRoom = new VideoRoom();
                                            videoRoom.userId = Integer.parseInt(substring2);
                                            videoRoom.roomId = Integer.parseInt(substring3);
                                            intent.putExtra("videoRoom", videoRoom);
                                            context.startActivity(intent);
                                        } else {
                                            context.startActivity(new Intent(context, LoginActivity.class));
                                        }
                                    }
                                    ToastUtils.show("观看当前正在进行的直播");

                                }
                            }
                        } else {
                            ToastUtils.show("网址不合法，请重试");
                        }
                       /* Intent intent = new Intent(context, LiveAnchorActivity.class);
                        intent.putExtra(CommonBrowserActivity.TITLE_KEY, adsInfo.name);
                        intent.putExtra(CommonBrowserActivity.URL_KEY, adsInfo.action);
                        context.startActivity(intent);*/
                    }
                }
            });
        }

        ((ViewGroup) container).addView(photoView);
        return photoView;
    }

}