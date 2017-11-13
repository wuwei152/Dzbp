package com.md.dzbp.tcp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.constants.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tcp服务
 */
public class TcpService extends Service {

    private Handler handler = new Handler(Looper.getMainLooper());
    private ServerManager mManager;
    private Logger logger;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mManager = ServerManager.getInstance(this);
        EventBus.getDefault().register(this);
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent!=null&&intent.hasExtra("Num")) {
            String num = intent.getStringExtra("Num");
            int Act = intent.getIntExtra("Act",0);
            String ext = intent.getStringExtra("ext");
            LogUtils.d("onStartCommand:----"+num+"/"+Act+"/"+ext);
            mManager.sendCardNum(num,Act,ext);
        }else if (intent!=null&&intent.hasExtra("test")){
            mManager.test();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void showToast(String str) {
        Toast.makeText(TcpService.this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 发送消息
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND) //在ui线程执行
    public void onDataSynEvent(VoiceSendMessage event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MsgType",event.getMsgType());
        jsonObject.put("ToUserName",event.getToUserName());
        jsonObject.put("FromUserName",event.getFromUserName());
        jsonObject.put("CreateTime",event.getCreateTime());
        jsonObject.put("VoicePath",event.getVoicePath());

        logger.debug("TcpService开始发送消息-->{}",jsonObject.toJSONString());
        mManager.messageHandle.uploadFile(event.getMsgType(),event.getVoiceLocalPath(),Constant.Ftp_Voice,0xA601,jsonObject.toJSONString());
    }
}
