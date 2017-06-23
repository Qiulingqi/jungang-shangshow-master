package com.shangshow.showlive.common.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shangshow.showlive.R;

public class CustomToast extends AppCompatActivity {

    /** Called when the activity is first created. */
    private Button btnTest;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heheheheheh);
        showCustomToast();
    }

    /*
     * 从布局文件中加载布局并且自定义显示Toast
     */
    private void showCustomToast(){
        //获取LayoutInflater对象，该对象可以将布局文件转换成与之一致的view对象
        LayoutInflater inflater=getLayoutInflater();
        //将布局文件转换成相应的View对象
        View layout=inflater.inflate(R.layout.activity_custom_toast,(ViewGroup)findViewById(R.id.toast_layout_root));
        //从layout中按照id查找imageView对象
        ImageView imageView=(ImageView)layout.findViewById(R.id.ivForToast);
        //设置ImageView的图片
        imageView.setBackgroundResource(R.mipmap.ic_launcher);
        //从layout中按照id查找TextView对象
        TextView textView=(TextView)layout.findViewById(R.id.tvForToast);
        //设置TextView的text内容
        textView.setText("This is Toast but cannot be eaten and hello world");
        //实例化一个Toast对象
        Toast toast=new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setView(layout);
        toast.show();
    }
}
