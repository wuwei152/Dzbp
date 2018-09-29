package com.md.dzbp.tcp;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.ProcessService;


/**
 * 用于唤醒LocalService的RemoteService
 */
public class RemoteService extends Service {

    private RemoteBinder binder;   //和LocalService的binder相互绑定
    private RemoteConn myConn;
    private final String TAG = "RemoteService";

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        binder = new RemoteBinder();
        if(myConn == null){
            myConn = new RemoteConn();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindService(new Intent(this, TcpService.class),
                myConn,
                Context.BIND_IMPORTANT);
        return super.onStartCommand(intent, flags, startId);
    }

    class RemoteBinder extends ProcessService.Stub{
        @Override
        public String getServiceName() throws RemoteException {
            return "RemoteService";
        }
    }


    class RemoteConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d("LocalService连接成功--------");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d("LocalService killed--------");
            startService(new Intent(RemoteService.this, TcpService.class));
            bindService(new Intent(RemoteService.this, TcpService.class),
                    myConn, Context.BIND_IMPORTANT);
        }
    }

}
