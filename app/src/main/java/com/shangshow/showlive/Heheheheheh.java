package com.shangshow.showlive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;

public class Heheheheheh extends AppCompatActivity {

    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heheheheheh);
        initView();
    }

    private void initView() {
        userModel = new UserModel(this);
        Button qingqiu = (Button) findViewById(R.id.qingqiu);
        qingqiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 * 测试
                 *
                 */
                String dingdanhao = "201703021602264634412";
                userModel.getPaySuccessEnd(dingdanhao, new Callback<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        ToastUtils.show("反馈成功");
                    }

                    @Override
                    public void onFailure(int resultCode, String message) {

                    }
                });
            }
        });


    }
}
