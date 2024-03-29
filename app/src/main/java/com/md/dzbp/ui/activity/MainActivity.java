package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.adapter.MyViewPagerAdapter;
import com.md.dzbp.adapter.StuListAdapter;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.Attendance;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.data.ClassInfoBean;
import com.md.dzbp.data.ClassManagerBean;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.MainData;
import com.md.dzbp.data.MainUpdateEvent;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.UpdateDate;
import com.md.dzbp.model.DeviceCtrlUtils;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.serial.SerialControl;
import com.md.dzbp.tcp.RemoteService;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.AutoScrollViewPager;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.MyRecyclerView;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.BannerVideoLoader;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.md.dzbp.utils.GlideZBImageLoader;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zk.banner.Banner;
import com.zk.banner.BannerConfig;
import com.zk.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

public class MainActivity extends BaseActivity implements TimeListener, UIDataListener, OnBannerListener {

    @BindView(R.id.title_classAddr)
    TextView mLoction;
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

    @BindView(R.id.main_listview)
    ListView mListview;
    //    @BindView(R.id.main_Loop)
//    Banner banner;
    @BindView(R.id.main_Loop)
    Banner banner;
    @BindView(R.id.main_stuList)
    MyRecyclerView mStuListRecycler;
    @BindView(R.id.main_classMng_icon)
    SimpleDraweeView mClassMngIcon;
    @BindView(R.id.main_classMng_name)
    TextView mClassMngName;
    @BindView(R.id.main_classMng_course)
    TextView mClassMngCourse;

    @BindView(R.id.main_honorListView)
    ListView mHonorListView;
    @BindView(R.id.main_recycler_Empty)
    TextView mRecyclerEmpty;
    @BindView(R.id.main_conStatus)
    ImageView mConStatus;
    @BindView(R.id.main_yingdao)
    TextView mYingdao;
    @BindView(R.id.main_shidao)
    TextView mShidao;
    @BindView(R.id.main_weidao)
    TextView mWeidao;
    @BindView(R.id.main_moto)
    TextView mMoto;

    @BindView(R.id.main_noticeTitle)
    TextView mNoticeTitle;
    @BindView(R.id.main_noticeTime)
    TextView mNoticeTime;
    @BindView(R.id.main_week2)
    TextView mWeek2;
    @BindView(R.id.main_nextCourse)
    TextView mNextCourse;
    @BindView(R.id.main_viewPager)
    AutoScrollViewPager mViewPager;
    @BindView(R.id.main_tab)
    TabLayout mTab;
    @BindView(R.id.main_ydList)
    ListView ydList;
    @BindView(R.id.main_tab1)
    LinearLayout tab1;
    @BindView(R.id.main_more)
    TextView more;
    private ACache mAcache;
    private StuListAdapter stuListAdapter;
    private NetWorkRequest netWorkRequest;
    private LinearLayoutManager linearLayoutManager;
    private MainData mainData;
    private List<MainData.ChatBean> mChatList;
    private Dialog dialog;
    private Logger logger;
    private String TAG = "MainActivity-->{}";
    private ArrayList<CameraInfo> mCameraInfos;
    private String currentTime;
    private MainData.CourseBean bean;
    private ArrayList<MainData.CourseBean> course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initUI() {
        LogUtils.d("main--onCreate");
        startService(new Intent(this, TcpService.class));
        startService(new Intent(this, RemoteService.class));
        mAcache = ACache.get(this);

//        mAcache.put("DeviceType", "1");

        dialog = MyProgressDialog.createLoadingDialog(MainActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mStuListRecycler.setLayoutManager(linearLayoutManager);
        stuListAdapter = new StuListAdapter(this);
        mStuListRecycler.setAdapter(stuListAdapter);
        mStuListRecycler.setEmptyView(mRecyclerEmpty);

        logger = LoggerFactory.getLogger(MainActivity.class);
//        //隐藏状态栏
        DeviceCtrlUtils.getInstance(MainActivity.this).SetStatusBar(false);

//        ArrayList<CameraInfo> mCameraInfos = new ArrayList<>();
//        mCameraInfos.add(new CameraInfo("192.168.0.112", "37777", "admin", "yc123456"));//测试
//        mAcache.put("CameraInfo", mCameraInfos);

        mTab.addTab(mTab.newTab().setText("考勤统计"));
//        mTab.addTab(mTab.newTab().setText("考勤排名"));

        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                LogUtils.e(tab.getPosition());
                if (tab.getPosition() == 0) {
                    tab1.setVisibility(View.VISIBLE);
                    ydList.setVisibility(View.GONE);
                    more.setVisibility(View.GONE);
                } else {
                    ydList.setVisibility(View.VISIBLE);
                    more.setVisibility(View.VISIBLE);
                    tab1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void initData() {
        new TimeUtils(MainActivity.this, this);

        getCardNum();

//        initPermission();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "下课界面");
        Constant.SCREENTYPE = 0;
        Act = 0;
        ext = "";
//        LogUtils.d("注册EventBus");
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        boolean cons = false;
        Object consobj = mAcache.getAsObject("conStatus");
        if (consobj != null) {
            cons = (boolean) consobj;
        }
        if (cons) {
            mConStatus.setImageResource(R.drawable.lianwang);
        } else {
            mConStatus.setImageResource(R.drawable.lianwang_no);
        }
        //首先加载缓存
        mainData = (MainData) mAcache.getAsObject("MainData");
//        LogUtils.e(mainData);
        if (mainData != null) {
            setUIData(mainData);
        }
        getUIdata();
//        logger.debug(Tag,"");


    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(MainActivity.this));
//        map.put("deviceId", "3bcf351f-ec26-475a-bd5c-8dab32235b4e");
        netWorkRequest.doGetRequest(0, Constant.getUrl(MainActivity.this, APIConfig.GET_Main), false, map);
        netWorkRequest.doGetRequest(2, Constant.getUrl(MainActivity.this, APIConfig.GET_LOAD_MSG), false, map);
//        netWorkRequest.doGetRequest(4, Constant.getUrl(MainActivity.this, APIConfig.getAllClassAttendanceDetail), false, map);
        mDate.setText(TimeUtils.getStringDate());
        mWeek.setText(TimeUtils.getStringWeek());
        mWeek2.setText(TimeUtils.getStringWeek());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("解注册EventBus");
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
        LogUtils.d("main--onStop");
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

//    private void initPermission() {
//        XXPermissions.with(MainActivity.this)
//                .request(new OnPermission() {
//
//                    @Override
//                    public void hasPermission(List<String> granted, boolean isAll) {
//                        if (!isAll) {
////                            Toast.makeText(MainActivity.this, "获取部分权限成功，但部分权限未正常授予", Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void noPermission(List<String> denied, boolean quick) {
//
//                    }
//                });
//    }

    /**
     * 获取通知列表
     */
    private void getNoticeList() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(MainActivity.this));
        netWorkRequest.doGetRequest(3, Constant.getUrl(MainActivity.this, APIConfig.GET_LOAD_NOTICE), false, map);
    }

    /**
     * 设置课表
     */
    private void setCourseList() {
        //寻找下一节课任务
        bean = null;
        if (course != null && course.size() > 0) {
            currentTime = TimeUtils.getCurrentTime("HH:mm:ss");
            for (MainData.CourseBean courseBean : course) {
                if (TimeUtils.compareTime(courseBean.getStartTime(), currentTime)) {
                    bean = courseBean;
                    break;
                }
            }
            if (bean == null && course.size() > 0) {
                bean = course.get(course.size() - 1);
            }
        }

        if (bean != null) {
            mNextCourse.setText("下一节课：  " + bean.getSubjectname());
        } else {
            mNextCourse.setText("下一节课：暂无");
        }
        mListview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mListview.setAdapter(new CommonAdapter<MainData.CourseBean>(MainActivity.this, R.layout.item_main_list, course) {
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

                if (TimeUtils.compareTime(item.getStartTime(), currentTime)) {
                    viewHolder.setBackgroundRes(R.id.item_qiu, R.drawable.gray_solid_circle_back);
                } else {
                    viewHolder.setBackgroundRes(R.id.item_qiu, R.drawable.green_solid_circle_back);
                }

                if (bean != null && bean.getPeriod().equals(item.getPeriod())) {
                    viewHolder.getView(R.id.item_icon).setVisibility(View.VISIBLE);
                    GlideImgManager.glideLoader(MainActivity.this, bean.getPhoto(), R.drawable.icon_head_teacher, R.drawable.icon_head_teacher, (ImageView) (viewHolder.getView(R.id.item_icon)), 0);
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

    /**
     * 设置荣誉评定
     */
    private void setHonorList(List<MainData.MoralScoreBean> mList) {
        mHonorListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mHonorListView.setAdapter(new CommonAdapter<MainData.MoralScoreBean>(MainActivity.this, R.layout.item_main_star, mList) {
            @Override
            protected void convert(ViewHolder viewHolder, MainData.MoralScoreBean item, int position) {
                viewHolder.setText(R.id.item_name, item.getName());
                float num = item.getScore() / 20;
                ((RatingBar) viewHolder.getView(R.id.item_star)).setRating(num);
//                viewHolder.setText(R.id.item_num, item.getNumStr());
            }
        });
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
                    Intent intent = new Intent(MainActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 0);
                    intent.putExtra("ext", "");
                    startService(intent);
                }
            }
        });
    }

    @OnClick({R.id.main_left, R.id.main_right, R.id.main_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_left:
                if (mChatList != null && mChatList.size() > 0) {
                    int recyclePosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (recyclePosition - 5 > 0) {
                        mStuListRecycler.smoothScrollToPosition(recyclePosition - 5);
                    } else {
                        mStuListRecycler.smoothScrollToPosition(0);
                    }
                }

                break;
            case R.id.main_right:
                if (mChatList != null && mChatList.size() > 0) {
                    int recyclePosition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (recyclePosition + 9 > mChatList.size()) {
                        mStuListRecycler.smoothScrollToPosition(mChatList.size());
                    } else {
                        mStuListRecycler.smoothScrollToPosition(recyclePosition + 9);
                    }
                }
                break;
            case R.id.main_more:
                startActivity(new Intent(MainActivity.this, AttendanceActivity.class));
                break;
        }
    }

    /**
     * 设置页首轮播图
     */
    private void setPager(List<MainData.PhotosBean> photos) {

//        LogUtils.e(mainData);

//        if (photos.size() > 25) {
//            photos = photos.subList(0, 25);
//        }
//        try {
//            ArrayList<String> images = new ArrayList<>();
//            for (MainData.PhotosBean photo : photos) {
//                images.add(photo.getUrl());
//            }
//            //设置banner样式
//            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
//            //设置图片加载器
//            banner.setImageLoader(new GlideImageLoader());
//            //设置图片集合
//            banner.setImages(images);
//            //设置banner动画效果
//            banner.setBannerAnimation(Transformer.RotateUp);
//            //设置标题集合（当banner样式有显示title时）
////        banner.setBannerTitles(titles);
//            //设置自动轮播，默认为true
//            banner.isAutoPlay(true);
//            //设置轮播时间
//            banner.setDelayTime(6000);
//            //设置指示器位置（当banner模式中有指示器时）
//            banner.setIndicatorGravity(BannerConfig.LEFT);
//            //banner设置方法全部调用完毕时最后调用
//            banner.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error(TAG, e.getMessage());
//        }

        List<String> list = new ArrayList<>();
        for (MainData.PhotosBean photo : photos) {
            list.add(photo.getUrl());
        }

        //设置图片加载器
        banner.setImageLoader(new GlideZBImageLoader())
                //设置视频加载
                .setVideoLoader(new BannerVideoLoader(photos))
                //设置图片和视频集合
                .setImages(list)
                .isAutoPlay(true)
                //设置轮播时间
                .setDelayTime(10000)
                //设置指示器位置（当banner模式中有指示器时）
                .setIndicatorGravity(BannerConfig.CENTER)
//        .setPageTransformer(true, new RotateUpTransformer())
                .setOnBannerListener(this)
                //banner设置方法全部调用完毕时最后调用
                .start();

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Jzvd.releaseAllVideos();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void OnBannerClick(int position) {
//        LogUtils.e(mainData);
        ArrayList<MainData.PhotosBean> photos = mainData.getPhotos();
        Intent intent = new Intent(MainActivity.this, LooperImgPlayActivity.class);
        intent.putExtra("photos", photos);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("main--onDestroy");
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    //更新时间
    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }

    //系统时间改变后更新日期
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDateChangeEvent(UpdateDate event) {
        mDate.setText(TimeUtils.getStringDate());
    }

    @Override
    public void loadDataFinish(int code, Object data) {
//        LogUtils.e(data);
        if (code == 0) {
            if (data != null) {
                mainData = JSON.parseObject(data.toString(), new TypeReference<MainData>() {
                });
//                LogUtils.e(mainData);
                if (mainData != null) {
                    setUIData(mainData);
                    mAcache.put("MainData", mainData);
                }
            }
        } else if (code == 2) {
            if (data != null) {
                List<MainData.ChatBean> mChatBeanList = JSON.parseObject(data.toString(), new TypeReference<List<MainData.ChatBean>>() {
                });
                if (mChatBeanList != null) {
                    stuListAdapter.setDatas((ArrayList<MainData.ChatBean>) mChatBeanList);
                }
            }
        } else if (code == 3) {
            if (data != null) {
                List<MainData.NoticeBean> mNoticeBeanList = JSON.parseObject(data.toString(), new TypeReference<List<MainData.NoticeBean>>() {
                });
                if (mNoticeBeanList != null) {
                    setNoticeList(mNoticeBeanList);
                }
            }
        } else if (code == 4) {
            if (data != null) {
                List<Attendance> mAttendanceList = JSON.parseObject(data.toString(), new TypeReference<List<Attendance>>() {
                });
                if (mAttendanceList != null) {

                    for (Attendance attendance : mAttendanceList) {
                        if (attendance != null && attendance.isActive()) {
                            List<Attendance.AttendancedataBean.StuBean> yd = attendance.getAttendancedata().getYd();
                            if (yd.size() > 10) {
                                yd = yd.subList(0, 10);
                            }
                            ydList.setAdapter(new CommonAdapter<Attendance.AttendancedataBean.StuBean>(MainActivity.this, R.layout.item_attendance_list, yd) {
                                @Override
                                protected void convert(ViewHolder viewHolder, Attendance.AttendancedataBean.StuBean item, int position) {
                                    viewHolder.setText(R.id.tab1, item.getStudentname());
                                    viewHolder.setText(R.id.tab2, item.getAttendancetime());
                                }
                            });
                        }
                    }
                }
            }
        }
    }

    /**
     * 设置界面数据
     *
     * @param mainData
     */
    private void setUIData(MainData mainData) {
        ClassInfoBean classInfo = mainData.getClassInfo();
        if (classInfo != null) {
            mClassName.setText(classInfo.getGradeName() + "\n\n" + classInfo.getClassName());
            mSchoolName.setText(classInfo.getSchoolName());

            mAcache.put("SchoolName", classInfo.getSchoolName());

            mAcache.put("ClassName", classInfo.getClassName());
            mAcache.put("GradeName", classInfo.getGradeName());
            if (!TextUtils.isEmpty(classInfo.getAliasName()) && !classInfo.getAliasName().equals("null")) {
                mAlias.setText("(" + classInfo.getAliasName() + ")");
                mAcache.put("Alias", classInfo.getAliasName());
            }

            mLoction.setText("教室编号:" + classInfo.getAddress());
            mAcache.put("Address", classInfo.getAddress());
            if (!TextUtils.isEmpty(classInfo.getManagerMessage())) {
                mClassMngCourse.setText("                  " + classInfo.getManagerMessage());
            } else {
                mClassMngCourse.setText("无");
            }
            if (!TextUtils.isEmpty(classInfo.getMotto())) {
                mMoto.setText("班训：" + classInfo.getMotto());
            } else {
                mMoto.setVisibility(View.GONE);
            }

            //校徽
            if (!TextUtils.isEmpty(classInfo.getSchoolLogo()) && !classInfo.getSchoolLogo().equals("null")) {
                GlideImgManager.glideLoader(MainActivity.this, classInfo.getSchoolLogo(), R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
                mAcache.put("Logo", classInfo.getSchoolLogo());
            } else {
                mSclIcon.setVisibility(View.GONE);
            }
        }
        ClassManagerBean classManager = mainData.getClassManager();
        if (classManager != null) {
//            GlideImgManager.glideLoader(MainActivity.this, classManager.getPhoto(), R.drawable.pic_not_found2, R.drawable.pic_not_found2, mClassMngIcon, 0);
            mClassMngIcon.setImageURI(Uri.parse(classManager.getPhoto()));

            mClassMngName.setText(classManager.getAccountName());
            mAcache.put("ClassMng", classManager.getAccountName());
        }
        List<MainData.NoticeBean> notice = mainData.getNotice();
        if (notice != null) {
            setNoticeList(notice);
        }

        //轮播
        if (mainData.getPhotos() != null && mainData.getPhotos().size() > 0) {
            mAcache.put("Photos", mainData.getPhotos());
            setPager(mainData.getPhotos());
        }

        if (mainData.getCourse() != null) {
            //保存课程数据，提供上课页面信息
            course = (ArrayList<MainData.CourseBean>) mainData.getCourse();
            for (MainData.CourseBean courseBean : course) {
                String period = courseBean.getPeriod();
                if (!TextUtils.isEmpty(period)) {
                    String start = period.substring(0, period.indexOf("-")) + ":00";
                    String end = period.substring(period.indexOf("-") + 1, period.length()) + ":00";
                    courseBean.setStartTime(start);
                    courseBean.setEndTime(end);
                }
            }
            mAcache.put("Course", course);
            setCourseList();

        }
        mChatList = mainData.getChat();
        if (mChatList != null) {
            stuListAdapter.setDatas((ArrayList<MainData.ChatBean>) mChatList);
        }

        if (mainData.getMoralScore() != null) {
            setHonorList(mainData.getMoralScore());
        }
        MainData.AttendanceBean attendance = mainData.getAttendance();
        if (attendance != null) {
            if (attendance.getYindao() == 0) {
                mYingdao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mShidao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mWeidao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                mYingdao.setText("统计中");
                mShidao.setText("统计中");
                mWeidao.setText("统计中");
            } else {
                mYingdao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 38);
                mShidao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 38);
                mWeidao.setTextSize(TypedValue.COMPLEX_UNIT_SP, 38);
                mYingdao.setText(attendance.getYindao() + "");
                mShidao.setText(attendance.getShidao() + "");
                mWeidao.setText(attendance.getWeidao() + "");
            }
        }
    }

    /**
     * 设置通知列表
     *
     * @param notice
     */
    private void setNoticeList(final List<MainData.NoticeBean> notice) {
//        MainData.NoticeBean bean = new MainData.NoticeBean("第二届优秀学生表彰大会如期举行", "2019-03-05 09:23:22");
//        MainData.NoticeBean bean2 = new MainData.NoticeBean("第三届优秀学生表彰大会如期举行", "2019-03-05 09:23:22");
//        MainData.NoticeBean bean3 = new MainData.NoticeBean("第四届优秀学生表彰大会如期举行第四届优秀学生表彰大会如期举", "2019-03-05 09:23:22");
//        notice.add(bean);
//        notice.add(bean2);
//        notice.add(bean3);
        if (notice != null && notice.size() > 0) {
//            final MainData.NoticeBean noticeBean = notice.get(0);
//            mNoticeTitle.setText(noticeBean.getTitle());
//            mNoticeTime.setText(noticeBean.getPublishtime());
//
//            mNoticeTitle.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
//                    intent.putExtra("id", noticeBean.getNoticeId());
//                    startActivity(intent);
//                }
//            });

            try {
                ArrayList<View> mViewlist = new ArrayList<>();
                for (MainData.NoticeBean noticeBean1 : notice) {
                    View view = getLayoutInflater().inflate(R.layout.item_main_notice_list, null);
                    TextView title = view.findViewById(R.id.item_noticeTitle);
                    TextView time = view.findViewById(R.id.item_noticeTime);
                    title.setText(noticeBean1.getTitle());
                    time.setText(noticeBean1.getPublishtime());
                    mViewlist.add(view);
                }
                MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(mViewlist);
                mViewPager.setAdapter(myViewPagerAdapter);
                mViewPager.setInterval(5000);

                if (notice.size() > 1) {
                    mViewPager.startAutoScroll();
                }
                myViewPagerAdapter.setOnItemClick(new MyViewPagerAdapter.OnItemClickPosition() {
                    @Override
                    public void onclick(int position) {
                        Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                        intent.putExtra("id", notice.get(position).getNoticeId());
                        startActivity(intent);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(TAG, e);
            }
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(MainActivity.this, message);
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
                } else if (errorCode == 3) {
                    getNoticeList();
                }
            }
        }, 30000);
    }

    @Override
    public void cancelRequest() {
        netWorkRequest.CancelPost();
    }

    /**
     * 接收到消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(MessageBase event) {
        LogUtils.d("MainActivity接收到消息");
        if (!event.isSendMsg()) {
            Map map = new HashMap();
            map.put("deviceId", Constant.getDeviceId(MainActivity.this));
            netWorkRequest.doGetRequest(2, Constant.getUrl(MainActivity.this, APIConfig.GET_LOAD_MSG), false, map);
        }
    }

    /**
     * 接收到消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onUpdateSynEvent(MainUpdateEvent event) {
        LogUtils.d("MainActivity接收到局部更新消息" + event.getId());
        switch (event.getId()) {
            case 1://首页更新
                getUIdata();
                break;
            case 2://课表更新
                break;
            case 3://公告更新
                getNoticeList();
                break;
            default:
                break;
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

    @Override
    public void onBackPressed() {
    }


}
