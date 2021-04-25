package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.MainData;
import com.md.dzbp.data.NoticeBean;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity implements UIDataListener, TimeListener {

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

    @BindView(R.id.notice_content)
    WebView mContent;
    @BindView(R.id.notice_title)
    TextView mTitle;
    @BindView(R.id.notice_create)
    TextView mCreate;
    @BindView(R.id.notice_createtime)
    TextView mCreateTime;
    @BindView(R.id.notice_back)
    TextView mClose;
    @BindView(R.id.notice_list)
    ListView mList;
    @BindView(R.id.notice_head)
    ImageView mHead;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private NoticeBean noticeBean;
    private MediaPlayer mp;
    private String TAG = "NoticeActivity-->{}";
    private String noticeId = "";
    private Logger logger;
    private ACache mAcache;
    private List<MainData.NoticeBean> mNoticeBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initUI() {
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        mAcache = ACache.get(this);
        logger = LoggerFactory.getLogger(getClass());

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            noticeId = intent.getStringExtra("id");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
        if (intent.hasExtra("id")) {
            noticeId = intent.getStringExtra("id");
        }
        getUIdata();
    }

    @Override
    protected void initData() {
        getCardNum();

        getUIdata();

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
            new TimeUtils(NoticeActivity.this, this);

            mClassName.setText(gradeName + "\n\n" + className);
            mAddr.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(NoticeActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            if (!TextUtils.isEmpty(alias) && !alias.equals("null")) {
                mAlias.setText("(" + alias + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "通知界面");
        Constant.SCREENTYPE = 5;
        Act = 5;
        ext = "";

        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map2 = new HashMap();
        map2.put("deviceId", Constant.getDeviceId(NoticeActivity.this));
        netWorkRequest.doGetRequest(1, Constant.getUrl(NoticeActivity.this, APIConfig.GET_LOAD_NOTICE), true, map2);

        if (!TextUtils.isEmpty(noticeId)) {
            Map map = new HashMap();
            map.put("deviceId", Constant.getDeviceId(NoticeActivity.this));
            map.put("noticeId", noticeId);
            netWorkRequest.doGetRequest(0, Constant.getUrl(NoticeActivity.this, APIConfig.GET_NOTICE), true, map);
        } else if (mNoticeBeanList != null && mNoticeBeanList.size() > 0) {
            Map map = new HashMap();
            map.put("deviceId", Constant.getDeviceId(NoticeActivity.this));
            map.put("noticeId", mNoticeBeanList.get(0).getNoticeId() + "");
        }
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
                    Intent intent = new Intent(NoticeActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 5);
                    intent.putExtra("ext", "");
                    startService(intent);
                }
            }
        });
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                noticeBean = JSON.parseObject(data.toString(), new TypeReference<NoticeBean>() {
                });
                if (noticeBean != null) {
                    setUIData(noticeBean);
                }
            }
        } else if (code == 1) {
            if (data != null) {
                mNoticeBeanList = JSON.parseObject(data.toString(), new TypeReference<List<MainData.NoticeBean>>() {
                });
                if (mNoticeBeanList != null) {
                    setNoticeList(mNoticeBeanList);
                }
            }
        }
    }

    /**
     * 设置通知列表
     */
    private void setNoticeList(final List<MainData.NoticeBean> mNoticeBeanList) {
        mList.setAdapter(new CommonAdapter<MainData.NoticeBean>(NoticeActivity.this, R.layout.item_main_notice, mNoticeBeanList) {
            @Override
            protected void convert(ViewHolder viewHolder, MainData.NoticeBean item, int position) {
                viewHolder.setText(R.id.item_name, item.getTitle());
                viewHolder.setText(R.id.item_person, item.getPublisher());
                viewHolder.setText(R.id.item_time, item.getPublishtime());
                GlideImgManager.glideLoader(NoticeActivity.this, item.getPublisherPhoto(), R.drawable.head_icon, R.drawable.head_icon, (ImageView) (viewHolder.getView(R.id.item_img)), 0);
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map map = new HashMap();
                map.put("deviceId", Constant.getDeviceId(NoticeActivity.this));
                map.put("noticeId", mNoticeBeanList.get(position).getNoticeId());
                netWorkRequest.doGetRequest(0, Constant.getUrl(NoticeActivity.this, APIConfig.GET_NOTICE), true, map);
            }
        });
    }

    /**
     * 设置界面数据
     *
     * @param noticeBean
     */
    private void setUIData(NoticeBean noticeBean) {
        try {
            mTitle.setText(noticeBean.getTitle());
            GlideImgManager.glideLoader(NoticeActivity.this, noticeBean.getPublisherPhoto(), R.drawable.head_icon, R.drawable.head_icon, mHead, 0);
            mContent.loadDataWithBaseURL("about:blank", noticeBean.getContent(), "text/html", "utf-8", null);
            mCreate.setText(noticeBean.getPublisherName());
            mCreateTime.setText("发布时间：" + noticeBean.getPublishTime());
            if (noticeBean.getUrgent()) {
                mClose.setText("\u003C  停止警报");
                playAlert();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(NoticeActivity.this, message);
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

    /**
     * 播放广播音乐
     */
    private void playAlert() {
        logger.debug(TAG, "开始播放");
        if (mp == null) {
            mp = MediaPlayer.create(NoticeActivity.this, R.raw.notify_alert);
            logger.debug(TAG, "初始化");
        } else {
            logger.debug(TAG, "重新开始播放");
            try {
                mp.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    logger.debug(TAG, "开始播放");
                    mediaPlayer.start();
                }
            });

            mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    logger.debug(TAG, "播放错误");

                    mp = MediaPlayer.create(NoticeActivity.this, R.raw.notify_alert);
                    logger.debug(TAG, "重置播放器");
                    return false;
                }
            });
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    logger.debug(TAG, "播放完成");
                    mediaPlayer.start();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug(TAG, e.getMessage());
        }
    }

    @OnClick({R.id.notice_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.notice_back:
                if (mp != null) {
                    mp.stop();
                    logger.debug(TAG, "关闭声音");
                } else {
                    finish();
                }
                break;
//            case R.id.notice_sx:
//                Map map2 = new HashMap();
//                map2.put("deviceId", Constant.getDeviceId(NoticeActivity.this));
//                netWorkRequest.doGetRequest(1, Constant.getUrl(NoticeActivity.this, APIConfig.GET_LOAD_NOTICE), true, map2);
//
//                break;
            default:
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mp != null) {
            mp.stop();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mp != null) {
            mp.stop();
        }
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp = null;
        }
    }

    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }
}
