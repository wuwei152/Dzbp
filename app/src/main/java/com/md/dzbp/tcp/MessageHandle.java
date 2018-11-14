package com.md.dzbp.tcp;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.FtpParams;
import com.md.dzbp.data.ImageReceiveMessage;
import com.md.dzbp.data.MainData;
import com.md.dzbp.data.MainUpdateEvent;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.MsgSendStatus;
import com.md.dzbp.data.SendSignEvent;
import com.md.dzbp.data.SignBean;
import com.md.dzbp.data.SignBean_Table;
import com.md.dzbp.data.SignEvent;
import com.md.dzbp.data.TextReceiveMessage;
import com.md.dzbp.data.UpdateDate;
import com.md.dzbp.data.VoiceReceiveMessage;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.data.WorkTimePeriod;
import com.md.dzbp.data.WorkTimePoint;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.task.SwitchTask;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.raizlabs.android.dbflow.sql.language.Delete;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * 处理接收消息，回应消息
 */
public class MessageHandle {
    private static final String TAG = "MessageHandle-->{}";
    private final Logger logger;
    private Context context;
    private TcpClient client;
    private Handler x_handler;
    private static int xintiaoTime = 10000;
    private SmdtManager smdtManager;
    public boolean IsEnable = false;
    private final ACache mACache;
    private String deviceId = "";
    private Runnable x_runnable;
    private int XTfailNum = 0;
    public final MsgHandleUtil msgHandleUtil;

    public MessageHandle(Context context, TcpClient client) {
        this.context = context;
        this.client = client;
        smdtManager = SmdtManager.create(context);
        mACache = ACache.get(context);
        deviceId = Constant.getDeviceId(context);
        logger = LoggerFactory.getLogger(context.getClass());
        msgHandleUtil = new MsgHandleUtil(context, client);
    }

    public void handleMessage(TCPMessage tcpMessage) {
        logger.debug(TAG, "收到消息>>>>>>>>>>>>>>" + tcpMessage.Type);
        switch (tcpMessage.Type) {
            case 0xA001://通用应答
                logger.debug(TAG, "0xA001收到通用应答消息");
                break;
            case 0xA002://登录应答，发心跳
                int result1 = tcpMessage.ReadInt();
//                LogUtils.d(result1);
                if (result1 == 0) {
                    logger.debug(TAG, "0xA002收到登录授权成功消息");
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
                    logger.debug(TAG, "0xA002设置开关机指令：关" + guanji + "/开" + kaiji);
                    smdtManager.smdtSetTimingSwitchMachine(guanji, kaiji, "1");
                    int length3 = tcpMessage.ReadInt();
                    String task = tcpMessage.ReadString(length3);
//                    LogUtils.d(task);
                    ArrayList<WorkTimePeriod> list = JSON.parseObject(task.toString(), new TypeReference<ArrayList<WorkTimePeriod>>() {
                    });
//                    LogUtils.d(list);
                    mACache.put("Task",list);
                    if (list != null) {
                        ArrayList<WorkTimePoint> list1 = WorkTimePoint.GetWorkTimePointList(list);
                        SwitchTask.getInstance(context).SetTaskList(list1);
                    }
//                    LogUtils.d("psw:" + psw);

                    FtpParams params = new FtpParams(ftp_ip, ftp_port, ftp_name, ftp_psw, web_api);
                    mACache.put("FtpParams", params);
                    mACache.put("AdminPsw", psw);
                    EventBus.getDefault().post(new SendSignEvent());
                } else if (result1 == 1) {
                    logger.debug(TAG, "0xA002收到登录授权失败消息");
                    IsEnable = false;
                } else {
                    logger.debug(TAG, "0xA002收到登录未知错误消息");
                    IsEnable = false;
                }
                break;
            case 0xA003://心跳应答
                logger.debug(TAG, "0xA003收到心跳应答消息");
                XTfailNum = 0;
                break;
            case 0xA501://定时开关机
                int msgid501 = tcpMessage.ReadInt();
                int length = tcpMessage.ReadInt();
                String guanji = tcpMessage.ReadString(length);
                int length2 = tcpMessage.ReadInt();
                String kaiji = tcpMessage.ReadString(length2);
                int IsEnable = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA501收到开关机指令：" + guanji + "/" + kaiji + "/" + IsEnable);
                smdtManager.smdtSetTimingSwitchMachine(guanji, kaiji, IsEnable + "");
                msgHandleUtil.yingda(0xA501, true, deviceId, msgid501);
                break;
            case 0xA502://关闭系统
                int msgid502 = tcpMessage.ReadInt();
                try {
                    logger.debug(TAG, "0xA502收到关机指令");
                    msgHandleUtil.yingda(0xA502, true, deviceId, msgid502);
                    smdtManager.shutDown();
                } catch (Exception e) {
                    logger.debug(TAG, e.getMessage());
                    msgHandleUtil.yingda(0xA502, false, deviceId, msgid502);
                    logger.debug(TAG, "关机失败");
                }
                break;
            case 0xA503://重启系统
                int msgid503 = tcpMessage.ReadInt();
                try {
                    logger.debug(TAG, "0xA503收到重启指令");
                    msgHandleUtil.yingda(0xA503, true, deviceId, msgid503);
                    smdtManager.smdtReboot("reboot");
                } catch (Exception e) {
                    logger.debug(TAG, e.getMessage());
                    msgHandleUtil.yingda(0xA503, false, deviceId, msgid503);
                    logger.debug(TAG, "重启失败");
                }
                break;
            case 0xA504://开关机时间间隔
                int msgid504 = tcpMessage.ReadInt();
                int gHours = tcpMessage.ReadInt();
                int gMinute = tcpMessage.ReadInt();
                int kHours = tcpMessage.ReadInt();
                int kMinute = tcpMessage.ReadInt();
                int IsEnable2 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA504收到开关机时间间隔指令" + gHours + "/" + gMinute + "/" + kHours + "/" + kMinute + "/" + IsEnable2);
                smdtManager.smdtSetPowerOnOff((char) gHours, (char) gMinute, (char) kHours, (char) kMinute, (char) IsEnable2);
                msgHandleUtil.yingda(0xA504, true, deviceId, msgid504);
                break;
            case 0xA505://截屏
                final int msgid505 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA505收到截屏指令" + msgid505);
                msgHandleUtil.TakeScreenshot(msgid505);
                break;

            case 0xA506://开关屏
                int msgid506 = tcpMessage.ReadInt();
                boolean result2 = tcpMessage.ReadBool();
                logger.debug(TAG, "0xA506收到开关屏指令" + result2);
                if (result2) {
                    smdtManager.smdtSetLcdBackLight(1);//打开背光
                    msgHandleUtil.yingda(0xA506, 1, true, deviceId, msgid506);
                } else {
                    smdtManager.smdtSetLcdBackLight(0);//关闭背光
                    msgHandleUtil.yingda(0xA506, 0, true, deviceId, msgid506);
                }
                break;
            case 0xA507://亮度调节
                int msgid507 = tcpMessage.ReadInt();
                int result3 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA507收到亮度调节指令" + result3);
                smdtManager.setBrightness(context.getContentResolver(), result3);
                msgHandleUtil.yingda(0xA507, true, deviceId, msgid507);
                break;
            case 0xA508://设置声音
                int msgid508 = tcpMessage.ReadInt();
                int voice = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA508收到设置声音指令" + voice);
                smdtManager.smdtSetVolume(context, voice);
                msgHandleUtil.yingda(0xA508, true, deviceId, msgid508);
                break;
            case 0xA509://远程升级
                int msgid509 = tcpMessage.ReadInt();
                int length9 = tcpMessage.ReadInt();
                String path9 = tcpMessage.ReadString(length9);
                logger.debug(TAG, "0xA509收到远程升级指令" + path9);
                msgHandleUtil.downloadFile(0xA509, Constant.Ftp_Upgrade + path9, msgid509);
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
                logger.debug(TAG, "0xA510收到设置以太网IP指令" + ip + "/" + mask + "/" + gateway + "/" + dns);
                try {
                    smdtManager.smdtSetEthIPAddress(ip, mask, gateway, dns);
                    msgHandleUtil.yingda(0xA510, true, deviceId, msgid510);
                    logger.debug(TAG, "设置以太网IP成功！");
                } catch (Exception e) {
                    logger.debug(TAG, e.getMessage());
                    msgHandleUtil.yingda(0xA510, false, deviceId, msgid510);
                    logger.debug(TAG, "设置以太网IP失败！");
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
                logger.debug(TAG, "0xA511收到设置系统时间指令" + time);
//                LogUtils.d(calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + (calendar.get(Calendar.DAY_OF_MONTH)) + "/" + calendar.get(Calendar.HOUR_OF_DAY) + "/" + calendar.get(Calendar.MINUTE));
                try {
                    smdtManager.setTime(context, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    msgHandleUtil.yingda(0xA511, true, deviceId, msgid511);
                    logger.debug(TAG, "设置系统时间成功");
                } catch (Exception e) {
                    logger.debug(TAG, e.getMessage());
                    msgHandleUtil.yingda(0xA511, false, deviceId, msgid511);
                    logger.debug(TAG, "设置系统时间失败");
                }
                EventBus.getDefault().post(new UpdateDate());
                break;
            case 0xA512://传log
                int msgid512 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA512收到上传log指令");
                msgHandleUtil.GetLog(msgid512);
                break;
            case 0xA513://发送版本信息
                logger.debug(TAG, "0xA513收到发送版本信息指令");
                int msgid513 = tcpMessage.ReadInt();
                try {
                    msgHandleUtil.sendVersionInfo(0xA513, true, deviceId, msgid513);
                } catch (Exception e) {
                    logger.debug(TAG, e.getMessage());
                }
                break;
            case 0xA550://刷卡屏幕跳转
                int status550 = tcpMessage.ReadInt();
                logger.debug("0xA550收到刷卡屏幕跳转指令", status550 + "");

                int type550 = tcpMessage.ReadInt();
                int length550 = tcpMessage.ReadInt();
                String userId550 = tcpMessage.ReadString(length550);
                int length5502 = tcpMessage.ReadInt();
                String ext550 = tcpMessage.ReadString(length5502);
                logger.debug("0xA550", type550 + "");
                if (status550 == 0) {
                    msgHandleUtil.gotoActivity(type550, userId550, ext550);
                } else {
                    msgHandleUtil.handleFail(status550, 550);
                }
                if (!TextUtils.isEmpty(ext550)) {
                    myToast.toast(context, ext550);
                }
                break;
            case 0xA555://通用屏幕跳转
                int type555 = tcpMessage.ReadInt();
                logger.debug("0xA555收到通用屏幕跳转指令", type555 + "");
                msgHandleUtil.gotoActivity(type555, "", "");
                msgHandleUtil.yingda(0xA555, true, deviceId);
                break;
            case 0xA600://收消息
                int msgType = tcpMessage.ReadInt();
                int length600 = tcpMessage.ReadInt();
                String msgStr = tcpMessage.ReadString(length600);
                logger.debug(TAG, "0xA600收到接收消息指令" + msgType + "///" + msgStr);
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
                    logger.debug(TAG, e.getMessage());
                }
                break;
            case 0xA601://发消息
                Boolean msgSuccess = tcpMessage.ReadBool();
                logger.debug(TAG, msgSuccess ? "消息发送成功" : "消息发送失败");
                try {
                    if (!msgSuccess) {
                        Toast.makeText(context, "消息发送失败!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    logger.error(TAG, "0x601Toast错误！");
                    e.printStackTrace();
                }

                break;
            case 0xA602://接收广播消息
                int msgid602 = tcpMessage.ReadInt();
                int length602 = tcpMessage.ReadInt();
                String broadcastUrl = tcpMessage.ReadString(length602);
                logger.debug(TAG, "0xA602收到广播消息：" + broadcastUrl);
                msgHandleUtil.downloadBroadcast(0xA602, Constant.Ftp_Broadcast + "/" + broadcastUrl, broadcastUrl, msgid602);
                break;
            case 0xA604://下发、删除任务
                int taskType = tcpMessage.ReadInt();//任务类型  0作息时间；1会议；2通知；3考试
                int opType = tcpMessage.ReadInt();//操作类型  0下发；1删除
                int length604 = tcpMessage.ReadInt();
                String data604 = tcpMessage.ReadString(length604);//数据 当操作类型为0时为任务json，当类型为1时为业务ObjectID
                logger.debug(TAG, "0xA604收到下发、删除任务消息：" + opType + "///" + data604);

                if (opType == 0) {
                    ArrayList<WorkTimePeriod> list = JSON.parseObject(data604.toString(), new TypeReference<ArrayList<WorkTimePeriod>>() {
                    });
//                    LogUtils.d(list);
                    if (list != null) {
                        ArrayList<WorkTimePoint> list1 = WorkTimePoint.GetWorkTimePointList(list);
                        SwitchTask.getInstance(context).AddTaskList(list1);
                    }
                } else if (opType == 1) {
                    ArrayList<WorkTimePoint> list = SwitchTask.getInstance(context).GetTaskList();
                    Iterator<WorkTimePoint> sListIterator = list.iterator();
                    while (sListIterator.hasNext()) {
                        WorkTimePoint e = sListIterator.next();
                        if (e.getTaskTag().equals(data604)) {
                            sListIterator.remove();
                        }
                    }
                    SwitchTask.getInstance(context).SetTaskList(list);
                }
                break;
            case 0xA605://会议签到
                int status605 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA605收到会议签到指令" + status605);
                String ext605 = "";
                int type605 = tcpMessage.ReadInt();
                int length605 = tcpMessage.ReadInt();
                String id605 = tcpMessage.ReadString(length605);
                int length6052 = tcpMessage.ReadInt();
                ext605 = tcpMessage.ReadString(length6052);
                logger.debug(TAG, "签到：" + id605);
                if (status605 == 0) {
                    EventBus.getDefault().post(new SignEvent(0, true, id605, ""));

                } else {
                    logger.debug(TAG, "签到失败");
//                    handleFail(status605, 605);
                    EventBus.getDefault().post(new SignEvent(0, false, "", ""));
                }
                if (!TextUtils.isEmpty(ext605)) {
                    myToast.toast(context, ext605);
                }
                break;
            case 0xA606://页面更新
                int msgid606 = tcpMessage.ReadInt();
                int status606 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA606收到页面更新指令" + status606);
                EventBus.getDefault().post(new MainUpdateEvent(status606, ""));
                msgHandleUtil.yingda(0xA606, true, deviceId, msgid606);
                break;
            case 0xA608://班级签到
                int status608 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA608收到班级签到指令" + status608);

                int type608 = tcpMessage.ReadInt();
                int length608 = tcpMessage.ReadInt();
                String id608 = tcpMessage.ReadString(length608);
                int length6082 = tcpMessage.ReadInt();
                String ext608 = tcpMessage.ReadString(length6082);
                int length6083 = tcpMessage.ReadInt();
                String cardNum = tcpMessage.ReadString(length6083);
                if (status608 == 0) {
                    logger.debug(TAG, "签到：" + id608 + ext608);
                    EventBus.getDefault().post(new SignEvent(1, true, id608, ext608,cardNum));
                } else {
                    logger.debug(TAG, "签到失败");
//                    handleFail(status608, 608);
                    EventBus.getDefault().post(new SignEvent(1, false, "", ext608));
                }
                if (!TextUtils.isEmpty(ext608)) {
                    myToast.toast(context, ext608);
                }
                break;
            case 0xA609://摄像头截屏
//                int status609 = tcpMessage.ReadInt();
                logger.debug(TAG, "0xA609收到摄像头截屏指令");
                msgHandleUtil.yingda(0xA609, true, deviceId);
                msgHandleUtil.TakeVideoPic();
                break;
            case 0xA610://摄像头截屏成功后回复
                logger.debug(TAG, "0xA610收到摄像头截屏回复指令");
                break;
            case 0xA611://离线考勤上传成功后回复
                Boolean issuccess = tcpMessage.ReadBool();
                logger.debug(TAG, "0xA611收到离线考勤上传回复指令"+issuccess);
                if (issuccess){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Delete.table(SignBean.class);
                        }
                    },30000);
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
        x_handler = null;
        x_runnable = null;
        x_handler = new Handler();
        x_runnable = new Runnable() {
            @Override
            public void run() {
                logger.debug(TAG, "发送心跳" + XTfailNum);
                try {
                    if (client == null || !IsEnable || XTfailNum > 3) {
                        logger.debug(TAG, "心跳异常，断开连接");
                        x_handler.removeCallbacks(this);
                        Stop();
                        XTfailNum = 0;
                        return;
                    }
                    TCPMessage message = new TCPMessage(0xA000);
                    client.getTransceiver().send(message);
                    XTfailNum++;
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.debug(TAG, e.getMessage());
                } finally {
                    if (x_handler != null)
                        x_handler.postDelayed(this, xintiaoTime);
                }
            }
        };
        x_handler.postDelayed(x_runnable, xintiaoTime);
    }

    /**
     * 断开连接
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
        logger.debug(TAG, "停止心跳");
        if (x_handler != null && x_runnable != null) {
            x_handler.removeCallbacks(x_runnable);
            x_handler = null;
            x_runnable = null;
        }
    }

    /**
     * 发送语音消息
     */
    public void sendVoiceMsg(final int msgType, final String fileName, final String ftpPath, final int xyh, final String JsonMsg) {
        msgHandleUtil.uploadVoiceFile(msgType, fileName, ftpPath, xyh, JsonMsg);
    }

    /**
     * 发送文本消息
     */
    public void sendTextMsg(final int msgType, final int xyh, final String JsonMsg) {
        msgHandleUtil.yingda(xyh, msgType, deviceId, JsonMsg);
    }

    /**
     * 获取snap
     */
    public void TakeSnap() {
        LogUtils.d("开始获取！");
        msgHandleUtil.TakeVideoPic();
    }
}
