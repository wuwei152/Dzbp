package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
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
import com.md.dzbp.data.CheckDelayEvent;
import com.md.dzbp.data.CourseBean;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.MainData;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.GetCardNumUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上课页面
 */
public class TeacherActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.teacher_back)
    TextView mBack;
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
    @BindView(R.id.teacher_qrcode)
    ImageView mQrcode;

    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private ACache mAcache;
    private String TAG = "TeacherActivity-->{}";
    private Logger logger;
    private String className;
    private String address;
    private String gradeName;

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
        mAcache = ACache.get(this);
        dialog = MyProgressDialog.createLoadingDialog(TeacherActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        logger = LoggerFactory.getLogger(getClass());

        try {
            className = mAcache.getAsString("ClassName");
            gradeName = mAcache.getAsString("GradeName");
            address = mAcache.getAsString("Address");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initData() {
        new TimeUtils(TeacherActivity.this, this);

        getCardNum();
        setLocalData();
        getUIdata();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        setLocalData();
        getUIdata();
    }

    private void setLocalData() {
        //首先加载本地数据
        try {
            CourseBean cb = new CourseBean();
            cb.setAddress(address);
            cb.setClassName(className);
            cb.setGradeName(gradeName);
            String current = TimeUtils.getCurrentTime1();
            ArrayList<MainData.CourseBean> course = (ArrayList<MainData.CourseBean>) mAcache.getAsObject("Course");
//            LogUtils.d(course);
            for (MainData.CourseBean courseBean : course) {
                if (compareDate(courseBean.getStartTime(), current) && compareDate(current, courseBean.getEndTime())) {
                    cb.setPeriodName(courseBean.getRemarks());
                    cb.setAccountName(courseBean.getAccountname());
                    cb.setSubjectName(courseBean.getSubjectname());
                }
            }
            setUIData(cb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "上课界面");
        Constant.SCREENTYPE = 1;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
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
//        LogUtils.d("解注册EventBus");
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        LogUtils.d("teacher--onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
        LogUtils.d("teacherAct--onDestroy");
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(this));
        map.put("timestamp", System.currentTimeMillis()+"");
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_COURSE), false, map);
        mDate.setText(TimeUtils.getStringDate());

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
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                CourseBean courseBean = JSON.parseObject(data.toString(), new TypeReference<CourseBean>() {
                });
                if (courseBean != null) {
                    setUIData(courseBean);
                    //上课信息不全。一分钟后重新请求
//                    if (TextUtils.isEmpty(courseBean.getSubjectName()) && TextUtils.isEmpty(courseBean.getAccountName())) {
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                getUIdata();
//                            }
//                        }, 30000);
//                    }
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
        try {
            if (!TextUtils.isEmpty(courseBean.getImage())) {
                Glide.with(this).load(courseBean.getImage()).into(mImg);
            } else {
                Glide.with(this).load(R.drawable.teacher).into(mImg);
            }
            mClassName.setText(courseBean.getGradeName() + courseBean.getClassName());
            if (!TextUtils.isEmpty(courseBean.getSubjectName())) {
                mCourseName.setText("课程：" + courseBean.getSubjectName());
                mCourseName.setVisibility(View.VISIBLE);
            } else {
                mCourseName.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(courseBean.getAccountName())) {
                mTeacherName.setText("教师：" + courseBean.getAccountName());
            } else if (!TextUtils.isEmpty(courseBean.getManagerAccountName())) {
                mTeacherName.setText("班主任：" + courseBean.getManagerAccountName());
            }
            if (!TextUtils.isEmpty(courseBean.getPeriodName())) {
                mPeriodName.setText("节次：" + courseBean.getPeriodName());
                mPeriodName.setVisibility(View.VISIBLE);
            } else {
                mPeriodName.setVisibility(View.GONE);
            }
            mAddr.setText("教室：" + courseBean.getAddress());
            Glide.with(this).load(courseBean.getQrcode()).into(mQrcode);

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    logger.debug(TAG, "重新请求");
                    getUIdata();
                }
            }
        }, 30000);
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

    public boolean compareDate(String time1, String time2) {
        try {
            //如果想比较日期则写成"yyyy-MM-dd"就可以了
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            //将字符串形式的时间转化为Date类型的时间
            Date a = sdf.parse(time1);
            Date b = sdf.parse(time2);
            //Date类的一个方法，如果a早于b返回true，否则返回false
            if (a.before(b))
                return true;
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    @OnClick(R.id.teacher_back)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        EventBus.getDefault().post(new CheckDelayEvent(300));
    }
}
