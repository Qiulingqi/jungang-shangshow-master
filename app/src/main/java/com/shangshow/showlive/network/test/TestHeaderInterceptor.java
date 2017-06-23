package com.shangshow.showlive.network.test;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * header拦截器{处理token}
 */
public class TestHeaderInterceptor implements Interceptor {
    public TestHeaderInterceptor() {
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request newRequest;

//        String userToken = TenementConfig.getInstance().getUserToken();
//        Head head = new Head();
//        head.applicationCode = TenementServiceMediator.APP_CODE;
//        head.sourceId = TenementApplication.getInstance().getCurrentChannel() != null && TenementApplication.getInstance().getCurrentChannel().length() > 0 ? TenementApplication.getInstance().getCurrentChannel() : "Cjia";
//        head.version = DeviceUtil.getAppVersionName(TenementApplication.getContext());
//        head.clientId = PersistenceUtil.getObjectFromSharePreferences(TenementServiceMediator.CLIENT_ID, String.class);

//        String userToken = "";
//        Head head = new Head();
//        head.applicationCode = "Tenement-Android";
//        head.sourceId = "Cjia";
//        head.version = "1.7";
//        head.clientId = "1597289f46684ee2a30b9a6f247665a8";

        //这里使用StrUtils，不使用单元测试不能使用TextUtils，坑爹
//        if (StrUtils.StringIsEmpty(userToken)) {
//            newRequest = chain.request().newBuilder()
//                    .addHeader("header", new Gson().toJson(head))
//                    .build();
//            System.out.println("header==>" + new Gson().toJson(head));
//        } else {
//            newRequest = chain.request().newBuilder()
//                    .addHeader("header", new Gson().toJson(head))
//                    .addHeader("userToken", userToken)
//                    .build();
//            System.out.println("header==>" + new Gson().toJson(head));
//            System.out.println("userToken==>" + userToken);
//        }

        newRequest = chain.request().newBuilder()
                .build();
        okhttp3.Response response = chain.proceed(newRequest);
//        String resultCode = response.header("resultCode");
//        if (!StrUtils.StringIsEmpty(resultCode) && !resultCode.equals(MConstants.SUCCESS_CODE)) {
//            String responseBodyStr = response.body().string();
//            System.out.println("resultCode==>" + resultCode);
//            System.out.println("response==>" + responseBodyStr);
//            ErrorResponse errResponse = new Gson().fromJson(responseBodyStr, ErrorResponse.class);
//            String result = errResponse.result;
//            throw new ResultException(resultCode, result);
//        }
        return response;

    }
}