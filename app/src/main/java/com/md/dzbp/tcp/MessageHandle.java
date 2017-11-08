package com.md.dzbp.tcp;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.FtpParams;
import com.md.dzbp.data.ImageReceiveMessage;
import com.md.dzbp.data.MainUpdateEvent;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.MsgSendStatus;
import com.md.dzbp.data.SignEvent;
import com.md.dzbp.data.TextReceiveMessage;
import com.md.dzbp.data.UpdateDate;
import com.md.dzbp.data.VoiceReceiveMessage;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.ui.activity.ExamActivity;
import com.md.dzbp.ui.activity.MainActivity;
import com.md.dzbp.ui.activity.MeetingActivity;
import com.md.dzbp.ui.activity.NoticeActivity;
import com.md.dzbp.ui.activity.PatrolActivity;
import com.md.dzbp.ui.activity.SignActivity;
import com.md.dzbp.ui.activity.StudentActivity;
import com.md.dzbp.ui.activity.TeacherActivity;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.Log4j;
import com.md.dzbp.utils.luban.Luban;
import com.md.dzbp.utils.luban.OnCompressListener;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import cn.finalteam.toolsfinal.ManifestUtils;

/**
 * 处理接收消息，回应消息
 */
public class MessageHandle {
    private static final String TAG = "MessageHandle";
    private Context context;
    private TcpClient client;
    private Handler x_handler;
    private static int xintiaoTime = 30000;
    private SmdtManager smdtManager;
    public boolean IsEnable = false;
    private final ACache mACache;
    private MediaPlayer mp;
    private String deviceId = "";
    private Runnable x_runnable;
    private int XTfailNum = 0;

    public MessageHandle(Context context, TcpClient client) {
        this.context = context;
        this.client = client;
        smdtManager = SmdtManager.create(context);
        mACache = ACache.get(context);
        deviceId = Constant.getDeviceId(context);
    }

    public void handleMessage(TCPMessage tcpMessage) {
        Log4j.d(TAG, "收到消息>>>>>>>>>>>>>>" + tcpMessage.Type);
        switch (tcpMessage.Type) {
            case 0xA001://通用应答
                Log4j.d(TAG, "0xA001收到通用应答消息");
                break;
            case 0xA002://登录应答，发心跳
                int result1 = tcpMessage.ReadInt();
                if (result1 == 0) {
                    Log4j.d(TAG, "0xA002收到登录授权成功消息");
                    IsEnable = true;
                    xintiao();
                    int length02 = tcpMessage.ReadInt();
                    String ftp_ip = tcpMessage.ReadString(length02);
                    int ftp_port = tcpMessage.ReadInt();
                    int length021 = tcpMessage.ReadInt();
                    String ftp_name = tcpMessage.ReadString(length021);
                    int length022 = tcpMessage.ReadInt();
                    String ftp_psw = tcpMessage.ReadString(length022);
                    int length023 = tcpMessage.ReadInt();
                    String web_api = tcpMessage.ReadString(length023);
                    int length024 = tcpMessage.ReadInt();
                    String psw = tcpMessage.ReadString(length024);

                    int length = tcpMessage.ReadInt();
                    String guanji = tcpMessage.ReadString(length);
                    int length2 = tcpMessage.ReadInt();
                    String kaiji = tcpMessage.ReadString(length2);
                    Log4j.d(TAG, "0xA002设置开关机指令：关" + guanji + "/开" + kaiji);
                    smdtManager.smdtSetTimingSwitchMachine(guanji, kaiji, "1");

                    LogUtils.d(psw);

                    FtpParams params = new FtpParams(ftp_ip, ftp_port, ftp_name, ftp_psw, web_api);
                    mACache.put("FtpParams", params);
                    mACache.put("AdminPsw", psw);
                } else if (result1 == 1) {
                    Log4j.d(TAG, "0xA002收到登录授权失败消息");
                    IsEnable = false;
                } else {
                    Log4j.d(TAG, "0xA002收到登录未知错误消息");
                    IsEnable = false;
                }
                break;
            case 0xA003://心跳应答
                Log4j.d(TAG, "0xA003收到心跳应答消息");
                XTfailNum = 0;
                break;
            case 0xA501://定时开关机
                int msgid501 = tcpMessage.ReadInt();
                int length = tcpMessage.ReadInt();
                String guanji = tcpMessage.ReadString(length);
                int length2 = tcpMessage.ReadInt();
                String kaiji = tcpMessage.ReadString(length2);
                int IsEnable = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA501收到开关机指令：" + guanji + "/" + kaiji + "/" + IsEnable);
                smdtManager.smdtSetTimingSwitchMachine(guanji, kaiji, IsEnable + "");
                yingda(0xA501, true, deviceId, msgid501);
                break;
            case 0xA502://关闭系统
                int msgid502 = tcpMessage.ReadInt();
                try {
                    Log4j.d(TAG, "0xA502收到关机指令");
                    yingda(0xA502, true, deviceId, msgid502);
                    smdtManager.shutDown();
                } catch (Exception e) {
                    Log4j.d(TAG, e.getMessage());
                    yingda(0xA502, false, deviceId, msgid502);
                    Log4j.d(TAG, "关机失败");
                }
                break;
            case 0xA503://重启系统
                int msgid503 = tcpMessage.ReadInt();
                try {
                    Log4j.d(TAG, "0xA503收到重启指令");
                    yingda(0xA503, true, deviceId, msgid503);
                    smdtManager.smdtReboot("reboot");
                } catch (Exception e) {
                    Log4j.d(TAG, e.getMessage());
                    yingda(0xA503, false, deviceId, msgid503);
                    Log4j.d(TAG, "重启失败");
                }
                break;
            case 0xA504://开关机时间间隔
                int msgid504 = tcpMessage.ReadInt();
                int gHours = tcpMessage.ReadInt();
                int gMinute = tcpMessage.ReadInt();
                int kHours = tcpMessage.ReadInt();
                int kMinute = tcpMessage.ReadInt();
                int IsEnable2 = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA504收到开关机时间间隔指令" + gHours + "/" + gMinute + "/" + kHours + "/" + kMinute + "/" + IsEnable2);
                smdtManager.smdtSetPowerOnOff((char) gHours, (char) gMinute, (char) kHours, (char) kMinute, (char) IsEnable2);
                yingda(0xA504, true, deviceId, msgid504);
                break;
            case 0xA505://截屏
                try {
                    final int msgid505 = tcpMessage.ReadInt();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
                    final String timeStr = df.format(new Date());
                    Log4j.d(TAG, "A505开始截屏");
                    File filedir = new File(FileUtils.getDiskCacheDir(context) + "Screenshot");
                    if (!filedir.exists()) {
                        filedir.mkdirs();
                    }
                    smdtManager.smdtTakeScreenshot(FileUtils.getDiskCacheDir(context) + "Screenshot/", "screenshot_" + timeStr + ".png", context);
                    File file = new File(FileUtils.getDiskCacheDir(context) + "Screenshot/screenshot_" + timeStr + ".png");
                    if (file.exists()) {
                        Log4j.d(TAG, "获取截屏成功！");

                        Luban.with(context)
                                .load(file)                                   // 传人要压缩的图片列表
                                .ignoreBy(50)                                  // 忽略不压缩图片的大小
                                .setTargetDir(FileUtils.getDiskCacheDir(context) + "Screenshot")                        // 设置压缩后文件存储位置
                                .setCompressListener(new OnCompressListener() { //设置回调
                                    @Override
                                    public void onStart() {
                                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                                        Log4j.d(TAG, "开始压缩");
                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        // TODO 压缩成功后调用，返回压缩后的图片文件
                                        Log4j.d(TAG, "压缩截屏文件成功！");
                                        uploadFile(file.getAbsolutePath(), FileUtils.getDiskCacheDir(context) + "Screenshot/screenshot_" + timeStr + ".png", Constant.Ftp_Screenshot, 0xA505, msgid505);
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        // TODO 当压缩过程出现问题时调用
                                        Log4j.d(TAG, "压缩截屏文件失败！" + e.getMessage());
                                        yingda(0xA505, false, deviceId, msgid505);
                                    }
                                }).launch();    //启动压缩

                    } else {
                        Log4j.d(TAG, "获取截屏文件失败！sn");
                        yingda(0xA505, false, deviceId, msgid505);
                    }
                } catch (Exception e) {
                    Log4j.d(TAG, e.getMessage());
                }
                break;

            case 0xA506://开关屏
                int msgid506 = tcpMessage.ReadInt();
                boolean result2 = tcpMessage.ReadBool();
                Log4j.d(TAG, "0xA506收到开关屏指令" + result2);
                if (result2) {
                    smdtManager.smdtSetLcdBackLight(1);//打开背光
                    yingda(0xA506, 1, true, deviceId, msgid506);
                } else {
                    smdtManager.smdtSetLcdBackLight(0);//关闭背光
                    yingda(0xA506, 0, true, deviceId, msgid506);
                }
                break;
            case 0xA507://亮度调节
                int msgid507 = tcpMessage.ReadInt();
                int result3 = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA507收到亮度调节指令" + result3);
                smdtManager.setBrightness(context.getContentResolver(), result3);
                yingda(0xA507, true, deviceId, msgid507);
                break;
            case 0xA508://设置声音
                int msgid508 = tcpMessage.ReadInt();
                int voice = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA508收到设置声音指令" + voice);
                smdtManager.smdtSetVolume(context, voice);
                yingda(0xA508, true, deviceId, msgid508);
                break;
            case 0xA509://远程升级
                int msgid509 = tcpMessage.ReadInt();
                int length9 = tcpMessage.ReadInt();
                String path9 = tcpMessage.ReadString(length9);
                Log4j.d(TAG, "0xA509收到远程升级指令" + path9);
                downloadFile(0xA509, Constant.Ftp_Upgrade + path9, msgid509);
                break;
            case 0xA510://设置以太网IP
                int msgid510 = tcpMessage.ReadInt();
                int l1 = tcpMessage.ReadInt();
                String ip = tcpMessage.ReadString(l1);
                int l2 = tcpMessage.ReadInt();
                String mask = tcpMessage.ReadString(l2);
                int l3 = tcpMessage.ReadInt();
                String gateway = tcpMessage.ReadString(l3);
                int l4 = tcpMessage.ReadInt();
                String dns = tcpMessage.ReadString(l4);
                Log4j.d(TAG, "0xA510收到设置以太网IP指令" + ip + "/" + mask + "/" + gateway + "/" + dns);
                try {
                    smdtManager.smdtSetEthIPAddress(ip, mask, gateway, dns);
                    yingda(0xA510, true, deviceId, msgid510);
                    Log4j.d(TAG, "设置以太网IP成功！");
                } catch (Exception e) {
                    Log4j.d(TAG, e.getMessage());
                    yingda(0xA510, false, deviceId, msgid510);
                    Log4j.d(TAG, "设置以太网IP失败！");
                }

                break;
            case 0xA511://设置系统时间
                int msgid511 = tcpMessage.ReadInt();
                int length11 = tcpMessage.ReadInt();
                String time = tcpMessage.ReadString(length11);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Date date2 = null;
                try {
                    date2 = format.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date2);
                LogUtils.d(time);
                Log4j.d(TAG, "0xA511收到设置系统时间指令" + time);
//                LogUtils.d(calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.DAY_OF_MONTH)) + "/" + calendar.get(Calendar.HOUR_OF_DAY) + "/" + calendar.get(Calendar.MINUTE));
                try {
                    smdtManager.setTime(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    yingda(0xA511, true, deviceId, msgid511);
                    Log4j.d(TAG, "设置系统时间成功");
                } catch (Exception e) {
                    Log4j.d(TAG, e.getMessage());
                    yingda(0xA511, false, deviceId, msgid511);
                    Log4j.d(TAG, "设置系统时间失败");
                }
                EventBus.getDefault().post(new UpdateDate());
                break;
            case 0xA512://传log
                int msgid512 = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA512收到上传log指令");
                File path = new File(FileUtils.getDiskCacheDir(context) + "Log/");
                //列出该目录下所有文件和文件夹
                File[] files = path.listFiles();
                if (files.length > 0) {
                    //按照文件最后修改日期倒序排序
                    Arrays.sort(files, new Comparator<File>() {
                        @Override
                        public int compare(File file1, File file2) {
                            return (int) (file2.lastModified() - file1.lastModified());
                        }
                    });
                    //取出第一个(即最新修改的)文件
                    Log4j.d(TAG, "开始上传" + files[0].getAbsolutePath());
                    uploadFile(files[0].getAbsolutePath(), Constant.Ftp_Log, 0xA512, msgid512);
                } else {
                    yingda(0xA512, false, deviceId, msgid512);
                    Log4j.d(TAG, "未查询到log文件");
                }
                break;
            case 0xA513://发送版本信息
                Log4j.d(TAG, "0xA513收到发送版本信息指令");
                int msgid513 = tcpMessage.ReadInt();
                try {
                    sendVersionInfo(0xA513, true, deviceId, msgid513);
                } catch (Exception e) {
                    Log4j.d(TAG,e.getMessage());
                }
                break;
            case 0xA550://刷卡屏幕跳转
                int status = tcpMessage.ReadInt();
                Log4j.d("0xA550收到刷卡屏幕跳转指令", status + "");
                if (status == 0) {
                    int type = tcpMessage.ReadInt();
                    int length550 = tcpMessage.ReadInt();
                    String userId = tcpMessage.ReadString(length550);
                    int length5502 = tcpMessage.ReadInt();
                    String ext = tcpMessage.ReadString(length5502);
                    Log4j.d("0xA550", type + "");
                    gotoActivity(type, userId, ext);
                } else {
                    handleFail(status, 550);
                }
                break;
            case 0xA555://通用屏幕跳转
                int type555 = tcpMessage.ReadInt();
                Log4j.d("0xA555收到通用屏幕跳转指令", type555 + "");
                gotoActivity(type555, "", "");
                yingda(0xA555, true, deviceId);
                break;
            case 0xA600://收消息
                int msgType = tcpMessage.ReadInt();
                int length600 = tcpMessage.ReadInt();
                String msgStr = tcpMessage.ReadString(length600);
                Log4j.d(TAG, "0xA600收到接收消息指令" + msgType + "///" + msgStr);
                try {
                    MessageBase result = null;
                    if (msgType == 1) {
                        result = JSON.parseObject(msgStr, new TypeReference<TextReceiveMessage>() {
                        });
                    } else if (msgType == 2) {
                        result = JSON.parseObject(msgStr, new TypeReference<ImageReceiveMessage>() {
                        });
                    } else if (msgType == 3 || msgType == 4) {
                        result = JSON.parseObject(msgStr, new TypeReference<VoiceReceiveMessage>() {
                        });
                    }
                    EventBus.getDefault().post(result);
                } catch (Exception e) {
                    Log4j.d(TAG, e.getMessage());
                }
                break;
            case 0xA601://发消息
                Boolean msgSuccess = tcpMessage.ReadBool();
                Log4j.d(TAG, msgSuccess ? "消息发送成功" : "消息发送失败");
                try {
                    if (!msgSuccess){
                        Toast.makeText(context,"消息发送失败!",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log4j.e(TAG,"0x601Toast错误！");
                    e.printStackTrace();
                }

                break;
            case 0xA602://接收广播消息
                int msgid602 = tcpMessage.ReadInt();
                int length602 = tcpMessage.ReadInt();
                String broadcastUrl = tcpMessage.ReadString(length602);
                Log4j.d(TAG, "0xA602收到广播消息：" + broadcastUrl);
                downloadBroadcast(0xA602, Constant.Ftp_Broadcast + "/" + broadcastUrl, broadcastUrl, msgid602);
                break;
            case 0xA605://会议签到
                int status605 = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA605收到会议签到指令" + status605);
                if (status605 == 0) {
                    int type = tcpMessage.ReadInt();
                    int length605 = tcpMessage.ReadInt();
                    String id = tcpMessage.ReadString(length605);
                    int length6052 = tcpMessage.ReadInt();
                    String ext = tcpMessage.ReadString(length6052);
                    Log4j.d(TAG, "签到：" + id);
                    EventBus.getDefault().post(new SignEvent(0, true, id, ""));
                } else {
                    Log4j.d(TAG, "签到失败");
                    handleFail(status605, 605);
                    EventBus.getDefault().post(new SignEvent(0, false, "", ""));
                }
                break;
            case 0xA606://页面更新
                int msgid606 = tcpMessage.ReadInt();
                int status606 = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA606收到页面更新指令" + status606);
                EventBus.getDefault().post(new MainUpdateEvent(status606, ""));
                yingda(0xA606, true, deviceId, msgid606);
                break;
            case 0xA608://页面更新
                int status608 = tcpMessage.ReadInt();
                Log4j.d(TAG, "0xA608收到班级签到指令" + status608);
                if (status608 == 0) {
                    int type = tcpMessage.ReadInt();
                    int length608 = tcpMessage.ReadInt();
                    String id = tcpMessage.ReadString(length608);
                    int length6082 = tcpMessage.ReadInt();
                    String ext = tcpMessage.ReadString(length6082);
                    Log4j.d(TAG, "签到：" + id + ext);
                    EventBus.getDefault().post(new SignEvent(1, true, id, ext));
                } else {
                    Log4j.d(TAG, "签到失败");
                    handleFail(status608, 608);
                    EventBus.getDefault().post(new SignEvent(1, false, "", ""));
                }
                break;
            default:
                break;
        }
    }


    /**
     * 心跳
     */
    private void xintiao() {
        x_handler = new Handler();
        x_runnable = new Runnable() {
            @Override
            public void run() {
                Log4j.d(TAG, "发送心跳"+XTfailNum);
                try {
                    if (client == null || !IsEnable || XTfailNum > 2) {
                        Log4j.d(TAG,"心跳异常，断开连接");
                        x_handler.removeCallbacks(this);
                        Stop();
                        return;
                    }
                    TCPMessage message = new TCPMessage(0xA000);
                    client.getTransceiver().send(message);
                    XTfailNum++;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log4j.d(TAG, e.getMessage());
                } finally {
                    x_handler.postDelayed(this, xintiaoTime);
                }
            }
        };
        x_handler.postDelayed(x_runnable, xintiaoTime);
    }

    /**
     * 停止
     */
    public void Stop() {
        if (client != null && client.isConnected()) {
            client.disconnect();
        }
    }

    /**
     * 停止心跳
     */
    public void StopXT() {
        Log4j.d(TAG, "停止心跳");
        if (x_handler != null && x_runnable != null) {
            x_handler.removeCallbacks(x_runnable);
        }
    }

    /**
     * 开关屏应答
     *
     * @param xyh  协议号
     * @param type 类型
     * @param b
     */
    private void yingda(int xyh, int type, boolean b, String diviceId, int msgid) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(msgid);
            message.Write(type);
            message.Write(b);
            message.Write(diviceId, 36);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用应答
     *
     * @param xyh 协议号
     * @param b
     */
    private void yingda(int xyh, boolean b, String diviceId, int id) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(id);
            message.Write(b);

//            int length1 = diviceId.getBytes("UTF-8").length;
//            message.Write(length1);
            message.Write(diviceId, 36);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用应答
     *
     * @param xyh 协议号
     * @param b
     */
    private void yingda(int xyh, boolean b, String diviceId) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(b);

//            int length1 = diviceId.getBytes("UTF-8").length;
//            message.Write(length1);
            message.Write(diviceId, 36);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 截屏应答,上传log应答
     *
     * @param xyh 协议号
     * @param b
     */
    private void yingda(int xyh, boolean b, String diviceId, String path, int id) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(id);
            message.Write(b);

//            int length1 = diviceId.getBytes("UTF-8").length;
//            message.Write(3);
            message.Write(diviceId, 36);

            int length2 = path.getBytes("UTF-8").length;
            message.Write(length2);
            message.Write(path, length2);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
            Log4j.d(TAG, e.getMessage());
        }
    }

    /**
     * 发消息应答
     *
     * @param xyh 协议号
     */
    private void yingda(int xyh, int msgType, String diviceId, String json) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(diviceId, 36);

            message.Write(msgType);
            int length2 = json.getBytes("UTF-8").length;
            message.Write(length2);
            message.Write(json, length2);

            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
            Log4j.d(TAG, e.getMessage());
        }
    }

    /**
     * 上传log
     *
     * @param fileName
     * @param ftpPath
     * @param xyh
     */
    public void uploadFile(final String fileName, final String ftpPath, final int xyh, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 上传  
                File file = new File(fileName);
                LogUtils.d(file.exists());
                try {
                    //单文件上传
                    new FTP(context).uploadSingleFile(file, ftpPath, new FTP.UploadProgressListener() {

                        @Override
                        public void onUploadProgress(String currentStep, long uploadSize, File file) {
                            // TODO Auto-generated method stub  
                            Log4j.d(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_SUCCESS)) {
                                Log4j.d(TAG, fileName + "-----shanchuan-log-successful");
                                yingda(xyh, true, deviceId, file.getName(), id);
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_LOADING)) {
                                long fize = file.length();
                                float num = (float) uploadSize / (float) fize;
                                int result = (int) (num * 100);
                                Log4j.d(TAG, "-----shangchuan--log-" + result + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_FAIL)) {
                                Log4j.d(TAG, "-----shangchuan-log-fail---");
                                yingda(xyh, false, deviceId, id);
                            }
                        }
                    });
                } catch (IOException e) {
                    // TODO Auto-generated catch block  
                    e.printStackTrace();
                    Log4j.e(TAG, e.getMessage());
                    yingda(xyh, false, deviceId, id);
                }
            }
        }).start();
    }

    /**
     * 上传截图
     *
     * @param fileName
     * @param ftpPath
     * @param xyh
     */
    private void uploadFile(final String fileName, final String fileName2, final String ftpPath, final int xyh, final int id) {
        Log4j.d(TAG, "开始上传截图" + fileName);
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 上传
                File file = new File(fileName);
                LogUtils.d(file.exists());
                try {
                    //单文件上传
                    new FTP(context).uploadSingleFile(file, ftpPath, new FTP.UploadProgressListener() {

                        @Override
                        public void onUploadProgress(String currentStep, long uploadSize, File file) {
                            // TODO Auto-generated method stub
                            Log4j.d(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_SUCCESS)) {
                                Log4j.d(TAG, fileName + "-----shanchuan-log-successful");
                                yingda(xyh, true, deviceId, file.getName(), id);
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_LOADING)) {
                                long fize = file.length();
                                float num = (float) uploadSize / (float) fize;
                                int result = (int) (num * 100);
                                Log4j.d(TAG, "-----shangchuan--log-" + result + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_FAIL)) {
                                Log4j.d(TAG, "-----shangchuan-log-fail---");
                                yingda(xyh, false, deviceId, id);
                            }
                        }
                    });
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log4j.e(TAG, e.getMessage());
                    yingda(xyh, false, deviceId, id);
                } finally {
                    File file1 = new File(fileName);
                    File file2 = new File(fileName2);

                    if (file1.exists()) {
                        file1.delete();
                    }
                    if (file2.exists()) {
                        file2.delete();
                    }
                }
            }
        }).start();
    }

    /**
     * 上传语音消息
     *
     * @param fileName
     * @param ftpPath
     * @param xyh
     */
    public void uploadFile(final int msgType, final String fileName, final String ftpPath, final int xyh, final String JsonMsg) {
        LogUtils.d(fileName + "$" + ftpPath);
        new Thread(new Runnable() {
            @Override
            public void run() {

                // 上传
                File file = new File(fileName);
                try {
                    //单文件上传
                    new FTP(context).uploadSingleFile(file, ftpPath, new FTP.UploadProgressListener() {

                        @Override
                        public void onUploadProgress(String currentStep, long uploadSize, File file) {
                            // TODO Auto-generated method stub
                            Log4j.d(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_SUCCESS)) {
                                Log4j.d(TAG, fileName + "-----shanchuan--VoiceSendMessage--successful");
                                yingda(xyh, msgType, deviceId, JsonMsg);
                                VoiceSendMessage mMessage = JSON.parseObject(JsonMsg, new TypeReference<VoiceSendMessage>() {
                                });
                                MsgSendStatus msgSendStatus = new MsgSendStatus();
                                msgSendStatus.setMsgTime(mMessage.getCreateTime());
                                msgSendStatus.setStatus(1);
                                EventBus.getDefault().post(msgSendStatus);
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_LOADING)) {
                                long fize = file.length();
                                float num = (float) uploadSize / (float) fize;
                                int result = (int) (num * 100);
                                Log4j.d(TAG, "-----shangchuan--VoiceSendMessage---" + result + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_FAIL)) {
                                Log4j.d(TAG, "-----shangchuan--VoiceSendMessage--fail---");
                                VoiceSendMessage mMessage = JSON.parseObject(JsonMsg, new TypeReference<VoiceSendMessage>() {
                                });
                                MsgSendStatus msgSendStatus = new MsgSendStatus();
                                msgSendStatus.setMsgTime(mMessage.getCreateTime());
                                msgSendStatus.setStatus(2);
                                EventBus.getDefault().post(msgSendStatus);
                            }
                        }
                    });
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log4j.e(TAG, e.getMessage());
                    VoiceSendMessage mMessage = JSON.parseObject(JsonMsg, new TypeReference<VoiceSendMessage>() {
                    });
                    MsgSendStatus msgSendStatus = new MsgSendStatus();
                    msgSendStatus.setMsgTime(mMessage.getCreateTime());
                    msgSendStatus.setStatus(2);
                    EventBus.getDefault().post(msgSendStatus);
                }
            }
        }).start();
    }

    /**
     * 安装apk
     *
     * @param xyh
     * @param path
     */
    private int retryTime = 0;

    public void downloadFile(final int xyh, final String path, final int id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 下载
                try {
                    //单文件下载
                    new FTP(context).downloadSingleFile(path, FileUtils.getDiskCacheDir(context) + "Apk/", "dzbp.apk", new FTP.DownLoadProgressListener() {

                        @Override
                        public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                            Log.d(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_DOWN_SUCCESS)) {
                                Log4j.d(TAG, "-----xiazai-apk-successful" + FileUtils.getDiskCacheDir(context) + "Apk/dzbp.apk");
                                yingda(xyh, true, deviceId, id);
//                                smdtManager.smdtSilentInstall(FileUtils.getDiskCacheDir(context) + "Apk/dzbp.apk", context);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setDataAndType(Uri.fromFile(new File(FileUtils.getDiskCacheDir(context) + "Apk/dzbp.apk")),
                                        "application/vnd.android.package-archive");
                                context.startActivity(intent);
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_LOADING)) {
                                Log4j.d(TAG, "-----xiazai-apk--" + downProcess + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_FAIL)) {
                                Log4j.d(TAG, "-----xiazai-apk-fail---");
                                new Handler(context.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (retryTime < 5) {
                                            Log4j.d(TAG, "-----xiazai-apk-fail-chongshi--");
                                            downloadFile(xyh, path, id);
                                        } else {
                                            yingda(xyh, false, deviceId, id);
                                        }
                                        retryTime++;
                                    }
                                }, 5000);
                            }
                        }

                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    yingda(xyh, false, deviceId, id);
                }

            }
        }).start();
    }

    /**
     * 下载广播
     *
     * @param xyh
     * @param path
     */
    public void downloadBroadcast(final int xyh, final String path, final String fileName, final int id) {
        Log4j.d(TAG, xyh + path + fileName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 下载
                try {
                    //单文件下载
                    new FTP(context).downloadSingleFile(path, FileUtils.getDiskCacheDir(context) + "BroadCast/", fileName, new FTP.DownLoadProgressListener() {

                        @Override
                        public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                            Log.d(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_DOWN_SUCCESS)) {
                                Log4j.d(TAG, "-----xiazai-guangbo-successful");
                                yingda(xyh, true, deviceId, id);
                                playBroad(FileUtils.getDiskCacheDir(context) + "BroadCast/" + fileName);
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_LOADING)) {
                                Log4j.d(TAG, "-----xiazai-guangbo--" + downProcess + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_FAIL)) {
                                Log4j.d(TAG, "-----xiazai-guangbo-fail---");
                                yingda(xyh, false, deviceId, id);
                            }
                        }

                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    yingda(xyh, false, deviceId, id);
                }

            }
        }).start();
    }

    /**
     * 播放广播
     */
    private void playBroad(final String path) {

//        try {
//            new Handler(context.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    MsgDialog dialog = new MsgDialog(context);
//                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//                    ArrayList<String> mlist = new ArrayList<>();
//                    mlist.add("我是广播提示");
//                    mlist.add("我也是广播提示！");
//                    dialog.setData(mlist);
//                    dialog.show();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Log4j.d(TAG, "开始播放" + path);
        if (mp == null) {
            mp = new MediaPlayer();
            Log4j.d(TAG, "初始化");
        } else {
            mp.stop();
            mp.reset();
        }
        try {
            Log4j.d(TAG, "设置资源");
            final FileInputStream is = new FileInputStream(path);
            mp.setDataSource(is.getFD());
//            mp.setDataSource(path);
            mp.prepareAsync();

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log4j.d(TAG, "开始播放");
                    mediaPlayer.start();
                }
            });

            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log4j.d(TAG, "播放错误");
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        Log4j.d(TAG, "重置播放器");
                    }
                    return false;
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Log4j.d(TAG, "播放完成");
                    if (is != null) {
                        try {
                            is.close();
                            File file = new File(path);
                            if (file.exists()) {
                                file.delete();
                            }
                        } catch (IOException e) {
                            Log4j.d(TAG, e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log4j.d(TAG, e.getMessage());
        }
    }

    /**
     * 发送版本信息
     */
    private void sendVersionInfo(int xyh, boolean b, String diviceId, int id) {
        String androidModel = smdtManager.getAndroidModel();//获取目前设备的型号。
        String AndroidDisplay = smdtManager.getAndroidDisplay();//固件系统版本和编译日期
        String apiversion = smdtManager.smdtGetAPIVersion();//获取目前API的平台-版本-日期信息。
        String androidVersion = smdtManager.getAndroidVersion();//获取目前设备的android系统的版本
        String FirmwareVersion = smdtManager.getFirmwareVersion();//设备的固件SDK版本
        String FormattedKernelVersion = smdtManager.getFormattedKernelVersion();//设备的固件内核版本
        String runningMemory = smdtManager.getRunningMemory();//获取设备的硬件内存大小容量
        String InternalStorageMemory = smdtManager.getInternalStorageMemory();//获取设备的硬件内部存储大小容量。
        String Appversion = ManifestUtils.getVersionName(context);

        int LcdLightStatus = smdtManager.smdtGetLcdLightStatus();//1开屏  0关屏
        int ScreenBrightness = smdtManager.getScreenBrightness(context);//亮度大小
        int Volume = smdtManager.smdtGetVolume(context);//声音大小
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String dateTime = df.format(new Date());//系统时间


        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(id);
            message.Write(b);

//            int length1 = diviceId.getBytes("UTF-8").length;
//            message.Write(length1);
            message.Write(diviceId, 36);

            int length2 = androidModel.getBytes("UTF-8").length;
            message.Write(length2);
            message.Write(androidModel, length2);

            int length3 = AndroidDisplay.getBytes("UTF-8").length;
            message.Write(length3);
            message.Write(AndroidDisplay, length3);

            int length4 = apiversion.getBytes("UTF-8").length;
            message.Write(length4);
            message.Write(apiversion, length4);

            int length5 = androidVersion.getBytes("UTF-8").length;
            message.Write(length5);
            message.Write(androidVersion, length5);

            int length6 = FirmwareVersion.getBytes("UTF-8").length;
            message.Write(length6);
            message.Write(FirmwareVersion, length6);

            int length7 = FormattedKernelVersion.getBytes("UTF-8").length;
            message.Write(length7);
            message.Write(FormattedKernelVersion, length7);

            int length8 = runningMemory.getBytes("UTF-8").length;
            message.Write(length8);
            message.Write(runningMemory, length8);

            int length9 = InternalStorageMemory.getBytes("UTF-8").length;
            message.Write(length9);
            message.Write(InternalStorageMemory, length9);

            int length10 = Appversion.getBytes("UTF-8").length;
            message.Write(length10);
            message.Write(Appversion, length10);

            message.Write(LcdLightStatus);
            message.Write(ScreenBrightness);
            message.Write(Volume);

            int length11 = dateTime.getBytes("UTF-8").length;
            message.Write(length11);
            message.Write(dateTime, length11);

            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 页面跳转
     *
     * @param code
     */
    private void handleFail(int code, int type) {
        Log4j.d(TAG, "handleFail处理错误信息" + code);
        String msg = "";
        switch (code) {
            case 1:
                msg = "操作失败！";
                break;
            case 2:
                msg = "卡未授权！";
                break;
            case 3:
                msg = "查无此人！";
                break;
            case 4:
                msg = "未关注微信号！";
                break;
            case 5:
                msg = "保存数据异常！";
                break;
            case 6:
                msg = "不允许此操作！";
                break;
            default:
                break;
        }
        myToast.toast(context, msg);
    }

    /**
     * 页面跳转
     *
     * @param type
     */
    private void gotoActivity(int type, String userId, String ext) {
        Intent intent = null;
        Log4j.d(TAG, "开始跳转屏幕" + type);
        switch (type) {
            case 0:
                intent = new Intent(context, MainActivity.class);
                break;
            case 1:
                intent = new Intent(context, TeacherActivity.class);
                break;
            case 2:
                intent = new Intent(context, StudentActivity.class);
                intent.putExtra("userId", userId);
                break;
            case 3:
                intent = new Intent(context, PatrolActivity.class);
                intent.putExtra("userId", userId);
                break;
            case 4:
                intent = new Intent(context, ExamActivity.class);
                break;
            case 5:
                intent = new Intent(context, NoticeActivity.class);
                break;
            case 6:
                intent = new Intent(context, MeetingActivity.class);
                break;
            case 7:
                intent = new Intent(context, SignActivity.class);
                break;
            default:
                break;
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            Log4j.d(TAG, "屏幕跳转成功" + type);
        }
    }
}
