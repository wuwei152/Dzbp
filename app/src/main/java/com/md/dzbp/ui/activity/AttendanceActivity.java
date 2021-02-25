package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.tabs.TabLayout;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.Attendance;
import com.md.dzbp.data.CourseBean;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 考勤查看页面
 */
public class AttendanceActivity extends BaseActivity implements TimeListener, UIDataListener {


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


    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.text1)
    TextView text1;
    @BindView(R.id.add1)
    TextView add1;
    @BindView(R.id.listview1)
    GridView listview1;
    @BindView(R.id.text2)
    TextView text2;
    @BindView(R.id.add2)
    TextView add2;
    @BindView(R.id.listview2)
    GridView listview2;
    @BindView(R.id.text3)
    TextView text3;
    @BindView(R.id.add3)
    TextView add3;
    @BindView(R.id.listview3)
    GridView listview3;
    @BindView(R.id.text4)
    TextView text4;
    @BindView(R.id.add4)
    TextView add4;
    @BindView(R.id.listview4)
    GridView listview4;
    @BindView(R.id.text5)
    TextView text5;
    @BindView(R.id.add5)
    TextView add5;
    @BindView(R.id.listview5)
    GridView listview5;


    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private ACache mAcache;
    private String TAG = "videoshowActivity-->{}";
    private Logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_attendance;
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(this);

        dialog = MyProgressDialog.createLoadingDialog(AttendanceActivity.this, "", this);
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
            new TimeUtils(AttendanceActivity.this, this);

            mClassName.setText(gradeName + "\n\n" + className);
            mAddr.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(AttendanceActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
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
        logger.debug(TAG, "考勤结果界面");
        Constant.SCREENTYPE = 112;
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
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
//        map.put("deviceId", "3bcf351f-ec26-475a-bd5c-8dab32235b4e");
        netWorkRequest.doGetRequest(0, Constant.getUrl(AttendanceActivity.this, APIConfig.getAllClassAttendanceDetail), true, map);
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
                    Intent intent = new Intent(AttendanceActivity.this, TcpService.class);
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
                List<Attendance> mAttendanceList = JSON.parseObject(data.toString(), new TypeReference<List<Attendance>>() {
                });
                if (mAttendanceList != null) {
                    setUIData(mAttendanceList);
                }
            }
        }
    }

    /**
     * 设置UI数据
     *
     * @param mAttendanceList
     */
    private void setUIData(List<Attendance> mAttendanceList) {

        int defaultPosition = 0;
        for (int i = 0; i < mAttendanceList.size(); i++) {
            tab.addTab(tab.newTab());
            tab.getTabAt(i).setText(mAttendanceList.get(i).getGroupname());

            if (mAttendanceList.get(i).isActive()) {
                setList(mAttendanceList.get(i).getAttendancedata());
                defaultPosition = i;
            }
        }

        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setList(mAttendanceList.get(position).getAttendancedata());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tab.getTabAt(defaultPosition).select();

    }

    private void setList(Attendance.AttendancedataBean dataBean) {

        text1.setText("已到" + dataBean.getYd().size() + "人");
        text2.setText("迟到" + dataBean.getCd().size() + "人");
        text3.setText("未到" + dataBean.getWd().size() + "人");
        text4.setText("事假" + dataBean.getSj().size() + "人");
        text5.setText("病假" + dataBean.getBj().size() + "人");

        listview1.setAdapter(new CommonAdapter<Attendance.AttendancedataBean.StuBean>(AttendanceActivity.this, R.layout.item_attendance_grid, dataBean.getYd()) {
            @Override
            protected void convert(ViewHolder viewHolder, Attendance.AttendancedataBean.StuBean item, int position) {
                Glide.with(AttendanceActivity.this).load(item.getPhoto())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into((ImageView) viewHolder.getView(R.id.item_img));
                viewHolder.setText(R.id.item_name, item.getStudentname());
            }
        });
        listview2.setAdapter(new CommonAdapter<Attendance.AttendancedataBean.StuBean>(AttendanceActivity.this, R.layout.item_attendance_grid, dataBean.getCd()) {
            @Override
            protected void convert(ViewHolder viewHolder, Attendance.AttendancedataBean.StuBean item, int position) {
                Glide.with(AttendanceActivity.this).load(item.getPhoto())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into((ImageView) viewHolder.getView(R.id.item_img));
                viewHolder.setText(R.id.item_name, item.getStudentname());
            }
        });
        listview3.setAdapter(new CommonAdapter<Attendance.AttendancedataBean.StuBean>(AttendanceActivity.this, R.layout.item_attendance_grid, dataBean.getWd()) {
            @Override
            protected void convert(ViewHolder viewHolder, Attendance.AttendancedataBean.StuBean item, int position) {
                Glide.with(AttendanceActivity.this).load(item.getPhoto())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into((ImageView) viewHolder.getView(R.id.item_img));
                viewHolder.setText(R.id.item_name, item.getStudentname());
            }
        });
        listview4.setAdapter(new CommonAdapter<Attendance.AttendancedataBean.StuBean>(AttendanceActivity.this, R.layout.item_attendance_grid, dataBean.getSj()) {
            @Override
            protected void convert(ViewHolder viewHolder, Attendance.AttendancedataBean.StuBean item, int position) {
                Glide.with(AttendanceActivity.this).load(item.getPhoto())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into((ImageView) viewHolder.getView(R.id.item_img));
                viewHolder.setText(R.id.item_name, item.getStudentname());
            }
        });
        listview5.setAdapter(new CommonAdapter<Attendance.AttendancedataBean.StuBean>(AttendanceActivity.this, R.layout.item_attendance_grid, dataBean.getBj()) {
            @Override
            protected void convert(ViewHolder viewHolder, Attendance.AttendancedataBean.StuBean item, int position) {
                Glide.with(AttendanceActivity.this).load(item.getPhoto())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into((ImageView) viewHolder.getView(R.id.item_img));
                viewHolder.setText(R.id.item_name, item.getStudentname());
            }
        });


    }

    @Override
    public void showToast(String message) {
        myToast.toast(AttendanceActivity.this, message);
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

    @OnClick({R.id.back, R.id.add11, R.id.add12, R.id.add13, R.id.add14, R.id.add15})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add11:
                if (add1.getText().equals("+")) {
                    add1.setText("-");
                    listview1.setVisibility(View.VISIBLE);
                } else {
                    add1.setText("+");
                    listview1.setVisibility(View.GONE);
                }
                break;
            case R.id.add12:
                if (add2.getText().equals("+")) {
                    add2.setText("-");
                    listview2.setVisibility(View.VISIBLE);
                } else {
                    add2.setText("+");
                    listview2.setVisibility(View.GONE);
                }
                break;
            case R.id.add13:
                if (add3.getText().equals("+")) {
                    add3.setText("-");
                    listview3.setVisibility(View.VISIBLE);
                } else {
                    add3.setText("+");
                    listview3.setVisibility(View.GONE);
                }
                break;
            case R.id.add14:
                if (add4.getText().equals("+")) {
                    add4.setText("-");
                    listview4.setVisibility(View.VISIBLE);
                } else {
                    add4.setText("+");
                    listview4.setVisibility(View.GONE);
                }
                break;
            case R.id.add15:
                if (add5.getText().equals("+")) {
                    add5.setText("-");
                    listview5.setVisibility(View.VISIBLE);
                } else {
                    add5.setText("+");
                    listview5.setVisibility(View.GONE);
                }
                break;
        }
    }
}
