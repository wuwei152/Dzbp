package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CourseBean;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.model.DahuaModel;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 教师打卡后视频查看页面
 */
public class VideoShowActivity extends BaseActivity implements SurfaceHolder.Callback,TimeListener, UIDataListener {

    @BindView(R.id.videoshow_time)
    TextView mTime;
    @BindView(R.id.videoshow_date)
    TextView mDate;
    @BindView(R.id.videoshow_temp)
    TextView mTemp;
    @BindView(R.id.videoshow_img)
    ImageView mImg;
    @BindView(R.id.videoshow_cardNum)
    EditText mCardNum;
    @BindView(R.id.videoshow_className)
    TextView mClassName;
    @BindView(R.id.videoshow_courseName)
    TextView mCourseName;
    @BindView(R.id.videoshow_teacherName)
    TextView mTeacherName;
    @BindView(R.id.videoshow_periodName)
    TextView mPeriodName;
    @BindView(R.id.videoshow_addr)
    TextView mAddr;
    @BindView(R.id.videoshow_imgRl)
    RelativeLayout mImgRl;
    @BindView(R.id.videoshow_qiehuan)
    ImageView mQiehuan;
    @BindView(R.id.videoshow_mSurface)
    SurfaceView mSurface;

    private Handler _handler = null;
    private Handler foucus_handler = null;
    private String _stringTemp;
    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private ACache mAcache;
    private String TAG = "videoshowActivity-->{}";
    private Logger logger;
    private int type = 0;
    private int videoPosition = 0;
    private DahuaModel dahuaModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_videoshow;
    }

    @Override
    protected void initUI() {
        LogUtils.d("videoshow--onCreate");
        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(VideoShowActivity.this, onGestureListener);
        mAcache = ACache.get(this);
        dialog = MyProgressDialog.createLoadingDialog(VideoShowActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        logger = LoggerFactory.getLogger(getClass());
        mSurface.getHolder().addCallback(this);
    }

    @Override
    protected void initData() {
        dahuaModel = new DahuaModel(VideoShowActivity.this,mSurface);
        new TimeUtils(VideoShowActivity.this, this);

        mDate.setText(TimeUtils.getStringDate());
        getCardNum();

        getUIdata();
    }

    @OnClick({R.id.videoshow_qiehuan, R.id.videoshow_imgRl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.videoshow_qiehuan:
                if (videoPosition == 0) {
                    videoPosition = 1;
                } else {
                    videoPosition = 0;
                }
                showToast("切换成功，请稍后...");
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        getUIdata();
        type = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "教师视频界面");
        Constant.SCREENTYPE = 1;
        EventBus.getDefault().register(this);
        boolean cons = (boolean) mAcache.getAsObject("conStatus");
        if (cons) {
            mTemp.setText("连接状态：已连接");
            mTemp.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTemp.setText("连接状态：已断开");
            mTemp.setTextColor(getResources().getColor(R.color.conf));
        }
        dahuaModel.LoginToPlay();
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("解注册EventBus");
        EventBus.getDefault().unregister(this);
        dahuaModel.stopPlay();
        dahuaModel.logout();
    }

    @Override
    protected void onStop() {
        LogUtils.d("videoshow--onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("videoshowAct--onDestroy");
        dahuaModel.releaseRes();
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(this));
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_COURSE), true, map);
    }

    /**
     * 读取卡号
     */
    private void getCardNum() {
        mCardNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                _stringTemp = arg0.toString();
//                LogUtils.i("卡号"+arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                if (_handler == null) {
                    _handler = new Handler();
                    _handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LogUtils.d(_stringTemp);
                            if (!TextUtils.isEmpty(_stringTemp)) {
                                Intent intent = new Intent(VideoShowActivity.this, TcpService.class);
                                intent.putExtra("Num", _stringTemp);
                                intent.putExtra("Act", 8);
                                intent.putExtra("ext", "");
                                startService(intent);
                            }
                            mCardNum.setText("");
                            _handler = null;

                        }
                    }, 1000);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                LogUtils.d(arg0);
            }
        });
        foucus_handler = null;
        foucus_handler = new Handler();
        foucus_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardNum.requestFocus();
                foucus_handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 500 && Math.abs(x) > Math.abs(y)) {
                        mainDialog.dismiss();
                    } else if (x < -500 && Math.abs(x) > Math.abs(y)) {
                        mainDialog.show();
                    }
                    return true;
                }
            };


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                CourseBean courseBean = JSON.parseObject(data.toString(), new TypeReference<CourseBean>() {
                });
                if (courseBean != null) {
                    setUIData(courseBean);
                }
            }
        }
    }

    /**
     * 设置UI数据
     *
     * @param courseBean
     */
    private void setUIData(CourseBean courseBean) {

        if (!TextUtils.isEmpty(courseBean.getPhoto())) {
            Glide.with(this).load(courseBean.getPhoto()).into(mImg);
        }
        mClassName.setText(courseBean.getGradeName() + courseBean.getClassName());
        if (!TextUtils.isEmpty(courseBean.getSubjectName())) {
            mCourseName.setText("课程：" + courseBean.getSubjectName());
        }else {
            mCourseName.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(courseBean.getAccountName())) {
            mTeacherName.setText("教师：" + courseBean.getAccountName());
        }else {
            mTeacherName.setText("班主任：" + courseBean.getManagerAccountName());
        }
        if (!TextUtils.isEmpty(courseBean.getPeriodName())) {
            mPeriodName.setText("节次：" + courseBean.getPeriodName());
        }else {
            mCourseName.setVisibility(View.GONE);
        }
        mAddr.setText("教室：" + courseBean.getAddress());
    }

    @Override
    public void showToast(String message) {
        myToast.toast(VideoShowActivity.this, message);
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

    /**
     * 接收到连接信息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUpdateSynEvent2(LoginEvent event) {
        logger.debug(TAG, "videoshowActivity接收到连接状态信息" + event.getType() + event.isStatus());
        if (event.isStatus()) {
            mTemp.setText("连接状态：已连接");
            mTemp.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTemp.setText("连接状态：已断开");
            mTemp.setTextColor(getResources().getColor(R.color.conf));
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        dahuaModel.initSurfaceView(mSurface);
        LogUtils.d("surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
