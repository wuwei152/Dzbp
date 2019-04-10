package com.md.dzbp.tcp;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.data.MsgSendStatus;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.data.SignBean;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.model.DahuaListener;
import com.md.dzbp.model.DahuaModel;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.service.UploadService;
import com.md.dzbp.ui.activity.ExamActivity;
import com.md.dzbp.ui.activity.MainActivity;
import com.md.dzbp.ui.activity.MeetingActivity;
import com.md.dzbp.ui.activity.NoticeActivity;
import com.md.dzbp.ui.activity.PatrolActivity;
import com.md.dzbp.ui.activity.SignActivity;
import com.md.dzbp.ui.activity.StudentActivity;
import com.md.dzbp.ui.activity.TeacherActivity;
import com.md.dzbp.ui.activity.VideoShowActivity;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.FileUtils;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.finalteam.toolsfinal.ManifestUtils;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * 处理TCP消息工具类
 * Created by Administrator on 2017/12/18.
 */

public class MsgHandleUtil {

    private static final String TAG = "MsgHandleUtil-->{}";
    private Logger logger;
    private Context context;
    private TcpClient client;
    private String deviceId = "";
    private final ACache mACache;
    private SmdtManager smdtManager;
    private MediaPlayer mp;
    private DahuaModel dahuaModel;
    private final ArrayList<CameraInfo> mCameraInfos;
    private int retry;
    private DahuaModel dahuaModel2;
    private DahuaModel dahuaModel3;

    public MsgHandleUtil(Context context, TcpClient client) {
        this.context = context;
        this.client = client;
        mACache = ACache.get(context);
        deviceId = Constant.getDeviceId(context);
        smdtManager = SmdtManager.create(context);
        logger = LoggerFactory.getLogger(context.getClass());
        mCameraInfos = (ArrayList<CameraInfo>) mACache.getAsObject("CameraInfo");
    }


    /**
     * 通用应答1
     *
     * @param xyh      协议号
     * @param b        是否成功
     * @param diviceId 设备号
     * @param id       消息ID
     */
    public void yingda(int xyh, boolean b, String diviceId, int id) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(id);
            message.Write(b);
            message.Write(diviceId, 36);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传考勤数据
     *
     * @param xyh 协议号
     */
    public void SendSignData(int xyh) {
        try {
            List<SignBean> signList = SQLite.select().from(SignBean.class).queryList();
            if (signList != null && signList.size() > 0) {
                logger.debug(TAG, "开始上传考勤本地数据");
                Intent intent = new Intent(context, UploadService.class);
                intent.putExtra("upload", "");
                context.startService(intent);
                HashMap<String, List<SignBean>> map = (HashMap) groupList(signList);
                for (String in : map.keySet()) {
                    List<SignBean> list = map.get(in);
                    String s = JSONArray.toJSONString(list);
                    logger.debug(TAG, s);
                    TCPMessage message = new TCPMessage(xyh);
                    message.Write(Constant.getDeviceId(context), 36);
                    int length2 = s.getBytes("UTF-8").length;
                    message.Write(length2);
                    message.Write(s, length2);
                    client.getTransceiver().send(message);
                }
            } else {
                logger.debug(TAG, "上传考勤无本地数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将集合拆分为10条一组
     *
     * @param list
     * @return
     */
    public Map groupList(List list) {

        int listSize = list.size();
        int toIndex = 10;
        Map map = new HashMap();     //用map存起来新的分组后数据
        int keyToken = 0;
        for (int i = 0; i < list.size(); i += 10) {
            if (i + 10 > listSize) {        //作用为toIndex最后没有100条数据则剩余几条newList中就装几条
                toIndex = listSize - i;
            }
            List newList = list.subList(i, i + toIndex);
            map.put("list" + keyToken, newList);
            keyToken++;
        }

        return map;
    }

    /**
     * 通用应答2
     *
     * @param xyh      协议号
     * @param b        是否成功
     * @param diviceId 设备号
     */
    public void yingda(int xyh, boolean b, String diviceId) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(b);
            message.Write(diviceId, 36);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通用应答3
     *
     * @param xyh      协议号
     * @param b        是否成功
     * @param diviceId 设备号
     */
    public void yingda(int xyh, boolean b, String diviceId, String id) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(b);
            message.Write(diviceId, 36);
            int length = id.getBytes("UTF-8").length;
            message.Write(length);
            message.Write(id, length);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开关屏应答
     *
     * @param xyh      协议号
     * @param type     类型  1打开背光 0关闭背光
     * @param b        是否成功
     * @param diviceId 设备号
     * @param msgid    消息ID
     */
    public void yingda(int xyh, int type, boolean b, String diviceId, int msgid) {
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
     * 截屏应答
     * 上传log应答
     *
     * @param xyh 协议号
     * @param b
     */
    public void yingda(int xyh, boolean b, String diviceId, String path, int id) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(id);
            message.Write(b);
            message.Write(diviceId, 36);
            int length2 = path.getBytes("UTF-8").length;
            message.Write(length2);
            message.Write(path, length2);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(TAG, e.getMessage());
        }
    }

    /**
     * 摄像头截屏应答
     *
     * @param xyh 协议号
     * @param b
     */
    private void yingda(int xyh, boolean b, String diviceId, String path, String openId) {
        try {
            TCPMessage message = new TCPMessage(xyh);
            message.Write(b);
            message.Write(diviceId, 36);
            int length2 = path.getBytes("UTF-8").length;
            message.Write(length2);
            message.Write(path, length2);
            int length3 = openId.getBytes("UTF-8").length;
            message.Write(length3);
            message.Write(openId, length3);
            client.getTransceiver().send(message);
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(TAG, e.getMessage());
        }
    }

    /**
     * 发消息应答
     *
     * @param xyh 协议号
     */
    public void yingda(int xyh, int msgType, String diviceId, String json) {
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
            logger.debug(TAG, e.getMessage());
        }
    }

    /**
     * 压缩文件
     *
     * @param file
     * @param targetDir
     * @param listener
     */

    public void CompressImg(File file, String targetDir, OnCompressListener listener) {
        Luban.with(context)
                .load(file)                                   // 传人要压缩的图片列表
                .ignoreBy(30)                                  // 忽略不压缩图片的大小
                .setTargetDir(targetDir)                        // 设置压缩后文件存储位置
                .setCompressListener(listener).launch();    //启动压缩
    }

    /**
     * 单文件上传
     *
     * @param fileDir    上传文件
     * @param ftpPath    上传路径
     * @param fileHandle 上传监听
     */
    public void UploadFile(final String fileDir, final String ftpPath, final FileHandle fileHandle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 上传
                File file = new File(fileDir);
                try {
                    //单文件上传
                    if (!file.exists()){
                        logger.error(TAG, "文件不存在！");
                        fileHandle.handleFail(1, "");
                        return;
                    }
                    new FTP(context).uploadSingleFile(file, ftpPath, new FTP.UploadProgressListener() {
                        @Override
                        public void onUploadProgress(String currentStep, long uploadSize, File file) {
                            // TODO Auto-generated method stub
                            logger.debug(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_SUCCESS)) {
                                fileHandle.handleSuccess(0, file.getName());
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_LOADING)) {
                                long fize = file.length();
                                float num = (float) uploadSize / (float) fize;
                                int result = (int) (num * 100);
                                logger.debug(TAG, "-----shangchuan----" + result + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_FAIL)) {
                                fileHandle.handleFail(1, "");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(TAG, e.getMessage());
                    fileHandle.handleFail(1, "");
                } finally {
                    fileHandle.handleFinished(1, "");
                }
            }
        }).start();
    }

    /**
     * 单文件下载
     *
     * @param fileDir    下载文件
     * @param fPath      下载路径
     * @param fName      文件名
     * @param fileHandle 下载监听
     */
    public void DownloadFile(final String fileDir, final String fPath, final String fName, final FileHandle fileHandle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 下载
                try {
                    //单文件下载
                    new FTP(context).downloadSingleFile(fileDir, fPath, fName, new FTP.DownLoadProgressListener() {

                        @Override
                        public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                            Log.d(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_DOWN_SUCCESS)) {
                                fileHandle.handleSuccess(0, file.getAbsolutePath());
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_LOADING)) {
                                logger.debug(TAG, "-----xiazai----" + downProcess + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_FAIL)) {
                                fileHandle.handleFail(1, "");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    fileHandle.handleFail(1, "");
                }
            }
        }).start();
    }

    /**
     * 上传Log
     *
     * @param msgid512
     */
    public void GetLog(final int msgid512) {
        File path = new File(FileUtils.getDiskCacheDir(context) + "Log/");
        //列出该目录下所有文件和文件夹
        File[] files = path.listFiles();
        if (files != null && files.length > 0) {
            //按照文件最后修改日期倒序排序
//            Arrays.sort(files, new Comparator<File>() {
//                @Override
//                public int compare(File file1, File file2) {
//                    return (int) (file2.lastModified() - file1.lastModified());
//                }
//            });
            File file_log = null;
            String cureentDate = TimeUtils.getCurrentDate2();

            for (File file : files) {
                String name = file.getName();
                name = name.substring(0, name.indexOf("l") - 1);
//                LogUtils.d(name);
                if (cureentDate.equals(name)) {
                    file_log = file;
                }
            }

//            LogUtils.d(cureentDate);

            if (file_log != null) {
                //取出第一个(即最新修改的)文件
//            logger.debug(TAG, "开始上传" + files[0].getAbsolutePath());
                logger.debug(TAG, "开始上传" + file_log.getAbsolutePath());
                UploadFile(file_log.getAbsolutePath(), Constant.Ftp_Log + Constant.getDeviceId(context) + "/", new FileHandle() {
                    @Override
                    public void handleSuccess(int code, String data) {
                        logger.debug(TAG, data + "-----shanchuan-log-successful");
                        if (msgid512 != 0) {
                            yingda(0xA512, true, deviceId, data, msgid512);
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "上传成功！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void handleFail(int code, String data) {
                        logger.debug(TAG, "-----shangchuan-log-fail---");
                        if (msgid512 != 0) {
                            yingda(0xA512, false, deviceId, msgid512);
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "上传失败！", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void handleFinished(int code, String data) {
                    }
                });
            }
        } else {
            if (msgid512 != 0) {
                yingda(0xA512, false, deviceId, msgid512);
            } else {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "未查询到log文件！", Toast.LENGTH_SHORT).show();
                    }
                });

            }
            logger.debug(TAG, "未查询到log文件");
        }
    }

    /**
     * 上传截图
     *
     * @param msgid505
     */
    public void TakeScreenshot(final int msgid505) {

        ScreenShotEvent event = new ScreenShotEvent(true, msgid505, deviceId);
        EventBus.getDefault().post(event);
//        try {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
//            final String timeStr = df.format(new Date());
//            logger.debug(TAG, "A505开始截屏");
//            File filedir = new File(FileUtils.getDiskCacheDir(context) + "Screenshot");
//            if (!filedir.exists()) {
//                filedir.mkdirs();
//            }
//            smdtManager.smdtTakeScreenshot(FileUtils.getDiskCacheDir(context) + "Screenshot/", "screenshot_" + timeStr + ".png", context);
//
////            BitmapUtils.getScreenViewBitmap(new MainActivity());
//
//            final File yuanfile = new File(FileUtils.getDiskCacheDir(context) + "Screenshot/screenshot_" + timeStr + ".png");
//            if (yuanfile.exists()) {
//                logger.debug(TAG, "获取截屏成功！");
//                CompressImg(yuanfile, FileUtils.getDiskCacheDir(context) + "Screenshot", new OnCompressListener() {
//                    @Override
//                    public void onStart() {
//                        logger.debug(TAG, "开始压缩");
//                    }
//
//                    @Override
//                    public void onSuccess(final File cplfile) {
//                        logger.debug(TAG, "压缩截屏文件成功！");
//                        UploadFile(cplfile.getAbsolutePath(), Constant.Ftp_Screenshot, new FileHandle() {
//                            @Override
//                            public void handleSuccess(int code, String data) {
//                                logger.debug(TAG, data + "-----shanchuan-jietu-successful");
//                                yingda(0xA505, true, deviceId, data, msgid505);
//                            }
//
//                            @Override
//                            public void handleFail(int code, String data) {
//                                logger.debug(TAG, "-----shangchuan-jietu-fail---");
//                                yingda(0xA505, false, deviceId, msgid505);
//                            }
//
//                            @Override
//                            public void handleFinished(int code, String data) {
//                                if (cplfile.exists()) {
//                                    cplfile.delete();
//                                }
//                                if (yuanfile.exists()) {
//                                    yuanfile.delete();
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        logger.debug(TAG, "压缩截屏文件失败！" + e.getMessage());
//                        yingda(0xA505, false, deviceId, msgid505);
//                    }
//                });
//            } else {
//                logger.debug(TAG, "获取截屏文件失败！");
//                yingda(0xA505, false, deviceId, msgid505);
//            }
//        } catch (Exception e) {
//            logger.debug(TAG, e.getMessage());
//        }
    }

    /**
     * 下载安装apk
     *
     * @param xyh
     * @param path
     */
    private int retryTime = 0;

    public void downloadFile(final int xyh, final String path, final int id) {

        DownloadFile(path, FileUtils.getDiskCacheDir(context) + "Apk/", "dzbp.apk", new FileHandle() {
            @Override
            public void handleSuccess(int code, String data) {
                logger.debug(TAG, "-----xiazai-apk-successful" + FileUtils.getDiskCacheDir(context) + "Apk/dzbp.apk");
                yingda(xyh, true, deviceId, id);
//                                smdtManager.smdtSilentInstall(FileUtils.getDiskCacheDir(context) + "Apk/dzbp.apk", context);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(new File(FileUtils.getDiskCacheDir(context) + "Apk/dzbp.apk")),
                        "application/vnd.android.package-archive");
                context.startActivity(intent);
            }

            @Override
            public void handleFail(int code, String data) {
                logger.debug(TAG, "-----xiazai-apk-fail---");
                new Handler(context.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (retryTime < 5) {
                            logger.debug(TAG, "-----xiazai-apk-fail-chongshi--");
                            downloadFile(xyh, path, id);
                        } else {
                            yingda(xyh, false, deviceId, id);
                        }
                        retryTime++;
                    }
                }, 5000);
            }

            @Override
            public void handleFinished(int code, String data) {
            }
        });
    }

    /**
     * 发送版本信息
     */
    public void sendVersionInfo(int xyh, boolean b, String diviceId, int id) {
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
     * 下载广播
     *
     * @param xyh
     * @param path
     */
    public void downloadBroadcast(final int xyh, final String path, final String fileName, final int id) {
        logger.debug(TAG, xyh + path + fileName);
        DownloadFile(path, FileUtils.getDiskCacheDir(context) + "BroadCast/", fileName, new FileHandle() {
            @Override
            public void handleSuccess(int code, String data) {
                logger.debug(TAG, "-----xiazai-guangbo-successful");
                yingda(xyh, true, deviceId, id);
                playBroad(FileUtils.getDiskCacheDir(context) + "BroadCast/" + fileName);
            }

            @Override
            public void handleFail(int code, String data) {
                logger.debug(TAG, "-----xiazai-guangbo-fail---");
                yingda(xyh, false, deviceId, id);
            }

            @Override
            public void handleFinished(int code, String data) {
            }
        });
    }

    /**
     * 播放广播
     */
    private void playBroad(final String path) {

        logger.debug(TAG, "开始播放" + path);
        if (mp == null) {
            mp = new MediaPlayer();
            logger.debug(TAG, "初始化");
        } else {
            mp.stop();
            mp.reset();
        }
        try {
            logger.debug(TAG, "设置资源");
            final FileInputStream is = new FileInputStream(path);
            mp.setDataSource(is.getFD());
//            mp.setDataSource(path);
            mp.prepareAsync();

            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    logger.debug(TAG, "开始播放");
                    mediaPlayer.start();
                }
            });

            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    logger.debug(TAG, "播放错误");
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        logger.debug(TAG, "重置播放器");
                    }
                    return false;
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    logger.debug(TAG, "播放完成");
                    if (is != null) {
                        try {
                            is.close();
                            File file = new File(path);
                            if (file.exists()) {
                                file.delete();
                            }
                        } catch (IOException e) {
                            logger.debug(TAG, e.getMessage());
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(TAG, e.getMessage());
        }
    }

    /**
     * 上传语音消息
     *
     * @param fileName
     * @param ftpPath
     * @param xyh
     */
    public void uploadVoiceFile(final int msgType, final String fileName, final String ftpPath, final int xyh, final String JsonMsg) {
        LogUtils.d(fileName + "$" + ftpPath);
        UploadFile(fileName, ftpPath, new FileHandle() {
            @Override
            public void handleSuccess(int code, String data) {
                logger.debug(TAG, fileName + "-----shanchuan--VoiceSendMessage--successful");
                yingda(xyh, msgType, deviceId, JsonMsg);
                VoiceSendMessage mMessage = JSON.parseObject(JsonMsg, new TypeReference<VoiceSendMessage>() {
                });
                MsgSendStatus msgSendStatus = new MsgSendStatus();
                msgSendStatus.setMsgTime(mMessage.getCreateTime());
                msgSendStatus.setStatus(1);
                EventBus.getDefault().post(msgSendStatus);
            }

            @Override
            public void handleFail(int code, String data) {
                logger.debug(TAG, "-----shangchuan--VoiceSendMessage--fail---");
                VoiceSendMessage mMessage = JSON.parseObject(JsonMsg, new TypeReference<VoiceSendMessage>() {
                });
                MsgSendStatus msgSendStatus = new MsgSendStatus();
                msgSendStatus.setMsgTime(mMessage.getCreateTime());
                msgSendStatus.setStatus(2);
                EventBus.getDefault().post(msgSendStatus);
            }

            @Override
            public void handleFinished(int code, String data) {
            }
        });
    }

    /**
     * 页面跳转
     *
     * @param code
     */
    public void handleFail(int code, int type) {
        logger.debug(TAG, "handleFail处理错误信息" + code);
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
        play(0);
    }

    /**
     * 页面跳转
     *
     * @param type
     */
    public void gotoActivity(final int type, final String userId, final String ext) {
        Intent intent = null;
        logger.debug(TAG, "开始跳转屏幕" + type);
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
                intent.putExtra("id", userId);
                break;
            case 6:
                intent = new Intent(context, MeetingActivity.class);
                break;
            case 7:
                intent = new Intent(context, SignActivity.class);
                break;
            case 8:
                intent = new Intent(context, VideoShowActivity.class);
                intent.putExtra("userId", userId);
                break;
            default:
                break;
        }

        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            logger.debug(TAG, "屏幕跳转成功" + type);
        } else {
            logger.debug(TAG, "屏幕跳转失败" + type);
        }
    }

    /**
     * 获取摄像头截屏
     * <p>
     * 发指令截屏
     */

    ArrayList<String> OpenList = new ArrayList<>();

    public void TakeVideoPic(String openId) {
        logger.debug(TAG, "openId:" + openId);
        retry = 0;
        if (!OpenList.contains(openId)) {
            OpenList.add(openId);
        }
        if (dahuaModel == null) {
            dahuaModel = new DahuaModel(context, new DahuaListener() {
                @Override
                public void resLis(int code, boolean isSuccess, String file) {
                    LogUtils.d(file);
                    if (isSuccess && !TextUtils.isEmpty(file)) {
                        CompressImg(new File(file), FileUtils.getDiskCacheDir(context) + "Screenshot/", new OnCompressListener() {
                            @Override
                            public void onStart() {
                                logger.debug(TAG, "开始压缩！");
                            }

                            @Override
                            public void onSuccess(File file) {
                                logger.debug(TAG, "压缩成功！"+file.getAbsolutePath());
                                UploadFile(file.getAbsolutePath(), Constant.Ftp_Camera, new FileHandle() {
                                    @Override
                                    public void handleSuccess(int code, String data) {
                                        logger.debug(TAG, "上传snap成功！" + data + "/");
                                        for (String s : OpenList) {
                                            yingda(0xA610, true, deviceId, data, s);
                                            logger.debug(TAG, "发送成功！" + data + "/" + s);
                                        }
                                        OpenList.clear();
                                    }

                                    @Override
                                    public void handleFail(int code, String data) {
                                        logger.debug(TAG, "上传snap失败！" + data);
                                        for (String s : OpenList) {
                                            yingda(0xA610, false, deviceId, s);
                                        }
                                        OpenList.clear();
                                    }

                                    @Override
                                    public void handleFinished(int code, String data) {
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                logger.debug(TAG, "压缩失败！");
                                for (String s : OpenList) {
                                    yingda(0xA610, false, deviceId, s);
                                }
                                OpenList.clear();
                            }
                        });
                    } else {
                        if (retry == 0) {
                            retry = 1;
                            logger.debug(TAG, "摄像头截屏获取失败重试中。。。！");
                            CameraInfo cameraInfo = mCameraInfos.get(0);
                            dahuaModel.logout();
                            dahuaModel.LoginToSnap(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
                            return;
                        }
                        logger.debug(TAG, "摄像头截屏获取失败！");
                        for (String s : OpenList) {
                            yingda(0xA610, false, deviceId, s);
                        }
                        OpenList.clear();
                    }
//                    dahuaModel.logout();
                }
            });
        }
//            if (dahuaModel.mLoginHandle == 0) {
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            logger.debug(TAG, "开始登录截屏");
            dahuaModel.logout();
            CameraInfo cameraInfo = mCameraInfos.get(0);
            dahuaModel.LoginToSnap(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
        } else {
            logger.debug(TAG, "未获取到摄像头信息");
            for (String s : OpenList) {
                yingda(0xA610, false, deviceId, s);
            }
            OpenList.clear();
        }
//            }
//            else {
//                logger.debug(TAG, "开始直接截屏");
//                dahuaModel.snap(0);
//            }
    }

    /**
     * 获取摄像头截屏2
     * <p>
     * 页面点击班级名称截屏
     */

    ArrayList<Integer> OpenList2 = new ArrayList<>();

    public void TakeVideoPic2(int openId) {
        logger.debug(TAG, "openId:" + openId);
        retry = 0;
        if (!OpenList2.contains(openId)) {
            OpenList2.add(openId);
        }
        if (dahuaModel2 == null) {
            dahuaModel2 = new DahuaModel(context, new DahuaListener() {
                @Override
                public void resLis(int code, boolean isSuccess, String file) {
                    LogUtils.d(file);
                    if (isSuccess && !TextUtils.isEmpty(file)) {
                        CompressImg(new File(file), FileUtils.getDiskCacheDir(context) + "Screenshot/", new OnCompressListener() {
                            @Override
                            public void onStart() {
                                logger.debug(TAG, "开始压缩！");
                            }

                            @Override
                            public void onSuccess(File file) {
                                logger.debug(TAG, "压缩成功！"+file.getAbsolutePath());
                                UploadFile(file.getAbsolutePath(), Constant.Ftp_Camera, new FileHandle() {
                                    @Override
                                    public void handleSuccess(int code, String data) {
                                        logger.debug(TAG, "上传snap成功！" + data + "/");
                                        for (Integer s : OpenList2) {
                                            yingda(0xA515, true, deviceId, data, s);
                                            logger.debug(TAG, "发送成功！" + data + "/" + s);
                                        }
                                        OpenList2.clear();
                                    }

                                    @Override
                                    public void handleFail(int code, String data) {
                                        logger.debug(TAG, "上传snap失败！" + data);
                                        for (Integer s : OpenList2) {
                                            yingda(0xA515, false, deviceId, s);
                                        }
                                        OpenList2.clear();
                                    }

                                    @Override
                                    public void handleFinished(int code, String data) {
                                        logger.debug(TAG, "上传进程完成！" + data);
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                logger.debug(TAG, "压缩失败！");
                                for (Integer s : OpenList2) {
                                    yingda(0xA515, false, deviceId, s);
                                }
                                OpenList2.clear();
                            }
                        });
                    } else {
                        if (retry == 0) {
                            retry = 1;
                            logger.debug(TAG, "摄像头截屏获取失败重试中。。。！");
                            CameraInfo cameraInfo = mCameraInfos.get(0);
                            dahuaModel2.logout();
                            dahuaModel2.LoginToSnap(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
                            return;
                        }
                        logger.debug(TAG, "摄像头截屏获取失败！");
                        for (Integer s : OpenList2) {
                            yingda(0xA515, false, deviceId, s);
                        }
                        OpenList2.clear();
                    }
//                    dahuaModel.logout();
                }
            });
        }
//            if (dahuaModel.mLoginHandle == 0) {
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            logger.debug(TAG, "开始登录截屏");
            dahuaModel2.logout();
            CameraInfo cameraInfo = mCameraInfos.get(0);
            dahuaModel2.LoginToSnap(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
        } else {
            logger.debug(TAG, "未获取到摄像头信息");
            for (Integer s : OpenList2) {
                yingda(0xA515, false, deviceId, s);
            }
            OpenList2.clear();
        }
//            }
//            else {
//                logger.debug(TAG, "开始直接截屏");
//                dahuaModel.snap(0);
//            }
    }

    /**
     * 获取摄像头截屏3
     * <p>
     * 考勤统计截屏
     */

    String mFileName = "";

    public void TakeVideoPic3(String fileName) {
        logger.debug(TAG, "开始:" + fileName);
        retry = 0;
        mFileName = fileName;
        if (dahuaModel3 == null) {
            dahuaModel3 = new DahuaModel(context, new DahuaListener() {
                @Override
                public void resLis(int code, boolean isSuccess, String file) {
                    LogUtils.d(file);
                    if (isSuccess && !TextUtils.isEmpty(file)) {
                        logger.debug(TAG, "截屏成功:" + file);
                        File file1 = new File(file);
                        CompressImg(file1, FileUtils.getDiskCacheDir(context) + "Screenshot/", new OnCompressListener() {
                            @Override
                            public void onStart() {
                                logger.debug(TAG, "开始压缩！");
                            }

                            @Override
                            public void onSuccess(File file) {
                                logger.debug(TAG, "压缩成功！"+file.getAbsolutePath());
                                File file2 = new File(file.getParent(), mFileName);
                                boolean rename = file.renameTo(file2);
                                logger.debug(TAG, "文件更名是否成功:" + rename);
                                logger.debug(TAG, "文件更名后为:" + file2.getAbsolutePath());
                                logger.debug(TAG, "文件是否存在:" + file2.exists());

                                UploadFile(file2.getAbsolutePath(), Constant.Ftp_Camera, new FileHandle() {
                                    @Override
                                    public void handleSuccess(int code, String data) {
                                        yingda(0xA516, true, deviceId, data);
                                        logger.debug(TAG, "发送成功！" + data + "/");
                                        mFileName = "";
                                    }

                                    @Override
                                    public void handleFail(int code, String data) {
                                        logger.debug(TAG, "上传snap失败！" + data);
                                        yingda(0xA516, false, deviceId, "");
                                        mFileName = "";
                                    }

                                    @Override
                                    public void handleFinished(int code, String data) {
                                        logger.debug(TAG, "上传进程完成！" + data);
                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {
                                logger.debug(TAG, "压缩失败！");
                                yingda(0xA515, false, deviceId, "");
                                mFileName = "";
                            }
                        });
                    } else {
                        if (retry == 0) {
                            retry = 1;
                            logger.debug(TAG, "摄像头截屏获取失败重试中。。。！");
                            CameraInfo cameraInfo = mCameraInfos.get(0);
                            dahuaModel3.logout();
                            dahuaModel3.LoginToSnap(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
                            return;
                        }
                        logger.debug(TAG, "摄像头截屏获取失败！");
                        yingda(0xA515, false, deviceId, "");
                        mFileName = "";
                    }
//                    dahuaModel.logout();
                }
            });
        }
//            if (dahuaModel.mLoginHandle == 0) {
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            logger.debug(TAG, "开始登录截屏");
            dahuaModel3.logout();
            CameraInfo cameraInfo = mCameraInfos.get(0);
            dahuaModel3.LoginToSnap(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
        } else {
            logger.debug(TAG, "未获取到摄像头信息");
            yingda(0xA515, false, deviceId, "");
            mFileName = "";
        }
//            }
//            else {
//                logger.debug(TAG, "开始直接截屏");
//                dahuaModel.snap(0);
//            }
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
}
