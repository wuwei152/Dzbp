package com.md.dzbp.utils;

import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;

/**
 * Created by Administrator on 2017/12/26.
 */

public class GetCardNumUtils {

    private Handler foucus_handler;
    private EditText et;

    public GetCardNumUtils(final EditText et) {
        this.et = et;
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

    public GetCardNumUtils(EditText et, boolean f) {
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
                        setNum.setNum(num);
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
