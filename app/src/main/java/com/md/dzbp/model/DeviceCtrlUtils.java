package com.md.dzbp.model;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.text.TextUtils;

import com.md.dzbp.utils.ACache;
import com.ys.rkapi.MyManager;

import java.util.Calendar;

/**
 * @Author: ww
 * @CreateDate: 2021/4/20 9:53
 * @Description:
 */
public class DeviceCtrlUtils {


    private static DeviceCtrlUtils deviceCtrlUtils;
    private ACache mACache;
    private Context context;
    private SmdtManager smdt;
    private MyManager manager;
    private int type;

    private DeviceCtrlUtils(Context context) {
        this.context = context;
        mACache = ACache.get(context);
        String deviceType = mACache.getAsString("DeviceType");
        if (TextUtils.isEmpty(deviceType)) {
            deviceType = "";
        }
        if (deviceType.equals("1")) {
            type = 1;
            manager = MyManager.getInstance(context.getApplicationContext());
        } else if (deviceType.equals("0")){
            type = 0;
            smdt = SmdtManager.create(context.getApplicationContext());
        }else {
            type = 3;
        }
    }

    public static DeviceCtrlUtils getInstance(Context context) {

        if (deviceCtrlUtils == null) {
            synchronized (DeviceCtrlUtils.class) {
                if (deviceCtrlUtils == null) {
                    deviceCtrlUtils = new DeviceCtrlUtils(context);
                }
            }
        }
        return deviceCtrlUtils;
    }

    /**
     * @description 显示隐藏状态栏
     */
    public void SetStatusBar(boolean c) {
        if (type == 0) {
            smdt.smdtSetStatusBar(context, c);
        } else if (type == 1) {
            manager.hideNavBar(c);
        } else {

        }

    }

    /**
     * @description 定时开关机
     */
    public void SetTimingSwitchMachine(String guanji, String kaiji) {
        if (type == 0) {
            smdt.smdtSetTimingSwitchMachine(guanji, kaiji, "1");
        } else if (type == 1) {
            if (TextUtils.isEmpty(kaiji)) {
                return;
            }
            if (TextUtils.isEmpty(guanji)) {
                return;
            }
            int[] timeonArray = new int[]{Integer.parseInt(kaiji.substring(0, kaiji.indexOf(":"))), Integer.parseInt(kaiji.substring(kaiji.indexOf(":") + 1))};
            int[] timeoffArray = new int[]{Integer.parseInt(guanji.substring(0, guanji.indexOf(":"))), Integer.parseInt(guanji.substring(guanji.indexOf(":") + 1))};
            int[] weekdays = new int[] {
                1, 1, 1, 1, 1, 1, 1
            } ;//周一到周日工作状态,1 为开机， 0 为不开机
            manager.setPowerOnOffWithWeekly(timeonArray, timeoffArray, weekdays);
        } else {

        }

    }

    /**
     * @description 关机
     */
    public void shutDown() {

        if (type == 0) {
            smdt.shutDown();
        } else if (type == 1) {
            manager.shutdown();
        } else {

        }
    }

    /**
     * @description 重启
     */
    public void Reboot() {

        if (type == 0) {
            smdt.smdtReboot("reboot");
        } else if (type == 1) {
            manager.reboot();
        } else {

        }
    }

    /**
     * @description 背光开关  1开 0 关
     */
    public void SetLcdBackLight(int k) {

        if (type == 0) {
            smdt.smdtSetLcdBackLight(k);
        } else if (type == 1) {
            if (k == 1) {
                manager.turnOnBackLight();
            } else {
                manager.turnOffBackLight();
            }

        } else {

        }
    }

    /**
     * @description 亮度调节
     */
    public void setBrightness(int r) {

        if (type == 0) {
            smdt.setBrightness(context.getContentResolver(), r);
        } else if (type == 1) {
            manager.changeScreenLight(r);//（1--100）
        } else {

        }
    }

    /**
     * @description 声音调节
     */
    public void SetVolume(int voice) {

        if (type == 0) {
            smdt.smdtSetVolume(context, voice);
        } else if (type == 1) {

        } else {

        }
    }

    /**
     * @description 设置以太网IP
     */
    public void SetEthIPAddress(String ip, String mask, String gateway, String dns) {

        if (type == 0) {
            smdt.smdtSetEthIPAddress(ip, mask, gateway, dns);
        } else if (type == 1) {

        } else {

        }
    }

    /**
     * @description 设置系统时间
     */
    public void setTime(Calendar calendar) {
        if (type == 0) {
            smdt.setTime(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));

        } else if (type == 1) {
//            manager.setTime(int year, int month, int day, int hour, int minute)
        } else {

        }
    }

    /**
     * @description 获取目前设备的型号。
     */
    public String getAndroidModel() {

        if (type == 0) {
            return smdt.getAndroidModel();
        } else if (type == 1) {
            return manager.getAndroidModle();
        } else {

        }
        return "";
    }

    /**
     * @description 固件系统版本和编译日期。
     */
    public String getAndroidDisplay() {

        if (type == 0) {
            return smdt.getAndroidDisplay();
        } else if (type == 1) {
            return manager.getAndroidDisplay();
        } else {

        }
        return "";
    }

    /**
     * @description 获取目前API的平台-版本-日期信息。
     */
    public String smdtGetAPIVersion() {
        if (type == 0) {
            return smdt.smdtGetAPIVersion();
        } else if (type == 1) {
            return manager.getApiVersion();
        } else {

        }
        return "";
    }

    /**
     * @description 获获取目前设备的android系统的版本
     */
    public String getAndroidVersion() {

        if (type == 0) {
            return smdt.getAndroidVersion();
        } else if (type == 1) {
            return manager.getAndroidVersion();
        } else {

        }
        return "";
    }

    /**
     * @description 设备的固件SDK版本
     */
    public String getFirmwareVersion() {

        if (type == 0) {
            return smdt.getFirmwareVersion();
        } else if (type == 1) {
            return manager.getFirmwareVersion();
        } else {

        }
        return "";
    }

    /**
     * @description 设备的固件内核版本
     */
    public String getFormattedKernelVersion() {

        if (type == 0) {
            return smdt.getFormattedKernelVersion();
        } else if (type == 1) {
            return manager.getKernelVersion();
        } else {

        }
        return "";
    }

    /**
     * @description 获取设备的硬件内存大小容量
     */
    public String getRunningMemory() {

        if (type == 0) {
            return smdt.getRunningMemory();
        } else if (type == 1) {
            return manager.getRunningMemory();
        } else {

        }
        return "";
    }

    /**
     * @description 获取设备的硬件内部存储大小容量
     */
    public String getInternalStorageMemory() {

        if (type == 0) {
            return smdt.getInternalStorageMemory();
        } else if (type == 1) {
            return manager.getInternalStorageMemory();
        } else {

        }
        return "";
    }

    /**
     * @description 1开屏  0关屏
     */
    public int GetLcdLightStatus() {

        if (type == 0) {
            return smdt.smdtGetLcdLightStatus();
        } else if (type == 1) {
            return manager.isBacklightOn() ? 1 : 0;
        } else {

        }
        return 1;
    }

    /**
     * @description 亮度大小
     */
    public int getScreenBrightness() {


        if (type == 0) {
            return smdt.getScreenBrightness(context);
        } else if (type == 1) {
            return manager.getSystemBrightness();  //0--100
        } else {

        }
        return 0;
    }

    /**
     * @description 声音大小
     */
    public int GetVolume() {

        if (type == 0) {
            return smdt.smdtGetVolume(context);//声音大小
        } else if (type == 1) {

        } else {

        }
        return 0;
    }

}
