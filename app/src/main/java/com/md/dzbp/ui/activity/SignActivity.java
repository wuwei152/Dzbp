package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.Meetingbean;
import com.md.dzbp.data.SignEvent;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.service.UploadService;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.Log4j;
import com.nanchen.compresshelper.CompressHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import butterknife.BindView;

public class SignActivity extends BaseActivity implements TimeListener, UIDataListener {

    @BindView(R.id.sign_cardNum)
    EditText mCardNum;
    @BindView(R.id.sign_time)
    TextView mTime;
    @BindView(R.id.sign_date)
    TextView mDate;
    private Camera camera;
    private String TAG = "SignActivity";
    private Handler card_handler = null;
    private Handler foucus_handler = null;
    private String card_stringTemp;
    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private Camera.Parameters parameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_sign;
    }

    @Override
    protected void initUI() {
        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(SignActivity.this, onGestureListener);
        //获取时间日期
        new TimeUtils(SignActivity.this, this);
        //进度
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log4j.e(TAG, e.getMessage());
            camera = Camera.open(Camera.getNumberOfCameras() - 1);
        }

        initCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    //相机参数的初始化设置
    private void initCamera() {
        parameters = camera.getParameters();
        parameters.setPictureFormat(PixelFormat.JPEG);
        parameters.setPictureSize(320, 320);  // 部分定制手机，无法正常识别该方法。
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
        camera.setParameters(parameters);
        camera.startPreview();
        camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

    }

    @Override
    protected void initData() {
        getCardNum();
        mDate.setText(TimeUtils.getStringDate());
        getUIdata();

    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
//        Map map = new HashMap();
//        map.put("deviceId", Constant.getDeviceId(SignActivity.this));
//        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_MEETING), true, map);
    }

    /**
     * 拍照
     */
    private void TakePicture(final String cardNum) {
        Log4j.d(TAG,"开始拍照！");
        camera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    File filePath = new File(FileUtils.getDiskCacheDir(SignActivity.this), "sign");
                    if (!filePath.exists()) {
                        filePath.mkdirs();
                    }
                    File fileName = new File(filePath, System.currentTimeMillis() + (int) (Math.random() * 1000) + ".jpg");
                    fileName.createNewFile();
                    FileOutputStream fos = new FileOutputStream(fileName);
                    fos.write(data);
                    fos.flush();
                    fos.close();

                    Log4j.d(TAG,"拍照成功！");
                    Intent intent = new Intent(SignActivity.this, TcpService.class);
                    intent.putExtra("Num", cardNum);
                    intent.putExtra("Act", 7);
                    intent.putExtra("ext", fileName.getName());
                    startService(intent);
                    compress(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * 压缩图片
     *
     * @param oldFile
     */
    private void compress(File oldFile) {
        File newFile = new CompressHelper.Builder(this)
                .setMaxWidth(320)  // 默认最大宽度为720
                .setMaxHeight(320) // 默认最大高度为960
                .setQuality(70)    // 默认压缩质量为80
                .setFileName(oldFile.getName().substring(0, oldFile.getName().indexOf("."))) // 设置你需要修改的文件名
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(FileUtils.getDiskCacheDir(SignActivity.this) + "sign_compress")
                .build()
                .compressToFile(oldFile);
        Log4j.d(TAG,"压缩图片！");
        Intent intent = new Intent(SignActivity.this, UploadService.class);
        intent.putExtra("file", newFile);
        startService(intent);
        if (oldFile.exists()) {
            oldFile.delete();
        }
    }

    /**
     * 获取刷卡卡号
     */
    private void getCardNum() {
        mCardNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                card_stringTemp = arg0.toString();
//                LogUtils.i("卡号"+arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                if (card_handler == null) {
                    card_handler = new Handler();
                    card_handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LogUtils.d(card_stringTemp);
                            if (!TextUtils.isEmpty(card_stringTemp)) {
                                TakePicture(card_stringTemp);
                            } else {
                                Log4j.d(TAG, "获取卡号失败");
                            }
                            mCardNum.setText("");
                            card_handler = null;

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
     * 手势滑动弹出框
     */
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

    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
//                meetingbean = JSON.parseObject(data.toString(), new TypeReference<Meetingbean>() {
//                });
//                if (meetingbean != null) {
//                    setUIData(meetingbean);
//                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(SignActivity.this, message);
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

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataStatusEvent(SignEvent event) {
        if (event.getType() == 1 && event.isStatus()) {
            showToast(event.getName() + "签到成功！");
        }else if (event.getType() == 1 && !event.isStatus()){
            showToast("签到失败！");
        }
    }

}
