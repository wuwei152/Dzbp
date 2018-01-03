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
import com.md.dzbp.utils.MainGestureDetector;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 会议模式
 */
public class MeetingActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.meet_cardNum)
    EditText mCardNum;
    @BindView(R.id.meet_time)
    TextView mTime;
    @BindView(R.id.meet_date)
    TextView mDate;
    @BindView(R.id.meet_temp)
    TextView mTemp;
    @BindView(R.id.meeting_GridView)
    GridView mGridView;
    @BindView(R.id.meet_mainAddr)
    TextView mMainAddr;
    @BindView(R.id.meet_mainTitle)
    TextView mMainTitle;
    @BindView(R.id.meet_mainSub)
    TextView mMainSub;
    @BindView(R.id.meet_mainDate)
    TextView mMainDate;
    @BindView(R.id.meet_host)
    TextView mHost;
    @BindView(R.id.meet_Num)
    TextView mNum;
    @BindView(R.id.meet_listTitle)
    TextView mListTitle;
    @BindView(R.id.meet_listTime)
    TextView mListTime;
    @BindView(R.id.meet_listAddr)
    TextView mListAddr;
    @BindView(R.id.meet_listHost)
    TextView mListHost;
    @BindView(R.id.meet_listSub)
    TextView mListSub;
    @BindView(R.id.meet_QRcode)
    ImageView mQRcode;
    @BindView(R.id.meet_QRcodeText)
    TextView mQRcodeText;

    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
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
        //主菜单
        mainDialog = new MainDialog(this);
        mAcache = ACache.get(this);
        logger = LoggerFactory.getLogger(getClass());
        gestureDetector = new GestureDetector(MeetingActivity.this, MainGestureDetector.getGestureDetector(mainDialog));
        //获取时间日期
        new TimeUtils(MeetingActivity.this, this);
        //进度
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        getUIdata();
    }

    @Override
    protected void initData() {
        getCardNum();
        mDate.setText(TimeUtils.getStringDate());

        getUIdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "会议界面");
        Constant.SCREENTYPE = 6;
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
        EventBus.getDefault().unregister(this);
        if (mainDialog != null && mainDialog.isShowing()) {
            mainDialog.dismiss();
        }
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
        }
        mMainAddr.setText(meetingbean.getAddress());
        mMainTitle.setText(meetingbean.getName());
        mMainSub.setText(meetingbean.getSummary());
        mMainDate.setText(meetingbean.getStartTime() + "        " + meetingbean.getAddress());
        mHost.setText("会议主持：" + hostName);
        mNum.setText("参会人员：" + meetingUserList.size() + "人");
        mListTitle.setText(meetingbean.getName());
        mListTime.setText(meetingbean.getStartTime());
        mListAddr.setText(meetingbean.getAddress());
        mListHost.setText(hostName);
        mListSub.setText(meetingbean.getSummary());
        Glide.with(MeetingActivity.this).load(meetingbean.getQrcodeUrl()).into(mQRcode);
        mQRcodeText.setText("扫码签到");
        setGridData(meetingUserList);
    }

    /**
     * 设置人员数据
     */
    private void setGridData(List meetingUserList) {
        mGridView.setAdapter(new CommonAdapter<Meetingbean.MeetingUserListBean>(MeetingActivity.this, R.layout.item_meeting_grid, meetingUserList) {
            @Override
            protected void convert(ViewHolder viewHolder, Meetingbean.MeetingUserListBean item, int position) {
                viewHolder.setText(R.id.item_meetgrid_name, item.getAccountName());
                if (item.getSigninStatus() == 1) {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.green));
                } else {
                    viewHolder.setTextColor(R.id.item_meetgrid_name, getResources().getColor(R.color.text_black));
                }
            }
        });
    }

    /**
     * 获取刷卡卡号
     */
    private void getCardNum() {
        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {
                    Intent intent = new Intent(MeetingActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 6);
                    intent.putExtra("ext", meetingbean.getId());
                    startService(intent);
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
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
        }, 5000);
    }

    @Override
    public void cancelRequest() {
        netWorkRequest.CancelPost();
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataStatusEvent(SignEvent event) {
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
            mTemp.setText("连接状态：已连接");
            mTemp.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTemp.setText("连接状态：已断开");
            mTemp.setTextColor(getResources().getColor(R.color.conf));
        }
    }
}
