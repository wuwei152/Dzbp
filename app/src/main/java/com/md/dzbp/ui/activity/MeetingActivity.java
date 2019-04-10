package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.Meetingbean;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.data.SignEvent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 会议模式
 */
public class MeetingActivity extends BaseActivity implements TimeListener, UIDataListener {

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

    @BindView(R.id.meeting_GridView)
    GridView mGridView;
    @BindView(R.id.meet_mainTitle)
    TextView mMainTitle;
    @BindView(R.id.meet_mainDate)
    TextView mMainDate;
    @BindView(R.id.meet_host)
    TextView mHost;
    @BindView(R.id.meet_QRcode)
    ImageView mQRcode;

    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private List<Meetingbean.MeetingUserListBean> meetingUserList;
    private Meetingbean meetingbean;
    private ACache mAcache;
    private Logger logger;
    private String TAG = "MeetingActivity-->{}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_meeting;
    }

    @Override
    protected void initUI() {

        mAcache = ACache.get(this);
        logger = LoggerFactory.getLogger(getClass());
        //进度
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

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
            new TimeUtils(MeetingActivity.this, this);

            mClassName.setText(gradeName + "\n\n" + className);
            mAddr.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(MeetingActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
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
        getUIdata();
    }

    @Override
    protected void initData() {
//        setUIData(new Meetingbean());
        getCardNum();
        getUIdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "会议界面");
        Constant.SCREENTYPE = 6;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(MeetingActivity.this));
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_MEETING), true, map);
    }

    /**
     * 设置界面数据
     */
    private void setUIData(Meetingbean meetingbean) {

//        meetingbean.setAddress("教学楼101室");
//        meetingbean.setEndTime("2019-04-22 11:30:00");
//        meetingbean.setName("2019届中考动员大会");
//        meetingbean.setQrcodeUrl("https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=7bcb659c9745d688a302b5a29cf91a23/2934349b033b5bb571dc8c5133d3d539b600bc12.jpg");
//        meetingbean.setStartTime("2019-04-22 09:30:00");
//        ArrayList<Meetingbean.MeetingUserListBean> list = new ArrayList<>();
//        list.add(new Meetingbean.MeetingUserListBean("1","李丽1",true,1));
//        list.add(new Meetingbean.MeetingUserListBean("2","李丽2"));
//        list.add(new Meetingbean.MeetingUserListBean("3","李丽3"));
//        list.add(new Meetingbean.MeetingUserListBean("4","李丽4"));
//        list.add(new Meetingbean.MeetingUserListBean("5","李丽5"));
//        list.add(new Meetingbean.MeetingUserListBean("6","李丽6"));
//        list.add(new Meetingbean.MeetingUserListBean("7","李丽7"));
//        list.add(new Meetingbean.MeetingUserListBean("8","李丽8"));
//        list.add(new Meetingbean.MeetingUserListBean("9","李丽9"));
//        list.add(new Meetingbean.MeetingUserListBean("10","李丽10"));
//        list.add(new Meetingbean.MeetingUserListBean("11","李丽11"));
//        list.add(new Meetingbean.MeetingUserListBean("12","李丽12"));
//        list.add(new Meetingbean.MeetingUserListBean("13","李丽13"));
//        list.add(new Meetingbean.MeetingUserListBean("14","李丽14"));
//        meetingbean.setMeetingUserList(list);


        meetingUserList = meetingbean.getMeetingUserList();
        Meetingbean.MeetingUserListBean host = null;
        String hostName = "无";
        if (meetingUserList != null) {
            for (Meetingbean.MeetingUserListBean m : meetingUserList) {
                if (m.getHost()) {
                    host = m;
                }
            }
            if (host != null) {
                hostName = host.getAccountName();
            }
            setGridData(meetingUserList);
        }
        mMainTitle.setText(meetingbean.getName());
        String endTime = meetingbean.getEndTime();
        if (!TextUtils.isEmpty(endTime)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(endTime);
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
                endTime = sdf2.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMainDate.setText(meetingbean.getStartTime() + "--" + endTime);
        mHost.setText("会议主持：" + hostName);
        Glide.with(MeetingActivity.this).load(meetingbean.getQrcodeUrl()).into(mQRcode);

    }

    /**
     * 设置人员数据
     */
    private void setGridData(List meetingUserList) {

        mGridView.setAdapter(new CommonAdapter<Meetingbean.MeetingUserListBean>(MeetingActivity.this, R.layout.item_meeting_grid, meetingUserList) {
            @Override
            protected void convert(ViewHolder viewHolder, Meetingbean.MeetingUserListBean item, int position) {
                viewHolder.setText(R.id.item_meetgrid_name, item.getAccountName());
                GlideImgManager.glideLoader(MeetingActivity.this, item.getPhoto(), R.drawable.head_icon, R.drawable.head_icon, (ImageView) (viewHolder.getView(R.id.item_meetgrid_img)), 0);
                if (item.getSigninStatus() == 1) {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.green));
                } else {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.text_gray));
                }
            }
        });
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
                    Intent intent = new Intent(MeetingActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 6);
                    if (meetingbean != null) {
                        intent.putExtra("ext", meetingbean.getId());
                    } else {
                        showToast("无参会人员信息");
                        return;
                    }
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
                meetingbean = JSON.parseObject(data.toString(), new TypeReference<Meetingbean>() {
                });
                if (meetingbean != null) {
                    setUIData(meetingbean);
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(MeetingActivity.this, message);
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
            if (event.getType() == 0 && event.isStatus()) {
                if (meetingUserList != null) {
                    for (Meetingbean.MeetingUserListBean m : meetingUserList) {
                        if (m.getAccountId().equals(event.getId())) {
                            m.setSigninStatus(1);
                            logger.debug("MeetingActivity-->{}", "匹配成功，签到成功！");
                        }
                    }
                    setGridData(meetingUserList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }
}
