package com.md.dzbp.serial;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.tcp.TcpService;

import java.util.Arrays;

public class SerialControl extends SerialHelper {
    private static boolean OnClick = false;
    private static final String TAG = "SerialControl";
    private Context context;

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
        sMsg.append(MyFunc.ByteArrToHex(ComRecData.bRec));
        sMsg.append("\r\n");
        LogUtils.i("onDataReceived: " + sMsg.toString());
//        MyFunc.ByteArrToHex(ComRecData.bRec)
//        ComRecData.bRec

        if (!TextUtils.isEmpty(MyFunc.ByteArrToHex(ComRecData.bRec))) {
            Intent intent = new Intent(context, TcpService.class);
            intent.putExtra("Num", MyFunc.ByteArrToHex(ComRecData.bRec));
            intent.putExtra("Act", 0);
            intent.putExtra("ext", "");
            context.startService(intent);
        }

        Toast.makeText(context, sMsg.toString(), Toast.LENGTH_LONG).show();

    }
}
