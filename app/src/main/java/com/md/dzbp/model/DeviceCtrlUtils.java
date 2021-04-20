package com.md.dzbp.model;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.text.TextUtils;

import com.md.dzbp.utils.ACache;

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
        } else {
            type = 0;
            smdt = SmdtManager.create(context);
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
    private void SetStatusBar(boolean c) {
        smdt.smdtSetStatusBar(context, c);
    }

    /**
     * @description 定时开关机
     */
    private void SetTimingSwitchMachine(String guanji, String kaiji) {
        smdt.smdtSetTimingSwitchMachine(guanji, kaiji, "1");
    }
    /**
     * @description 关机
     */
    private void shutDown() {
        smdt.shutDown();
    }
    /**
     * @description 重启
     */
    private void Reboot() {
        smdt.smdtReboot("reboot");
    }
    /**
     * @description 背光开关
     */
    private void SetLcdBackLight(int k) {
        smdt.smdtSetLcdBackLight(k);
    }
    /**
     * @description 亮度调节
     */
    private void setBrightness(int r) {
        smdt.setBrightness(context.getContentResolver(), r);
    }
    /**
     * @description 声音调节
     */
    private void SetVolume(int voice) {
        smdt.smdtSetVolume(context, voice);
    }
    /**
     * @description 设置以太网IP
     */
    private void SetEthIPAddress(String ip,String mask,String gateway,String dns) {
        smdt.smdtSetEthIPAddress(ip, mask, gateway, dns);
    }
    /**
     * @description 设置系统时间
     */
    private void setTime(Calendar calendar,String ip,String mask,String gateway,String dns) {
        smdt.setTime(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }
    /**
     * @description 获取目前设备的型号。
     */
    private String getAndroidModel() {
        return smdt.getAndroidModel();
    }
    /**
     * @description 固件系统版本和编译日期。
     */
    private String getAndroidDisplay() {
        return smdt.getAndroidDisplay();
    }
    /**
     * @description 获取目前API的平台-版本-日期信息。
     */
    private String smdtGetAPIVersion() {
        return smdt.smdtGetAPIVersion();
    }
    /**
     * @description 获获取目前设备的android系统的版本
     */
    private String getAndroidVersion() {
        return smdt.getAndroidVersion();
    }
    /**
     * @description 设备的固件SDK版本
     */
    private String getFirmwareVersion() {
        return smdt.getFirmwareVersion();
    }
    /**
     * @description 设备的固件内核版本
     */
    private String getFormattedKernelVersion() {
        return smdt.getFormattedKernelVersion();
    }
    /**
     * @description 获取设备的硬件内存大小容量
     */
    private String getRunningMemory() {
        return smdt.getRunningMemory();
    }
    /**
     * @description 获取设备的硬件内部存储大小容量
     */
    private String getInternalStorageMemory() {
        return smdt.getInternalStorageMemory();
    }
    /**
     * @description 1开屏  0关屏
     */
    private int GetLcdLightStatus() {
        return smdt.smdtGetLcdLightStatus();
    }
    /**
     * @description 亮度大小
     */
    private int getScreenBrightness() {
        return smdt.getScreenBrightness(context);
    }
    /**
     * @description 声音大小
     */
    private int GetVolume() {
        return smdt.smdtGetVolume(context);//声音大小
    }

}
