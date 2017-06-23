package com.shangshow.showlive.base.crash;

import android.content.Context;

import com.pgyersdk.crash.PgyCrashManager;
import com.shangshow.showlive.network.http.subscriber.ApiException;
import com.shaojun.utils.log.Logger;

import java.lang.Thread.UncaughtExceptionHandler;

public class AppCrashHandler {

    private Context context;

    private UncaughtExceptionHandler uncaughtExceptionHandler;

    private static AppCrashHandler instance;

    private AppCrashHandler(final Context context) {
        this.context = context;

        // get default
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable ex) {
                Logger.e(ex, ex.getMessage());
                //Throwable不知道怎么转成Exception
                ApiException apiException = new ApiException(10000000, ex.getMessage());
                //蒲公英上报crash异常信息
                PgyCrashManager.reportCaughtException(context, apiException);
                // save log
                saveException(ex, true);
                // uncaught
                uncaughtExceptionHandler.uncaughtException(thread, ex);
            }
        });
    }

    public static AppCrashHandler getInstance(Context mContext) {
        if (instance == null) {
            instance = new AppCrashHandler(mContext);
            //蒲公英
            PgyCrashManager.register(mContext);
        }

        return instance;
    }

    public final void saveException(Throwable ex, boolean uncaught) {
        CrashSaver.save(context, ex, uncaught);
    }

    public void setUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        if (handler != null) {
            this.uncaughtExceptionHandler = handler;
        }
    }
}
