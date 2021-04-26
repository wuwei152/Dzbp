package com.md.dzbp.Base;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.apkfuns.logutils.LogLevel;
import com.apkfuns.logutils.LogUtils;
import com.company.NetSDK.INetSDK;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.md.dzbp.model.NetSDKLib;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.serial.SerialControl;
import com.md.dzbp.ui.activity.MainActivity;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.CrashHandler;
import com.md.dzbp.utils.FileUtils;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger;
    private String cureentDate;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initLogs();
        mAcache = ACache.get(this);
        logger = LoggerFactory.getLogger(getClass());
        FlowManager.init(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);

        Fresco.initialize(this);
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder();
        OkHttpFinal.getInstance().init(builder.build());

        NetSDKLib.getInstance().init(this);

        try {
            deleteCache();
        } catch (Exception e) {
            LogUtils.e(e.getMessage());
            logger.error("ClientApp", e.getMessage());
        }

//        mAcache.put("DeviceId","ab0f2b4e-9c71-45fe-a717-efcd59223cd9");
//        mAcache.put("DeviceId","b01efd04-048f-47c7-97f2-4fc58d313b1a");
//        mAcache.put("CameraType", "2");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        boolean retLogout = INetSDK.Logout(NetSDKLib.loginSessionId);
        if (retLogout) {
            NetSDKLib.loginSessionId = 0;
            logger.debug("摄像头退出登录！");
        } else {
            logger.debug("摄像头退出失败！");
        }
    }

    public static ClientApp getInstance() {
        return instance;
    }


    private void initLogs() {
        LogUtils.getLogConfig()
                .configAllowLog(false)
                .configTagPrefix("MyApp")
                .configShowBorders(true)
                .configLevel(LogLevel.TYPE_VERBOSE);
    }

    /**
     * 每登录固定次数后删除缓存
     */
    private void deleteCache() {
        cureentDate = TimeUtils.getCurrentDate2();
        Object num = mAcache.getAsObject("NumCount");
        int numCount = 0;
        if (num != null) {
            numCount = (int) num;
            LogUtils.d("登录次数：" + numCount);
            logger.debug("ClientApp", "登录次数：" + numCount);
        }
        if (numCount <= 50) {
            numCount++;
            mAcache.put("NumCount", numCount);
        } else {
            numCount = 0;
            mAcache.put("NumCount", numCount);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    logger.debug("ClientApp", "开始删除缓存！");
                    ArrayList<File> files = new ArrayList<>();
                    File file1 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "Log");
                    File file2 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "receiveVoice");
                    File file3 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "SendVoiceCache");
                    File file4 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "Apk");
                    File file5 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "BroadCast");
                    File file6 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "Screenshot");
                    File file7 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "sign");
//                    File file8 = new File(FileUtils.getDiskCacheDir(ClientApp.this) + "sign_compress");//离线图片不能删除
                    files.add(file1);
                    files.add(file2);
                    files.add(file3);
                    files.add(file4);
                    files.add(file5);
                    files.add(file6);
                    files.add(file7);
//                    files.add(file8);
                    for (int j = 0; j < files.size(); j++) {
                        if (files.get(j).exists()) {
                            File[] files2 = files.get(j).listFiles();
                            if (files2 != null && files2.length > 0) {
                                for (int i = 0; i < files2.length; i++) {
                                    if (files2[i].exists()) {
                                        if (j == 0) {//保留最近的Log文件
                                            String name = files2[i].getName();
                                            name = name.substring(0, name.indexOf("l") - 1);
                                            if (!cureentDate.equals(name)) {
                                                files2[i].delete();
                                            }
                                        } else {
                                            files2[i].delete();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }).start();
        }
    }

    /**
     * 分割 Dex 支持
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
