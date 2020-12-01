package com.md.dzbp.utils.hcUtils;

import android.content.Context;
import android.widget.Toast;


import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.data.CameraInfo;

import java.util.HashMap;

public class HCSdkManager {

    private static HCSdkManager singleton;

    public static HCSdkManager getInstance() {
        if (singleton == null) {
            synchronized (HCSdkManager.class) {
                if (singleton == null) {
                    singleton = new HCSdkManager();
                }
            }
        }
        return singleton;
    }

    private boolean init;

    private static HashMap<LoginInfo, HCSdk> hcSdks = new HashMap<>();

    private static HCSdk getHCSdk(Context context, LoginInfo loginInfo) {
        HCSdk hcSdk = hcSdks.get(loginInfo);
        if (hcSdk == null) {
            hcSdk = HCSdk.getInstance(context, loginInfo);
            hcSdks.put(loginInfo, hcSdk);
        }
        return hcSdk;
    }

    public static HCSdk getNormalHCSdk(Context context, CameraInfo cameraInfo) {
        return getHCSdk(context, new LoginInfo(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw()));
    }


    public HCSdk initAndLogin(Context context, CameraInfo cameraInfo) {

//        if (init) {
//            return null;
//        }
        // 高清摄像头初始化
        HCSdk normalHCSdk = getNormalHCSdk(context, cameraInfo);
        boolean initResult = normalHCSdk.init();
        if (initResult) {
            boolean result = normalHCSdk.login();
            if (!result) {
                Toast.makeText(context, "高清摄像头登录失败", Toast.LENGTH_SHORT).show();
                return normalHCSdk;
            }
        } else {
            Toast.makeText(context, "高清摄像头初始化失败", Toast.LENGTH_SHORT).show();
            return normalHCSdk;
        }


//        init = true;
        return normalHCSdk;
    }
}
