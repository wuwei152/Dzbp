package com.md.dzbp.Base;

import android.app.Application;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.Log4j;
import com.md.dzbp.utils.Log4jConfigure;

import java.io.File;
import java.util.ArrayList;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

/**
 * @ClassName: ClientApp
 * @Description: [BaseApp]
 * @Author: wuw
 */
public class ClientApp extends Application {
    private static ClientApp instance;
    private ACache mAcache;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLogs();
        mAcache = ACache.get(this);
        Fresco.initialize(this);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());

        //注册错误处理保存本地
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);
        Log4jConfigure.configure(this);

        try {
            deleteCache();
        } catch (Exception e) {
            Log4j.e("ClientApp", e.getMessage());
        }
        //加入初始密码
        mAcache.put("AdminPsw", "1234");
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

    /**
     * 每登录固定次数后删除缓存
     */
    private void deleteCache() {
        Object num = mAcache.getAsObject("NumCount");
        int numCount = 0;
        if (num != null) {
            numCount = (int) num;
            Log4j.d("ClientApp", "登录次数："+numCount);
        }
        if (numCount <= 15) {
            numCount++;
            mAcache.put("NumCount", numCount);
        } else {
            numCount = 0;
            mAcache.put("NumCount", numCount);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log4j.d("ClientApp", "删除缓存！");
                    ArrayList<File> files = new ArrayList<>();
                    File file1 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "Log");
                    File file2 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "receiveVoice");
                    File file3 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "SendVoiceCache");
                    File file4 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "Apk");
                    File file5 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "BroadCast");
                    File file6 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "Screenshot");
                    File file7 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "sign");
                    File file8 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "sign_compress");
                    files.add(file1);
                    files.add(file2);
                    files.add(file3);
                    files.add(file4);
                    files.add(file5);
                    files.add(file6);
                    files.add(file7);
                    files.add(file8);
                    for (File f : files) {
                        if (f.exists()) {
                            File[] files2 = f.listFiles();
                            for (int i = 0; i < files2.length; i++) {
                                files2[i].delete();
                            }
                        }
                    }
                    Log4jConfigure.configure(ClientApp.this);
                }
            }).start();
        }
    }
}
