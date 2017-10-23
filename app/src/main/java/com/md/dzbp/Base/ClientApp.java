package com.md.dzbp.Base;

import android.app.Application;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.md.dzbp.utils.Log4jConfigure;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * @ClassName: ClientApp
 * @Description: [BaseApp]
 * @Author: wuw
 */
public class ClientApp extends Application {
    private static ClientApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLogs();
        Fresco.initialize(this);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());

        //注册错误处理保存本地
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        Log4jConfigure.configure(this);
    }

    public static ClientApp getInstance() {
        return instance;
    }


    private void initLogs() {
        LogUtils.getLogConfig()
                .configAllowLog(true)
                .configTagPrefix("MyApp")
                .configShowBorders(true)
                .configLevel(LogLevel.TYPE_VERBOSE);
    }
}
