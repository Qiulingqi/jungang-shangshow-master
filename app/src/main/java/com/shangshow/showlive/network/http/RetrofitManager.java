package com.shangshow.showlive.network.http;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.shangshow.showlive.base.MConstants;
import com.shangshow.showlive.network.service.ApiService;
import com.shangshow.showlive.network.test.TestApiService;
import com.shaojun.utils.log.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * retrofit管理类
 */
public class RetrofitManager {
    private Context mContext;
    private static String API_HOST = MConstants.BASE_URL;// 服务器地址
    private static Retrofit retrofit;
    private static Retrofit testRetrofit;
    private static ApiService apiService;//api
    private static TestApiService testApiService;//测试api

    private static class RetrofitManagerHolder {
        static final RetrofitManager INSTANCE = new RetrofitManager();
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private RetrofitManager() {

    }

    public static RetrofitManager getInstance() {
        return RetrofitManagerHolder.INSTANCE;
    }

    public void init(Context context) {
        this.mContext = context;
        initRetrofit();
    }

    public void initRetrofit() {
        if (mContext == null) {
            Logger.e("mContext is null , you need call init(Context context)!");
            return;
        }
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                Logger.i(message);
//            }
//        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(MConstants.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(MConstants.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor(mContext))
                .addInterceptor(loggingInterceptor)
//                            .addNetworkInterceptor(new MyTokenInterceptor())
//                            .authenticator(new MyAuthenticator())
//                            .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .client(mOkHttpClient)
                .addConverterFactory(com.shangshow.showlive.network.http.converter.GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static void initTestRetrofit() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                System.out.println(message);
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(MConstants.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(MConstants.HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ssZ")
                .create();
        testRetrofit = new Retrofit.Builder()
                .baseUrl(API_HOST)
                .client(mOkHttpClient)
                .addConverterFactory(com.shangshow.showlive.network.http.converter.GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 测试接口服务
     *
     * @return
     */
    public static TestApiService getTestApiService() {
        initTestRetrofit();
        if (testApiService == null) {
            testApiService = testRetrofit.create(TestApiService.class);
        }
        return testApiService;
    }

    /**
     * 商秀接口
     *
     * @return
     */
    public static ApiService getUserService() {
        if (apiService == null) {
            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }

//    /**
//     * 当{@link ApiService}中接口的注解为{@link retrofit2.http.Multipart}时，参数为{@link RequestBody}
//     * 生成对应的RequestBody
//     *
//     * @param param
//     * @return
//     */
//    protected RequestBody createRequestBody(int param) {
//        return RequestBody.create(MediaType.parse("text/plain"), String.valueOf(param));
//    }
//
//    protected RequestBody createRequestBody(long param) {
//        return RequestBody.create(MediaType.parse("text/plain"), String.valueOf(param));
//    }
//
//    protected RequestBody createRequestBody(String param) {
//        return RequestBody.create(MediaType.parse("text/plain"), param);
//    }
//
//    protected RequestBody createRequestBody(File param) {
//        return RequestBody.create(MediaType.parse("image/*"), param);
//    }

//    /**
//     * 已二进制传递图片文件，对图片文件进行了压缩
//     *
//     * @param path 文件路径
//     * @return
//     */
//    protected RequestBody createPictureRequestBody(String path) {
//        Bitmap bitmap = ClippingPicture.decodeResizeBitmapSd(path, 400, 800);
//        return RequestBody.create(MediaType.parse("image/*"), ClippingPicture.bitmapToBytes(bitmap));
//    }


}
