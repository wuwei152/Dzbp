package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.adapter.ChatAdapter;
import com.md.dzbp.adapter.HonorListAdapter;
import com.md.dzbp.adapter.ParentListAdapter;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.MSGTYPE;
import com.md.dzbp.data.BookBean;
import com.md.dzbp.data.HistoryMsg;
import com.md.dzbp.data.ImageReceiveMessage;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.MsgSendStatus;
import com.md.dzbp.data.StudentInfoBean;
import com.md.dzbp.data.TextReceiveMessage;
import com.md.dzbp.data.TextSendMessage;
import com.md.dzbp.data.VoiceReceiveMessage;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.AudioRecorder.AudioRecorderButton;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.MyRecyclerView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class StudentActivity extends BaseActivity implements UIDataListener, TimeListener {


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

    @BindView(R.id.student_chatlist)
    ListView mChatlist;
    @BindView(R.id.student_audioRecorder)
    AudioRecorderButton mAudioRecorder;
    @BindView(R.id.student_icon)
    SimpleDraweeView mIcon;
    @BindView(R.id.student_class)
    TextView mClass;
    @BindView(R.id.student_name)
    TextView mName;
    @BindView(R.id.student_recycle)
    RecyclerView mRecycle;
    @BindView(R.id.stu_honorRecyclerView)
    MyRecyclerView mHonorRecyclerView;
    @BindView(R.id.student_inputType)
    ImageView mInputType;
    @BindView(R.id.student_textInput)
    EditText mTextInput;
    @BindView(R.id.student_textSend)
    TextView mTextSend;
    @BindView(R.id.student_yue)
    TextView mYue;
    @BindView(R.id.stu_BookListView)
    ListView mBookListView;
    private ArrayList<MessageBase> msgList;
    private ChatAdapter chatAdapter;

    private ACache mAcache;
    private LinearLayoutManager linearLayoutManager;
    private ParentListAdapter parentListAdapter;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private StudentInfoBean studentInfo;
    private StudentInfoBean.ParentsBean currentParent;
    private String mStuUserId;
    private HonorListAdapter honorListAdapter;
    private LinearLayoutManager linearLayoutManager2;
    private Logger logger;
    private String TAG = "StudentActivity-->{}";
    private int inputType = 0;
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
        logger = LoggerFactory.getLogger(getClass());
        mAcache = ACache.get(this);

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
            setIntent(intent);
            mStuUserId = intent.getStringExtra("userId");
            getUIdata();
        }

        inputType = 0;
        mInputType.setImageResource(R.drawable.jianpan);
        mTextInput.setVisibility(View.GONE);
        mAudioRecorder.setVisibility(View.VISIBLE);
        mTextSend.setVisibility(View.GONE);
        mTextInput.setFocusable(false);
        mTextInput.setFocusableInTouchMode(true);
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(mTextInput.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(StudentActivity.this));
        map.put("studentId", mStuUserId);
        netWorkRequest.doGetRequest(0, Constant.getUrl(StudentActivity.this, APIConfig.GET_STUDENT), true, map);

        Map map2 = new HashMap();
        map2.put("studentId", mStuUserId);
        netWorkRequest.doGetRequest(2, Constant.getUrl(StudentActivity.this, APIConfig.GET_BALANCE), false, map2);

        mBookListView.setAdapter(null);

        Map map3 = new HashMap();
        map3.put("studentId", mStuUserId);
        map3.put("is_lend_history", "1");
        netWorkRequest.doGetRequest(3, Constant.getUrl(StudentActivity.this, APIConfig.GET_DZLENDLIST), false, map3);

//        List<BookBean> bookBeansList = new ArrayList<>();
//        bookBeansList.add(new BookBean("我的伯父毛岸英","1","45.6","2020-09-23","2020-10-22"));
//        bookBeansList.add(new BookBean("毛岸英","2","45.6","2020-09-23","2020-10-22"));
//        bookBeansList.add(new BookBean("父毛岸英","2","45.6","2020-09-23","2020-10-22"));
//        bookBeansList.add(new BookBean("我的伯父","1","45.6","2020-09-23","2020-10-22"));
//        bookBeansList.add(new BookBean("我的伯父毛岸英","1","45.6","2020-09-23","2020-10-22"));
//        bookBeansList.add(new BookBean("我的伯父毛岸英我的伯父毛岸英","2","45.6","2020-09-23","2020-10-22"));
//        bookBeansList.add(new BookBean("我的伯父毛岸英我的伯父毛岸英我的伯父毛岸英","1","45.6","2020-09-23","2020-10-22"));
//        mBookListView.setAdapter(new CommonAdapter<BookBean>(StudentActivity.this, R.layout.item_book_list, bookBeansList) {
//            @Override
//            protected void convert(ViewHolder viewHolder, BookBean item, int position) {
//                viewHolder.setText(R.id.tab1, item.getTitle());
//                viewHolder.setText(R.id.tab2, item.getPrice());
//                viewHolder.setText(R.id.tab3, item.getLend_status().equals("1") ? "借出" : "还回");
//                viewHolder.setText(R.id.tab4, item.getAdd_time());
//                viewHolder.setText(R.id.tab5, item.getMust_time());
//                viewHolder.setText(R.id.tab6, item.getEnd_time());
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "学生界面");
        Constant.SCREENTYPE = 2;
        Act = 2;
        ext = "";
        LogUtils.d("EventBus注册");
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("EventBus解注册");
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

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
            new TimeUtils(StudentActivity.this, this);

            mClassName.setText(gradeName + "\n\n" + className);
            mClass.setText(gradeName + "  " + className);
            mAddr.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(StudentActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            if (!TextUtils.isEmpty(alias) && !alias.equals("null")) {
                mAlias.setText("(" + alias + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e);
        }

    }

    @OnClick({R.id.student_back, R.id.student_inputType, R.id.student_textSend})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.student_back:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.student_textSend:
                SendTextMsg();
                break;
            case R.id.student_inputType:
                if (inputType == 0) {
                    inputType = 1;
                    mInputType.setImageResource(R.drawable.yuyin);
                    mTextInput.setVisibility(View.VISIBLE);
                    mAudioRecorder.setVisibility(View.GONE);
                    mTextSend.setVisibility(View.VISIBLE);
                    mTextInput.requestFocus();
                    mTextInput.setFocusable(true);
                    mTextInput.setFocusableInTouchMode(true);
                    //开启软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.showSoftInput(mTextInput, InputMethodManager.SHOW_FORCED);
                    }
                } else if (inputType == 1) {
                    inputType = 0;
                    mInputType.setImageResource(R.drawable.jianpan);
                    mTextInput.setVisibility(View.GONE);
                    mAudioRecorder.setVisibility(View.VISIBLE);
                    mTextSend.setVisibility(View.GONE);
                    mTextInput.setFocusable(false);
                    mTextInput.setFocusableInTouchMode(true);
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(mTextInput.getWindowToken(), 0); //强制隐藏键盘
                    }
                }
                break;
        }
    }

    /**
     * 发送文字消息
     */
    private void SendTextMsg() {
        String inputText = mTextInput.getText().toString();
        if (TextUtils.isEmpty(inputText)) {
            showToast("请输入要发送的内容！");
            return;
        }
        TextSendMessage msg = new TextSendMessage(MSGTYPE.MSGTYPE_TEXT, TimeUtils.currentTimeLong(), studentInfo.getStudent().getPhoto(), studentInfo.getStudent().getAccountname(), inputText, studentInfo.getStudent().getAccountid(), currentParent.getAccountid());
        msg.setSendMsg(true);
        chatAdapter.addData(msg);
        EventBus.getDefault().post(msg);
        mTextInput.setText("");
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(mTextInput.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * 获取卡号
     * 未加持续请求焦点。防止输入时抢焦点
     */
    private void getCardNum() {

        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum, true, this);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {
                    Intent intent = new Intent(StudentActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 2);
                    intent.putExtra("ext", "");
                    startService(intent);
                }
            }
        });

        mTextInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    LogUtils.d("失去焦点");
                    mCardNum.setFocusable(true);
                    mCardNum.requestFocus();
                }
            }
        });
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
                    try {
                        VoiceSendMessage msg = new VoiceSendMessage(MSGTYPE.MSGTYPE_VOICE, TimeUtils.currentTimeLong(), studentInfo.getStudent().getPhoto(), studentInfo.getStudent().getAccountname(), FilePath, 0, file.getName(), studentInfo.getStudent().getAccountid(), currentParent.getAccountid());
                        msg.setSendMsg(true);
                        chatAdapter.addData(msg);
                        EventBus.getDefault().post(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.debug(TAG, e.getMessage());
                    }
                }
            }
        });

        mTextInput.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    SendTextMsg();
                    return true;
                }
                return false;
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
        if (!event.isSendMsg()) {
            LogUtils.d(event);
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

//    private void setFlickerAnimation(int offset, View iv_chat_head) {
//        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
//        animation.setDuration(50); // duration - half a second
//        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
//        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
//        animation.setRepeatMode(Animation.REVERSE); //
//        animation.setStartOffset(offset);
//        iv_chat_head.setAnimation(animation);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
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
                if (historyMsgsList != null) {

                    String studentId = studentInfo.getStudent().getAccountid();
                    msgList.clear();
                    for (HistoryMsg msg : historyMsgsList) {
                        if (msg.getMsgtype() == 1) {
                            if (msg.getSendid().equals(currentParent.getAccountid())) {
                                TextReceiveMessage message = new TextReceiveMessage(TimeUtils.parseLong(msg.getCreatetime()), currentParent.getPhoto(), currentParent.getAccountname(), msg.getContent());
                                msgList.add(message);
                            } else if (msg.getReceiveid().equals(currentParent.getAccountid())) {
                                TextSendMessage message = new TextSendMessage(1, TimeUtils.parseLong(msg.getCreatetime()), studentInfo.getStudent().getPhoto(), studentInfo.getStudent().getAccountname(), msg.getContent(), studentId, currentParent.getAccountid());
                                message.setSendMsg(true);
                                msgList.add(message);
                            }
                        } else if ((msg.getMsgtype() == 3 || msg.getMsgtype() == 4)) {
                            if (msg.getSendid().equals(currentParent.getAccountid())) {
                                VoiceReceiveMessage message = new VoiceReceiveMessage(TimeUtils.parseLong(msg.getCreatetime()), currentParent.getPhoto(), currentParent.getAccountname(), msg.getRes());
                                msgList.add(message);
                            } else if (msg.getReceiveid().equals(currentParent.getAccountid())) {
                                VoiceSendMessage message = new VoiceSendMessage(4, TimeUtils.parseLong(msg.getCreatetime()), studentInfo.getStudent().getPhoto(), studentInfo.getStudent().getAccountname(), "", 0, msg.getRes(), studentId, currentParent.getAccountid());
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
        } else if (code == 2) {
            if (data != null) {
                String yue = data + "";
                if (!TextUtils.isEmpty(yue)) {
                    mYue.setText("¥ " + yue + " 元");
                }
            }
        } else if (code == 3) {
            if (data != null) {
                ArrayList<BookBean> bookBeansList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<BookBean>>() {
                });
                if (bookBeansList != null) {
                    mBookListView.setAdapter(new CommonAdapter<BookBean>(StudentActivity.this, R.layout.item_book_list, bookBeansList) {
                        @Override
                        protected void convert(ViewHolder viewHolder, BookBean item, int position) {
                            viewHolder.setText(R.id.tab1, item.getTitle());
                            viewHolder.setText(R.id.tab2, item.getPrice());
                            viewHolder.setText(R.id.tab3, item.getLend_status().equals("1") ? "借出" : "还回");
                            viewHolder.setText(R.id.tab4, item.getAdd_time());
                            viewHolder.setText(R.id.tab5, item.getMust_time());
                            viewHolder.setText(R.id.tab6, item.getEnd_time());
                        }
                    });
                }
            }
        }
    }

    /**
     * 设置UI数据
     */
    private void setUIData(StudentInfoBean studentInfo) {
        mAddr.setText("教室编号:" + studentInfo.getAddress());
        StudentInfoBean.StudentBean student = studentInfo.getStudent();
        if (student != null) {
            try {
                mIcon.setImageURI(Uri.parse(student.getPhoto()));
            } catch (Exception e) {
                logger.error("Studentactivity-->{}", e.getMessage());
            }
            mName.setText(student.getAccountname());
            mClassName.setText(student.getGradename() + "\n\n" + student.getClassname());
            mClass.setText(student.getGradename() + "  " + student.getClassname());
        }
        List<StudentInfoBean.ParentsBean> parents = studentInfo.getParents();
        if (parents != null) {
            if (parents.size() > 0) {
                parents.get(0).setSelect(true);
            }
            parentListAdapter.setDatas((ArrayList<StudentInfoBean.ParentsBean>) parents);
        }
        if (parents != null && parents.size() > 0 && student != null) {
            currentParent = parents.get(0);
            getHistoryMsg(student.getAccountid(), parents.get(0).getAccountid());
        }

        List<StudentInfoBean.HonorBean> honor = studentInfo.getHonor();
        if (honor != null) {
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
    public void getTime(String time) {
        mTime.setText(time);
    }

    @Override
    public void cancelRequest() {
        netWorkRequest.CancelPost();
    }

}
