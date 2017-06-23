package com.shangshow.showlive.controller.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.common.utils.ShangXiuUtil;
import com.shangshow.showlive.controller.adapter.SearchAdpter;
import com.shangshow.showlive.controller.liveshow.LiveAudienceActivity;
import com.shangshow.showlive.controller.member.UserInfoActivity;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.LabelInfo;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.VideoRoom;
import com.shangshow.showlive.network.service.models.body.BasePageBody;
import com.shangshow.showlive.network.service.models.body.PageBody;
import com.shangshow.showlive.network.service.models.responseBody.EditFriendBody;
import com.shangshow.showlive.widget.BottomScrollView;
import com.shangshow.showlive.widget.FlowLayout;
import com.shangshow.showlive.widget.ScrollerListView;

import java.util.ArrayList;
import java.util.List;

// 首页 - 搜索

/**
 * OK .
 */

/**
 * 当搜索的主播正在开播时  直接进入直播间  如果没有在直播  进入个人中心 查看信息 ；
 */
public class HomeSearchActivity extends Activity implements View.OnClickListener {

    private UserModel userModel;
    private SearchAdpter searchAdpter;
    private ScrollerListView searchResultList;
    private BottomScrollView bsv_search;
    private EditText et_home_search_content;
    private TextView tv_search_close, tv_search_item;
    private FlowLayout fl_home_search;
    private List<LabelInfo> labelInfoList = new ArrayList<LabelInfo>();
    private long currentPage = 1;
    private Context context;
    private VideoRoom videoRoom1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉titile
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_search);
        // 禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        searchResultList = (ScrollerListView) findViewById(R.id.searchResultList);
        fl_home_search = (FlowLayout) findViewById(R.id.fl_home_search);
        tv_search_close = (TextView) findViewById(R.id.tv_search_close);
        tv_search_item = (TextView) findViewById(R.id.tv_search_item);
        et_home_search_content = (EditText) findViewById(R.id.et_home_search_content);
        bsv_search = (BottomScrollView) findViewById(R.id.bsv_search);

        userModel = new UserModel(this);
        searchAdpter = new SearchAdpter(this, R.layout.item_recycler_friend, new ArrayList<UserInfo>());
        searchResultList.setAdapter(searchAdpter);
        fl_home_search.setHorizontalSpacing(getResources().getDimension(R.dimen.dp20));
        fl_home_search.setVerticalSpacing(getResources().getDimension(R.dimen.dp10));

        loadHomeRecommend(MConstants.DATA_4_REFRESH);
        getHotLabelList();

        tv_search_close.setOnClickListener(this);
        tv_search_item.setOnClickListener(this);
        //点击关注 取消关注  禁止试图回到最顶部
        searchAdpter.setOnItemClickListener(new SearchAdpter.OnItemClickListener() {
            @Override
            public void attention(UserInfo userInfo) {
                if (userInfo.isFriend) {
                    cancleAttention(userInfo.userId);
                } else {
                    attentionUser(userInfo.userId);
                }
            }

            @Override
            public void onItemClick(UserInfo userInfo, int position) {
                if (0 == userInfo.roomId) {
                    Intent intent = new Intent(HomeSearchActivity.this, UserInfoActivity.class);
                    intent.putExtra(UserInfoActivity.key_USERINFO, userInfo);
                    startActivity(intent);
                } else {
                    /**
                     * 当前主播正在直播，去看直播  进入观众端
                     * new 一个对象videoRoom
                     */
                  /*  VideoRoom videoRoom = new VideoRoom();
                    videoRoom.roomId = userInfo.roomId;
                    videoRoom.userName = userInfo.userName;
                   // videoRoom.logoUrl = userInfo.videoId;
                   // videoRoom.videoUrl = userInfo.videoUrl
                    // videoRoom.logoUrl = userInfo.logoUrl
                    videoRoom.userId = userInfo.userId;
                    videoRoom.avatarUrl = userInfo.avatarUrl;
                    videoRoom.userType = userInfo.userType;
                    videoRoom.httpPullUrl = userInfo.httpPullUrl;*/
                    long roomId = userInfo.roomId;
                    userModel.getSingeoVideoRoom(roomId, new Callback<VideoRoom>() {
                        @Override
                        public void onSuccess(VideoRoom videoRoom) {
                            videoRoom1 = videoRoom;
                        }
                        @Override
                        public void onFailure(int resultCode, String message) {
                        }
                    });
                    LiveAudienceActivity.start(HomeSearchActivity.this, videoRoom1);
                    /*Intent intent = new Intent(HomeSearchActivity.this, LiveAudienceActivity.class);
                    intent.putExtra(UserInfoActivity.key_USERINFO, userInfo);
                    startActivity(intent);*/
                }
            }
        });
        et_home_search_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    tv_search_close.setText("取消");
                } else {
                    tv_search_close.setText("搜索");
                }
            }
        });
        bsv_search.setOnScrollToBottomLintener(new BottomScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollBottomListener(boolean isBottom) {
                if (isBottom) {
                    loadHomeRecommend(MConstants.DATA_4_LOADMORE);
                }
            }
        });
    }

    /**
     * 加载推荐列表
     *
     * @param loadType
     */
    private void loadHomeRecommend(final long loadType) {
        PageBody pageBody = ShangXiuUtil.refreshPagerBodey(loadType, currentPage);
        pageBody.pageSize = MConstants.PAGE_SIZE;
        userModel.getRecomUserList(pageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                currentPage = userInfoPager.pageNum;
                List<UserInfo> userInfos = userInfoPager.list;
                if (loadType == MConstants.DATA_4_REFRESH) {
                    searchAdpter.replaceAll(userInfos);
                    currentPage++;
                } else {
                    currentPage++;
                    searchAdpter.addAll(userInfos);
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }


    //增加多个标签

    private void getHotLabelList() {
        BasePageBody pageBody = new BasePageBody();
        pageBody.pageNum = currentPage;
        pageBody.pageSize = MConstants.PAGE_SIZE;
        userModel.getHotLabelList(pageBody, new Callback<Pager<LabelInfo>>() {
            @Override
            public void onSuccess(Pager<LabelInfo> labelInfoPager) {
                if (labelInfoPager.list.size() > 0) {
                    labelInfoList.clear();
                    labelInfoList.addAll(labelInfoPager.list);
                    for (LabelInfo labelInfo : labelInfoList) {
                        try {
                            View view = getLayoutInflater().inflate(R.layout.button_layout, null);
                            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) getResources().getDimension(R.dimen.dp30));
//                            params.height = (int) getResources().getDimension(R.dimen.dp10);
                            view.setLayoutParams(params);
                            Button button = (Button) view.findViewById(R.id.button);
                            button.setText("#" + labelInfo.getLabelName() + "#");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Button button = (Button) view;
                                    Intent intent = new Intent(HomeSearchActivity.this, SearchActivity.class);
                                    intent.putExtra("searchContent", button.getText().toString() + "");
                                    startActivity(intent);
                                }
                            });
                            fl_home_search.addView(button);
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    currentPage++;
                } else {
                    Toast.makeText(HomeSearchActivity.this, "没有更多标签", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 关注
     * 不在刷新列表  让视图停在当前位置
     *
     * @param userId
     */
    private void attentionUser(final long userId) {
        userModel.addFriend(userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                loadHomeRecommend(MConstants.DATA_4_REFRESH);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                loadHomeRecommend(MConstants.DATA_4_REFRESH);
            }
        });
    }

    /**
     * 取消关注
     * 不在刷新列表  让视图停在当前位置
     *
     * @param userId
     */
    private void cancleAttention(final long userId) {
        userModel.cancelFriend(userId, new Callback<EditFriendBody>() {
            @Override
            public void onSuccess(EditFriendBody editFriendBody) {
                loadHomeRecommend(MConstants.DATA_4_REFRESH);
            }

            @Override
            public void onFailure(int resultCode, String message) {
                loadHomeRecommend(MConstants.DATA_4_REFRESH);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_close: {

                String searchContent = et_home_search_content.getText().toString();
                if (TextUtils.isEmpty(searchContent)) {
                    finish();
                    return;
                }
                Intent intent = new Intent(HomeSearchActivity.this, SearchActivity.class);
                intent.putExtra("searchContent", et_home_search_content.getText().toString() + "");
                startActivity(intent);
                //  强制隐藏输入键盘
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(getCurrentFocus()
                                        .getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
            }
            break;
            case R.id.tv_search_item: {
                //增加多个标签
                getHotLabelList();
            }
            break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHomeRecommend(MConstants.DATA_4_REFRESH);
    }
}
