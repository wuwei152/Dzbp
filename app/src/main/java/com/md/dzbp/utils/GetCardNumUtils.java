package com.md.dzbp.utils;

import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.constants.Constant;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/26.
 */

public class GetCardNumUtils {

    private Handler foucus_handler;
    private EditText et;
    private BaseActivity act;
    private ArrayList<String> adminNum;

    public GetCardNumUtils(final EditText et, BaseActivity activity) {
        this.et = et;
        this.act = activity;
        adminNum = Constant.getAdminNum();
        foucus_handler = null;
        foucus_handler = new Handler();
        foucus_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                et.requestFocus();
                foucus_handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public GetCardNumUtils(EditText et, boolean f, BaseActivity activity) {
        this.act = activity;
        adminNum = Constant.getAdminNum();
        this.et = et;
    }

    public void getNum(final SetNum setNum) {
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
//                actionId == EditorInfo.IME_ACTION_SEND|| actionId == EditorInfo.IME_ACTION_DONE||
                if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction()) {
                    String num = et.getText().toString().trim();
                    if (!TextUtils.isEmpty(num) && !"\n".equals(num)) {
                        LogUtils.d(num + "卡号");

                        boolean isMng = false;
                        for (String s : adminNum) {
                            if (num.equals(s)) {
                                isMng = true;
                            }
                        }
                        if (isMng) {
                            act.getDialog().show();
                        } else {
                            setNum.setNum(num);
                        }
                    }
                    et.setText("");
                }
                return false;
            }
        });
    }

    public interface SetNum {
        public void setNum(String num);
    }


}
