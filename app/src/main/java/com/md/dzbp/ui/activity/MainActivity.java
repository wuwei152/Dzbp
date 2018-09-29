package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.app.smdt.SmdtManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.adapter.StuListAdapter;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.data.ClassInfoBean;
import com.md.dzbp.data.ClassManagerBean;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.MainData;
import com.md.dzbp.data.MainUpdateEvent;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.data.UpdateDate;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.RemoteService;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.ListViewForScrollView;
import com.md.dzbp.ui.view.LoopViewPagerLayout.BannerInfo;
import com.md.dzbp.ui.view.LoopViewPagerLayout.IndicatorLocation;
import com.md.dzbp.ui.view.LoopViewPagerLayout.LoopStyle;
import com.md.dzbp.ui.view.LoopViewPagerLayout.LoopViewPagerLayout;
import com.md.dzbp.ui.view.LoopViewPagerLayout.OnBannerItemClickListener;
import com.md.dzbp.ui.view.LoopViewPagerLayout.OnDefaultImageViewLoader;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.MyRecyclerView;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.main_time)
    TextView mTime;
    @BindView(R.id.main_date)
    TextView mDate;
    @BindView(R.id.main_temp)
    TextView mTemp;
    @BindView(R.id.main_loction)
    TextView mLoction;
    @BindView(R.id.main_listview)
    ListView mListview;
    @BindView(R.id.main_Loop)
    LoopViewPagerLayout mLoop;
    @BindView(R.id.main_loopName)
    TextView mLoopName;
    @BindView(R.id.main_scroll)
    ScrollView mScroll;
    @BindView(R.id.main_cardNum)
    EditText mCardNum;
    @BindView(R.id.main_stuList)
    MyRecyclerView mStuListRecycler;
    @BindView(R.id.main_className)
    TextView mClassName;
    @BindView(R.id.main_classMng_icon)
    ImageView mClassMngIcon;
    @BindView(R.id.main_classMng_name)
    TextView mClassMngName;
    @BindView(R.id.main_classMng_course)
    TextView mClassMngCourse;

    @BindView(R.id.main_honorListView)
    ListView mHonorListView;
    @BindView(R.id.main_recycler_Empty)
    TextView mRecyclerEmpty;
    @BindView(R.id.main_noticeListView)
    ListViewForScrollView mNoticeListView;
    //通知滚动
    private final Handler noticeScroll_handler = new Handler();
    @BindView(R.id.main_conStatus)
    TextView mConStatus;
    @BindView(R.id.main_yingdao)
    TextView mYingdao;
    @BindView(R.id.main_shidao)
    TextView mShidao;
    @BindView(R.id.main_weidao)
    TextView mWeidao;
    @BindView(R.id.main_classAlias)
    TextView mAlias;
    @BindView(R.id.main_moto)
    TextView mMoto;
    @BindView(R.id.main_sclIcon)
    ImageView mSclIcon;
    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int off = mNoticeListView.getMeasuredHeight() - mScroll.getHeight();//判断高度
            if (off > 0) {
                mScroll.scrollBy(0, 2);
                if (mScroll.getScrollY() == off) {
                    noticeScroll_handler.removeCallbacks(ScrollRunnable);
                    noticeScroll_handler.postDelayed(ScrollRunnable, 6000);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScroll.fullScroll(View.FOCUS_UP);
                        }
                    }, 3000);
                } else {
                    noticeScroll_handler.postDelayed(this, 150);
                }
            }
        }
    };
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

        dialog = MyProgressDialog.createLoadingDialog(MainActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

        noticeScroll_handler.postDelayed(ScrollRunnable, 3000);

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mStuListRecycler.setLayoutManager(linearLayoutManager);
        stuListAdapter = new StuListAdapter(this);
        mStuListRecycler.setAdapter(stuListAdapter);
        mStuListRecycler.setEmptyView(mRecyclerEmpty);

        logger = LoggerFactory.getLogger(ExamActivity.class);

        SmdtManager smdt = SmdtManager.create(this);
        //隐藏状态栏
        smdt.smdtSetStatusBar(MainActivity.this, false);
    }

    @Override
    protected void initData() {
        new TimeUtils(MainActivity.this, this);
        mDate.setText(TimeUtils.getStringDate());
        getCardNum();

        mNoticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
                intent.putExtra("id", mainData.getNotice().get(i).getNoticeId());
                startActivity(intent);
            }
        });

//        mCameraInfos = new ArrayList<>();
//        mCameraInfos.add(new CameraInfo("192.168.8.80", "37777", "admin", "12345"));//二中
//        mCameraInfos.add(new CameraInfo("192.168.8.81", "37777", "admin", "12345"));//二中
//        mCameraInfos.add(new CameraInfo("172.16.13.222", "37777", "admin", "12345"));//六中
//        mCameraInfos.add(new CameraInfo("192.168.0.89", "37777", "admin", "12345"));//测试
//        mCameraInfos.add(new CameraInfo("192.168.0.80", "37777", "admin", "12345"));//测试
//        mCameraInfos.add(new CameraInfo("192.168.0.112", "37777", "admin", "yc123456"));//测试
//        mAcache.put("CameraInfo", mCameraInfos);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "课中界面");
        Constant.SCREENTYPE = 0;
        LogUtils.d("注册EventBus");
        EventBus.getDefault().register(this);
        boolean cons = false;
        Object consobj = mAcache.getAsObject("conStatus");
        if (consobj != null) {
            cons = (boolean) consobj;
        }
        if (cons) {
            mConStatus.setText("连接状态：已连接");
            mConStatus.setTextColor(getResources().getColor(R.color.cons));
        } else {
            mConStatus.setText("连接状态：已断开");
            mConStatus.setTextColor(getResources().getColor(R.color.conf));
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
        netWorkRequest.doGetRequest(0, Constant.getUrl(MainActivity.this, APIConfig.GET_Main), true, map);
        netWorkRequest.doGetRequest(2, Constant.getUrl(MainActivity.this, APIConfig.GET_LOAD_MSG), false, map);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("解注册EventBus");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.d("main--onStop");
    }

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
    private void setCourseList(List<MainData.CourseBean> mList) {
        mListview.setAdapter(new CommonAdapter<MainData.CourseBean>(MainActivity.this, R.layout.item_main_list, mList) {
            @Override
            protected void convert(ViewHolder viewHolder, MainData.CourseBean item, int position) {
                viewHolder.setText(R.id.item_l1, item.getRemarks() + "");
                viewHolder.setText(R.id.item_l2, item.getSubjectname());
                viewHolder.setText(R.id.item_l3, item.getPeriod());
                viewHolder.setText(R.id.item_l4, item.getAccountname());
            }
        });
    }

    /**
     * 设置荣誉评定
     */
    private void setHonorList(List<MainData.MoralScoreBean> mList) {

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
        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum,this);
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

    @OnClick({R.id.main_left, R.id.main_right, R.id.main_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_test:
                Intent intent = new Intent(MainActivity.this, TcpService.class);
                intent.putExtra("test", 0);
                startService(intent);
                break;
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
        }
    }

    /**
     * 设置页首轮播图
     */
    private void setPager(final List<MainData.PhotosBean> photos) {
        mLoop.stopLoop();
        mLoop.setLoop_ms(5000);//轮播的速度(毫秒)
        mLoop.setLoop_duration(500);//滑动的速率(毫秒)
        mLoop.setLoop_style(LoopStyle.Empty);//轮播的样式-默认empty
        mLoop.setIndicatorLocation(IndicatorLocation.Center);//指示器位置-中Center
        mLoop.initializeData(MainActivity.this);//初始化数据
        ArrayList<BannerInfo> bannerInfos = new ArrayList<>();
        for (MainData.PhotosBean photo : photos) {
            bannerInfos.add(new BannerInfo<String>(photo.getUrl(), ""));
        }
        mLoop.setOnLoadImageViewListener(new OnDefaultImageViewLoader() {
            @Override
            public void onLoadImageView(int position, ImageView imageView, Object parameter) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                Glide.with(MainActivity.this).load(parameter).into(imageView);
                GlideImgManager.glideLoader(MainActivity.this, parameter.toString(), R.drawable.pic_not_found, R.drawable.pic_not_found, imageView, "");
                position = position == 0 ? photos.size() - 1 : position - 1;
                mLoopName.setText(photos.get(position).getDescription());
//                LogUtils.d(position + "");
            }
        });//设置图片加载&自定义图片监听
        mLoop.setOnBannerItemClickListener(new OnBannerItemClickListener() {
            @Override
            public void onBannerClick(int index, ArrayList<BannerInfo> banner) {
                LogUtils.d(index + "");
            }
        });//设置监听
        mLoop.setLoopData(bannerInfos);//设置数据
        mLoop.startLoop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("main--onDestroy");
        if (mLoop != null) {
            mLoop.stopLoop();
        }
        if (noticeScroll_handler != null && ScrollRunnable != null) {
            noticeScroll_handler.removeCallbacks(ScrollRunnable);
        }
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
        if (code == 0) {
            if (data != null) {
                mainData = JSON.parseObject(data.toString(), new TypeReference<MainData>() {
                });
                if (mainData != null) {
                    setUIData(mainData);
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
            mClassName.setText(classInfo.getGradeName() + classInfo.getClassName());
            if (!TextUtils.isEmpty(classInfo.getAliasName()) && !classInfo.getAliasName().equals("null")) {
                mAlias.setText("(" + classInfo.getAliasName() + ")");
            }

            mLoction.setText(classInfo.getAddress());
            if (!TextUtils.isEmpty(classInfo.getManagerMessage())) {
                mClassMngCourse.setText(classInfo.getManagerMessage());
            } else {
                mClassMngCourse.setText("无");
            }
            if (!TextUtils.isEmpty(classInfo.getMotto())) {
                SpannableString spannableString = new SpannableString("班训：    " + classInfo.getMotto());
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#000000"));
                RelativeSizeSpan sizeSpan = new RelativeSizeSpan(1.2f);
                StyleSpan styleSpan_B = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(colorSpan, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(sizeSpan, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(styleSpan_B, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mMoto.setText(spannableString);
            } else {
                mMoto.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(classInfo.getSchoolLogo()) && !classInfo.getSchoolLogo().equals("null")) {
                GlideImgManager.glideLoader(MainActivity.this, classInfo.getSchoolLogo(), R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            } else {
                mSclIcon.setVisibility(View.GONE);
            }
        }
        ClassManagerBean classManager = mainData.getClassManager();
        if (classManager != null) {
            Glide.with(MainActivity.this).load(classManager.getPhoto()).into(mClassMngIcon);
            mClassMngName.setText(classManager.getAccountName());
        }
        List<MainData.NoticeBean> notice = mainData.getNotice();
        if (notice != null) {
            setNoticeList(notice);
        }

        if (mainData.getPhotos() != null && mainData.getPhotos().size() > 0) {
            setPager(mainData.getPhotos());
        }

        if (mainData.getCourse() != null) {
            setCourseList(mainData.getCourse());
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
            mYingdao.setText(attendance.getYindao() + "人");
            mShidao.setText(attendance.getShidao() + "人");
            mWeidao.setText(attendance.getWeidao() + "人");
        }
    }

    /**
     * 设置通知列表
     *
     * @param notice
     */
    private void setNoticeList(final List<MainData.NoticeBean> notice) {
        mNoticeListView.setAdapter(new CommonAdapter<MainData.NoticeBean>(MainActivity.this, R.layout.item_main_notice, notice) {
            @Override
            protected void convert(ViewHolder viewHolder, MainData.NoticeBean item, int position) {
                viewHolder.setText(R.id.item_name, item.getTitle());
                viewHolder.setText(R.id.item_person, item.getPublisher());
                viewHolder.setText(R.id.item_time, item.getPublishtime());

            }
        });
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
        }, 5000);
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
            case 1://相册更新
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
            mConStatus.setText("连接状态：已连接");
            mConStatus.setTextColor(getResources().getColor(R.color.cons));
        } else {
            mConStatus.setText("连接状态：已断开");
            mConStatus.setTextColor(getResources().getColor(R.color.conf));
        }
    }
}
