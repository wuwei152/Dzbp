package com.md.dzbp.tcp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.Log4j;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ServerManager {

    private static final String TAG = "ServerManager";
    private static ServerManager instance = null;
    private Context context;
    private static int retryTime = 10000;
    private Handler handler = new Handler(Looper.getMainLooper());
    private TcpClient client;
    public final MessageHandle messageHandle;
    private final String Tag = "ServerManager";
    private String deviceId = "";
    private ACache mACache;


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
        deviceId = Constant.getDeviceId(context);
        client = new TcpClient() {

            @Override
            public void onConnect(SocketTransceiver transceiver) {
                Log4j.d(Tag, "Tcp连接成功");
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
                Log4j.d(Tag, "断开连接");
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
                Log4j.d(Tag, "Connect连接失败");
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
    public void test() {
    }

    /**
     * 启动
     */
    public void Start(String hostIP, int port) {
        try {
            Log4j.d(TAG, "开始连接" + hostIP + "/" + port);
            client.connect(hostIP, port);
        } catch (NumberFormatException e) {
            Log4j.d(TAG, "Start连接失败" + e.getMessage());
        }
    }


    /**
     * 登录
     */
    private void login() {
        Log4j.d(TAG, "开始登录");
        try {
            TCPMessage message = new TCPMessage(0xA002);
            message.Write(deviceId, 36);
            message.Write(2);
            client.getTransceiver().send(message);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!messageHandle.IsEnable) {
                        Log4j.e(TAG, "登录无回应，断开重试！");
                        Stop();
                    }
                }
            }, retryTime);
        } catch (Exception e) {
            Log4j.d(TAG, "登录失败" + e.getMessage());
        }
    }

    /**
     * 发送卡号
     */
    public void sendCardNum(String num, int act, String ext) {
        Log4j.d("发送卡号", "是否已登录" + messageHandle.IsEnable);
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
                Log4j.d("发送卡号失败", e.getMessage());
            }
        } else {
            showToast("暂未授权该班牌，请联系系统管理员！");
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

    private void showToast(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
