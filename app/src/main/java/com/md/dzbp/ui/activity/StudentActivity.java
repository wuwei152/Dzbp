package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.adapter.ChatAdapter;
import com.md.dzbp.adapter.HonorListAdapter;
import com.md.dzbp.adapter.ParentListAdapter;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.MSGTYPE;
import com.md.dzbp.data.HistoryMsg;
import com.md.dzbp.data.ImageReceiveMessage;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.MsgSendStatus;
import com.md.dzbp.data.StudentInfoBean;
import com.md.dzbp.data.TextReceiveMessage;
import com.md.dzbp.data.VoiceReceiveMessage;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.AudioRecorder.AudioRecorderButton;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.MyRecyclerView;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.Log4j;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class StudentActivity extends BaseActivity implements UIDataListener {


    @BindView(R.id.student_chatlist)
    ListView mChatlist;
    @BindView(R.id.student_audioRecorder)
    AudioRecorderButton mAudioRecorder;
    @BindView(R.id.student_cardNum)
    EditText mCardNum;
    @BindView(R.id.student_title)
    TextView mTitle;
    @BindView(R.id.student_addr)
    TextView mAddr;
    @BindView(R.id.student_icon)
    ImageView mIcon;
    @BindView(R.id.student_name)
    TextView mName;
    @BindView(R.id.student_class)
    TextView mClass;
    @BindView(R.id.student_recycle)
    RecyclerView mRecycle;
    @BindView(R.id.stu_honorRecyclerView)
    MyRecyclerView mHonorRecyclerView;
    private ArrayList<MessageBase> msgList;
    private ChatAdapter chatAdapter;

    private Handler getCard_handler = null;
    private Handler foucus_handler = null;
    private String getCard_stringTemp;
    private ACache mAcache;
    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
    private LinearLayoutManager linearLayoutManager;
    private ParentListAdapter parentListAdapter;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private StudentInfoBean studentInfo;
    private StudentInfoBean.ParentsBean currentParent;
    private String mStuUserId;
    private HonorListAdapter honorListAdapter;
    private LinearLayoutManager linearLayoutManager2;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_student;
    }

    @Override
    protected void initUI() {

        Intent intent = getIntent();
        if (intent.hasExtra("userId")) {
            mStuUserId = intent.getStringExtra("userId");
        }

        mAcache = ACache.get(this);

        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(StudentActivity.this, onGestureListener);

        currentParent = new StudentInfoBean.ParentsBean();
        studentInfo = new StudentInfoBean();

        msgList = new ArrayList<>();
        dialog = MyProgressDialog.createLoadingDialog(StudentActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

        linearLayoutManager = new LinearLayoutManager(StudentActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycle.setLayoutManager(linearLayoutManager);
        parentListAdapter = new ParentListAdapter(this);
        mRecycle.setAdapter(parentListAdapter);

        linearLayoutManager2 = new LinearLayoutManager(StudentActivity.this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        mHonorRecyclerView.setLayoutManager(linearLayoutManager2);
        honorListAdapter = new HonorListAdapter(StudentActivity.this);
        mHonorRecyclerView.setAdapter(honorListAdapter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        if (intent.hasExtra("userId")) {
            mStuUserId = intent.getStringExtra("userId");
            getUIdata();
        }
    }

    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(StudentActivity.this));
        map.put("studentId", mStuUserId);
        netWorkRequest.doGetRequest(0, Constant.getUrl(StudentActivity.this, APIConfig.GET_STUDENT), true, map);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("EventBus注册");
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("EventBus解注册");
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {

        getUIdata();

        setList();
        getCardNum();

        parentListAdapter.setOnItemClickLitener(new ParentListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                currentParent = studentInfo.getParents().get(position);
                getHistoryMsg(studentInfo.getStudent().getAccountid(), studentInfo.getParents().get(position).getAccountid());
            }
        });


//        Date d = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String dateNowStr = sdf.format(d);
//        String clearCache = mAcache.getAsString("clearCache");
//        if (TextUtils.isEmpty(clearCache)){
//            mAcache.put("clearCache","");
//        }
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1,Date date2)
    {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }

    /**
     * 获取卡号
     */
    private void getCardNum() {
        mCardNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                getCard_stringTemp = arg0.toString();
//                LogUtils.i("卡号"+arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                if (getCard_handler == null) {
                    getCard_handler = new Handler();
                    getCard_handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LogUtils.d(getCard_stringTemp);
                            if (!TextUtils.isEmpty(getCard_stringTemp)) {
                                Intent intent = new Intent(StudentActivity.this, TcpService.class);
                                intent.putExtra("Num", getCard_stringTemp);
                                intent.putExtra("Act", 2);
                                intent.putExtra("ext", "");
                                startService(intent);
                            }
                            mCardNum.setText("");
                            getCard_handler = null;

                        }
                    }, 1000);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                LogUtils.d(arg0);
            }
        });

        foucus_handler = new Handler();
        foucus_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardNum.requestFocus();
                foucus_handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    /**
     * 设置聊天列表
     * 发送语音消息
     */
    private void setList() {
        chatAdapter = new ChatAdapter(StudentActivity.this, msgList);
        mChatlist.setAdapter(chatAdapter);
        mAudioRecorder.setAudioFinishRecorderListener(new AudioRecorderButton.AudioFinishRecorderListener() {
            @Override
            public void onFinish(int seconds, String FilePath) {
                File file = new File(FilePath);
                if (file.exists()) {
//                    "D:\\\\Website\\\\智慧校园平台\\\\FTP\\\\Voice\\\\" +
                    VoiceSendMessage msg = new VoiceSendMessage(MSGTYPE.MSGTYPE_VOICE, TimeUtils.currentTimeLong(), studentInfo.getStudent().getPhoto(), studentInfo.getStudent().getAccountname(), FilePath, 0, file.getName(), studentInfo.getStudent().getAccountid(), currentParent.getAccountid());
                    msg.setSendMsg(true);
                    chatAdapter.addData(msg);
                    EventBus.getDefault().post(msg);
                }
            }
        });

    }

    /**
     * 接收消息
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(MessageBase event) {
        LogUtils.d(event);
        if (!event.isSendMsg()) {
            String fromUserId = event.getFromUserName();
            if (fromUserId.equals(currentParent.getAccountid())) {
                event.setHeadIcon(currentParent.getPhoto());
                event.setUserName(currentParent.getAccountname());
                if (event.getMsgType() == MSGTYPE.MSGTYPE_TEXT) {
                    LogUtils.d("接受到文本消息");
                    TextReceiveMessage message = (TextReceiveMessage) event;
                    chatAdapter.addData(message);
                } else if (event.getMsgType() == MSGTYPE.MSGTYPE_VOICE) {
                    LogUtils.d("接受到语音消息");
                    VoiceReceiveMessage message = (VoiceReceiveMessage) event;
                    chatAdapter.addData(message);
                } else if (event.getMsgType() == MSGTYPE.MSGTYPE_IMG) {
                    LogUtils.d("接受到图片消息");
                    ImageReceiveMessage message = (ImageReceiveMessage) event;
                    chatAdapter.addData(message);
                }
                Map map = new HashMap();
                map.put("deviceId", Constant.getDeviceId(StudentActivity.this));
                map.put("studentId", studentInfo.getStudent().getAccountid());
                map.put("parentId", currentParent.getAccountid());
                netWorkRequest.doGetRequest(2, Constant.getUrl(StudentActivity.this, APIConfig.GET_READ_MSG), false, map);
            } else {
                for (StudentInfoBean.ParentsBean p : studentInfo.getParents()) {
                    if (p.getAccountid().equals(fromUserId)) {
                        p.setMsgcount(p.getMsgcount() + 1);
                        parentListAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    /**
     * 语音消息发送成功，改变发送状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataStatusEvent(MsgSendStatus event) {
        for (MessageBase messageBase : msgList) {
            if (messageBase instanceof VoiceSendMessage && messageBase.getCreateTime() == event.getMsgTime()) {
                ((VoiceSendMessage) messageBase).setStatus(event.getStatus());
                chatAdapter.notifyDataSetChanged();
            }
        }
    }

    @OnClick(R.id.student_back)
    public void onViewClicked() {
        startActivity(new Intent(this, MainActivity.class));
    }

    private void setFlickerAnimation(int offset, View iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(50); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        animation.setStartOffset(offset);
        iv_chat_head.setAnimation(animation);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                studentInfo = JSON.parseObject(data.toString(), new TypeReference<StudentInfoBean>() {
                });
                if (studentInfo != null) {
                    setUIData(studentInfo);
                }
            }
        } else if (code == 1) {
            if (data != null) {
                ArrayList<HistoryMsg> historyMsgsList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<HistoryMsg>>() {
                });
                if (historyMsgsList != null && historyMsgsList.size() > 0) {

                    String studentId = studentInfo.getStudent().getAccountid();
                    msgList.clear();
                    for (HistoryMsg msg : historyMsgsList) {
                        if (msg.getMsgtype() == 1) {
                            if (msg.getSendid().equals(currentParent.getAccountid())) {
                                TextReceiveMessage message = new TextReceiveMessage(TimeUtils.parseLong(msg.getCreatetime()), currentParent.getPhoto(), currentParent.getAccountname(), msg.getContent());
                                msgList.add(message);
                            }
                        } else if ((msg.getMsgtype() == 3 || msg.getMsgtype() == 4)) {
                            if (msg.getSendid().equals(currentParent.getAccountid())) {
                                VoiceReceiveMessage message = new VoiceReceiveMessage(TimeUtils.parseLong(msg.getCreatetime()), currentParent.getPhoto(), currentParent.getAccountname(), msg.getRes());
                                msgList.add(message);
                            } else if (msg.getReceiveid().equals(currentParent.getAccountid())) {
                                VoiceSendMessage message = new VoiceSendMessage(4, TimeUtils.parseLong(msg.getCreatetime()), studentInfo.getStudent().getPhoto(), studentInfo.getStudent().getAccountname(), msg.getRes(), 0, "", studentId, currentParent.getAccountid());
                                message.setStatus(1);
                                message.setSendMsg(true);
                                msgList.add(message);
                            }
                        }
                    }
                    chatAdapter.notifyDataSetChanged();
                    if (msgList != null && msgList.size() > 5) {
                        mChatlist.smoothScrollToPosition(msgList.size() - 1);
                    }
                    currentParent.setMsgcount(0);
                    LogUtils.d(studentInfo.getParents());
                    parentListAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 设置UI数据
     */
    private void setUIData(StudentInfoBean studentInfo) {
        mAddr.setText(studentInfo.getAddress());
        StudentInfoBean.StudentBean student = studentInfo.getStudent();
        if (student != null) {
            mTitle.setText(student.getAccountname());
            try {
                Glide.with(StudentActivity.this).load(student.getPhoto()).into(mIcon);
            } catch (Exception e) {
                Log4j.e("Studentactivity", e.getMessage());
            }
            mName.setText(student.getAccountname());
            mClass.setText(student.getGradename() + student.getClassname());
        }
        List<StudentInfoBean.ParentsBean> parents = studentInfo.getParents();
        if (parents != null) {
            if (parents.size()>0){
                parents.get(0).setSelect(true);
            }
            parentListAdapter.setDatas((ArrayList<StudentInfoBean.ParentsBean>) parents);
        }
        if (parents != null && parents.size() > 0 && student != null) {
            currentParent = parents.get(0);
            getHistoryMsg(student.getAccountid(), parents.get(0).getAccountid());
        }

        List<StudentInfoBean.HonorBean> honor = studentInfo.getHonor();
        if (honor!=null){
            honorListAdapter.setDatas((ArrayList<StudentInfoBean.HonorBean>) honor);
        }
    }

    /**
     * 获取历史消息
     *
     * @param studentId
     * @param parentId
     */
    private void getHistoryMsg(String studentId, String parentId) {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(StudentActivity.this));
        map.put("studentId", studentId);
        map.put("parentId", parentId);
        netWorkRequest.doGetRequest(1, Constant.getUrl(StudentActivity.this, APIConfig.GET_STUDENT_HISTORY_MSG), true, map);
    }

    @Override
    public void showToast(String message) {
        myToast.toast(StudentActivity.this, message);
    }

    @Override
    public void showDialog() {
        if (dialog != null) {
            dialog.show();
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

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 500 && Math.abs(x) > Math.abs(y)) {
                        mainDialog.dismiss();
                    } else if (x < -500 && Math.abs(x) > Math.abs(y)) {
                        mainDialog.show();
                    }
                    return true;
                }
            };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
