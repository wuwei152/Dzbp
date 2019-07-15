package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
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
import com.md.dzbp.utils.GlideImageLoader;
import com.md.dzbp.utils.GlideImgManager;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

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
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 上课页面
 */
public class TeacherActivity extends BaseActivity implements TimeListener, UIDataListener {


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


    @BindView(R.id.teacher_courseName)
    TextView mCourseName;
    @BindView(R.id.teacher_teacherName)
    TextView mTeacherName;
    @BindView(R.id.teacher_qrcode)
    ImageView mQrcode;
    @BindView(R.id.teacher_icon)
    SimpleDraweeView mTeacherIcon;
    @BindView(R.id.teacher_Loop)
    Banner banner;
    @BindView(R.id.teacher_week2)
    TextView mWeek2;
    @BindView(R.id.teacher_listview)
    ListView mListview;
    @BindView(R.id.teacher_nextCourse)
    TextView mNextCourse;
    @BindView(R.id.main_conStatus)
    ImageView mConStatus;
    @BindView(R.id.teacher_start)
    TextView mStart;
    @BindView(R.id.teacher_end)
    TextView mEnd;
    @BindView(R.id.teacher_seekbar)
    SeekBar mSeekbar;

    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private ACache mAcache;
    private String TAG = "TeacherActivity-->{}";
    private Logger logger;
    private String className;
    private String address;
    private String gradeName;
    private String schoolName;
    private ArrayList<MainData.CourseBean> course;
    private MainData.CourseBean bean;
    private String current;
    private String logo;
    private String alias;
    private String startTime;
    private String endTime;
    private Timer timer;

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
            schoolName = mAcache.getAsString("SchoolName");
            logo = mAcache.getAsString("Logo");
            alias = mAcache.getAsString("Alias");

            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(TeacherActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            if (!TextUtils.isEmpty(alias) && !alias.equals("null")) {
                mAlias.setText("(" + alias + ")");
            }
        } catch (Exception e) {
            logger.error(TAG, e);
            e.printStackTrace();
        }

        //禁止手动滑动
        mSeekbar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
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
            current = TimeUtils.getCurrentTime1();
            course = (ArrayList<MainData.CourseBean>) mAcache.getAsObject("Course");
//            LogUtils.d(course);
            bean = null;
            if (course != null && course.size() > 0) {
                for (MainData.CourseBean courseBean : course) {
                    if (compareDate(courseBean.getStartTime(), current) && compareDate(current, courseBean.getEndTime())) {
                        bean = courseBean;
                        cb.setPeriodName(courseBean.getRemarks());
                        cb.setAccountName(courseBean.getAccountname());
                        cb.setSubjectName(courseBean.getSubjectname());
                    }
                }
            }
            setUIData(cb);
            setCourseList();
            setPager();
        } catch (Exception e) {
            logger.error(TAG, e);
        }
    }

    private void setCourseList() {
        //寻找当前课任务
        if (bean != null) {
            try {
                mNextCourse.setText(bean.getSubjectname() + " 上课中...");
                startTime = bean.getStartTime();
                endTime = bean.getEndTime();
                mStart.setText(startTime);
                mEnd.setText(endTime);

                if (!TextUtils.isEmpty(bean.getPhoto())) {
                    mTeacherIcon.setImageURI(Uri.parse(bean.getPhoto()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.debug(TAG, e);
            }
        } else {
            mNextCourse.setText("上课中...");
        }
//        mListview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (course != null) {
            mListview.setAdapter(new CommonAdapter<MainData.CourseBean>(TeacherActivity.this, R.layout.item_main_list, course) {
                @Override
                protected void convert(ViewHolder viewHolder, MainData.CourseBean item, int position) {
                    viewHolder.setText(R.id.item_l1, item.getRemarks() + "");
                    viewHolder.setText(R.id.item_l3, item.getSubjectname());
                    viewHolder.setText(R.id.item_l2, item.getPeriod());
                    viewHolder.setText(R.id.item_l4, item.getAccountname());
                    if (position == 0) {
                        viewHolder.getView(R.id.item_top).setVisibility(View.INVISIBLE);
                    } else {
                        viewHolder.getView(R.id.item_top).setVisibility(View.VISIBLE);
                    }
                    if (position == course.size() - 1) {
                        viewHolder.getView(R.id.item_bot).setVisibility(View.INVISIBLE);
                    } else {
                        viewHolder.getView(R.id.item_bot).setVisibility(View.VISIBLE);
                    }

                    if (TimeUtils.compareTime(item.getStartTime(), current)) {
                        viewHolder.setBackgroundRes(R.id.item_qiu, R.drawable.gray_solid_circle_back);
                    } else {
                        viewHolder.setBackgroundRes(R.id.item_qiu, R.drawable.green_solid_circle_back);
                    }

                    if (bean != null && bean.getPeriod().equals(item.getPeriod())) {
                        viewHolder.getView(R.id.item_icon).setVisibility(View.VISIBLE);
                        GlideImgManager.glideLoader(TeacherActivity.this, bean.getPhoto(), R.drawable.icon_head_teacher, R.drawable.icon_head_teacher, (ImageView) (viewHolder.getView(R.id.item_icon)), 0);
                        viewHolder.setBackgroundRes(R.id.item_qiu, R.drawable.red_solid_circle_back);
                        viewHolder.setBackgroundRes(R.id.item_ll, R.color.green2);

                        ((TextView) (viewHolder.getView(R.id.item_l1))).setTextColor(getResources().getColor(R.color.white));
                        ((TextView) (viewHolder.getView(R.id.item_l2))).setTextColor(getResources().getColor(R.color.white));
                        ((TextView) (viewHolder.getView(R.id.item_l3))).setTextColor(getResources().getColor(R.color.white));
                        ((TextView) (viewHolder.getView(R.id.item_l4))).setTextColor(getResources().getColor(R.color.white));

                    } else {
                        viewHolder.setBackgroundRes(R.id.item_ll, R.color.white);
                        viewHolder.getView(R.id.item_icon).setVisibility(View.INVISIBLE);
                        ((TextView) (viewHolder.getView(R.id.item_l1))).setTextColor(getResources().getColor(R.color.text_gray));
                        ((TextView) (viewHolder.getView(R.id.item_l2))).setTextColor(getResources().getColor(R.color.text_black));
                        ((TextView) (viewHolder.getView(R.id.item_l3))).setTextColor(getResources().getColor(R.color.text_black));
                        ((TextView) (viewHolder.getView(R.id.item_l4))).setTextColor(getResources().getColor(R.color.text_black));
                    }
                }
            });
        }
    }

    /**
     * 设置页首轮播图
     */
    private void setPager() {
        try {
            List<MainData.PhotosBean> photos = (List<MainData.PhotosBean>) mAcache.getAsObject("Photos");

            if (photos.size() > 16) {
                photos = photos.subList(0, 16);
            }

            ArrayList<String> images = new ArrayList<>();
            for (MainData.PhotosBean photo : photos) {
                images.add(photo.getUrl());
            }
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            banner.setImages(images);
            //设置banner动画效果
            banner.setBannerAnimation(Transformer.BackgroundToForeground);
            //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(titles);
            //设置自动轮播，默认为true
            banner.isAutoPlay(true);
            //设置轮播时间
            banner.setDelayTime(6000);
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.LEFT);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e);
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
            mConStatus.setImageResource(R.drawable.lianwang);
        } else {
            mConStatus.setImageResource(R.drawable.lianwang_no);
        }
        //更新课程进度信息
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    int i = (int) (TimeUtils.getpercentageTime(startTime, endTime) * 100);
                    logger.debug(TAG, "课程进度：" + i);
                    mSeekbar.setProgress(i);
                }
            }, 5000, 60000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LogUtils.d("解注册EventBus");
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);

        if (timer != null) {
            timer.cancel();
            timer = null;
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
        map.put("timestamp", System.currentTimeMillis() + "");
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_COURSE), false, map);
        mDate.setText(TimeUtils.getStringDate());
        mWeek.setText(TimeUtils.getStringWeek());
        mWeek2.setText(TimeUtils.getStringWeek());

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
            mClassName.setText(courseBean.getGradeName() + "\n\n" + courseBean.getClassName());
            if (!TextUtils.isEmpty(courseBean.getSubjectName())) {
                mCourseName.setText(courseBean.getSubjectName());
                mCourseName.setVisibility(View.VISIBLE);
            } else {
                mCourseName.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(courseBean.getAccountName())) {
                mTeacherName.setText(courseBean.getAccountName());
            } else if (!TextUtils.isEmpty(courseBean.getManagerAccountName())) {
                mTeacherName.setText(courseBean.getManagerAccountName());
            }
            mAddr.setText("教室编号:" + courseBean.getAddress());
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
            mConStatus.setImageResource(R.drawable.lianwang);
        } else {
            mConStatus.setImageResource(R.drawable.lianwang_no);
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
            if (b.before(a))
                return false;
            else
                return true;
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
