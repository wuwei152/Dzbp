package com.md.dzbp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/12/6.
 */
public class FileUtils {

    /**
     * 获取缓存目录
     *
     * @param context
     * @return
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath()+ File.separator;///sdcard/Android/data/<application package>/cache
        } else {
            cachePath = context.getCacheDir().getPath()+ File.separator;///data/data/<application package>/cache
        }
        return cachePath;
    }
}
