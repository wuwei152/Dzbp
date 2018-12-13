package com.md.dzbp.model;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.SurfaceView;

import com.apkfuns.logutils.LogUtils;
import com.company.NetSDK.CB_fRealDataCallBackEx;
import com.company.NetSDK.CB_fSnapRev;
import com.company.NetSDK.EM_LOGIN_SPAC_CAP_TYPE;
import com.company.NetSDK.INetSDK;
import com.company.NetSDK.NET_DEVICEINFO_Ex;
import com.company.NetSDK.SDK_RealPlayType;
import com.company.NetSDK.SNAP_PARAMS;
import com.company.PlaySDK.IPlaySDK;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.utils.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 摄像头设置
 * Created by Administrator on 2017/11/29.
 */

public class DahuaModel {
    private final Context mContext;
    private int mPlayPort = 0;
    private long mRealHandle = 0;
    public long mLoginHandle = 0;
    private SurfaceView mSurface;
    private NET_DEVICEINFO_Ex mDeviceInfo;
    private final int STREAM_BUF_SIZE = 1024 * 1024 * 2;
    Map<Integer, Integer> streamTypeMap = new HashMap<Integer, Integer>();
    private CB_fRealDataCallBackEx mRealDataCallBackEx;
    private final int RAW_AUDIO_VIDEO_MIX_DATA = 0; ///原始音视频混合数据;
    private String TAG = "DahuaModel-->{}";
    private Logger logger;
    private DahuaListener listener;

    public DahuaModel(Context mContext, DahuaListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        init();
    }

    public DahuaModel(Context mContext, CameraInfo cameraInfo, DahuaListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        init();
        logout();
        LoginToSnap(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
    }

    public DahuaModel(Context context, SurfaceView mSurface) {
        this.mContext = context;
        this.mSurface = mSurface;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        try {
//            NetSDKLib.getInstance().init();
            mPlayPort = IPlaySDK.PLAYGetFreePort();
            ///码流类型的hash
            streamTypeMap.clear();
            streamTypeMap.put(0, SDK_RealPlayType.SDK_RType_Realplay_0);
            streamTypeMap.put(1, SDK_RealPlayType.SDK_RType_Realplay_1);
            logger = LoggerFactory.getLogger(getClass());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e.getMessage());
        }
    }

    /**
     * 初始化视频窗口
     *
     * @param sv
     */
    public void initSurfaceView(final SurfaceView sv) {
        if (sv == null)
            return;
        try {
            IPlaySDK.InitSurface(mPlayPort, sv);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e.getMessage());
        }
    }


    /**
     * 登录
     *
     * @param address
     * @param port
     * @param username
     * @param password
     * @return
     */
    public boolean login(String address, String port, String username, String password) {
        try {
            Integer err = new Integer(0);
            mDeviceInfo = new NET_DEVICEINFO_Ex();
            logger.debug(TAG, "开始登录");
            mLoginHandle = INetSDK.LoginEx2(address, Integer.parseInt(port), username, password, EM_LOGIN_SPAC_CAP_TYPE.EM_LOGIN_SPEC_CAP_MOBILE, null, mDeviceInfo, err);
            if (0 == mLoginHandle) {
                int mErrorCode = INetSDK.GetLastError();
                logger.debug(TAG, "登录失败！" + mErrorCode + "/" + address);
                if (listener != null)
                    listener.resLis(0, false, null);
                return false;
            }
            logger.debug(TAG, "登录成功！");
            return true;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            logger.error(TAG, "登录出错" + e.getMessage());
            if (listener != null)
                listener.resLis(0, false, null);
            return false;
        }
    }

    /**
     * 登出
     *
     * @return
     */
    public boolean logout() {
        logger.debug(TAG, "摄像头开始退出登录！");
        if (0 == mLoginHandle) {
            LogUtils.e("mLoginHandle为0");
            return false;
        }

        boolean retLogout = INetSDK.Logout(mLoginHandle);
        if (retLogout) {
            mLoginHandle = 0;
            logger.debug(TAG, "摄像头退出登录！");
        } else {
            logger.debug(TAG, "摄像头退出失败！");
        }

        return retLogout;
    }

    /**
     * 登录后播放任务
     */
    private class LoginAndPlayTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return login(params[0], params[1], params[2], params[3]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                logger.debug(TAG, "登录成功");
                startPlay();
            } else {
                logger.debug(TAG, "登录失败");
            }
        }
    }

    /**
     * 登录后抓图任务
     */
    private class LoginAndSnapTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            return login(params[0], params[1], params[2], params[3]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                logger.debug(TAG, "登录成功");
                snap(0);
            } else {
                logger.debug(TAG, "登录失败");
                listener.resLis(1, false, null);
            }
        }
    }

    /**
     * 登录后播放
     */
    public void LoginToPlay(String address, String port, String username, String password) {
        String[] params = {address, port, username, password};
        LoginAndPlayTask loginAndPlayTask = new LoginAndPlayTask();
        loginAndPlayTask.execute(params);
    }

    /**
     * 登录后抓图
     */
    public void LoginToSnap(String address, String port, String username, String password) {
        String[] params = {address, port, username, password};
        LoginAndSnapTask loginAndSnapTask = new LoginAndSnapTask();
        loginAndSnapTask.execute(params);
    }


    /**
     * 视频预览前设置
     *
     * @param channel
     * @param streamType
     * @param sv
     * @return
     */
    public boolean prePlay(int channel, int streamType, SurfaceView sv) {
        try {
            mRealHandle = INetSDK.RealPlayEx(mLoginHandle, channel, streamType);
            if (mRealHandle == 0) {
                return false;
            }
            boolean isOpened = IPlaySDK.PLAYOpenStream(mPlayPort, null, 0, STREAM_BUF_SIZE) == 0 ? false : true;
            if (!isOpened) {
                logger.debug(TAG, "OpenStream Failed");
                return false;
            }
            boolean isPlayin = IPlaySDK.PLAYPlay(mPlayPort, sv) == 0 ? false : true;
            if (!isPlayin) {
                logger.debug(TAG, "PLAYPlay Failed");
                IPlaySDK.PLAYCloseStream(mPlayPort);
                return false;
            }
            boolean isSuccess = IPlaySDK.PLAYPlaySoundShare(mPlayPort) == 0 ? false : true;
            if (!isSuccess) {
                logger.debug(TAG, "SoundShare Failed");
                IPlaySDK.PLAYStop(mPlayPort);
                IPlaySDK.PLAYCloseStream(mPlayPort);
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e.getMessage());
            return false;
        }
//        if (-1 == mCurVolume) {
//            mCurVolume = IPlaySDK.PLAYGetVolume(mPlayPort);
//        } else {
//            IPlaySDK.PLAYSetVolume(mPlayPort, mCurVolume);
//        }

    }

    /**
     * 开始预览视频
     */
    public void startPlay() {
        try {
            if (!prePlay(0, streamTypeMap.get(0), mSurface)) {
                logger.debug(TAG, "prePlay returned false..");
                return;
            }
            if (mRealHandle != 0) {
                mRealDataCallBackEx = new CB_fRealDataCallBackEx() {
                    @Override
                    public void invoke(long rHandle, int dataType, byte[] buffer, int bufSize, int param) {
                        //                    logger.debug(TAG, "dataType:" + dataType + "; bufSize:" + bufSize + "; param:" + param);
                        if (RAW_AUDIO_VIDEO_MIX_DATA == dataType) {
                            //                        logger.debug(TAG, "dataType == 0");
                            IPlaySDK.PLAYInputData(mPlayPort, buffer, buffer.length);
                        }
                    }
                };
                INetSDK.SetRealDataCallBackEx(mRealHandle, mRealDataCallBackEx, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e.getMessage());
        }
    }

    /**
     * 停止预览视频
     */
    public void stopPlay() {
        try {
            IPlaySDK.PLAYStop(mPlayPort);
            IPlaySDK.PLAYStopSoundShare(mPlayPort);
            IPlaySDK.PLAYCloseStream(mPlayPort);
            INetSDK.StopRealPlayEx(mRealHandle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e.getMessage());
        }
        mRealHandle = 0;
    }

    /**
     * 释放资源
     */
    public void releaseRes() {
        try {
            logout();
            IPlaySDK.PLAYStop(mPlayPort);
            IPlaySDK.PLAYStopSoundShare(mPlayPort);
            IPlaySDK.PLAYCloseStream(mPlayPort);
            INetSDK.StopRealPlayEx(mRealHandle);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e.getMessage());
        }
        mRealHandle = 0;
        mSurface = null;
    }


    /**
     * 远程抓图
     *
     * @param channel
     */
    public void snap(int channel) {


        //设置抓图回调
        TestfSnapRev stCb = new TestfSnapRev();
        INetSDK.SetSnapRevCallBack(stCb);

        ///发送抓图请求
        SNAP_PARAMS stSnapParam = new SNAP_PARAMS();
        stSnapParam.Channel = channel;
        stSnapParam.Quality = 1;
        stSnapParam.ImageSize = 1;
        stSnapParam.mode = 0;
        stSnapParam.InterSnap = 5;
        stSnapParam.CmdSerial = 100;
        if (INetSDK.SnapPictureEx(mLoginHandle, stSnapParam)) {
            logger.debug(TAG, "远程抓图成功");
            try {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        logger.debug(TAG, "远程抓图失败重试");
                        snap(0);
                    }
                }, 5000);
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(TAG, e.getMessage());
            }
        } else {
            logger.debug(TAG, "远程抓图失败");
            listener.resLis(1, false, null);
            return;
        }
    }

    Timer timer;

    /**
     * 抓图回调
     */
    public class TestfSnapRev implements CB_fSnapRev {
        @Override
        public void invoke(long lLoginID, byte pBuf[], int RevLen, int EncodeType, int CmdSerial) {
            logger.debug(TAG, "截图回调");
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(TAG, e.getMessage());
            }
            String strFileName = "";
            if (10 == EncodeType) {
                strFileName = createInnerAppFile("jpg");
            } else if (0 == EncodeType) {
                strFileName = createInnerAppFile("mpeg4");
            }

            logger.debug(TAG, "FileName:" + strFileName);
            if (TextUtils.isEmpty(strFileName)) {
                logger.debug(TAG, "异常：FileName为空");
                listener.resLis(1, false, null);
//                logout();
                return;
            }

            FileOutputStream fileStream = null;

            try {
                fileStream = new FileOutputStream(strFileName, true);
                logger.debug(TAG, "fileStream");
                fileStream.write(pBuf, 0, RevLen);
                fileStream.flush();
                listener.resLis(1, true, strFileName);
//                logout();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                listener.resLis(1, false, null);
//                logout();
                logger.error(TAG, e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                listener.resLis(1, false, null);
//                logout();
                logger.error(TAG, e.getMessage());
            } finally {
                try {
                    if (null != fileStream) {
                        fileStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error(TAG, e.getMessage());
                }
//                logout();
            }
        }
    }

    private synchronized String createInnerAppFile(String suffix) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String time = format.format(new Date());
        File dir = new File(FileUtils.getDiskCacheDir(mContext) + "Screenshot/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String file = FileUtils.getDiskCacheDir(mContext) + "Screenshot/snap_" + Constant.getDeviceId(mContext) + "_" + time.replace(":", "_") +
                "." + suffix;
        return file;
    }

}
