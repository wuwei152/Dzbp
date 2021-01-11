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
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.ProcessService;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CheckDelayEvent;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.data.SendSignEvent;
import com.md.dzbp.data.SignBean;
import com.md.dzbp.data.SignBean_Table;
import com.md.dzbp.data.SignEvent;
import com.md.dzbp.data.TextSendMessage;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.task.CheckAppRunningForegroundTask;
import com.md.dzbp.task.SwitchTask;
import com.md.dzbp.utils.ACache;
import com.raizlabs.android.dbflow.sql.language.SQLite;

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
    public SwitchTask mSwitchTask;
    private ACache mACache;
    private CheckAppRunningForegroundTask checkAppRunningForegroundTask;

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
        mACache = ACache.get(this);
        mManager = ServerManager.getInstance(this);
        EventBus.getDefault().register(this);
        logger = LoggerFactory.getLogger(getClass());

//        ArrayList<WorkTimePeriod> list = new ArrayList<>();
//        list.add(new WorkTimePeriod("111","1",0,"?:?:?:14:35:00","?:?:?:14:46:00"));
//        list.add(new WorkTimePeriod("222","1",0,"?:?:?:14:46:30","?:?:?:14:47:00"));
//        list.add(new WorkTimePeriod("223","7",1,"?:?:?:14:46:40","?:?:?:14:49:10"));
//        list.add(new WorkTimePeriod("333","1",0,"?:?:?:14:47:30","?:?:?:14:48:00"));
//        list.add(new WorkTimePeriod("333","3",2,"?:?:?:14:47:22","?:?:?:14:47:34"));
//        list.add(new WorkTimePeriod("544","1",0,"?:?:?:14:48:30","?:?:?:14:49:00"));
//        list.add(new WorkTimePeriod("555","1",0,"?:?:?:14:49:30","?:?:?:14:00:00"));
//        list.add(new WorkTimePeriod("666","1",0,"?:?:?:14:00:30","?:?:?:14:01:00"));

        mSwitchTask = SwitchTask.getInstance(this);
//        mSwitchTask.SetTaskList(WorkTimePoint.GetWorkTimePointList(list));
        mSwitchTask.TaskRun();
//        mSwitchTask.CheckCurrentTask();

        checkAppRunningForegroundTask = CheckAppRunningForegroundTask.getInstance(this);
        checkAppRunningForegroundTask.TaskRun();
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
            if (Act == 7) {
                SignBean signBean = SQLite.select().from(SignBean.class).where(SignBean_Table.CarNum.eq(num)).querySingle();
                if (signBean != null) {//已存在改对象，如果时间间隔大于2小时则记录，否则不记录
                    long[] distanceTimes = TimeUtils.getDistanceTimes(signBean.AttendanceTime, TimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
                    if (distanceTimes[0] > 0 || distanceTimes[1] > 1) {
                        new SignBean(num, TimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"), ext).save();
                    }
                } else {//不存在直接记录
                    new SignBean(num, TimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"), ext).save();
                }
            }
            mManager.sendCardNum(num, Act, ext);
//            LogUtils.d(SQLite.select().from(SignBean.class).queryList());
        } else if (intent != null && intent.hasExtra("test")) {
//            mManager.test();
//            mManager.messageHandle.msgHandleUtil.TakeVideoPic3("123.jpg");
        } else if (intent != null && intent.hasExtra("Log")) {
            try {
                mManager.messageHandle.msgHandleUtil.GetLog(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataStatusEvent(SignEvent event) {
        if (event.isStatus() && event.getType() == 1) {
            SQLite.delete()
                    .from(SignBean.class)
                    .where(SignBean_Table.CarNum.eq(event.getCardNum()))
                    .query();
        }
        LogUtils.d(SQLite.select().from(SignBean.class).queryList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onSendSignEvent(SendSignEvent event) {
        mManager.messageHandle.msgHandleUtil.SendSignData(0xA611);
    }

    /**
     * 上课界面返回主页后延时回归正常页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onCheckDelayEvent(CheckDelayEvent event) {
        if (mSwitchTask != null && event != null) {
            mSwitchTask.CheckCurrentTaskdelay(event.getSeconds());
        }else {
            logger.debug("mSwitchTask为空-->{}","空数据！！！！！！");
        }
    }
}
