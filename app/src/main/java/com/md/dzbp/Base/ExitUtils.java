package com.md.dzbp.Base;

import android.app.Activity;

import java.util.ArrayList;


/**
 * @ClassName:      ExitUtils
 * @Description:    退出应用程序。
 * 在每一个Activity中加入（ExitUtils.getInstance().addActivity(this);）
 * 在退出应用的按钮时调用：exitActivity.getInstance().exit();
 * @Author:         wuw
 */
public class ExitUtils {


    private ArrayList<Activity> activityList = new ArrayList<Activity>();
    private static ExitUtils instance;

    private ExitUtils() {
    }

    // 单例模式中获取唯一的实例
    public static ExitUtils getInstance() {
        if (null == instance) {
            instance = new ExitUtils();
        }
        return instance;
    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    // 遍历所有Activity并finish

    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
//        System.exit(0);
    }
}
