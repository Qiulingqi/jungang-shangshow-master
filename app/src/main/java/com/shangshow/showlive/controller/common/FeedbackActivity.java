package com.shangshow.showlive.controller.common;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.common.widget.custom.ClearableEditTextWithIcon;
import com.shangshow.showlive.common.widget.dialog.CustomDialogHelper;


/**
 * 提交反馈
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener {
    private ClearableEditTextWithIcon feedback_Remark;//用户反馈
    private BaseButton feedback_button;//提交资料
    private RelativeLayout yijianfanhuifubuju;

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        setSwipeBackEnable(true);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        titleBarView.initCenterTitle(R.string.feedback);
        feedback_Remark = (ClearableEditTextWithIcon) findViewById(R.id.feedback_Remark);
        feedback_button = (BaseButton) findViewById(R.id.feedback_button);
        yijianfanhuifubuju = (RelativeLayout) findViewById(R.id.yijianfanhuifubuju);
    }


    @Override
    protected void bindEven() {
        feedback_button.setEnabled(false);
        feedback_button.setOnClickListener(this);
        feedback_Remark.addTextChangedListener(watcher);
        yijianfanhuifubuju.setOnClickListener(this);
    }

    @Override
    protected void setView() {

    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (feedback_Remark.getText().length() > 0) {
                feedback_button.setEnabled(true);
            } else {
                feedback_button.setEnabled(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_button:
                FeedBackSubmit();
                break;
           case  R.id.yijianfanhuifubuju:

               hideInput(getBaseContext(),yijianfanhuifubuju);
            break;
            default:
                break;
        }
    }

    private void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    // 其他位置 自动隐藏软键盘




    private void FeedBackSubmit() {

        CustomDialogHelper.OneButtonDialog(this, getString(R.string.prompt), getString(R.string.dialog_feekback_message), getString(R.string.close), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
