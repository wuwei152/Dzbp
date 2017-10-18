package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.data.ClassInfoBean;
import com.md.dzbp.data.ClassManagerBean;
import com.md.dzbp.data.PatrolBean;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.constants.Constant;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PatrolActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.patrol_cardNum)
    EditText mCardNum;
    @BindView(R.id.patrol_classinfo)
    TextView mClassinfo;
    @BindView(R.id.patrol_addr)
    TextView mAddr;
    @BindView(R.id.patrol_teacherIcon)
    ImageView mTeacherIcon;
    @BindView(R.id.patrol_teacherName)
    TextView mTeacherName;
    @BindView(R.id.patrol_teacherCourse)
    TextView mTeacherCourse;
    @BindView(R.id.patrol_teacherPeroid)
    TextView mTeacherPeroid;
    @BindView(R.id.patrol_teacherChapter)
    TextView mTeacherChapter;
    @BindView(R.id.patrol_mngClassInfo)
    TextView mMngClassInfo;
    @BindView(R.id.patrol_mngIcon)
    ImageView mMngIcon;
    @BindView(R.id.patrol_mngName)
    TextView mMngName;
    @BindView(R.id.patrol_mngCourse)
    TextView mMngCourse;
    @BindView(R.id.patrol_listview)
    ListView mListview;
    private Dialog dialog;
    private Handler _handler = null;
    private Handler foucus_handler = null;
    private String _stringTemp;
    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
    private NetWorkRequest netWorkRequest;
    private PatrolBean patrolBean;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_patrol;
    }

    @Override
    protected void initUI() {
        Intent intent = getIntent();
        if (intent.hasExtra("userId")){
            userId = intent.getStringExtra("userId");
        }

        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);
        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(PatrolActivity.this, onGestureListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("userId")){
            userId = intent.getStringExtra("userId");
        }
        getUIdata();
    }

    @Override
    protected void initData() {
        getCardNum();

        getUIdata();
    }

    /**
     * 获取UI数据
     */
    private void getUIdata() {
        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(PatrolActivity.this));
        map.put("accountId", userId);
        netWorkRequest.doGetRequest(0, Constant.getUrl(PatrolActivity.this, APIConfig.GET_PATROL), true, map);
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
                                Intent intent = new Intent(PatrolActivity.this, TcpService.class);
                                intent.putExtra("Num", _stringTemp);
                                intent.putExtra("Act", 3);
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
        foucus_handler = new Handler();
        foucus_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardNum.requestFocus();
                foucus_handler.postDelayed(this, 1000);
            }
        }, 1000);
    }


    @OnClick({R.id.patrol_back, R.id.patrol_confrim})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.patrol_back:
                finish();
                break;
            case R.id.patrol_confrim:
                ClassInfoBean classInfo = patrolBean.getClassInfo();
                PatrolBean.TeacherBean teacher = patrolBean.getTeacher();
                List<PatrolBean.InspectionParametersBean> inspectionParameters = patrolBean.getInspectionParameters();
                if (patrolBean!=null&&classInfo!=null&&teacher!=null&&inspectionParameters!=null) {
                    JSONObject obj = new JSONObject();
                    obj.put("deviceId", Constant.getDeviceId(PatrolActivity.this));
                    obj.put("classId",classInfo.getClassId());
                    obj.put("teacherId",teacher.getAccountId());
                    obj.put("periodId", teacher.getPeriodId());
                    obj.put("subjectId", teacher.getSubjectId());
                    obj.put("createAccountId", userId);
                    JSONArray array = new JSONArray();
                    for (PatrolBean.InspectionParametersBean bean : inspectionParameters) {
                        JSONObject object = new JSONObject();
                        object.put("parameterId",bean.getId());
                        object.put("positive",bean.isPositive());
                        array.add(object);
                    }
                    obj.put("scores",array);
                    netWorkRequest.doPostRequest(1, Constant.getUrl(PatrolActivity.this, APIConfig.Post_PATROL), true, obj.toJSONString());
                }else {
                    showToast("未获取到巡查信息！");
                }
                break;
        }
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


    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                patrolBean = JSON.parseObject(data.toString(), new TypeReference<PatrolBean>() {
                });
                if (patrolBean != null) {
                    setUIData(patrolBean);
                }
            }
        }else if (code ==1){
            showToast("提交成功！");
            startActivity(new Intent(PatrolActivity.this,TeacherActivity.class));
        }
    }

    /**
     * 设置界面数据
     *
     * @param patrolBean
     */
    private void setUIData(PatrolBean patrolBean) {
        ClassInfoBean classInfo = patrolBean.getClassInfo();
        PatrolBean.TeacherBean teacher = patrolBean.getTeacher();
        ClassManagerBean classManager = patrolBean.getClassManager();
        if (classInfo != null) {
            mClassinfo.setText(classInfo.getGradeName() + classInfo.getClassName());
            mMngClassInfo.setText(classInfo.getGradeName() + classInfo.getClassName());
            mAddr.setText(classInfo.getAddress());
        }
        if (teacher != null) {
            Glide.with(PatrolActivity.this).load(teacher.getPhoto()).into(mTeacherIcon);
            mTeacherName.setText("教师：" + teacher.getAccountName());
            mTeacherCourse.setText("任  课：" + teacher.getSubjectName().toString());
            mTeacherPeroid.setText("节次：" + teacher.getPeriodName());
            mTeacherChapter.setText("章节：");
        }

        if (classManager != null) {
            Glide.with(PatrolActivity.this).load(classManager.getPhoto()).into(mMngIcon);
            mMngName.setText("班主任：" + classManager.getAccountName());
            mMngCourse.setText("任课：" + classManager.getSubjects().toString());
        }
        List<PatrolBean.InspectionParametersBean> inspectionParameters = patrolBean.getInspectionParameters();
        if (inspectionParameters != null && inspectionParameters.size() > 0) {
            mListview.setAdapter(new CommonAdapter<PatrolBean.InspectionParametersBean>(PatrolActivity.this, R.layout.item_patrol, inspectionParameters) {
                @Override
                protected void convert(ViewHolder viewHolder, final PatrolBean.InspectionParametersBean item, int position) {
                    viewHolder.setText(R.id.item_name, item.getParametername());
                    ((ToggleButton) viewHolder.getView(R.id.item_check)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if (b){
                                item.setPositive(true);
                            }else {
                                item.setPositive(false);
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(PatrolActivity.this, message);
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
        if (errorCode == 1) {
            showToast("提交失败！");
            return;
        }
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

}
