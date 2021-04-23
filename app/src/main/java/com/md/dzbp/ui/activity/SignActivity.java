package com.md.dzbp.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CheckDelayEvent;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.MainData;
import com.md.dzbp.data.SendSignEvent;
import com.md.dzbp.data.SignEvent;
import com.md.dzbp.data.SignInfoBean;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.service.UploadService;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.ProgressCustomView;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.GlideImgManager;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 班级签到
 */
public class SignActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.title_classAddr)
    TextView mMainAddr;
    @BindView(R.id.title_cardNum)
    EditText mCardNum;
    @BindView(R.id.title_sclIcon)
    ImageView mSclIcon;
    @BindView(R.id.title_schoolName)
    TextView mSchoolName;
    @BindView(R.id.title_className)
    TextView mClassName;
    @BindView(R.id.title_classAlias)
    TextView mAlias;
    @BindView(R.id.title_time)
    TextView mTime;
    @BindView(R.id.title_week)
    TextView mWeek;
    @BindView(R.id.title_date)
    TextView mDate;

    @BindView(R.id.sign_surface)
    SurfaceView mSurface;
    @BindView(R.id.signing_GridView)
    GridView mGridView;
    @BindView(R.id.sign_conStatus)
    ImageView mConStatus;
    @BindView(R.id.sign_clock)
    TextView mClock;
    @BindView(R.id.sign_progress)
    ProgressCustomView mProgress;
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
    private String mSignEndTime;
    private SimpleDateFormat sdf;
    private ArrayList<MainData.CourseBean> course;

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
        Intent intent = getIntent();
        if (intent.hasExtra("End")) {
            mSignEndTime = intent.getStringExtra("End");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }
        }

        callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                logger.debug(TAG, "surfaceCreated");
                initCamera(); // 用于启动摄像头
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                logger.debug(TAG, "surfaceDestroyed");
                stopCamera(); // 用于关闭摄像头
            }
        };


        try {
            String className = mAcache.getAsString("ClassName");
            String gradeName = mAcache.getAsString("GradeName");
            String address = mAcache.getAsString("Address");
            String schoolName = mAcache.getAsString("SchoolName");
            String logo = mAcache.getAsString("Logo");
            String alias = mAcache.getAsString("Alias");

            mClassName.setText(gradeName + "\n\n" + className);
            mMainAddr.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(SignActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            if (!TextUtils.isEmpty(alias) && !alias.equals("null")) {
                mAlias.setText("(" + alias + ")");
            }
        } catch (Exception e) {
            logger.error(TAG, e);
            e.printStackTrace();
        }

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
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        boolean cons = (boolean) mAcache.getAsObject("conStatus");
        if (cons) {
            mConStatus.setImageResource(R.drawable.lianwang);
        } else {
            mConStatus.setImageResource(R.drawable.lianwang_no);
        }
        mSurface.setZOrderOnTop(true);
        mSurface.setZOrderMediaOverlay(true);
        mSurface.getHolder().setFixedSize(Pwidth, Phight);
        //下面设置surfaceView不维护自己的缓冲区,而是等待屏幕的渲染引擎将内容推送到用户面前
//        mSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurface.getHolder().addCallback(callback); // 将Callback绑定到SurfaceView

//        mCardNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//
//                } else {
//                    logger.error(TAG, "失去焦点，开始请求焦点");
//                    mCardNum.setFocusable(true);
//                    mCardNum.setFocusableInTouchMode(true);
//                    mCardNum.requestFocus();
//                    mCardNum.findFocus();
//                }
//            }
//        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("End")) {
            mSignEndTime = intent.getStringExtra("End");
        }
        LogUtils.d("onNewIntent");
        getUIdata();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().post(new SendSignEvent());
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    //相机参数的初始化设置
    private void initCamera() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
//                    camera = Camera.open();
                    logger.debug(TAG, "摄像头开始初始化");
                    camera = Camera.open(0);
                    logger.debug(TAG, "摄像头开启");
                    camera.setPreviewDisplay(mSurface.getHolder());
                    //        camera.setDisplayOrientation(90);
                    parameters = camera.getParameters();
                    parameters.setPictureFormat(PixelFormat.JPEG);
//                    parameters.setPictureSize(Pwidth, Phight);  // 部分定制手机，无法正常识别该方法。
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
                    camera.setParameters(parameters);
                    camera.startPreview();
                    camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
                } catch (Exception e) {
                    logger.debug(TAG, "摄像头初始化失败");
                    e.printStackTrace();
                    logger.error(TAG, e);
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            retry++;
                            if (retry < 5) {
                                logger.debug(TAG, "摄像头初始化失败，重试中。。。" + retry);
                                startActivity(new Intent(SignActivity.this, MainActivity.class));
                                EventBus.getDefault().post(new CheckDelayEvent(5));
                            }
                        }
                    }, 15000);
                }
            }
        }).start();
    }

    @Override
    protected void initData() {
        getCardNum();
        mDate.setText(TimeUtils.getStringDate());
        mWeek.setText(TimeUtils.getStringWeek());
        sdf = new SimpleDateFormat("HH:mm:ss");
        course = (ArrayList<MainData.CourseBean>) mAcache.getAsObject("Course");
        getUIdata();

    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(SignActivity.this));
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_SIGN), false, map);
    }

    /**
     * 拍照
     */
    private void TakePicture(final String cardNum, final String picName) {
        logger.debug(TAG, "开始拍照！" + cardNum);
        if (camera != null) {
            camera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    try {
                        logger.debug(TAG, "拍照写入文件");
                        File filePath = new File(FileUtils.getDiskCacheDir(SignActivity.this), "sign");
                        if (!filePath.exists()) {
                            filePath.mkdirs();
                        }
                        File fileName = new File(filePath, picName);
                        fileName.createNewFile();
                        FileOutputStream fos = new FileOutputStream(fileName);
                        fos.write(data);
                        fos.flush();
                        fos.close();

                        logger.debug(TAG, "拍照成功！");
                        compress(fileName);
                    } catch (Exception e) {
                        logger.error(TAG, "拍照处理异常，不带文件上传考勤！" + e);
                        e.printStackTrace();
                    }
                }
            });
            logger.debug(TAG, "拍照结束！");
        } else {
            logger.debug(TAG, "camera为空异常，不带文件上传考勤！");
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

        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum, this);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {
                    String picName = System.currentTimeMillis() + (int) (Math.random() * 1000) + ".jpeg";
                    Intent intent = new Intent(SignActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 7);
                    intent.putExtra("ext", picName);
                    startService(intent);
                    TakePicture(num, picName);
                }
            }
        });
    }

    @Override
    public void getTime(String time) {
        mTime.setText(time);
        if (!TextUtils.isEmpty(mSignEndTime)) {
            mClock.setText(TimeUtils.longTimeToDay(TimeUtils.getRemainingTime(time, mSignEndTime, sdf)));
        }
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
            mMainAddr.setText("教室编号:" + classInfo.getAddress());
            mClassName.setText(classInfo.getGradeName() + "\n\n" + classInfo.getClassName());
        }
        if (studentsList != null && studentsList.size() > 0) {
            setGridData(studentsList);
        }
    }

    /**
     * 设置人员数据
     */
    private void setGridData(List<SignInfoBean.StudentBean> students) {

//        students.addAll(students);

        double signed = 0;
        for (SignInfoBean.StudentBean student : students) {
            if (student.getState() == 1) {
                signed++;
            }
        }


        int progress = (int) ((signed / students.size()) * 1000);
        logger.debug(TAG, "更新打卡进度" + progress);
        mProgress.setProgress(progress);
        mGridView.setAdapter(new CommonAdapter<SignInfoBean.StudentBean>(SignActivity.this, R.layout.item_meeting_grid, students) {
            @Override
            protected void convert(ViewHolder viewHolder, SignInfoBean.StudentBean item, int position) {
                viewHolder.setText(R.id.item_meetgrid_name, item.getAccountname());
                GlideImgManager.glideLoader(SignActivity.this, item.getPhoto(), R.drawable.head_icon, R.drawable.head_icon, (ImageView) (viewHolder.getView(R.id.item_meetgrid_img)), 0);
                if (item.getState() == 1) {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.green2));
                } else {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.text_gray));
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
        }, 30000);
    }

    @Override
    public void cancelRequest() {
        netWorkRequest.CancelPost();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataStatusEvent(SignEvent event) {
        try {
            if (event.getType() == 1 && event.isStatus()) {
                showToast(event.getName() + "签到成功！");
                if (studentsList != null && studentsList.size() > 0) {
                    for (SignInfoBean.StudentBean m : studentsList) {
                        if (m.getAccountid().equals(event.getId())) {
                            m.setState(1);
                            logger.debug("SignActivity", event.getName() + "签到成功！");
                        }

                    }
                    setGridData(studentsList);
                }
                play(1);
            } else if (event.getType() == 1 && !event.isStatus()) {
                play(0);
            }
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
            mConStatus.setImageResource(R.drawable.lianwang);
        } else {
            mConStatus.setImageResource(R.drawable.lianwang_no);
        }
    }

    private void play(int k) {
        logger.debug(TAG, "播放刷卡提示音");
        SoundPool soundPool;
        soundPool = new SoundPool(21, AudioManager.STREAM_SYSTEM, 10);
        if (k == 1) {
            soundPool.load(this, R.raw.card_sucsess, 1);
        } else {
            soundPool.load(this, R.raw.card_fails, 1);
        }
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                logger.debug(TAG, "开始播放！");
                soundPool.play(1, 1, 1, 0, 0, 1.2f);
            }
        });
    }

//    @OnClick(R.id.sign_daka)
//    public void onViewClicked() {
//        mProgress.setProgress(mProgress.getProgress()+100);
//    }
}
