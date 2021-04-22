package com.md.dzbp.tcp;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.model.DeviceCtrlUtils;
import com.md.dzbp.utils.ACache;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ServerManager {

    private static final String TAG = "ServerManager-->{}";
    private static ServerManager instance = null;
    private final Logger logger;
    private Context context;
    private static int retryTime = 20000;
    private Handler handler = new Handler(Looper.getMainLooper());
    private TcpClient client;
    public final MessageHandle messageHandle;
    private final String Tag = "ServerManager-->{}";
    private String deviceId = "";
    private ACache mACache;
    private int mRestart = 0;


    /* 1:懒汉式，静态工程方法，创建实例 */
    public static synchronized ServerManager getInstance(Context context) {
        if (instance == null) {
            instance = new ServerManager(context);
        }
        return instance;
    }

    /* 私有构造方法，防止被实例化 */
    private ServerManager(final Context context) {
        this.context = context;
        mACache = ACache.get(context);
        logger = LoggerFactory.getLogger(context.getClass());
        deviceId = Constant.getDeviceId(context);
        client = new TcpClient() {

            @Override
            public void onConnect(SocketTransceiver transceiver) {
                logger.debug(Tag, "Tcp连接成功");
                mRestart = 0;
                EventBus.getDefault().post(new LoginEvent(0, true, "", ""));
                mACache.put("conStatus", true);
                new Handler(context.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        login();
                    }
                }, 3000);
            }

            @Override
            public void onDisconnect(SocketTransceiver transceiver) {
                logger.debug(Tag, "断开连接");
                EventBus.getDefault().post(new LoginEvent(0, false, "", ""));
                mACache.put("conStatus", false);
                messageHandle.IsEnable = false;
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Start(Constant.IP, Constant.PORT);
                    }
                }, retryTime);
                messageHandle.StopXT();
            }

            @Override
            public void onConnectFailed() {
                logger.error(Tag, "Connect连接失败" + mRestart);
                EventBus.getDefault().post(new LoginEvent(0, false, "", ""));
                mACache.put("conStatus", false);
                if (handler == null) {
                    handler = new Handler(Looper.getMainLooper());
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Start(Constant.IP, Constant.PORT);
                    }
                }, retryTime);
                messageHandle.StopXT();

                mRestart++;
                if (mRestart >= 200 && !isNetworkConnected()) {
                    reboot();
                }
            }

            @Override
            public void onReceive(SocketTransceiver transceiver,
                                  final List<TCPMessage> messageList) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (TCPMessage tcpMessage : messageList) {
                            messageHandle.handleMessage(tcpMessage);
                        }
                    }
                });
            }
        };
        messageHandle = new MessageHandle(context, client);
        //开始连接
        Start(Constant.IP, Constant.PORT);
    }

    /**
     * 测试
     */
    int type = 1;

    public void test() {

//        final Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (type==1){
//                    type =0;
//                }else {
//                    type=1;
//                }
//                messageHandle.gotoActivity(type,"","");
//                handler.postDelayed(this,6000);
//            }
//        },6000);

    }

    public void reboot() {
        DeviceCtrlUtils.getInstance(context).Reboot();
    }

    public boolean isNetworkConnected() {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 启动
     */
    public void Start(String hostIP, int port) {
        try {
            logger.debug(TAG, "开始连接" + hostIP + "/" + port);
            client.connect(hostIP, port);
        } catch (NumberFormatException e) {
            logger.debug(TAG, "Start连接失败" + e.getMessage());
        }
    }


    /**
     * 登录
     */
    private void login() {
        logger.debug(TAG, "开始登录");
        try {
            TCPMessage message = new TCPMessage(0xA002);
            message.Write(deviceId, 36);
            message.Write(2);
            client.getTransceiver().send(message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!messageHandle.IsEnable) {
                        logger.error(TAG, "登录无回应，断开重试！");
                        Stop();
                    }
                }
            }, retryTime);
        } catch (Exception e) {
            logger.debug(TAG, "登录失败" + e.getMessage());
        }
    }

    /**
     * 发送卡号
     */
    public void sendCardNum(String num, int act, String ext) {
        logger.debug("发送卡号" + num, "是否已登录" + messageHandle.IsEnable);
        if (messageHandle.IsEnable) {
            try {
                TCPMessage message = new TCPMessage(0xA550);
                message.Write(deviceId, 36);
                message.Write(act);
                int length = num.getBytes("UTF-8").length;
                message.Write(length);
                message.Write(num, length);
                int length2 = ext.getBytes("UTF-8").length;
                message.Write(length2);
                message.Write(ext, length2);

                client.getTransceiver().send(message);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug("发送卡号失败", e.getMessage());
            }
        } else {
            if (act == 7) {
                showToast("刷卡成功，已存储刷卡信息！");
                play(1);
            } else {
                showToast("网络暂时不通畅，请稍后再试！");
            }
            login();
        }
    }

    /**
     * 停止
     */
    public void Stop() {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }

    private void play(int k) {
        logger.debug(TAG, "播放刷卡提示音");
        SoundPool soundPool;
        soundPool = new SoundPool(21, AudioManager.STREAM_SYSTEM, 10);
        if (k == 1) {
            soundPool.load(context, R.raw.card_sucsess, 1);
        } else {
            soundPool.load(context, R.raw.card_fails, 1);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                logger.debug(TAG, "开始播放！");
                soundPool.play(1, 1, 1, 0, 0, 1.2f);
            }
        });
    }

    private void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
