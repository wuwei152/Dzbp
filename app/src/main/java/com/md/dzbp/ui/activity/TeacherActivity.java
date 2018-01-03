package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.MainGestureDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * 上课页面
 */
public class TeacherActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.teacher_time)
    TextView mTime;
    @BindView(R.id.teacher_date)
    TextView mDate;
    @BindView(R.id.teacher_temp)
    TextView mTemp;
    @BindView(R.id.teacher_cardNum)
    EditText mCardNum;
    @BindView(R.id.teacher_img)
    ImageView mImg;
    @BindView(R.id.teacher_className)
    TextView mClassName;
    @BindView(R.id.teacher_courseName)
    TextView mCourseName;
    @BindView(R.id.teacher_teacherName)
    TextView mTeacherName;
    @BindView(R.id.teacher_periodName)
    TextView mPeriodName;
    @BindView(R.id.teacher_addr)
    TextView mAddr;
    @BindView(R.id.teacher_imgRl)
    RelativeLayout mImgRl;

    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private ACache mAcache;
    private String TAG = "TeacherActivity-->{}";
    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_teacher;
    }

    @Override
    protected void initUI() {
        LogUtils.d("teacher--onCreate");
        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(TeacherActivity.this, MainGestureDetector.getGestureDetector(mainDialog));
        mAcache = ACache.get(this);
        dialog = MyProgressDialog.createLoadingDialog(TeacherActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    protected void initData() {
        new TimeUtils(TeacherActivity.this, this);

        mDate.setText(TimeUtils.getStringDate());
        getCardNum();

        getUIdata();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        getUIdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "上课界面");
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
        LogUtils.d("teacher--onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("teacherAct--onDestroy");
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
                    Intent intent = new Intent(TeacherActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 1);
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
        if (!TextUtils.isEmpty(courseBean.getImage())) {
            Glide.with(this).load(courseBean.getImage()).into(mImg);
        } else {
            Glide.with(this).load(R.drawable.teacher).into(mImg);
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
        myToast.toast(TeacherActivity.this, message);
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
        logger.debug(TAG, "TeacherActivity接收到连接状态信息" + event.getType() + event.isStatus());
        if (event.isStatus()) {
            mTemp.setText("连接状态：已连接");
            mTemp.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTemp.setText("连接状态：已断开");
            mTemp.setTextColor(getResources().getColor(R.color.conf));
        }
    }
}
