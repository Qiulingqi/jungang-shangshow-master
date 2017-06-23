package com.shangshow.showlive.controller.personal.cooperate;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.netease.nim.uikit.model.UserInfo;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BasePageFragment;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.base.cache.CacheCenter;
import com.shangshow.showlive.common.utils.CommonUtil;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;
import com.shangshow.showlive.common.widget.ultra.PtrDefaultHandler;
import com.shangshow.showlive.common.widget.ultra.PtrFrameLayout;
import com.shangshow.showlive.common.widget.ultra.PtrHTFrameLayout;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.Pager;
import com.shangshow.showlive.network.service.models.body.CooperationPageBody;
import com.shangshow.showlive.network.service.models.body.UserBusinessCooperationBody;
import com.shangshow.showlive.widget.SwipeListView;

import java.util.ArrayList;
import java.util.List;


/**
 * @author
 */

/**
 * 等待合作
 */
public class WaitingCooperateFragment extends BasePageFragment {
    public static String key_USER_TYPE;
    private UserModel userModel;
    private PtrHTFrameLayout waitingCooperatePtrFramentlayout;
    private ArrayList<UserInfo> userInfos = new ArrayList<UserInfo>();
    private ArrayList<UserInfo> userInfos2 = new ArrayList<UserInfo>();

    UserInfo currentUser = CacheCenter.getInstance().getCurrUser();
    private long currPage = 1;
    private Context mContext;
    private SwipeListView waitingCooperateRecyclerView,waitingCooperateRecyclerView2;

    private WaitingCooperateRecyclerViewAdapter waitingCooperateRecyclerViewAdapter;
    private WaitingCooperateRecyclerViewAdapter2 waitingCooperateRecyclerViewAdapter2;
    private int cooperateType = 1;

    public static WaitingCooperateFragment newInstance(String userType) {
        WaitingCooperateFragment f = new WaitingCooperateFragment();
        Bundle b = new Bundle();
        b.putString(key_USER_TYPE, userType);
        f.setArguments(b);
        return f;
    }

    @Override
    public void lazyLoad() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_waiting_cooperate_type_fragment;
    }

    @Override
    protected void initWidget(View rootView) {
        userModel = new UserModel(mContext);

        waitingCooperatePtrFramentlayout = (PtrHTFrameLayout) rootView.findViewById(R.id.waiting_cooperate_ptr_framentlayout);
        CommonUtil.SetPtrRefreshConfig(mContext, waitingCooperatePtrFramentlayout, MConstants.REFRESH_HEADER_WHITE);
        waitingCooperatePtrFramentlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getwaitingCooperate(MConstants.DATA_4_REFRESH);
            }
        });

        //  别人发出的合作申请
        waitingCooperateRecyclerView = (SwipeListView) rootView.findViewById(R.id.waiting_cooperate_type_recyclerView);
        waitingCooperateRecyclerView.setRightViewWidth(160);
        waitingCooperateRecyclerViewAdapter = new WaitingCooperateRecyclerViewAdapter(waitingCooperateRecyclerView.getRightViewWidth(),
                mContext, R.layout.item_recycler_waiting_cooperate, new ArrayList<UserInfo>());
        waitingCooperateRecyclerView.setAdapter(waitingCooperateRecyclerViewAdapter);

        //  自己发出的合作申请
        waitingCooperateRecyclerView2 = (SwipeListView) rootView.findViewById(R.id.waiting_cooperate_type_recyclerView2);
        waitingCooperateRecyclerView2.setRightViewWidth(80);
        waitingCooperateRecyclerViewAdapter2 = new WaitingCooperateRecyclerViewAdapter2(waitingCooperateRecyclerView2.getRightViewWidth(),
                mContext, R.layout.item_recycler_waiting_cooperate2, new ArrayList<UserInfo>());
        waitingCooperateRecyclerView2.setAdapter(waitingCooperateRecyclerViewAdapter2);

        // 刷新
        waitingCooperatePtrFramentlayout.autoRefresh(true);

    }

    @Override
    protected void bindEven() {
        waitingCooperateRecyclerViewAdapter.setOperater(new WaitingCooperateRecyclerViewAdapter.CooperaterOperater() {
            @Override
            public void aggree(int position) {
                UserInfo userInfo = userInfos.get(position);
                UserBusinessCooperationBody userBusinessCooperationBody = new UserBusinessCooperationBody();
                userBusinessCooperationBody.userBusinessCooperationId = userInfo.userBusinessCooperationId + "";
                userBusinessCooperationBody.status = "EBL";
                if (currentUser.userType == MConstants.USER_TYPE_MERCHANT) {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = currentUser.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    }
                } else {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = currentUser.userId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = currentUser.userId + "";
                    }
                }
                agreeCooperation(userBusinessCooperationBody);
                cooperateType = 1;
            }

            @Override
            public void disaggree(int position) {
                UserInfo userInfo = userInfos.get(position);
                UserBusinessCooperationBody userBusinessCooperationBody = new UserBusinessCooperationBody();
                userBusinessCooperationBody.userBusinessCooperationId = userInfo.userBusinessCooperationId + "";
                userBusinessCooperationBody.status = "DBL";
                if (currentUser.userType == MConstants.USER_TYPE_MERCHANT) {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = currentUser.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    }
                } else {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = currentUser.userId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.userId + "";
                    }
                }
                agreeCooperation(userBusinessCooperationBody);
                cooperateType = 2;
            }

            @Override
            public void cancle(int position) {
                UserInfo userInfo = userInfos.get(position);
                UserBusinessCooperationBody userBusinessCooperationBody = new UserBusinessCooperationBody();
                userBusinessCooperationBody.userBusinessCooperationId = userInfo.userBusinessCooperationId + "";
                userBusinessCooperationBody.status = "DEL";
                if (currentUser.userType == MConstants.USER_TYPE_MERCHANT) {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = currentUser.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    }
                } else {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = currentUser.userId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.userId + "";
                    }
                }
                agreeCooperation(userBusinessCooperationBody);
                cooperateType = 3;
            }
        });

        // 第二个适配器的监听事件
        waitingCooperateRecyclerViewAdapter2.setOperater(new WaitingCooperateRecyclerViewAdapter2.CooperaterOperater() {
            @Override
            public void disaggree(int position) {
                UserInfo userInfo = userInfos2.get(position);
                UserBusinessCooperationBody userBusinessCooperationBody = new UserBusinessCooperationBody();
                userBusinessCooperationBody.userBusinessCooperationId = userInfo.userBusinessCooperationId + "";
                userBusinessCooperationBody.status = "DBL";
                if (currentUser.userType == MConstants.USER_TYPE_MERCHANT) {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = currentUser.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    }
                } else {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = currentUser.userId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.userId + "";
                    }
                }
                agreeCooperation(userBusinessCooperationBody);
                cooperateType = 2;
            }

            @Override
            public void cancle(int position) {
                UserInfo userInfo = userInfos2.get(position);
                UserBusinessCooperationBody userBusinessCooperationBody = new UserBusinessCooperationBody();
                userBusinessCooperationBody.userBusinessCooperationId = userInfo.userBusinessCooperationId + "";
                userBusinessCooperationBody.status = "DEL";
                if (currentUser.userType == MConstants.USER_TYPE_MERCHANT) {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = currentUser.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.userId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.applayUserId + "";
                    }
                } else {
                    if (userInfo.applayUserId == userInfo.userId) {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = currentUser.userId + "";
                    } else {
                        userBusinessCooperationBody.userId = userInfo.applayUserId + "";
                        userBusinessCooperationBody.businessUserId = userInfo.userId + "";
                    }
                }
                agreeCooperation(userBusinessCooperationBody);
                cooperateType = 3;
            }
        });






    }

    @Override
    protected void setView() {

    }

    /**
     * 获取待合作列表
     */
    public void getwaitingCooperate(final int loadType) {
        CooperationPageBody cooperationPageBody = new CooperationPageBody();
        //如果刷新，请求第一页
        if (loadType == MConstants.DATA_4_REFRESH) {
            cooperationPageBody.pageNum = MConstants.PAGE_INDEX;
        } else {
            cooperationPageBody.pageNum = currPage;
        }
        cooperationPageBody.pageSize = MConstants.PAGE_SIZE;
        cooperationPageBody.orders = "";
        cooperationPageBody.status = "APL";
        userModel.getUserCooperationList(cooperationPageBody, new Callback<Pager<UserInfo>>() {
            @Override
            public void onSuccess(Pager<UserInfo> userInfoPager) {
                currPage = userInfoPager.pageNum;
                //刷新时
                //刷新时
                if (loadType == MConstants.DATA_4_REFRESH) {
                    userInfos.clear();
                    userInfos2.clear();
                    List<UserInfo> loadUserInfo = userInfoPager.list;
                    for (UserInfo userInfo : loadUserInfo) {
                        //(userInfo.applayUserId != userInfo.userId || userInfo.applayUserId != currentUser.userId) && userInfo.applayUserId != 0
                        if (userInfo.applayUserId == userInfo.userId ) {
                            userInfos.add(userInfo);
                        }else if (userInfo.applayUserId != userInfo.userId && userInfo.applayUserId != 0){
                            userInfos2.add(userInfo);
                        }
                    }
                    waitingCooperateRecyclerViewAdapter.replaceAll(userInfos);
                    waitingCooperateRecyclerViewAdapter2.replaceAll(userInfos2);

                } else {
                    List<UserInfo> loadUserInfo = userInfoPager.list;
                    for (UserInfo userInfo : loadUserInfo) {
                       /* if ((userInfo.applayUserId != userInfo.userId || userInfo.applayUserId != currentUser.userId) && userInfo.applayUserId != 0) {
                            userInfos.add(userInfo);
                        }else {
                            userInfos2.add(userInfo);
                        }*/
                        if (userInfo.applayUserId == userInfo.userId ) {
                            userInfos.add(userInfo);
                        }else if (userInfo.applayUserId != userInfo.userId){
                            userInfos2.add(userInfo);
                        }
                    }
                    waitingCooperateRecyclerViewAdapter.addAll(userInfos);
                    waitingCooperateRecyclerViewAdapter2.addAll(userInfos2);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        waitingCooperatePtrFramentlayout.refreshComplete();
                    }
                }, 500);
                waitingCooperateRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int resultCode, String message) {

            }
        });
    }

    /**
     * 拒绝/同意
     *
     * @param userBusinessCooperationBody
     */
    public void agreeCooperation(UserBusinessCooperationBody userBusinessCooperationBody) {
        userModel.agreeCooperation(userBusinessCooperationBody, new Callback<Object>() {
            @Override
            public void onSuccess(Object s) {
                String result = "您已同意和对方合作";
                switch (cooperateType) {
                    case 2: {
                        result = "您已拒绝和对方合作";
                    }
                    break;
                    case 3: {
                        result = "您已取消和对方合作";
                    }
                    break;
                }
                CustomDialogHelper.OneButtonDialog(getActivity(), "提示", result + "", "关闭", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                waitingCooperatePtrFramentlayout.autoRefresh();
            }

            @Override
            public void onFailure(int resultCode, String message) {
                showToast(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        userModel.unSubscribe();
    }
}
