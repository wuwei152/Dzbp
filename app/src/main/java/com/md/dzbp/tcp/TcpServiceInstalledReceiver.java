package com.md.dzbp.tcp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.md.dzbp.ui.activity.MainActivity;
import com.md.dzbp.utils.Log4j;

/**
 * Created by Administrator on 2017/8/22.
 */
public class TcpServiceInstalledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log4j.d("TcpServiceInstalledReceiver","维护服务广播");
//        if (intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY")) {
            /* 服务开机自启动 */
        Intent service = new Intent(context, TcpService.class);
        context.startService(service);
//        }
//        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                        /* 应用开机自启动 */
        Intent intent_n = new Intent(context,
                MainActivity.class);
        intent_n.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent_n);
//        }
    }
}
