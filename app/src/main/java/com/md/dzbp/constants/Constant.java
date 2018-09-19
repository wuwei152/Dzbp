package com.md.dzbp.constants;

import android.content.Context;
import android.text.TextUtils;

import com.md.dzbp.data.FtpParams;
import com.md.dzbp.utils.ACache;

/**
 * 设备参数
 * Created by Administrator on 2017/8/22.
 */
public class Constant {
    /**
     * 服务器地址
     */
//    public static String IP = "119.97.137.22";//武汉
//    public static String IP = "119.97.137.24";
//    public static String IP = "192.168.0.52";

    public static String IP = "119.97.137.24";//宜昌

    /**
     * API地址
     */
//    public static final String BASE_HOST = "http://www.cnzhxy.com/";//武汉
    public static final String BASE_HOST = "http://yc.cnzhxy.com/";//宜昌
//    public static final String BASE_HOST = "http://test.cnzhxy.com/";
//    public static final String BASE_HOST = "http://192.168.0.237:9999/";

    public static int PORT = 9898;
    public static String FtpHostName = "119.97.137.24";
    public static int FtpPort = 21;
//    public static String FtpUserName = "ftp-device";
//    public static String FtpPsw = "1234";

    /**
     * 宜昌
     */
    public static String FtpUserName = "ftp-zhxy";
    public static String FtpPsw = "Hxy_2017";


    //设备参数
//    public static String DeviceId = "e9d50961-3a47-460f-bfc9-1398e3b64c4e";

    //存储路径
    public static String Ftp_Log = "/Log/";
    public static String Ftp_Voice = "/Voice";
    public static String Ftp_Video = "/Video";
    public static String Ftp_Image = "/Image";
    public static String Ftp_Screenshot = "/Screenshot";
    public static String Ftp_Broadcast = "/Broadcast";
    public static String Ftp_Upgrade = "/Upgrade/";
    public static String Ftp_Snapshot = "/Snapshot/";
    public static String Ftp_Camera = "/ScreenshotForCamera/";


    public static int SCREENTYPE = 0;


    /**
     * 获取存储服务器地址
     */
    public static String getUrl(Context context, String url) {
//        ACache mAcache = ACache.get(context);
//        FtpParams params = (FtpParams) mAcache.getAsObject("FtpParams");
//        if (params != null && TextUtils.isEmpty(params.getWebApi())) {
//            return params.getWebApi() + url;
//        } else {
        return BASE_HOST + url;
//        }
    }

    /**
     * 获取存储设备号
     */
    public static String getDeviceId(Context context) {
        ACache mAcache = ACache.get(context);
        String mDeviceId = mAcache.getAsString("DeviceId");
        if (TextUtils.isEmpty(mDeviceId)) {
            return "";
        } else {
            return mDeviceId;
        }
    }

}
