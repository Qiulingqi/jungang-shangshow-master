package com.shangshow.showlive.controller.personal.cooperate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.shangshow.showlive.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的合作   待合作/已合作
 *
 * @author
 */
public class MyCooperateActivity extends FragmentActivity implements View.OnClickListener {

    private List<Fragment> fragments = new ArrayList<Fragment>();
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private View common_activity_titlebar_left_image;
    private TextView tv_my_cooperate_wait;
    private TextView tv_my_cooperate_cooperate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_mycooperate);

        common_activity_titlebar_left_image = findViewById(R.id.common_activity_titlebar_left_image);
        tv_my_cooperate_wait = (TextView) findViewById(R.id.tv_my_cooperate_wait);
        tv_my_cooperate_cooperate = (TextView) findViewById(R.id.tv_my_cooperate_cooperate);

        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        fragments.add(fragmentManager.findFragmentById(R.id.fg_cooperate_goods));
        fragments.add(fragmentManager.findFragmentById(R.id.fg_cooperate_addvalue));
        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
        }
        transaction.show(fragments.get(0)).commit();

        common_activity_titlebar_left_image.setOnClickListener(this);
        tv_my_cooperate_wait.setOnClickListener(this);
        tv_my_cooperate_cooperate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_activity_titlebar_left_image:{
                finish();
            }
            break;
            case R.id.tv_my_cooperate_wait:{
                tv_my_cooperate_wait.setTextColor(getResources().getColor(R.color.ColorD));
                tv_my_cooperate_cooperate.setTextColor(getResources().getColor(R.color.gray_10));
                transaction = fragmentManager.beginTransaction();
                for (Fragment fragment : fragments) {
                    transaction.hide(fragment);
                }
                transaction.show(fragments.get(0)).commit();
            }
            break;
            case R.id.tv_my_cooperate_cooperate:{
                tv_my_cooperate_cooperate.setTextColor(getResources().getColor(R.color.ColorD));
                tv_my_cooperate_wait.setTextColor(getResources().getColor(R.color.gray_10));
                transaction = fragmentManager.beginTransaction();
                for (Fragment fragment : fragments) {
                    transaction.hide(fragment);
                }
                transaction.show(fragments.get(1)).commit();
            }
            break;
        }
    }

}
