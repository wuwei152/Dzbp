package com.md.dzbp.task;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: ww
 * @CreateDate: 2021/1/6 16:52
 * @Description:
 */
public class CheckAppRunningForegroundTask extends Timer {
    private Context context;
    private Logger logger;
    private static volatile TimerTask timerTask;
    private static volatile CheckAppRunningForegroundTask checkTask;
    private static final String TAG = "CheckAppRunningForegroundTask-->{}";
    private int IsStop = 0;

    private CheckAppRunningForegroundTask(Context context) {
        this.context = context;
        logger = LoggerFactory.getLogger(context.getClass());

        timerTask = new TimerTask() {
            public void run() {
                RunTask();
            }
        };
    }

    public static CheckAppRunningForegroundTask getInstance(Context context) {

        if (checkTask == null) {
            synchronized (SwitchTask.class) {
                if (checkTask == null) {
                    checkTask = new CheckAppRunningForegroundTask(context);
                }
            }
        }
        return checkTask;
    }

    /**
     * 运行任务
     */
    public void TaskRun() {
        checkTask.scheduleAtFixedRate(timerTask, 1000, 5000);
    }

    /**
     * 运行任务
     */
    public void TaskChange(int flag) {
        IsStop = flag;
    }


    private void RunTask() {
        if (isApplicationBroughtToBackground(context) && IsStop == 0) {
            logger.info("班牌非前置APP，切屏！");
            setTopApp();
        } else {
//            logger.info("班牌前置APP检查！");
        }

    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    public boolean isApplicationBroughtToBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;

    }

    //当本应用位于后台时，则将它切换到最前端
    public void setTopApp() {
        //获取ActivityManager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task(任务)
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
            //找到本应用的 task，并将它切换到前台
            if (taskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                activityManager.moveTaskToFront(taskInfo.id, 0);
                break;
            }
        }
    }

}
