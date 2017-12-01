package com.md.dzbp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;

import org.slf4j.LoggerFactory;

/**
 * Created by Administrator on 2017/11/27.
 * 监听开关机广播，统计流量数据
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    private String TAG = "BroadcastReceiver-->{}";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {//开机广播
            LoggerFactory.getLogger(BroadcastReceiver.class).debug(TAG, "设备开机");

        } else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {//关机广播

            try {
                long totalTxBytes = TrafficStats.getTotalTxBytes();//手机全部网络接口 包括wifi，3g、2g上传的总流量
                long totalRxBytes = TrafficStats.getTotalRxBytes();//手机全部网络接口 包括wifi，3g、2g下载的总流量
                long totleMb = (totalTxBytes + totalRxBytes) / 1048576;
                LoggerFactory.getLogger(BroadcastReceiver.class).debug(TAG, "设备关机，统计流量,今日消耗：" + totleMb + "MB流量");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}