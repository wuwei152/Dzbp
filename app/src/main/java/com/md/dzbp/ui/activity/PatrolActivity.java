package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.data.ClassInfoBean;
import com.md.dzbp.data.ClassManagerBean;
import com.md.dzbp.data.PatrolBean;
import com.md.dzbp.model.DahuaListener;
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
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.yjing.imageeditlibrary.editimage.EditImageActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PatrolActivity extends BaseActivity implements SurfaceHolder.Callback, TimeListener, UIDataListener {


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


    @BindView(R.id.patrol_teacherIcon)
    ImageView mTeacherIcon;
    @BindView(R.id.patrol_teacherName)
    TextView mTeacherName;
    @BindView(R.id.patrol_teacherCourse)
    TextView mTeacherCourse;
    @BindView(R.id.patrol_teacherPeroid)
    TextView mTeacherPeroid;
    @BindView(R.id.patrol_mngIcon)
    ImageView mMngIcon;
    @BindView(R.id.patrol_mngName)
    TextView mMngName;
    @BindView(R.id.patrol_listview)
    ListView mListview;
    @BindView(R.id.patrol_yingdao)
    TextView mYingdao;
    @BindView(R.id.patrol_shidao)
    TextView mShidao;
    @BindView(R.id.patrol_weidao)
    TextView mWeidao;
    @BindView(R.id.patrol_confrim)
    TextView mConfrim;
    @BindView(R.id.patrol_mSurface)
    SurfaceView mSurface;
    @BindView(R.id.patrol_videoList)
    HorizontalListView mVideoList;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private PatrolBean patrolBean;
    private String userId;
    private String TAG = "PatrolActivity-->{}";
    private Logger logger;
    private DahuaModel dahuaModel;
    private int videoPosition = 0;
    private ArrayList<CameraInfo> mCameraInfos;
    private ACache mAcache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_patrol;
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
        }
        mAcache = ACache.get(this);
        mCameraInfos = (ArrayList<CameraInfo>) mAcache.getAsObject("CameraInfo");

        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
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
            new TimeUtils(PatrolActivity.this, this);

            mClassName.setText(gradeName + "\n\n" + className);
            mAddr.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(PatrolActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            if (!TextUtils.isEmpty(alias) && !alias.equals("null")) {
                mAlias.setText("(" + alias + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        if (intent.hasExtra("userId")) {
            userId = intent.getStringExtra("userId");
        }
        if (patrolBean != null) {//防止未加载到数据显示上一次的数据
            patrolBean.setInspectionParameters(new ArrayList<PatrolBean.InspectionParametersBean>());
            setUIData(patrolBean);
        }
        getUIdata();
    }

    @Override
    protected void initData() {
        EventBus.getDefault().register(this);
        getCardNum();

        getUIdata();

        if (mCameraInfos != null) {
            mVideoList.setAdapter(new CommonAdapter<CameraInfo>(PatrolActivity.this, R.layout.item_video_list, mCameraInfos) {
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
                        dahuaModel.stopPlay();
                        dahuaModel.logout();
                        if (mSurface.isAttachedToWindow()) {
                            dahuaModel.LoginToPlay(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
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

//        dahuaModel.setListener(new DahuaListener() {
//            @Override
//            public void resLis(int code, boolean isSuccess, String file) {
//                dismissDialog();
//                if (isSuccess && !TextUtils.isEmpty(file)) {
//                    EditImageActivity.start(PatrolActivity.this, file, FileUtils.getDiskCacheDir(PatrolActivity.this) + "Screenshot/patrol_" + new File(file).getName(), 200);
//                } else {
//                    //出错：Can't create handler inside thread that has not called Looper.prepare()
//                    showToast("未获取到视频截图，请重试！");
//                }
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.d("onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "巡查界面");
        Constant.SCREENTYPE = 3;
        mSurface.setZOrderOnTop(true);
        mSurface.setZOrderMediaOverlay(true);
        mSurface.getHolder().addCallback(this);
        dahuaModel = new DahuaModel(PatrolActivity.this, mSurface);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        dahuaModel.releaseRes();
        LogUtils.d("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(PatrolActivity.this));
        map.put("accountId", userId);
        netWorkRequest.doGetRequest(0, Constant.getUrl(PatrolActivity.this, APIConfig.GET_PATROL), false, map);
    }

    /**
     * 获取卡号
     */
    private void getCardNum() {
        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum, this);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {
                    Intent intent = new Intent(PatrolActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 3);
                    intent.putExtra("ext", "");
                    startService(intent);
                }
            }
        });
    }


    @OnClick({R.id.patrol_back, R.id.patrol_confrim})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.patrol_back:
                finish();
                break;
//            case R.id.patrol_share:
//                showDialog();
//                dahuaModel.snap(0);
//                break;
            case R.id.patrol_confrim:
                ClassInfoBean classInfo = patrolBean.getClassInfo();
                PatrolBean.TeacherBean teacher = patrolBean.getTeacher();
                List<PatrolBean.InspectionParametersBean> inspectionParameters = patrolBean.getInspectionParameters();
                if (patrolBean != null && classInfo != null && teacher != null && inspectionParameters != null) {
                    JSONObject obj = new JSONObject();
                    obj.put("deviceId", Constant.getDeviceId(PatrolActivity.this));
                    obj.put("classId", classInfo.getClassId());
                    obj.put("teacherId", teacher.getAccountId());
                    obj.put("periodId", teacher.getPeriodId());
                    obj.put("subjectId", teacher.getSubjectId());
                    obj.put("createAccountId", userId);
                    JSONArray array = new JSONArray();
                    for (PatrolBean.InspectionParametersBean bean : inspectionParameters) {
                        JSONObject object = new JSONObject();
                        object.put("parameterId", bean.getId());
                        object.put("positive", bean.isPositive());
                        array.add(object);
                    }
                    obj.put("scores", array);
                    netWorkRequest.doPostRequest(1, Constant.getUrl(PatrolActivity.this, APIConfig.Post_PATROL), true, obj.toJSONString());
                } else {
                    showToast("未获取到巡查信息！");
                }
                break;
        }
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                patrolBean = JSON.parseObject(data.toString(), new TypeReference<PatrolBean>() {
                });
                if (patrolBean != null) {
                    setUIData(patrolBean);
                }
            }
        } else if (code == 1) {
            showToast("提交成功！");
            startActivity(new Intent(PatrolActivity.this, TeacherActivity.class));
        }
    }

    /**
     * 设置界面数据
     *
     * @param patrolBean
     */
    private void setUIData(PatrolBean patrolBean) {

        if (patrolBean == null) {
            return;
        }
        ClassInfoBean classInfo = patrolBean.getClassInfo();
        PatrolBean.TeacherBean teacher = patrolBean.getTeacher();
        ClassManagerBean classManager = patrolBean.getClassManager();
        if (classInfo != null) {
            mClassName.setText(classInfo.getGradeName() + "\n\n" + classInfo.getClassName());
            mAddr.setText("教室编号:" + classInfo.getAddress());
        }
        if (teacher != null) {
//            Glide.with(PatrolActivity.this).load(teacher.getPhoto()).into(mTeacherIcon);
            GlideImgManager.glideLoader(PatrolActivity.this, teacher.getPhoto(), R.drawable.icon_head_teacher2, R.drawable.icon_head_teacher2, mTeacherIcon, 1);
            mTeacherName.setText("教师：" + teacher.getAccountName());
            mTeacherCourse.setText(teacher.getSubjectName().toString());
            mTeacherPeroid.setText(teacher.getPeriodName());
        }

        if (classManager != null) {
            GlideImgManager.glideLoader(PatrolActivity.this, classManager.getPhoto(), R.drawable.icon_head_teacher, R.drawable.icon_head_teacher, mMngIcon, 0);
            mMngName.setText(classManager.getAccountName());
        }
        List<PatrolBean.InspectionParametersBean> inspectionParameters = patrolBean.getInspectionParameters();
        if (inspectionParameters != null && inspectionParameters.size() > 0) {
            mListview.setAdapter(new CommonAdapter<PatrolBean.InspectionParametersBean>(PatrolActivity.this, R.layout.item_patrol, inspectionParameters) {
                @Override
                protected void convert(ViewHolder viewHolder, final PatrolBean.InspectionParametersBean item, int position) {
                    viewHolder.setText(R.id.item_name, item.getParametername());

                    ((ToggleButton) viewHolder.getView(R.id.item_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b) {
                                item.setPositive(true);
                            } else {
                                item.setPositive(false);
                            }
                        }
                    });
                }
            });
            mConfrim.setVisibility(View.VISIBLE);
        } else {
            mConfrim.setVisibility(View.INVISIBLE);

        }

        PatrolBean.AttendanceBean attendance = patrolBean.getAttendance();
        if (attendance != null) {
            mYingdao.setText(attendance.getYindao() + "");
            mShidao.setText(attendance.getShidao() + "");
            mWeidao.setText(attendance.getWeidao() + "");
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(PatrolActivity.this, message);
    }

    @Override
    public void showDialog() {
        if (dialog != null) {
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
        if (errorCode == 1) {
            showToast("提交失败！");
            return;
        }
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
        dahuaModel.initSurfaceView(mSurface);
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            CameraInfo cameraInfo = mCameraInfos.get(0);
            dahuaModel.LoginToPlay(cameraInfo.getIp(), cameraInfo.getPort(), cameraInfo.getUsername(), cameraInfo.getPsw());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        LogUtils.d("surfaceChanged");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        LogUtils.d("surfaceDestroyed");
        if (mCameraInfos != null && mCameraInfos.size() > 0) {
            dahuaModel.stopPlay();
            dahuaModel.logout();
        }
    }

    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }

    /**
     * 接收摄像头截图编辑完后图片
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUpdateSynEvent(HashMap<String, String> event) {
        LogUtils.d("编辑成功：" + event);
    }

}
