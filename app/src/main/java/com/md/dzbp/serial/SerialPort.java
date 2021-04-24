package com.md.dzbp.serial;

import android.util.Log;

import com.apkfuns.logutils.LogUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.BaseSerialPort;

public class SerialPort extends BaseSerialPort{

    private static final String TAG = "SerialPort";

    /*
     * Do not remove or rename the field mFd: it is used by native method close();
     */
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
        LogUtils.d("device:" + device.getAbsolutePath());
        /* Check access permission */
        if (!device.canRead() || !device.canWrite()) {
            try {
                /* Missing read/write permission, trying to chmod the file */
                Process su;
                su = Runtime.getRuntime().exec("/system/xbin/su");
                String cmd = "chmod 777 " + device.getAbsolutePath() + "\n"
                        + "exit\n";
                su.getOutputStream().write(cmd.getBytes());
                if ((su.waitFor() != 0) || !device.canRead()
                        || !device.canWrite()) {
                    throw new SecurityException();
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new SecurityException();
            }
        }
        try {
            mFd = open(device.getAbsolutePath(), baudrate, flags);
            LogUtils.d("打开串口");
        }catch (UnsatisfiedLinkError error){
            error.printStackTrace();
            LogUtils.e("error: "+error.toString());
        }
        if (mFd == null) {
            LogUtils.e("native open returns null");
            throw new IOException();
        }
        else
        {
            mFileInputStream = new FileInputStream(mFd);
            mFileOutputStream = new FileOutputStream(mFd);
        }
    }

    // Getters and setters
    @Override
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }

    // JNI
    private native static FileDescriptor open(String path, int baudrate, int flags);
    public native void close();
    static {
        System.loadLibrary("serial_port");
    }
}
