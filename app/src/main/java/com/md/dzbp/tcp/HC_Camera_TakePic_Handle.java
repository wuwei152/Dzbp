package com.md.dzbp.tcp;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.hcUtils.HCSdk;
import com.md.dzbp.utils.hcUtils.HCSdkManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

/**
 * @Author: ww
 * @CreateDate: 2020/11/19 16:35
 * @Description: 海康摄像头处理截屏后事件
 */
public class HC_Camera_TakePic_Handle {
    private static final String TAG = "HC_Camera_TakePic_Handle-->{}";
    private Logger logger;
    private Context context;
    private int retry;

    private String deviceId = "";
    private final ACache mACache;
    private MsgHandleUtil util;
    private final ArrayList<CameraInfo> mCameraInfos;

    public HC_Camera_TakePic_Handle(Context context, MsgHandleUtil util) {
        this.context = context;
        this.util = util;
        logger = LoggerFactory.getLogger(context.getClass());
        mACache = ACache.get(context);
        mCameraInfos = (ArrayList<CameraInfo>) mACache.getAsObject("CameraInfo");
        deviceId = Constant.getDeviceId(context);
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

        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            logger.debug(TAG, "开始登录截屏");
            CameraInfo cameraInfo = mCameraInfos.get(0);

            //登录设备
            HCSdk  hcSdk =  HCSdkManager.getInstance().initAndLogin(context, cameraInfo);
//            hcSdk = HCSdkManager.getNormalHCSdk(context, cameraInfo);
            String bitmapFileAbsolutePath = hcSdk.getImg();
            File file = new File(bitmapFileAbsolutePath);
            if (!TextUtils.isEmpty(bitmapFileAbsolutePath) && file.exists()) {
                util.CompressImg(new File(bitmapFileAbsolutePath), FileUtils.getDiskCacheDir(context) + "Screenshot/", new com.md.dzbp.model.OnCompressListener() {

                    @Override
                    public void onSuccess(File file) {
                        logger.debug(TAG, "压缩成功！" + file.getAbsolutePath());
                        util.UploadFile(file.getAbsolutePath(), Constant.Ftp_Camera, new FileHandle() {
                            @Override
                            public void handleSuccess(int code, String data) {
                                logger.debug(TAG, "上传snap成功！" + data + "/");
                                for (String s : OpenList) {
                                    util.yingda(0xA610, true, deviceId, data, s);
                                    logger.debug(TAG, "发送成功！" + data + "/" + s);
                                }
                                OpenList.clear();
                            }

                            @Override
                            public void handleFail(int code, String data) {
                                logger.debug(TAG, "上传snap失败！" + data);
                                for (String s : OpenList) {
                                    util.yingda(0xA610, false, deviceId, s);
                                }
                                OpenList.clear();
                            }

                            @Override
                            public void handleFinished(int code, String data) {
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        logger.debug(TAG, "压缩失败！");
                        for (String s : OpenList) {
                            util.yingda(0xA610, false, deviceId, s);
                        }
                        OpenList.clear();
                    }
                });
            } else {
                logger.debug(TAG, "摄像头截屏获取失败！");
                for (String s : OpenList) {
                    util.yingda(0xA610, false, deviceId, s);
                }
                OpenList.clear();
            }
        } else {
            logger.debug(TAG, "未获取到摄像头信息");
            for (String s : OpenList) {
                util.yingda(0xA610, false, deviceId, s);
            }
            OpenList.clear();
        }
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

        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            logger.debug(TAG, "开始登录截屏");
            CameraInfo cameraInfo = mCameraInfos.get(0);
            //登录设备
            HCSdk  hcSdk = HCSdkManager.getInstance().initAndLogin(context, cameraInfo);
//            hcSdk = HCSdkManager.getNormalHCSdk(context, cameraInfo);
            String bitmapFileAbsolutePath = hcSdk.getImg();
            LogUtils.d(bitmapFileAbsolutePath);
            File file1 = new File(bitmapFileAbsolutePath);
            if (!TextUtils.isEmpty(bitmapFileAbsolutePath) && file1.exists()) {
                util.CompressImg(new File(bitmapFileAbsolutePath), FileUtils.getDiskCacheDir(context) + "Screenshot/", new com.md.dzbp.model.OnCompressListener() {

                    @Override
                    public void onSuccess(File file) {
                        logger.debug(TAG, "压缩成功！" + file.getAbsolutePath());
                        util.UploadFile(file.getAbsolutePath(), Constant.Ftp_Camera, new FileHandle() {
                            @Override
                            public void handleSuccess(int code, String data) {
                                logger.debug(TAG, "上传snap成功！" + data + "/");
                                for (Integer s : OpenList2) {
                                    util.yingda(0xA515, true, deviceId, data, s);
                                    logger.debug(TAG, "发送成功！" + data + "/" + s);
                                }
                                OpenList2.clear();
                            }

                            @Override
                            public void handleFail(int code, String data) {
                                logger.debug(TAG, "上传snap失败！" + data);
                                for (Integer s : OpenList2) {
                                    util.yingda(0xA515, false, deviceId, s);
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
                    public void onError() {
                        logger.debug(TAG, "压缩失败！");
                        for (Integer s : OpenList2) {
                            util.yingda(0xA515, false, deviceId, s);
                        }
                        OpenList2.clear();
                    }
                });
            } else {
                logger.debug(TAG, "摄像头截屏获取失败！");
                for (Integer s : OpenList2) {
                    util.yingda(0xA515, false, deviceId, s);
                }
                OpenList2.clear();
            }
        } else {
            logger.debug(TAG, "未获取到摄像头信息");
            for (Integer s : OpenList2) {
                util.yingda(0xA515, false, deviceId, s);
            }
            OpenList2.clear();
        }
    }

    /**
     * 获取摄像头截屏21
     * <p>
     * 页面点击班级名称截屏
     */

    ArrayList<String> OpenList21 = new ArrayList<>();

    public void TakeVideoPic2(String openId) {
        logger.debug(TAG, "openId:" + openId);
        retry = 0;
        if (!OpenList21.contains(openId)) {
            OpenList21.add(openId);
        }

        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            logger.debug(TAG, "开始登录截屏");
            CameraInfo cameraInfo = mCameraInfos.get(0);
            //登录设备
            HCSdk hcSdk = HCSdkManager.getInstance().initAndLogin(context, cameraInfo);
//            hcSdk = HCSdkManager.getNormalHCSdk(context, cameraInfo);
            String bitmapFileAbsolutePath = hcSdk.getImg();

            LogUtils.d(bitmapFileAbsolutePath);
            File file = new File(bitmapFileAbsolutePath);
            if (!TextUtils.isEmpty(bitmapFileAbsolutePath) && file.exists()) {
                util.CompressImg(new File(bitmapFileAbsolutePath), FileUtils.getDiskCacheDir(context) + "Screenshot/", new com.md.dzbp.model.OnCompressListener() {

                    @Override
                    public void onSuccess(File file) {
                        logger.debug(TAG, "压缩成功！" + file.getAbsolutePath());
                        util.UploadFile(file.getAbsolutePath(), Constant.Ftp_Camera, new FileHandle() {
                            @Override
                            public void handleSuccess(int code, String data) {
                                logger.debug(TAG, "上传snap成功！" + data + "/");
                                for (String s : OpenList21) {
                                    util.yingda(0xE515, true, deviceId, data, s, 1);
                                    logger.debug(TAG, "发送成功！" + data + "/" + s);
                                }
                                OpenList21.clear();
                            }

                            @Override
                            public void handleFail(int code, String data) {
                                logger.debug(TAG, "上传snap失败！" + data);
                                for (String s : OpenList21) {
                                    util.yingda(0xE515, false, deviceId, s);
                                }
                                OpenList21.clear();
                            }

                            @Override
                            public void handleFinished(int code, String data) {
                                logger.debug(TAG, "上传进程完成！" + data);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        logger.debug(TAG, "压缩失败！");
                        for (String s : OpenList21) {
                            util.yingda(0xE515, false, deviceId, s);
                        }
                        OpenList21.clear();
                    }
                });
            } else {
                logger.debug(TAG, "摄像头截屏获取失败！");
                for (String s : OpenList21) {
                    util.yingda(0xE515, false, deviceId, s);
                }
                OpenList21.clear();
            }
        } else {
            logger.debug(TAG, "未获取到摄像头信息");
            for (String s : OpenList21) {
                util.yingda(0xE515, false, deviceId, s);
            }
            OpenList21.clear();
        }
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

        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            logger.debug(TAG, "开始登录截屏");
            CameraInfo cameraInfo = mCameraInfos.get(0);

            //登录设备
            HCSdk hcSdk =  HCSdkManager.getInstance().initAndLogin(context, cameraInfo);
//            hcSdk = HCSdkManager.getNormalHCSdk(context, cameraInfo);
            String bitmapFileAbsolutePath = hcSdk.getImg();

            LogUtils.d(bitmapFileAbsolutePath);
            File file = new File(bitmapFileAbsolutePath);
            if (!TextUtils.isEmpty(bitmapFileAbsolutePath) && file.exists()) {
                logger.debug(TAG, "截屏成功:" + bitmapFileAbsolutePath);
                util.CompressImg(file, FileUtils.getDiskCacheDir(context) + "Screenshot/", new com.md.dzbp.model.OnCompressListener() {
                    @Override
                    public void onSuccess(File file) {
                        logger.debug(TAG, "压缩成功！" + file.getAbsolutePath());
                        File file2 = new File(file.getParent(), mFileName);
                        boolean rename = file.renameTo(file2);
                        logger.debug(TAG, "文件更名是否成功:" + rename);
                        logger.debug(TAG, "文件更名后为:" + file2.getAbsolutePath());
                        logger.debug(TAG, "文件是否存在:" + file2.exists());

                        util.UploadFile(file2.getAbsolutePath(), Constant.Ftp_Camera, new FileHandle() {
                            @Override
                            public void handleSuccess(int code, String data) {
                                util.yingda(0xA516, true, deviceId, data);
                                logger.debug(TAG, "发送成功！" + data + "/");
                                mFileName = "";
                            }

                            @Override
                            public void handleFail(int code, String data) {
                                logger.debug(TAG, "上传snap失败！" + data);
                                util.yingda(0xA516, false, deviceId, "");
                                mFileName = "";
                            }

                            @Override
                            public void handleFinished(int code, String data) {
                                logger.debug(TAG, "上传进程完成！" + data);
                            }
                        });
                    }

                    @Override
                    public void onError() {
                        logger.debug(TAG, "压缩失败！");
                        util.yingda(0xA515, false, deviceId, "");
                        mFileName = "";
                    }
                });
            } else {
                logger.debug(TAG, "摄像头截屏获取失败！");
                util.yingda(0xA515, false, deviceId, "");
                mFileName = "";
            }
        } else {
            logger.debug(TAG, "未获取到摄像头信息");
            util.yingda(0xA515, false, deviceId, "");
            mFileName = "";
        }
    }
}
