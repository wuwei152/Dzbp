package com.md.dzbp.constants;

import android.content.Context;
import android.text.TextUtils;

import com.md.dzbp.utils.ACache;

/**
 * 设备参数
 * Created by Administrator on 2017/8/22.
 */
public class Constant {

    //设备参数
    public static String DeviceId = "e9d50961-3a47-460f-bfc9-1398e3b64c4e";
    public static String IP = "119.97.137.24";
//    public static String IP = "192.168.0.52";
    public static int PORT = 9898;
    public static String FtpHostName = "119.97.137.24";
    public static int FtpPort = 21;
    public static String FtpUserName = "ftp-device";
    public static String FtpPsw = "1234";

    //存储路径
    public static String Ftp_Log = "/Log";
    public static String Ftp_Voice = "/Voice";
    public static String Ftp_Video = "/Video";
    public static String Ftp_Image = "/Image";
    public static String Ftp_Screenshot = "/Screenshot";
    public static String Ftp_Broadcast = "/Broadcast";
    public static String Ftp_Upgrade = "/Upgrade/";
    public static String Ftp_Snapshot = "/Snapshot/";

    /**
     * 服务器地址
     */
    public static final String BASE_HOST = "http://test.cnzhxy.com/";
//    public static final String BASE_HOST = "http://192.168.0.237:9999/";
    /**
     * 获取存储服务器地址
     */
    public static String getUrl(Context context,String url){
        ACache mAcache = ACache.get(context);
        String host = mAcache.getAsString("HOST");
        if (TextUtils.isEmpty(host)){
            return BASE_HOST+url;
        }else {
            return host+url;
        }
    }
    /**
     * 获取存储设备号
     */
    public static String getDeviceId(Context context){
        ACache mAcache = ACache.get(context);
        String mDeviceId = mAcache.getAsString("DeviceId");
        if (TextUtils.isEmpty(mDeviceId)){
            return DeviceId;
        }else {
            return mDeviceId;
        }
    }

}
