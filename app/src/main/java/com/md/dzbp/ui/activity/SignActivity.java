package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.data.SignEvent;
import com.md.dzbp.data.SignInfoBean;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.service.UploadService;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.MainGestureDetector;
import com.md.dzbp.utils.SnapUtils;
import com.nanchen.compresshelper.CompressHelper;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 班级签到
 */
public class SignActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.sign_cardNum)
    EditText mCardNum;
    @BindView(R.id.sign_time)
    TextView mTime;
    @BindView(R.id.sign_date)
    TextView mDate;
    @BindView(R.id.sign_surface)
    SurfaceView mSurface;
    @BindView(R.id.sign_mainAddr)
    TextView mMainAddr;
    @BindView(R.id.sign_mainTitle)
    TextView mMainTitle;
    @BindView(R.id.sign_mainSub)
    TextView mMainSub;
    @BindView(R.id.sign_host)
    TextView mHost;
    @BindView(R.id.sign_Num)
    TextView mNum;
    @BindView(R.id.sign_listTitle)
    TextView mListTitle;
    @BindView(R.id.sign_listsub)
    TextView mListsub;
    @BindView(R.id.signing_GridView)
    GridView mGridView;
    @BindView(R.id.sign_temp)
    TextView mTemp;
    private SurfaceHolder.Callback callback;
    private Camera camera;
    private String TAG = "SignActivity-->{}";
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private Camera.Parameters parameters;
    private int Pwidth = 240;
    private int Phight = 320;
    private int retry = 0;
    private SignInfoBean signInfoBean;
    private List<SignInfoBean.StudentBean> studentsList;
    private ACache mAcache;
    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("onCreate");
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initUI() {
        EventBus.getDefault().register(this);
        mAcache = ACache.get(this);
        logger = LoggerFactory.getLogger(getClass());
        //获取时间日期
        new TimeUtils(SignActivity.this, this);
        //进度
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

        callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                initCamera(); // 用于启动摄像头

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                stopCamera(); // 用于关闭摄像头

            }
        };
        mSurface.getHolder().setFixedSize(Pwidth, Phight);
        //下面设置surfaceView不维护自己的缓冲区,而是等待屏幕的渲染引擎将内容推送到用户面前
        mSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurface.getHolder().addCallback(callback); // 将Callback绑定到SurfaceView

    }

    private void stopCamera() {
        if (camera != null) {
            LogUtils.d("stopcamera");
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "签到界面");
        Constant.SCREENTYPE = 7;
        LogUtils.d("onResume");
//        EventBus.getDefault().register(this);
        boolean cons = (boolean) mAcache.getAsObject("conStatus");
        if (cons) {
            mTemp.setText("连接状态：已连接");
            mTemp.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTemp.setText("连接状态：已断开");
            mTemp.setTextColor(getResources().getColor(R.color.conf));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
//        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        getUIdata();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //相机参数的初始化设置
    private void initCamera() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
//                    camera = Camera.open();
                    logger.debug(TAG, "initCamera");
                    camera = Camera.open(Camera.getNumberOfCameras() - 1);
                    camera.setPreviewDisplay(mSurface.getHolder());
                    //        camera.setDisplayOrientation(90);
                    parameters = camera.getParameters();
                    parameters.setPictureFormat(PixelFormat.JPEG);
                    parameters.setPictureSize(Pwidth, Phight);  // 部分定制手机，无法正常识别该方法。
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
                    camera.setParameters(parameters);
                    camera.startPreview();
                    camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error(TAG, e.getMessage());
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            retry++;
                            if (retry < 5) {
                                initCamera();
                            }
                        }
                    }, 5000);
                }
            }
        }).start();
    }

    @Override
    protected void initData() {
        getCardNum();
        mDate.setText(TimeUtils.getStringDate());
        getUIdata();

    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(SignActivity.this));
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_SIGN), true, map);
    }

    /**
     * 拍照
     */
    private void TakePicture(final String cardNum) {
        logger.debug(TAG, "开始拍照！");
        if (camera != null) {
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    try {
                        File filePath = new File(FileUtils.getDiskCacheDir(SignActivity.this), "sign");
                        if (!filePath.exists()) {
                            filePath.mkdirs();
                        }
                        File fileName = new File(filePath, System.currentTimeMillis() + (int) (Math.random() * 1000) + ".jpg");
                        fileName.createNewFile();
                        FileOutputStream fos = new FileOutputStream(fileName);
                        fos.write(data);
                        fos.flush();
                        fos.close();

                        Intent intent = new Intent(SignActivity.this, TcpService.class);
                        intent.putExtra("Num", cardNum);
                        intent.putExtra("Act", 7);
                        intent.putExtra("ext", fileName.getName());
                        startService(intent);

                        logger.debug(TAG, "拍照成功！");
                        compress(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else {
            Intent intent = new Intent(SignActivity.this, TcpService.class);
            intent.putExtra("Num", cardNum);
            intent.putExtra("Act", 7);
            intent.putExtra("ext", "");
            startService(intent);
        }

    }

    /**
     * 压缩图片
     *
     * @param oldFile
     */
    private void compress(File oldFile) {
        File newFile = new CompressHelper.Builder(this)
                .setMaxWidth(Pwidth)  // 默认最大宽度为720
                .setMaxHeight(Phight) // 默认最大高度为960
                .setQuality(70)    // 默认压缩质量为80
                .setFileName(oldFile.getName().substring(0, oldFile.getName().indexOf("."))) // 设置你需要修改的文件名
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(FileUtils.getDiskCacheDir(SignActivity.this) + "sign_compress")
                .build()
                .compressToFile(oldFile);
        logger.debug(TAG, "压缩图片！");
        Intent intent = new Intent(SignActivity.this, UploadService.class);
        intent.putExtra("file", newFile);
        startService(intent);
        if (oldFile.exists()) {
            oldFile.delete();
        }
    }

    /**
     * 获取刷卡卡号
     */
    private void getCardNum() {

        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum,this);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {
                    TakePicture(num);
                }
            }
        });
    }

    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                signInfoBean = JSON.parseObject(data.toString(), new TypeReference<SignInfoBean>() {
                });
                if (signInfoBean != null) {
                    setUIData(signInfoBean);
                }
            }
        }
    }

    /**
     * 设置界面数据
     */
    private void setUIData(SignInfoBean signInfoBean) {
        SignInfoBean.ClassInfoBean classInfo = signInfoBean.getClassInfo();
        studentsList = signInfoBean.getStudent();
        if (classInfo != null) {
            mMainAddr.setText(classInfo.getAddress());
            mMainSub.setText(classInfo.getGradeName() + classInfo.getClassName());
            mListTitle.setText(classInfo.getGradeName() + classInfo.getClassName());
            ;
            mListsub.setText(classInfo.getAddress());
        }
        if (studentsList != null && studentsList.size() > 0) {
            setGridData(studentsList);
        }
    }

    /**
     * 设置人员数据
     */
    private void setGridData(List students) {
        mGridView.setAdapter(new CommonAdapter<SignInfoBean.StudentBean>(SignActivity.this, R.layout.item_meeting_grid, students) {
            @Override
            protected void convert(ViewHolder viewHolder, SignInfoBean.StudentBean item, int position) {
                viewHolder.setText(R.id.item_meetgrid_name, item.getAccountname());
                if (item.getState() == 1) {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.green));
                } else {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.text_black));
                }
            }
        });
    }

    @Override
    public void showToast(String message) {
        myToast.toast(SignActivity.this, message);
    }

    @Override
    public void showDialog() {
        if (dialog != null && !mainDialog.isShowing()) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void dismissDialog() {
        if (dialog != null && !isFinishing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(final int errorCode, String errorMessage) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (errorCode == 0) {
                    getUIdata();
                }
            }
        }, 5000);
    }

    @Override
    public void cancelRequest() {
        netWorkRequest.CancelPost();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataStatusEvent(SignEvent event) {

        try {
            if (event.getType() == 1 && event.isStatus()) {
    //            showToast(event.getName() + "签到成功！");
                if (studentsList != null && studentsList.size() > 0) {
                    for (SignInfoBean.StudentBean m : studentsList) {
                        if (m.getAccountid().equals(event.getId())) {
                            m.setState(1);
                            logger.debug("SignActivity", "匹配成功，签到成功！");
                        }
                    }
                    setGridData(studentsList);
                }
            }
//        else if (event.getType() == 1 && !event.isStatus()) {
//            showToast("签到失败！");
//        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收到连接信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUpdateSynEvent2(LoginEvent event) {
        LogUtils.d("MainActivity接收到连接状态信息" + event.getType() + event.isStatus());
        if (event.isStatus()) {
            mTemp.setText("连接状态：已连接");
            mTemp.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTemp.setText("连接状态：已断开");
            mTemp.setTextColor(getResources().getColor(R.color.conf));
        }
    }

}
