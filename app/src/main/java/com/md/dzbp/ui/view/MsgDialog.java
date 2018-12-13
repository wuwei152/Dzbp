package com.md.dzbp.ui.view;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.md.dzbp.R;
import com.md.dzbp.ui.activity.InterestClassActivity;
import com.md.dzbp.utils.GlideImgManager;

import java.util.ArrayList;

/**
 * 创建自定义的dialog
 */
public class MsgDialog extends AlertDialog {

    private ImageView img;
    private LinearLayout ll;
    private Context context;
    private int res;

    public MsgDialog(Context context) {
        super(context, R.style.MyDialog2);
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
        img = (ImageView) findViewById(R.id.img);
        ll = (LinearLayout) findViewById(R.id.ll);

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {

        if (res!=0){
            GlideImgManager.glideLoader(context, res, R.drawable.pic_not_found, R.drawable.pic_not_found, img, 1);
        }
    }

    public void setData(int res) {
        this.res = res;
    }
}
