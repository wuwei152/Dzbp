package com.md.dzbp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UploadService extends Service {

    private String TAG = "UploadService-->{}";
    private ExecutorService pool;
    private Logger logger;

    public UploadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        pool = Executors.newSingleThreadExecutor();
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.hasExtra("file")) {
            File file = (File) intent.getSerializableExtra("file");
            pool.execute(getUploadFileThread(file));
        }
        if (intent != null && intent.hasExtra("upload")) {
           UploadFile();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 上传
     */
    private Thread getUploadFileThread(final File file) {

        logger.debug(TAG, "开始上传相片");
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //单文件上传
                    new FTP(UploadService.this).uploadSingleFile(file, Constant.Ftp_Snapshot, new FTP.UploadProgressListener() {

                        @Override
                        public void onUploadProgress(String currentStep, long uploadSize, File file) {
                            // TODO Auto-generated method stub
                            logger.debug(TAG, currentStep);
                            if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_SUCCESS)) {
                                logger.debug(TAG, "-----shanchuan-sign-successful");
                                file.delete();
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_LOADING)) {
                                long fize = file.length();
                                float num = (float) uploadSize / (float) fize;
                                int result = (int) (num * 100);
                                logger.debug(TAG, "-----shangchuan--sign-" + result + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_FAIL)) {
                                logger.debug(TAG, "-----shangchuan-sign-fail---");
                            }
                        }
                    });
                } catch (IOException e) {
                    logger.error(TAG, e.getMessage());
                }
            }
        });
    }


    /**
     * 离线上传
     */
    private void UploadFile() {

        File file1 = new File(FileUtils.getDiskCacheDir(this) + "sign_compress");
        if (file1 != null && file1.exists()) {
            File[] childFile = file1.listFiles();
            if (childFile != null && childFile.length > 0) {
                for (File file2 : childFile) {
                    try {
                        //单文件上传
                        if (file2 != null && file2.exists()) {

                            LogUtils.d("开始上传:" + file2.getName());
                            new FTP(UploadService.this).uploadSingleFile(file2, Constant.Ftp_Snapshot, new FTP.UploadProgressListener() {

                                @Override
                                public void onUploadProgress(String currentStep, long uploadSize, File file) {
                                    // TODO Auto-generated method stub
                                    logger.debug(TAG, currentStep);
                                    if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_SUCCESS)) {
                                        logger.debug(TAG, "-----shanchuan-sign-successful");
                                        file.delete();
                                    } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_LOADING)) {
                                        long fize = file.length();
                                        float num = (float) uploadSize / (float) fize;
                                        int result = (int) (num * 100);
                                        logger.debug(TAG, "-----shangchuan--sign-" + result + "%");
                                    } else if (currentStep.equals(ERRORTYPE.FTP_UPLOAD_FAIL)) {
                                        logger.debug(TAG, "-----shangchuan-sign-fail---");
                                    }
                                }
                            });

                            //开始上传一个文件等待一段时间再上传另一个
                            Thread.sleep(500);
                        }
                    } catch (Exception e) {
                        logger.error(TAG, e.getMessage());
                    }
                }
            }
        }
    }

        @Override
        public void onDestroy () {
            super.onDestroy();
            if (pool != null){
                pool.shutdown ();
            }
        }
    }
