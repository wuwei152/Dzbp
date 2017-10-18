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
import android.widget.TextView;

import com.md.dzbp.R;
import com.md.dzbp.ui.activity.ExamActivity;
import com.md.dzbp.ui.activity.MainActivity;
import com.md.dzbp.ui.activity.MeetingActivity;
import com.md.dzbp.ui.activity.NoticeActivity;
import com.md.dzbp.ui.activity.PatrolActivity;
import com.md.dzbp.ui.activity.StudentActivity;
import com.md.dzbp.ui.activity.TeacherActivity;
import com.md.dzbp.utils.ACache;

/**
 * 创建自定义的dialog
 */
public class MainDialog extends Dialog {

    private TextView act1, act2, act3, act4, act5, act6, act7, bt1, bt2;
    private Context context;
    private final SmdtManager smdt;
    private EditText et;
    private final ACache mAcache;

    public MainDialog(Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
        WindowManager.LayoutParams attr = getWindow().getAttributes();
        if (attr != null) {
            attr.height = android.view.ViewGroup.LayoutParams.MATCH_PARENT;
            attr.gravity = Gravity.RIGHT;
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
        et.setText("");
        super.dismiss();
    }

    public void closeBoard() {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
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

        bt1 = (TextView) findViewById(R.id.bt1);
        bt2 = (TextView) findViewById(R.id.bt2);
        et = (EditText) findViewById(R.id.et);


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

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et.getText().toString())) {
                    myToast.toast(context, "请输入管理密码！");
                    return;
                }
                String adminPsw = mAcache.getAsString("AdminPsw");
                if (!et.getText().toString().equals(adminPsw)) {
                    myToast.toast(context, "密码错误！");
                    return;
                }
                //显示状态栏
                smdt.smdtSetStatusBar(context, true);
                closeBoard();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //隐藏状态栏
                smdt.smdtSetStatusBar(context, false);
                closeBoard();
            }
        });


    }

}
