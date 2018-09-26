package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
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
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.data.CourseBean;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.model.DahuaListener;
import com.md.dzbp.model.DahuaModel;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.HorizontalListView;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.md.dzbp.utils.MainGestureDetector;
import com.md.dzbp.utils.SnapUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 教师打卡后视频查看页面
 */
public class VideoShowActivity extends BaseActivity implements SurfaceHolder.Callback, TimeListener, UIDataListener {

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
    @BindView(R.id.videoshow_mSurface)
    SurfaceView mSurface;
    @BindView(R.id.videoshow_videoList)
    HorizontalListView mVideoList;

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
    private ArrayList<CameraInfo> mCameraInfos;

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
        EventBus.getDefault().register(this);
        LogUtils.d("videoshow--onCreate");
        mAcache = ACache.get(this);
        mCameraInfos = (ArrayList<CameraInfo>) mAcache.getAsObject("CameraInfo");

        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(VideoShowActivity.this, MainGestureDetector.getGestureDetector(mainDialog));
        dialog = MyProgressDialog.createLoadingDialog(VideoShowActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        logger = LoggerFactory.getLogger(getClass());
        mSurface.getHolder().addCallback(this);
    }

    @Override
    protected void initData() {
        dahuaModel = new DahuaModel(VideoShowActivity.this, mSurface);
        new TimeUtils(VideoShowActivity.this, this);

        mDate.setText(TimeUtils.getStringDate());
        getCardNum();

        getUIdata();

        mVideoList.setAdapter(new CommonAdapter<CameraInfo>(VideoShowActivity.this, R.layout.item_video_list, mCameraInfos) {
            @Override
            protected void convert(final ViewHolder viewHolder, CameraInfo item, int position) {
                new DahuaModel(VideoShowActivity.this, item, new DahuaListener() {
                    @Override
                    public void resLis(final int code, final boolean isSuccess, final String file) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isSuccess) {
                                    GlideImgManager.glideLoader(VideoShowActivity.this, file, R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_img));
                                }
                            }
                        });
                    }
                });
                viewHolder.setText(R.id.item_text, position + 1 + "路视频");
            }
        });
        mVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CameraInfo cameraInfo = mCameraInfos.get(position);
                dahuaModel.stopPlay();
                dahuaModel.logout();
                if (mSurface.isAttachedToWindow()) {
                    dahuaModel.LoginToPlay(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
                }
            }
        });
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("解注册EventBus");
        EventBus.getDefault().unregister(this);
        if (mainDialog != null && mainDialog.isShowing()) {
            mainDialog.dismiss();
        }
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
        EventBus.getDefault().unregister(this);
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

        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {
                    Intent intent = new Intent(VideoShowActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 8);
                    intent.putExtra("ext", "");
                    startService(intent);
                }
            }
        });
    }

    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }

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

        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            mImg.setVisibility(View.GONE);
            mSurface.setVisibility(View.VISIBLE);
        } else {
            mImg.setVisibility(View.VISIBLE);
            mSurface.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(courseBean.getImage())) {
                Glide.with(this).load(courseBean.getImage()).into(mImg);
            } else {
                Glide.with(this).load(R.drawable.teacher).into(mImg);
            }
        }
        mClassName.setText(courseBean.getGradeName() + courseBean.getClassName());
        if (!TextUtils.isEmpty(courseBean.getSubjectName())) {
            mCourseName.setText("课程：" + courseBean.getSubjectName());
        } else {
            mCourseName.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(courseBean.getAccountName())) {
            mTeacherName.setText("教师：" + courseBean.getAccountName());
        } else {
            mTeacherName.setText("班主任：" + courseBean.getManagerAccountName());
        }
        if (!TextUtils.isEmpty(courseBean.getPeriodName())) {
            mPeriodName.setText("节次：" + courseBean.getPeriodName());
        } else {
            mPeriodName.setVisibility(View.GONE);
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
        LogUtils.d("surfaceCreated");
        dahuaModel.initSurfaceView(mSurface);

        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            CameraInfo cameraInfo = mCameraInfos.get(0);
            dahuaModel.LoginToPlay(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.d("surfaceDestroyed");
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            dahuaModel.stopPlay();
            dahuaModel.logout();
        }
    }

    @OnClick(R.id.videoshow_back)
    public void onViewClicked() {
        finish();
    }

}
