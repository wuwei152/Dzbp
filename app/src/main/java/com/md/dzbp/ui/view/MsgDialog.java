package com.md.dzbp.ui.view;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.dzbp.R;

import java.util.ArrayList;

/**
 * 创建自定义的dialog
 */
public class MsgDialog extends AlertDialog {

    private TextView tv;
    private LinearLayout ll;
    private ArrayList<String> mlist;
    private Context context;

    public MsgDialog(Context context) {
        super(context, R.style.MybrostcastDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msglayout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(true);
        //初始化界面控件
        initView();

        initData();

    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mlist = new ArrayList<>();
        tv = (TextView) findViewById(R.id.tv);
        ll = (LinearLayout) findViewById(R.id.ll);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {
    }

    public void setData(ArrayList list) {
        this.mlist = list;
    }
}
