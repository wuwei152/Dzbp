package com.md.dzbp.tcp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.ProcessService;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.data.TextSendMessage;
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
    private LocalBinder binder;
    private LocalConn conn;

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        binder = new LocalBinder();
        if (conn == null) {
            conn = new LocalConn();
        }
        mManager = ServerManager.getInstance(this);
        EventBus.getDefault().register(this);
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(TcpService.this, RemoteService.class),
                conn,
                Context.BIND_IMPORTANT);
        if (intent != null && intent.hasExtra("Num")) {
            String num = intent.getStringExtra("Num");
            int Act = intent.getIntExtra("Act", 0);
            String ext = intent.getStringExtra("ext");
            LogUtils.d("onStartCommand:----" + num + "/" + Act + "/" + ext);
            mManager.sendCardNum(num, Act, ext);
        } else if (intent != null && intent.hasExtra("test")) {
//            mManager.test();
//            mManager.messageHandle.TakeSnap();
        } else if (intent != null && intent.hasExtra("Log")) {
//            mManager.messageHandle.GetLog(0);
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

    class LocalBinder extends ProcessService.Stub {
        @Override
        public String getServiceName() throws RemoteException {
            return "LocalService";
        }
    }


    /**
     * 绑定连接需要ServiceConnection
     */
    class LocalConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d("Local连接远程服务成功 --------");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //远程服务被干掉；连接断掉的时候走此回调
            //在连接RemoateService异常断时，会回调；也就是RemoteException
            LogUtils.d("RemoteService killed--------");
            startService(new Intent(TcpService.this, RemoteService.class));
            //绑定远程服务
            bindService(new Intent(TcpService.this, RemoteService.class),
                    conn, Context.BIND_IMPORTANT);
        }
    }

    /**
     * 发送语音消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND) //在ui线程执行
    public void onDataSynEvent(VoiceSendMessage event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MsgType", event.getMsgType());
        jsonObject.put("ToUserName", event.getToUserName());
        jsonObject.put("FromUserName", event.getFromUserName());
        jsonObject.put("CreateTime", event.getCreateTime());
        jsonObject.put("VoicePath", event.getVoicePath());

        logger.debug("TcpService开始发送语音消息-->{}", jsonObject.toJSONString());
        mManager.messageHandle.sendVoiceMsg(event.getMsgType(), event.getVoiceLocalPath(), Constant.Ftp_Voice, 0xA601, jsonObject.toJSONString());
    }

    /**
     * 发送文字消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND) //在ui线程执行
    public void onTextInputEvent(TextSendMessage event) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MsgType", event.getMsgType());
        jsonObject.put("ToUserName", event.getToUserName());
        jsonObject.put("FromUserName", event.getFromUserName());
        jsonObject.put("CreateTime", event.getCreateTime());
        jsonObject.put("Content", event.getContent());

        logger.debug("TcpService开始发送文字消息-->{}", jsonObject.toJSONString());
        mManager.messageHandle.sendTextMsg(event.getMsgType(), 0xA601, jsonObject.toJSONString());
    }

    /**
     * 发送截屏回复
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.BACKGROUND) //在ui线程执行
    public void onScreenshotEvent(ScreenShotEvent event) {
        if (!event.isSend()) {
            if (event.isSuccess()) {
                mManager.messageHandle.msgHandleUtil.yingda(0xA505, true, event.getDeviceId(), event.getData(), event.getMsgid());
            } else {
                mManager.messageHandle.msgHandleUtil.yingda(0xA505, false, event.getDeviceId(), event.getMsgid());
            }
        }
    }
}
