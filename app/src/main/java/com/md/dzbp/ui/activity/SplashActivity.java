package com.md.dzbp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.utils.ACache;

/**
 * 启动界面
 * (1)判断是否是首次加载应用
 * (2)是，则进入GuideActivity；否，则进入MainActivity
 * (3)3s后执行(2)操作
 */
public class SplashActivity extends BaseActivity {
    boolean isLogin = false;

    private static final int GO_LOGIN = 1001;
    private static final int GO_HOME = 1002;
    // 延迟秒
    private static final long SPLASH_DELAY_MILLIS = 0;

    /**
     * Handler:跳转到不同界面
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_LOGIN:
                    goLogin();
                    break;
                case GO_HOME:
                    goHome();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private ACache mAcache;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(SplashActivity.this);
    }

    @Override
    protected void initData() {
        deviceId = mAcache.getAsString("DeviceId");
        if (!TextUtils.isEmpty(deviceId)) {
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS);
        }
    }

    /**
     * 进入登陆页面
     */
    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, SettingActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    /**
     * 进入首页
     */
    private void goHome() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }
}