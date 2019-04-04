package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.MainData;
import com.md.dzbp.data.Meetingbean;
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
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 兴趣班--走班制
 */
public class InterestClassActivity extends BaseActivity implements TimeListener, UIDataListener {

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

    @BindView(R.id.interesting_GridView)
    GridView mGridView;
    @BindView(R.id.interest_mainTitle)
    TextView mMainTitle;
    @BindView(R.id.interest_mainSub)
    TextView mMainSub;
    @BindView(R.id.interest_mainDate)
    TextView mMainDate;
    @BindView(R.id.interest_host)
    TextView mHost;
    @BindView(R.id.interest_Num)
    TextView mNum;
    @BindView(R.id.interest_yingdao)
    TextView mYingdao;
    @BindView(R.id.interest_shidao)
    TextView mShidao;
    @BindView(R.id.interest_weidao)
    TextView mWeidao;
    @BindView(R.id.interest_qrcode)
    ImageView mQrcode;
    @BindView(R.id.interest_listview)
    HorizontalListView mListview;

    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private List<Meetingbean.MeetingUserListBean> interestingUserList;
    private ACache mAcache;
    private Logger logger;
    private String TAG = "InterestClassActivity-->{}";
    private String classMng;
    private CommonAdapter adapter;
    private ArrayList<MainData.PhotosBean> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_interest;
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
            classMng = mAcache.getAsString("ClassMng");
            String gradeName = mAcache.getAsString("GradeName");
            String address = mAcache.getAsString("Address");
            String schoolName = mAcache.getAsString("SchoolName");
            String logo = mAcache.getAsString("Logo");
            String alias = mAcache.getAsString("Alias");

            mDate.setText(TimeUtils.getStringDate());
            mWeek.setText(TimeUtils.getStringWeek());
            //获取时间日期
            new TimeUtils(InterestClassActivity.this, this);

            mClassName.setText(gradeName + "\n\n"+ className);
            mAddr.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(InterestClassActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
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
        getCardNum();
        getUIdata();

        interestingUserList = new ArrayList<>();
        interestingUserList.add(new Meetingbean.MeetingUserListBean("1557433861", "张晓霞"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓1"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓2"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓3"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓4"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓5"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓6"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓7"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓8"));
        interestingUserList.add(new Meetingbean.MeetingUserListBean("22", "张晓9"));
        setUIData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "走班制界面");
        Constant.SCREENTYPE = 9;
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
//        Map map = new HashMap();
//        map.put("deviceId", Constant.getDeviceId(InterestClassActivity.this));
//        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_interestING), true, map);
    }

    /**
     * 设置界面数据
     */
    private void setUIData() {


        mMainTitle.setText("校社团活动课\n立体思维");
        mHost.setText("指导老师：" + classMng);
        mNum.setText("学习人数：" + "36人");
        GlideImgManager.glideLoader(InterestClassActivity.this, R.drawable.erweima, R.drawable.pic_not_found, R.drawable.pic_not_found, mQrcode, 1);

        setGridData(interestingUserList);

        try {
            photos = (ArrayList<MainData.PhotosBean>) mAcache.getAsObject("Photos");
            if (photos!=null&&photos.size()>0){
                photos.get(0).setChecked(true);
                adapter = new CommonAdapter<MainData.PhotosBean>(InterestClassActivity.this, R.layout.item_interest_list, photos) {
                    @Override
                    protected void convert(ViewHolder viewHolder, MainData.PhotosBean item, int position) {
                        if (item.isChecked()) {
                            viewHolder.setVisible(R.id.item_img_cw, false);
                        } else {
                            viewHolder.setVisible(R.id.item_img_cw, true);
                        }
                        GlideImgManager.glideLoader(InterestClassActivity.this, item.getUrl(), R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) (viewHolder.getView(R.id.item_img)), 1);

                    }
                };

                mListview.setAdapter(adapter);
                mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (MainData.PhotosBean photo : photos) {
                            photo.setChecked(false);
                        }
                        photos.get(position).setChecked(true);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(TAG,e);
        }
    }

    /**
     * 设置人员数据
     */
    private void setGridData(List interestingUserList) {
        mGridView.setAdapter(new CommonAdapter<Meetingbean.MeetingUserListBean>(InterestClassActivity.this, R.layout.item_meeting_grid, interestingUserList) {
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
        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum, this);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {

                    Intent intent = new Intent(InterestClassActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 9);
                    intent.putExtra("ext", "");
                    startService(intent);

//                    if (interestingUserList != null) {
//                    for (Meetingbean.MeetingUserListBean m : interestingUserList) {
//                        if (m.getAccountId().equals(num)) {
//                            m.setSigninStatus(1);
//                            showToast(m.getAccountName()+"兴趣班签到成功！");
//                            logger.debug("interestingActivity-->{}", "匹配成功，签到成功！");
//                        }
//                    }
//                    setGridData(interestingUserList);
//                }
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
//                interestingbean = JSON.parseObject(data.toString(), new TypeReference<interestingbean>() {
//                });
//                if (interestingbean != null) {
//                    setUIData(interestingbean);
//                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(InterestClassActivity.this, message);
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

//    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
//    public void onDataStatusEvent(SignEvent event) {
//        try {
//            if (event.getType() == 0 && event.isStatus()) {
//                if (interestingUserList != null) {
//                    for (interestingbean.interestingUserListBean m : interestingUserList) {
//                        if (m.getAccountId().equals(event.getId())) {
//                            m.setSigninStatus(1);
//                            logger.debug("interestingActivity-->{}", "匹配成功，签到成功！");
//                        }
//                    }
//                    setGridData(interestingUserList);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }
}
