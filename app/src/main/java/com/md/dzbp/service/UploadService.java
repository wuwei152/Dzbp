package com.md.dzbp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.ftp.FTP;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pool != null)
            pool.shutdown();
    }
}
