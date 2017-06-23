package com.shangshow.showlive.controller.personal.pay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.pingplusplus.android.Pingpp;
import com.shangshow.showlive.R;
import com.shangshow.showlive.base.BaseActivity;
import com.shangshow.showlive.common.utils.HttpUtil;
import com.shangshow.showlive.common.utils.ToastUtils;
import com.shangshow.showlive.common.widget.custom.BaseButton;
import com.shangshow.showlive.model.UserModel;
import com.shangshow.showlive.model.callback.Callback;
import com.shangshow.showlive.network.service.models.body.PayOrderDto;
import com.shangshow.showlive.network.service.models.body.PayRecBody;
import com.shangshow.showlive.network.service.models.responseBody.BuyProductResponse;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 支付订单界面
 */
public class PayOrderActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    private View ll_pay_order_zhifubao;
    private View ll_pay_order_weixin;
    TextView tv_pay_order_acount;
    TextView tv_pay_order_sum;
    ImageView iv_pay_order_zhifubao;
    ImageView iv_pay_order_weixin;
    private BaseButton bb_pay_order_pay;
    private int check = 1;
    private BuyProductResponse buyProductResponse;
    public static final String PAYORDER_BUYPRODUCT = "payorder_buyproduct";
    private UserModel userModel;
    private String orderId;

    private static String YOUR_URL = "http://120.55.82.96:8082/payment/crash/0";
    public static final String CHARGE_URL = YOUR_URL;
    private String orderNo;
    private String amount;


    @Override
    protected int getActivityLayout() {
        return R.layout.activity_pay_order;
    }

    @Override
    protected void setContentViewOption(int resId) {
        super.setContentViewOption(resId);
        buyProductResponse = (BuyProductResponse) getIntent().getSerializableExtra(PAYORDER_BUYPRODUCT);
    }

    @Override
    protected void bindEven() {
        ll_pay_order_zhifubao.setOnClickListener(this);
        ll_pay_order_weixin.setOnClickListener(this);
        bb_pay_order_pay.setOnClickListener(this);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        userModel = new UserModel(this);
        titleBarView.initCenterTitle(getString(R.string.person_pay_order));
        ll_pay_order_zhifubao = findViewById(R.id.ll_pay_order_zhifubao);
        ll_pay_order_weixin = findViewById(R.id.ll_pay_order_weixin);
        tv_pay_order_acount = (TextView) findViewById(R.id.tv_pay_order_acount);
        tv_pay_order_sum = (TextView) findViewById(R.id.tv_pay_order_sum);
        iv_pay_order_zhifubao = (ImageView) findViewById(R.id.iv_pay_order_zhifubao);
        iv_pay_order_weixin = (ImageView) findViewById(R.id.iv_pay_order_weixin);
        bb_pay_order_pay = (BaseButton) findViewById(R.id.bb_pay_order_pay);
        orderNo = getIntent().getStringExtra("orderNo");
        amount = getIntent().getStringExtra("jiushiwo");
        orderId = getIntent().getStringExtra("orderId");
        tv_pay_order_sum.setText("￥" + amount + "");
        tv_pay_order_acount.setText(orderNo);

    }

    @Override
    protected void setView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_pay_order_zhifubao: {
                iv_pay_order_zhifubao.setBackgroundResource(R.drawable.xuanzhong);
                iv_pay_order_weixin.setBackgroundResource(R.drawable.weixuanzhong);
                check = 1;
            }
            break;
            case R.id.ll_pay_order_weixin: {
                iv_pay_order_zhifubao.setBackgroundResource(R.drawable.weixuanzhong);
                iv_pay_order_weixin.setBackgroundResource(R.drawable.xuanzhong);
                check = 2;
            }
            break;
            case R.id.bb_pay_order_pay: {
                String ipAddress = HttpUtil.getIPAddress(getBaseContext());
                //   new PaymentTask().execute(new PaymentRequest(orderId,ipAddress,CHANNEL_WECHAT, Integer.parseInt(amount)));
                PayOrderDto payOrderDto = new PayOrderDto();
                payOrderDto.clientIp = ipAddress;
                payOrderDto.orderId = orderId;
                if(check==1){
                    payOrderDto.paymentWay=CHANNEL_ALIPAY;
                }else{
                    payOrderDto.paymentWay = CHANNEL_WECHAT;
                }

                userModel.getPayStringBody(payOrderDto, new Callback<PayRecBody>() {
                    @Override
                    public void onSuccess(PayRecBody s) {
                        Gson gson = new Gson();
                        String json = gson.toJson(s);
                        Pingpp.createPayment(PayOrderActivity.this, json);
                    }
                    @Override
                    public void onFailure(int resultCode, String message) {
                    }
                });
            }
            break;
        }
    }

    class PaymentTask extends AsyncTask<PaymentRequest, Void, String> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected String doInBackground(PaymentRequest... pr) {
            PaymentRequest paymentRequest = pr[0];
            String data = null;
            try {
                JSONObject object = new JSONObject();
                object.put("paymentWay", paymentRequest.paymentWay);
                object.put("amount", paymentRequest.amount);
                object.put("clientIp", paymentRequest.clientIp);
                object.put("orderId", paymentRequest.orderId);
                String json = object.toString();
                //向Your Ping++ Server SDK请求数据
                data = postJson(CHARGE_URL, json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }
        /**
         * 获得服务端的charge，调用ping++ sdk。
         */
        @Override
        protected void onPostExecute(String data) {
            if (null == data) {
                ToastUtils.show("请求出错请检查URLURL无法获取charge");
                return;
            }
            Log.d("charge", data);
            //除QQ钱包外，其他渠道调起支付方式：
            //参数一：Activity  当前调起支付的Activity
            //参数二：data  获取到的charge或order的JSON字符串
        //    Pingpp.createPayment(PayOrderActivity.this, data);
            Pingpp.createPayment(PayOrderActivity.this,data);
            //QQ钱包调用方式
            //参数一：Activity  当前调起支付的Activity
            //参数二：data  获取到的charge或order的JSON字符串
            //参数三：“qwalletXXXXXXX”需与AndroidManifest.xml中的scheme值一致
            //Pingpp.createPayment(ClientSDKActivity.this, data, "qwalletXXXXXXX");
        }
    }
    /**
     * 获取charge
     *
     * @param urlStr charge_url
     * @param json   获取charge的传参
     * @return charge
     * @throws IOException
     */
    private static String postJson(String urlStr, String json) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(8000);
        conn.setReadTimeout(8000);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.getOutputStream().write(json.getBytes());
        if (conn.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
        return null;
    }
    class PaymentRequest {
        String orderId;
        String clientIp;
        String paymentWay;
        int amount;
        public PaymentRequest(String orderId, String clientIp, String paymentWay, int amount) {
            this.orderId = orderId;
            this.clientIp = clientIp;
            this.paymentWay = paymentWay;
            this.amount = amount;
        }
    }
    /**
     * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。
     * 最终支付成功根据异步通知为准
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                /* 处理返回值
                 * "success" - payment succeed
                 * "fail"    - payment failed
                 * "cancel"  - user canceld
                 * "invalid" - payment plugin not installed
                 */
                String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                showMsg(result, errorMsg, extraMsg);
            }
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null != msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null != msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(PayOrderActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }
}
