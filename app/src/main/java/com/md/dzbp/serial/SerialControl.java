package com.md.dzbp.serial;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.MainDialogEvent;
import com.md.dzbp.tcp.TcpService;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;

public class SerialControl extends SerialHelper {
    private static boolean OnClick = false;
    private static final String TAG = "SerialControl";
    private Context context;
    private ArrayList<String> adminNum = Constant.getAdminNum();
    ;

    public SerialControl(Context context) {
        this.context = context;
    }

    @Override
    protected void onDataReceived(final ComBean ComRecData) {
        LogUtils.i("onDataReceived: " + Arrays.toString(ComRecData.bRec));
        StringBuilder sMsg = new StringBuilder();
        sMsg.append(ComRecData.sRecTime);
        sMsg.append("[");
        sMsg.append(ComRecData.sComPort);
        sMsg.append("]");
        String cardNum = new String(ComRecData.bRec).trim();
        sMsg.append(cardNum);
        sMsg.append("\r\n");
        LogUtils.i("onDataReceived: " + sMsg.toString());
//        MyFunc.ByteArrToHex(ComRecData.bRec)
//        ComRecData.bRec

        boolean isMng = false;
        for (String s : adminNum) {
            if (cardNum.equals(s)) {
                isMng = true;
            }
        }
        if (isMng) {
//            act.getDialog().show();
            EventBus.getDefault().post(new MainDialogEvent(cardNum, 0));
        } else {
            if (!TextUtils.isEmpty(cardNum)) {
                EventBus.getDefault().post(new MainDialogEvent(cardNum, 1));
//                Intent intent = new Intent(context, TcpService.class);
//                intent.putExtra("Num", cardNum);
//                intent.putExtra("Act", 0);
//                intent.putExtra("ext", "");
//                context.startService(intent);
            }
        }

//        Toast.makeText(context, sMsg.toString(), Toast.LENGTH_LONG).show();

    }
}
