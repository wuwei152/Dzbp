package com.md.dzbp.serial;

import android.content.Context;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;

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

        Toast.makeText(context, sMsg.toString(), Toast.LENGTH_LONG).show();

    }
}
