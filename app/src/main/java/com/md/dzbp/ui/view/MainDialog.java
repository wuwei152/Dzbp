package com.md.dzbp.ui.view;


import android.app.Dialog;
import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.activity.ExamActivity;
import com.md.dzbp.ui.activity.InterestClassActivity;
import com.md.dzbp.ui.activity.MainActivity;
import com.md.dzbp.ui.activity.MeetingActivity;
import com.md.dzbp.ui.activity.NoticeActivity;
import com.md.dzbp.ui.activity.PatrolActivity;
import com.md.dzbp.ui.activity.SignActivity;
import com.md.dzbp.ui.activity.StudentActivity;
import com.md.dzbp.ui.activity.TeacherActivity;
import com.md.dzbp.ui.activity.VideoShowActivity;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.GlideImgManager;

import java.util.ArrayList;

/**
 * 创建自定义的dialog
 */
public class MainDialog extends Dialog {

    private TextView act1, act2, act3, act4, act5, act6, act7, act8, act9, act10, act11, bt2, bt1;
    private EditText et1, et2,et3;
    private Context context;
    private final SmdtManager smdt;
    private final ACache mAcache;
    private ArrayList<String> adminNum;

    public MainDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        if (attr != null) {
            attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            attr.gravity = Gravity.LEFT;
        }
        smdt = SmdtManager.create(context);
        mAcache = ACache.get(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.dialog_mainlayout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();

    }

    @Override
    public void dismiss() {
        closeBoard();
//        et1.setText("");
//        et2.setText("");
        super.dismiss();
    }

    public void closeBoard() {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et2.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(et3.getWindowToken(), 0);
//        if (imm.isActive())  //一直是true
//            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
//                    InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        act1 = (TextView) findViewById(R.id.act1);
        act2 = (TextView) findViewById(R.id.act2);
        act3 = (TextView) findViewById(R.id.act3);
        act4 = (TextView) findViewById(R.id.act4);
        act5 = (TextView) findViewById(R.id.act5);
        act6 = (TextView) findViewById(R.id.act6);
        act7 = (TextView) findViewById(R.id.act7);
        act8 = (TextView) findViewById(R.id.act8);
        act9 = (TextView) findViewById(R.id.act9);
        act10 = (TextView) findViewById(R.id.act10);
        act11 = (TextView) findViewById(R.id.act11);

        bt1 = findViewById(R.id.bt1);
        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);

        bt2 = (TextView) findViewById(R.id.bt2);

        adminNum = Constant.getAdminNum();

        act1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MainActivity.class));
                dismiss();
            }
        });
        act2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, TeacherActivity.class));
                dismiss();
            }
        });
        act3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, StudentActivity.class));
                dismiss();
            }
        });
        act4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ExamActivity.class));
                dismiss();
            }
        });
        act5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, PatrolActivity.class));
                dismiss();
            }
        });
        act6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, NoticeActivity.class));
                dismiss();
            }
        });
        act7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, MeetingActivity.class));
                dismiss();
            }
        });
        act8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SignActivity.class));
                dismiss();
            }
        });
        act9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, InterestClassActivity.class));
                dismiss();
            }
        });
        act10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TcpService.class);
                intent.putExtra("Log", "");
                context.startService(intent);
                dismiss();
            }
        });
        act11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, VideoShowActivity.class));
                dismiss();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏状态栏
                smdt.smdtSetStatusBar(context, false);
                dismiss();
            }
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(et1.getText().toString().trim()) && !TextUtils.isEmpty(et2.getText().toString().trim()) && !TextUtils.isEmpty(et3.getText().toString().trim())) {
                    ArrayList<CameraInfo> mCameraInfos = new ArrayList<>();
                    mCameraInfos.add(new CameraInfo(et1.getText().toString().trim(), et3.getText().toString().trim(), "admin", et2.getText().toString().trim()));//测试
                    mAcache.put("CameraInfo", mCameraInfos);
                    et1.setText("");
                    et2.setText("");
                    et3.setText("");
                    myToast.toast(context, "存储成功！");
                } else {
                    myToast.toast(context, "请完善ip端口与密码信息！");
                }
            }
        });


    }

    @Override
    public void show() {
        super.show();
//        smdt.smdtSetStatusBar(context, true);
    }
}
