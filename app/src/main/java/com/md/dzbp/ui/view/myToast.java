package com.md.dzbp.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.md.dzbp.R;

/**
 * @ClassName: myToast
 * @Description: [Toast工具]
 * @Author: wuw
 * @CreateDate: 2016-04-07  11:18
 */
public class myToast {
    public static Toast toast;
    private static View view;

    public static void toast(Context context, String str) {
        if (toast == null) {
            view = null;
            if (context != null) {
                view = LayoutInflater.from(context).inflate(R.layout.toast_item,
                        null); // 加载布局文件
                toast = new Toast(context); // 创建一个toast
                toast.setDuration(Toast.LENGTH_SHORT); // 设置toast显示时间，整数值
                toast.setGravity(Gravity.CENTER, 0, 300); // toast的显示位置，这里居中靠下显示
                toast.setView(view); // 設置其显示的view,
                toast.setText(str);
                if (!TextUtils.isEmpty(str)) {
                    toast.show();
                }
            }
        } else {
            toast.setText(str);
            toast.show();
        }
    }
}