package com.md.dzbp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.tcp.FileHandle;
import com.md.dzbp.utils.luban.Luban;
import com.md.dzbp.utils.luban.OnCompressListener;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 用于屏幕截图的工具类
 * Created by Administrator on 2018/9/26.
 */

public class SnapUtils {

    private final Logger logger;
    private Context context;
    private static final String TAG = "SnapUtils-->{}";
    private String devId;
    private int msgId;

    public SnapUtils(Context context, String devId, int msgId) {
        this.context = context;
        this.devId = devId;
        this.msgId = msgId;
        logger = LoggerFactory.getLogger(context.getClass());
    }
    //******************************************************************************************
    //                                                                            屏幕截图
    //******************************************************************************************

    /**
     * 获取指定Activity整个屏幕截图，这种方式只能获取当前应用的截图（连顶部状态栏中的时间等都获取不到）
     */
    public Bitmap snapShot(Activity activity) {
        int statusBarHeight = 0;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;

        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metric);
        return snapShot(activity, 0, statusBarHeight, metric.widthPixels, metric.heightPixels- (statusBarHeight*3));
    }

    /**
     * 获取指定Activity指定View的屏幕截图，这种方式只能获取当前应用的截图（连顶部状态栏中的时间等都获取不到）
     *
     * @param activity
     * @param view
     * @return
     */
    public Bitmap snapShot(Activity activity, View view) {
        return snapShot(activity, (int) view.getX(), (int) view.getY(), (int) view.getWidth(), (int) view.getHeight());
    }

    /**
     * 获取指定Activity指定区域的屏幕截图，这种方式只能获取当前应用的截图（连顶部状态栏中的时间等都获取不到）
     *
     * @param activity 所要截取的Activity
     * @param x        The x coordinate of the first pixel in source
     * @param y        The y coordinate of the first pixel in source
     * @param width    The number of pixels in each row
     * @param height   The number of rows
     * @return A copy of a subset of the source bitmap or the source bitmap itself.
     */
    public Bitmap snapShot(Activity activity, int x, int y, int width, int height) {
        try {
            View view = activity.getWindow().getDecorView();
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache();
            Bitmap cache = view.getDrawingCache();
            Bitmap bitmap = Bitmap.createBitmap(cache, x, y, width, height);
            view.destroyDrawingCache();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存bitmap为图片
     *
     * @param bitmap
     * @param fileName 文件名，注意是保存在了SD卡根目录下
     */
    public File saveBitmap2Pic(Bitmap bitmap, String fileName) {
        try {
            File filedir = new File(FileUtils.getDiskCacheDir(context) + "Screenshot");
            if (!filedir.exists()){
                filedir.mkdirs();
            }
            File file = new File(FileUtils.getDiskCacheDir(context) + "Screenshot", fileName);
            file.createNewFile();//在window中没问题，但是在Android上必须加这一句，否然报异常 java.io.FileNotFoundException: xxx: open failed: EROFS (Read-only file system)
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void requestScreenShot() {
        Bitmap bitmap = snapShot((Activity) context);
        if (bitmap != null) {
            final File file = saveBitmap2Pic(bitmap, SystemClock.currentThreadTimeMillis() + ".png");
            if (file != null) {

                CompressImg(file, FileUtils.getDiskCacheDir(context) + "Screenshot", new OnCompressListener() {
                    @Override
                    public void onStart() {
                        logger.debug(TAG, "开始压缩");
                    }

                    @Override
                    public void onSuccess(final File cplfile) {
                        logger.debug(TAG, "压缩截屏文件成功！");
                        UploadFile(cplfile.getAbsolutePath(), Constant.Ftp_Screenshot, new FileHandle() {
                            @Override
                            public void handleSuccess(int code, String data) {
                                ScreenShotEvent event = new ScreenShotEvent(msgId, devId, data, true);
                                EventBus.getDefault().post(event);
                                logger.debug(TAG, data + "-----shanchuan-jietu-successful");
                            }

                            @Override
                            public void handleFail(int code, String data) {
                                ScreenShotEvent event = new ScreenShotEvent(msgId, devId, data, false);
                                EventBus.getDefault().post(event);
                                logger.debug(TAG, "-----shangchuan-jietu-fail---");
                            }

                            @Override
                            public void handleFinished(int code, String data) {
                                if (file.exists()) {
                                    file.delete();
                                }
                                if (cplfile.exists()) {
                                    cplfile.delete();
                                }
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.debug(TAG, "压缩截屏文件失败！" + e.getMessage());
                        ScreenShotEvent event = new ScreenShotEvent(msgId, devId, "", false);
                        EventBus.getDefault().post(event);
                    }
                });

            } else {
                ScreenShotEvent event = new ScreenShotEvent(msgId, devId, "", false);
                EventBus.getDefault().post(event);
                logger.debug(TAG, "存储截屏文件失败！");
            }
        } else {
            ScreenShotEvent event = new ScreenShotEvent(msgId, devId, "", false);
            EventBus.getDefault().post(event);
            logger.debug(TAG, "获取截屏失败！");
        }
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
                } catch (IOException e) {
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

}
