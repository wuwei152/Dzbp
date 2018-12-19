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
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.FileInfo;
import com.md.dzbp.data.LoginEvent;
import com.md.dzbp.data.Meetingbean;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.HorizontalListView;
import com.md.dzbp.ui.view.MsgDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 兴趣班--走班制
 */
public class InterestClassActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.interest_cardNum)
    EditText mCardNum;
    @BindView(R.id.interest_time)
    TextView mTime;
    @BindView(R.id.interest_date)
    TextView mDate;
    @BindView(R.id.interest_temp)
    TextView mTemp;
    @BindView(R.id.interesting_GridView)
    GridView mGridView;
    @BindView(R.id.interest_mainAddr)
    TextView mMainAddr;
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
    @BindView(R.id.interest_listTitle)
    TextView mListTitle;
    @BindView(R.id.interest_listTime)
    TextView mListTime;
    @BindView(R.id.interest_listAddr)
    TextView mListAddr;
    @BindView(R.id.interest_listHost)
    TextView mListHost;
    @BindView(R.id.interest_Hl)
    HorizontalListView mHl;

    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private List<Meetingbean.MeetingUserListBean> interestingUserList;
    private ACache mAcache;
    private Logger logger;
    private String TAG = "InterestClassActivity-->{}";
    private ArrayList<FileInfo> files;
    private String address;
    private String classMng;

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
        //获取时间日期
        new TimeUtils(InterestClassActivity.this, this);
        //进度
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

        address = mAcache.getAsString("Address").trim();
        classMng = mAcache.getAsString("ClassMng").trim();
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
//        mMainAddr.setText(interestingbean.getAddress());
//        mMainTitle.setText(interestingbean.getName());
//        mMainSub.setText(interestingbean.getSummary());
//        mMainDate.setText(interestingbean.getStartTime() + "        " + interestingbean.getAddress());
//        mHost.setText("会议主持：" + hostName);
//        mNum.setText("参会人员：" + interestingUserList.size() + "人");
//        mListTitle.setText(interestingbean.getName());
//        mListTime.setText(interestingbean.getStartTime());
//        mListAddr.setText(interestingbean.getAddress());
//        mListHost.setText(hostName);
//        mListSub.setText(interestingbean.getSummary());

        mMainAddr.setText("教学地址："+address+"教室");
        mListAddr.setText(address+"教室");

//        if (address.equals("C304")) {//0204
//            mMainTitle.setText("深圳路小学社团活动课\n昂姆机器人");
//            mListTitle.setText("社团活动课--昂姆机器人");
//            mHost.setText("指导老师：向昌斌  唐锦锦 外 聘");
//            mListHost.setText("向昌斌  唐锦锦 外 聘");
//            mNum.setText("学习人数：" + "50人");
//        } else if (address.equals("A203")) {//301
//            mMainTitle.setText("深圳路小学社团活动课\n手  鼓");
//            mListTitle.setText("社团活动课--手  鼓");
//            mHost.setText("指导老师：向媛媛  外 聘");
//            mListHost.setText("向媛媛  外 聘");
//            mNum.setText("学习人数：" + "18人");
//        } else if (address.equals("A208")) {//306
//            mMainTitle.setText("深圳路小学社团活动课\n小小讲解员");
//            mListTitle.setText("社团活动课--小小讲解员");
//            mHost.setText("指导老师：曹 玢  李玉雪");
//            mListHost.setText("曹 玢  李玉雪");
//            mNum.setText("学习人数：" + "20人");
//        }else if (address.equals("A303")) {//401
//            mMainTitle.setText("深圳路小学社团活动课\n尤克里里");
//            mListTitle.setText("社团活动课--尤克里里");
//            mHost.setText("指导老师：杨晴贇  外 聘");
//            mListHost.setText("杨晴贇  外 聘");
//            mNum.setText("学习人数：" + "16人");
//        }else if (address.equals("A403")) {//501
//            mMainTitle.setText("深圳路小学社团活动课\n百灵鸟朗诵社");
//            mListTitle.setText("社团活动课--百灵鸟朗诵社");
//            mHost.setText("指导老师：余玉兰 吕程晨 王 舒");
//            mListHost.setText("余玉兰 吕程晨 王 舒");
//            mNum.setText("学习人数：" + "30人");
//        }else if (address.equals("A503")) {//603
//            mMainTitle.setText("深圳路小学社团活动课\n立体思维");
//            mListTitle.setText("社团活动课--立体思维");
//            mHost.setText("指导老师：陈雯雯   邱 珺");
//            mListHost.setText("陈雯雯   邱 珺");
//            mNum.setText("学习人数：" + "40人");
//        }else if (address.equals("A101")) {//604
//            mMainTitle.setText("深圳路小学社团活动课\n田   径");
//            mListTitle.setText("社团活动课--田   径");
//            mHost.setText("指导老师：温君臣");
//            mListHost.setText("温君臣");
//            mNum.setText("学习人数：" + "35人");
//        }else if (address.equals("测试机")){
//            mMainTitle.setText("XXX小学社团活动课\n百灵鸟朗诵社");
//            mListTitle.setText("社团活动课--百灵鸟朗诵社");
//            mHost.setText("指导老师：余玉兰 吕程晨 王 舒");
//            mListHost.setText("余玉兰 吕程晨 王 舒");
//            mNum.setText("学习人数：" + "30人");
//        }else {
            mMainTitle.setText("校社团活动课\n立体思维");
            mListTitle.setText("社团活动课");
            mHost.setText("指导老师："+classMng);
            mListHost.setText(classMng);
            mNum.setText("学习人数：" + "36人");
//        }


        setGridData(interestingUserList);

        files = new ArrayList<>();
        files.add(new FileInfo(R.drawable.a1, "0101班-电影鉴赏"));
        files.add(new FileInfo(R.drawable.a2, "0102班-彩贝口才"));
        files.add(new FileInfo(R.drawable.a3, "0103班-儿童简笔画"));
        files.add(new FileInfo(R.drawable.a4, "0104班-国学启蒙"));
//        files.add(new FileInfo(R.drawable.a5, "0105班-童心舞动"));
//        files.add(new FileInfo(R.drawable.a6, "0106班-故事会"));
//        files.add(new FileInfo(R.drawable.a62, "0106班-故事会2"));
//        files.add(new FileInfo(R.drawable.a7, "0108班-电影赏析"));
//        files.add(new FileInfo(R.drawable.a8, "0108班-书法"));
//        files.add(new FileInfo(R.drawable.a35, "0108班-书法2"));
//        files.add(new FileInfo(R.drawable.a9, "0201班-国学经典"));
//        files.add(new FileInfo(R.drawable.a10, "0202班-阅读"));
//        files.add(new FileInfo(R.drawable.a11, "0203班-有声绘本"));
//        files.add(new FileInfo(R.drawable.a12, "0205班-小小阅读者"));
//        files.add(new FileInfo(R.drawable.a13, "0206班-书法"));
//        files.add(new FileInfo(R.drawable.a14, "0301班-手鼓"));
//        files.add(new FileInfo(R.drawable.a15, "0401班-尤克里里"));
//        files.add(new FileInfo(R.drawable.a16, "0402班-乞巧乐园"));
//        files.add(new FileInfo(R.drawable.a17, "0405班-书韵幽幽"));
//        files.add(new FileInfo(R.drawable.a18, "0404班-静雅书社"));
//
//        files.add(new FileInfo(R.drawable.a19, "0406班-动感韵律"));
//        files.add(new FileInfo(R.drawable.a20, "0503班-五年级百花齐放社团"));
//        files.add(new FileInfo(R.drawable.a21, "0504班-五年级文学阅读(1)"));
//        files.add(new FileInfo(R.drawable.a22, "0506班-五年级英语动画"));
//        files.add(new FileInfo(R.drawable.a23, "0601班-传统女工"));
//        files.add(new FileInfo(R.drawable.a24, "0601班-传统女工1"));
//        files.add(new FileInfo(R.drawable.a25, "0602班-棋类"));
//        files.add(new FileInfo(R.drawable.a26, "0605班-软笔书法"));
//        files.add(new FileInfo(R.drawable.a27, "0606班-阅读"));
//        files.add(new FileInfo(R.drawable.a28, "大队部活动室-红领巾鼓号队"));
//        files.add(new FileInfo(R.drawable.a29, "鼓号队社团"));
//        files.add(new FileInfo(R.drawable.a30, "美术活动室-立体思维美术"));
//        files.add(new FileInfo(R.drawable.a31, "美术活动室-玩趣彩墨美术"));
//        files.add(new FileInfo(R.drawable.a32, "美术教室-综合材料美术社团"));
//        files.add(new FileInfo(R.drawable.a33, "舞蹈教室-儿童舞蹈"));
//        files.add(new FileInfo(R.drawable.a34, "音乐活动室-合唱"));



        mHl.setAdapter(new CommonAdapter<FileInfo>(InterestClassActivity.this, R.layout.item_interest_list, files) {
            @Override
            protected void convert(final ViewHolder viewHolder, FileInfo item, int position) {

                GlideImgManager.glideLoader(InterestClassActivity.this, files.get(position).getFileLocal(), R.drawable.pic_not_found, R.drawable.pic_not_found, (ImageView) viewHolder.getView(R.id.item_img), 1);
                viewHolder.setText(R.id.item_text, item.getName());
            }
        });

        mHl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MsgDialog dialog = new MsgDialog(InterestClassActivity.this);
                dialog.setData(files.get(position).getFileLocal());
                dialog.show();
            }
        });
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
                    intent.putExtra("Act", 1);
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
        }, 5000);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }
}
