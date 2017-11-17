package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.NoticeBean;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.notice_cardNum)
    EditText mCardNum;
    @BindView(R.id.notice_content)
    WebView mContent;
    @BindView(R.id.notice_title)
    TextView mTitle;
    @BindView(R.id.notice_create)
    TextView mCreate;
    @BindView(R.id.notice_time)
    TextView mTime;
    @BindView(R.id.notice_close)
    TextView mClose;
    @BindView(R.id.notice_ll)
    LinearLayout mLl;
    private Handler _handler = null;
    private Handler foucus_handler = null;
    private String _stringTemp;
    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private NoticeBean noticeBean;
    private MediaPlayer mp;
    private String TAG = "NoticeActivity-->{}";
    private String noticeId = "";
    private Logger logger;

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
        mainDialog = new MainDialog(this);
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        gestureDetector = new GestureDetector(NoticeActivity.this, onGestureListener);

        logger = LoggerFactory.getLogger(getClass());

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            noticeId = intent.getStringExtra("id");
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("id")) {
            noticeId = intent.getStringExtra("id");
        }
        getUIdata();
    }
    @Override
    protected void initData() {
        getCardNum();

        getUIdata();

    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG,"通知界面");
        Constant.SCREENTYPE = 5;
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(NoticeActivity.this));
        map.put("noticeId", noticeId);
        netWorkRequest.doGetRequest(0, Constant.getUrl(NoticeActivity.this, APIConfig.GET_NOTICE), true, map);
    }

    /**
     * 获取卡号
     */
    private void getCardNum() {
        mCardNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                _stringTemp = arg0.toString();
//                LogUtils.i("卡号"+arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                if (_handler == null) {
                    _handler = new Handler();
                    _handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LogUtils.d(_stringTemp);
                            if (!TextUtils.isEmpty(_stringTemp)) {
                                Intent intent = new Intent(NoticeActivity.this, TcpService.class);
                                intent.putExtra("Num", _stringTemp);
                                intent.putExtra("Act", 5);
                                intent.putExtra("ext", "");
                                startService(intent);
                            }
                            mCardNum.setText("");
                            _handler = null;

                        }
                    }, 1000);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                LogUtils.d(arg0);
            }
        });
        foucus_handler = null;
        foucus_handler = new Handler();
        foucus_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardNum.requestFocus();
                foucus_handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    try {
                        float x = e2.getX() - e1.getX();
                        float y = e2.getY() - e1.getY();

                        if (x > 500 && Math.abs(x) > Math.abs(y)) {
                            mainDialog.dismiss();
                        } else if (x < -500 && Math.abs(x) > Math.abs(y)) {
                            mainDialog.show();
                        }

                    } catch (Exception e) {
                    }
                    return true;
                }
            };


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        return super.dispatchTouchEvent(ev);
        LogUtils.d(gestureDetector.onTouchEvent(ev));
        return super.dispatchTouchEvent(ev);
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
        }
    }

    /**
     * 设置界面数据
     *
     * @param noticeBean
     */
    private void setUIData(NoticeBean noticeBean) {
        mTitle.setText(noticeBean.getTitle());
//        mContent.setText(Html.fromHtml(noticeBean.getContent()));
        mContent.loadDataWithBaseURL("about:blank", noticeBean.getContent(), "text/html", "utf-8", null);
        mCreate.setText(noticeBean.getSchoolName() + "\n" + noticeBean.getPublisherName());
        mTime.setText(noticeBean.getPublishTime());
        if (noticeBean.getUrgent()) {
            mClose.setText("停止警报");
            playAlert();
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(NoticeActivity.this, message);
    }

    @Override
    public void showDialog() {
        if (dialog != null&&!mainDialog.isShowing()) {
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

    /**
     * 播放广播音乐
     */
    private void playAlert() {
        logger.debug(TAG, "开始播放");
        if (mp == null) {
            mp = MediaPlayer.create(NoticeActivity.this, R.raw.notify_alert);
            logger.debug(TAG, "初始化");
        }else {
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

    @OnClick(R.id.notice_close)
    public void onViewClicked() {
        if (mp != null) {
            mp.stop();
            logger.debug(TAG, "关闭声音");
        } else {
            finish();
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mp != null) {
            mp.stop();
            mp=null;
        }
    }
}
