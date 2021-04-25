package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.data.CourseBean;
import com.md.dzbp.model.DahuaModel;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.HorizontalListView;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.md.dzbp.utils.hcUtils.HCSdk;
import com.md.dzbp.utils.hcUtils.HCSdkManager;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
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


    @BindView(R.id.title_classAddr)
    TextView mAddr;
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

    @BindView(R.id.videoshow_courseName)
    TextView mCourseName;
    @BindView(R.id.videoshow_teacherName)
    TextView mTeacherName;
    @BindView(R.id.videoshow_periodName)
    TextView mPeriodName;
    @BindView(R.id.videoshow_addr)
    TextView mAddr2;
    @BindView(R.id.videoshow_mSurface)
    SurfaceView mSurface;
    @BindView(R.id.videoshow_videoList)
    HorizontalListView mVideoList;

    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private ACache mAcache;
    private String TAG = "videoshowActivity-->{}";
    private Logger logger;
    private int type = 0;
    private int videoPosition = 0;
    private DahuaModel dahuaModel;
    private ArrayList<CameraInfo> mCameraInfos;
    private String cameraType;
    private HCSdk hcSdk;

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
        mAcache = ACache.get(this);
        mCameraInfos = (ArrayList<CameraInfo>) mAcache.getAsObject("CameraInfo");

        cameraType = mAcache.getAsString("CameraType");
        if (TextUtils.isEmpty(cameraType)) {
            cameraType = "";
        }

        dialog = MyProgressDialog.createLoadingDialog(VideoShowActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        logger = LoggerFactory.getLogger(getClass());

        try {
            String className = mAcache.getAsString("ClassName");
            String gradeName = mAcache.getAsString("GradeName");
            String address = mAcache.getAsString("Address");
            String schoolName = mAcache.getAsString("SchoolName");
            String logo = mAcache.getAsString("Logo");
            String alias = mAcache.getAsString("Alias");

            mDate.setText(TimeUtils.getStringDate());
            mWeek.setText(TimeUtils.getStringWeek());
            //获取时间日期
            new TimeUtils(VideoShowActivity.this, this);

            mClassName.setText(gradeName + "\n\n" + className);
            mAddr.setText("教室编号:" + address);
            mAddr2.setText("教室:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(VideoShowActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            if (!TextUtils.isEmpty(alias) && !alias.equals("null")) {
                mAlias.setText("(" + alias + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e);
        }
    }

    @Override
    protected void initData() {
        getCardNum();

        getUIdata();

        if (mCameraInfos != null) {
            mVideoList.setAdapter(new CommonAdapter<CameraInfo>(VideoShowActivity.this, R.layout.item_video_list, mCameraInfos) {
                @Override
                protected void convert(final ViewHolder viewHolder, CameraInfo item, int position) {
                    viewHolder.setText(R.id.item_text, position + 1 + "路视频");
                }
            });
            mVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CameraInfo cameraInfo = mCameraInfos.get(position);
                    if (cameraInfo.getIsPlay() == 0) {
                        if (cameraType.equals("2")) {
                            hcSdk = HCSdkManager.getInstance().initAndLogin(VideoShowActivity.this, cameraInfo);
                            hcSdk.setSurfaceView(mSurface);
                            if (hcSdk != null) {
                                hcSdk.startSinglePreview();
                            }
                        } else {
                            dahuaModel.stopPlay();
                            dahuaModel.logout();
                            if (mSurface.isAttachedToWindow()) {
                                dahuaModel.LoginToPlay(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
                            }
                        }
                        for (int i = 0; i < mCameraInfos.size(); i++) {
                            mCameraInfos.get(i).setIsPlay(0);
                        }
                        cameraInfo.setIsPlay(1);
                    } else {
                        showToast("当前正在播放该路视频！");
                    }
                }
            });
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
        Constant.SCREENTYPE = 8;
        Act = 8;
        ext = "";
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        mSurface.setZOrderOnTop(true);
        mSurface.setZOrderMediaOverlay(true);
        mSurface.getHolder().addCallback(this);
        if (!cameraType.equals("2"))
            dahuaModel = new DahuaModel(VideoShowActivity.this, mSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("解注册EventBus");
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        LogUtils.d("videoshow--onStop");
        super.onStop();
        if (!cameraType.equals("2")) {
            dahuaModel.releaseRes();
        } else {
            if (hcSdk != null) {
                hcSdk.stopSinglePreview();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("videoshowAct--onDestroy");
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(this));
        map.put("timestamp", System.currentTimeMillis() + "");
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_COURSE), true, map);
    }

    /**
     * 读取卡号
     */
    private void getCardNum() {

        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum, this);
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
            mSurface.setVisibility(View.VISIBLE);
        } else {
            mSurface.setVisibility(View.GONE);
        }
        mClassName.setText(courseBean.getGradeName() + "\n\n" + courseBean.getClassName());
        if (!TextUtils.isEmpty(courseBean.getSubjectName())) {
            mCourseName.setText(courseBean.getSubjectName());
        } else {
            mCourseName.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(courseBean.getAccountName())) {
            mTeacherName.setText("教师：" + courseBean.getAccountName());
        } else {
            mTeacherName.setText("班主任：" + courseBean.getManagerAccountName());
        }
        if (!TextUtils.isEmpty(courseBean.getPeriodName())) {
            mPeriodName.setText(courseBean.getPeriodName());
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
        }, 30000);
    }

    @Override
    public void cancelRequest() {
        netWorkRequest.CancelPost();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        LogUtils.d("surfaceCreated");
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            CameraInfo cameraInfo = mCameraInfos.get(0);
            if (!cameraType.equals("2")) {
                dahuaModel.initSurfaceView(mSurface);
                dahuaModel.LoginToPlay(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
            } else {
                hcSdk =HCSdkManager.getInstance().initAndLogin(VideoShowActivity.this, cameraInfo);
                hcSdk.setSurfaceView(mSurface);
                if (hcSdk != null) {
                    hcSdk.startSinglePreview();
                }
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.d("surfaceDestroyed");
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            if (!cameraType.equals("2")) {
                dahuaModel.stopPlay();
                dahuaModel.logout();
            }
        }
    }

    @OnClick(R.id.videoshow_back)
    public void onViewClicked() {
        finish();
    }

}
