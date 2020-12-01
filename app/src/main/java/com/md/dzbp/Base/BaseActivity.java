package com.md.dzbp.Base;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.R;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.utils.SnapUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpCycleContext;

public abstract class BaseActivity extends AppCompatActivity implements HttpCycleContext {

    protected final String HTTP_TASK_KEY = "HttpTaskKey_" + hashCode();
    protected MainDialog mainDialog;
//    protected GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnim();
        ExitUtils.getInstance().addActivity(this);
        // 去标题栏
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        setContentView(getLayoutResID());

        //打印打开的activity的类名和所在的包
        LogUtils.i(getClass().getName());
        ButterKnife.bind(this) ;
        mainDialog = new MainDialog(this);
//        gestureDetector = new GestureDetector(BaseActivity.this, MainGestureDetector.getGestureDetector(mainDialog));
        initUI();
        initData();
    }

    /**
     * getContentView ID
     *
     * @return
     */
    protected abstract int getLayoutResID();

    protected void setAnim() {
        overridePendingTransition(R.anim.base_slide_right_in, R.anim.base_slide_remain);
    }

    public MainDialog getDialog(){
        return mainDialog;
    }

    /**
     * 初始化UI
     */
    protected abstract void initUI();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    protected void onPause() {
        super.onPause();
        if (mainDialog != null && mainDialog.isShowing()) {
            mainDialog.dismiss();
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return gestureDetector.onTouchEvent(event);
//    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, R.anim.base_slide_out);
        super.onBackPressed();
    }
    @Override
    public String getHttpTaskKey() {
        return HTTP_TASK_KEY;
    }

    /**
     * 发送截屏回复
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScreenshotEvent(ScreenShotEvent event) {
        if (event.isSend()) {
            try {
                LogUtils.d("开始截屏");
                SnapUtils snapUtils = new SnapUtils(this,event.getDeviceId(),event.getMsgid());
                snapUtils.requestScreenShot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
