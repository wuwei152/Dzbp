package com.md.dzbp.serial;

import android.os.Build;
import android.os.Looper;
import android.os.SystemClock;

import com.apkfuns.logutils.LogUtils;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import android_serialport_api.BaseSerialPort;

/**
 * @author benjaminwan
 * 串口辅助工具类
 */
public abstract class SerialHelper {
    private BaseSerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private SendThread mSendThread;
    private String sPort = null;
    private int iBaudRate = 9600;
    private boolean _isOpen = false;
    private byte[] _bLoopData = new byte[]{0x30};
    private int iDelay = 500;

    //----------------------------------------------------
    public SerialHelper(String sPort, int iBaudRate) {
        this.sPort = sPort;
        this.iBaudRate = iBaudRate;
    }

    public SerialHelper() {
        this("/dev/ttyS3", 9600);
    }

    public SerialHelper(String sPort) {
        this(sPort, 9600);
    }

    public SerialHelper(String sPort, String sBaudRate) {
        this(sPort, Integer.parseInt(sBaudRate));
    }

    //----------------------------------------------------
    public boolean open() {
//        LogUtils.d("open: "+ sPort+"  "+iBaudRate+"  "+android.os.Build.MODEL);
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mSerialPort = new android_serialport_api.SerialPort(new File(sPort), iBaudRate, 0);
            } else {
                mSerialPort = new SerialPort(new File(sPort), iBaudRate, 0);
            }
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            this.sendHex("AABB0600000001060205");
            mReadThread = new ReadThread();
            mReadThread.start();
            mSendThread = new SendThread();
            mSendThread.setSuspendFlag();
            mSendThread.start();
            _isOpen = true;
            return _isOpen;
        } catch (Exception e) {
            LogUtils.e("打开串口失败: " + e.toString());
            return false;
        } catch (Error error) {
            LogUtils.e("打开串口失败: " + error.toString());
            return false;
        }

    }

    //----------------------------------------------------
    public void close() {
        LogUtils.d("close: " + sPort + "  " + iBaudRate);
        if (mReadThread != null) {
            mReadThread.interrupt();
            mReadThread = null;
        }
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        _isOpen = false;
    }

    //----------------------------------------------------
    public void send(byte[] bOutArray) {
        try {
            LogUtils.d("write1" + Arrays.toString(bOutArray));
            if (mOutputStream != null) {
                mOutputStream.write(bOutArray);
                mOutputStream.flush();
                LogUtils.d("write2");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("e: " + e.toString());
        }
    }

    private static final String TAG = "SerialHelper";

    //----------------------------------------------------
    public void sendHex(String sHex) {
        LogUtils.d("sendHex: " + sHex);
        byte[] bOutArray = MyFunc.HexToByteArr(sHex);
        send(bOutArray);
    }

    //----------------------------------------------------
    public void sendTxt(String sTxt) {
        LogUtils.d("sendTxt: " + sTxt);
        byte[] bOutArray = sTxt.getBytes();
        send(bOutArray);
    }

    //----------------------------------------------------
    private class ReadThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();
            while (!isInterrupted()) {
                try {
                    if (mInputStream == null) {
                        LogUtils.d("mInputStream == null");
                        return;
                    }
                    byte[] buffer = new byte[512];
//                    int size = mInputStream.read(buffer);
//                    LogUtils.d("ReadThread: ");
                    int size = mInputStream.available();
//                    LogUtils.d("ReadThread: "+size);
                    if (size > 0) {

                        size = mInputStream.read(buffer);
                        ComBean ComRecData = new ComBean(sPort, buffer, size);
                        onDataReceived(ComRecData);
                    }
                    SystemClock.sleep(50);
                } catch (Exception e) {
                    LogUtils.d("e: " + e.toString());
                    e.printStackTrace();
                    return;
                } catch (Error error) {
                    LogUtils.d("e: " + error.toString());
                    error.printStackTrace();
                    return;
                }
            }
        }
    }

    //----------------------------------------------------
    private class SendThread extends Thread {
        public boolean suspendFlag = false;// 控制线程的执行

        @Override
        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    LogUtils.d("suspendFlag: " + suspendFlag);
                    while (suspendFlag) {
                        SystemClock.sleep(50);
                    }
                }
                send(getbLoopData());
                try {
                    Thread.sleep(iDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //线程暂停
        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        //唤醒线程
        public synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }
    }

    //----------------------------------------------------
    public int getBaudRate() {
        return iBaudRate;
    }

    public boolean setBaudRate(int iBaud) {
        LogUtils.d("设置波特率: " + iBaud + "   " + _isOpen);
        if (_isOpen) {
            return false;
        } else {
            iBaudRate = iBaud;
            return true;
        }
    }

    public boolean setBaudRate(String sBaud) {
        int iBaud = Integer.parseInt(sBaud);
        return setBaudRate(iBaud);
    }

    //----------------------------------------------------
    public String getPort() {
        return sPort;
    }

    public boolean setPort(String sPort) {
        if (_isOpen) {
            return false;
        } else {
            this.sPort = sPort;
            return true;
        }
    }

    //----------------------------------------------------
    public boolean isOpen() {
        return _isOpen;
    }

    //----------------------------------------------------
    public byte[] getbLoopData() {
        return _bLoopData;
    }

    //----------------------------------------------------
    public void setbLoopData(byte[] bLoopData) {
        this._bLoopData = bLoopData;
    }

    //----------------------------------------------------
    public void setTxtLoopData(String sTxt) {
        this._bLoopData = sTxt.getBytes();
    }

    //----------------------------------------------------
    public void setHexLoopData(String sHex) {
        this._bLoopData = MyFunc.HexToByteArr(sHex);
    }

    //----------------------------------------------------
    public int getiDelay() {
        return iDelay;
    }

    //----------------------------------------------------
    public void setiDelay(int iDelay) {
        this.iDelay = iDelay;
    }

    //----------------------------------------------------
    public void startSend() {
        if (mSendThread != null) {
            mSendThread.setResume();
        }
    }

    //----------------------------------------------------
    public void stopSend() {
        if (mSendThread != null) {
            mSendThread.setSuspendFlag();
        }
    }

    public void initOutputStream() {
        mOutputStream = mSerialPort.getOutputStream();
    }

    public OutputStream getmOutputStream() {
        return mOutputStream;
    }

    //----------------------------------------------------
    protected abstract void onDataReceived(ComBean ComRecData);
}