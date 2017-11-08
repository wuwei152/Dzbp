package com.md.dzbp.utils;

/**
 * Created by Administrator on 2017/8/23.
 */

import java.io.File;

import org.apache.log4j.Level;

import android.content.Context;

import com.md.dzbp.constants.Constant;
import com.md.dzbp.model.TimeUtils;

import de.mindpipe.android.logging.log4j.LogConfigurator;

public class Log4jConfigure {
    private static final int MAX_FILE_SIZE = 1024 * 1024 * 5;
    private static final String DEFAULT_LOG_FILE_NAME = "DZBP.log";
    private static final String TAG = "Log4jConfigure";
    private static Context context;
    // 对应AndroidManifest文件中的package

    public static void configure(String fileName) {
        final LogConfigurator logConfigurator = new LogConfigurator();
        try {
//            if (isSdcardMounted()) {
//                logConfigurator.setFileName(Environment.getExternalStorageDirectory()
//                        + DEFAULT_LOG_DIR + fileName);
//            } else {
            logConfigurator.setFileName(FileUtils.getDiskCacheDir(context) + "Log"
                    + File.separator + TimeUtils.currentTimelog() + "_" + Constant.getDeviceId(context) + "_" + fileName);
//            }
            //以下设置是按指定大小来生成新的文件

            logConfigurator.setMaxBackupSize(4);
            logConfigurator.setMaxFileSize(MAX_FILE_SIZE);


            //以下设置是按天生成新的日志文件,与以上两句互斥,MAX_FILE_SIZE将不在起作用
            //文件名形如 MyApp.log.2016-06-02,MyApp.log.2016-06-03
//            logConfigurator.setUseDailyRollingFileAppender(true);
//            logConfigurator.setUseFileAppender(true);
            //以下为通用配置
            logConfigurator.setImmediateFlush(true);
            logConfigurator.setRootLevel(Level.ALL);
//            logConfigurator.setFilePattern("%d\t%p/%c:\t%m%n");
            logConfigurator.setFilePattern("%d %-5p [%c{2}]-[%L] %m%n");
            logConfigurator.configure();
            android.util.Log.e(TAG, "Log4j config finish");
        } catch (Throwable throwable) {
            logConfigurator.setResetConfiguration(true);
            android.util.Log.e(TAG, "Log4j config error, use default config. Error:" + throwable);
        }
    }

    public static void configure(Context mcontext) {
        context = mcontext;
        configure(DEFAULT_LOG_FILE_NAME);
    }

//    private static boolean isSdcardMounted() {
//        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
//    }
}
