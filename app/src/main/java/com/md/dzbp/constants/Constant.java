package com.md.dzbp.constants;

import android.content.Context;
import android.text.TextUtils;

import com.md.dzbp.utils.ACache;

import java.util.ArrayList;

/**
 * 设备参数
 * Created by Administrator on 2017/8/22.
 */
public class Constant {
    /**
     * 服务器地址
     */

//    public static String IP = "119.97.137.24";
//    public static String IP = "192.168.0.52";
//    public static String IP = "192.168.0.102";

    /**
     * 宜昌
     */
//    public static String IP = "192.168.0.102";
//    public static final String BASE_HOST = "http://yc.cnzhxy.com/";//宜昌
//    public static String IP = "119.97.137.24";//宜昌
    public static String IP = "119.96.217.111";//宜昌
    public static int PORT = 4000;
    public static final String BASE_HOST = "http://www.jiaxiaoyj.com/";//宜昌

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

    /**
     * 获取管理卡号
     */
    public static ArrayList<String> getAdminNum() {
        ArrayList<String> numList = new ArrayList<String>();
        numList.clear();
        numList.add("1553792053");
        numList.add("1560283157");
        numList.add("1560471029");
        numList.add("1561044917");
        numList.add("1555720933");
        numList.add("1557423733");
        numList.add("1555820501");
        numList.add("1555819413");
        numList.add("1555820549");
        numList.add("2773717515");
        numList.add("1558169957");


        numList.add("1857847019");
        numList.add("1802319637");
        numList.add("0020519250");
        numList.add("1953760908");
        numList.add("2108493996");
        numList.add("1953141260");
        numList.add("2547022701");
        numList.add("0242101132");
        numList.add("4269084850");
        numList.add("0196709188");


        numList.add("3533746030");//公交卡


        return numList;
    }

}
