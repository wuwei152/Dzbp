package com.md.dzbp.task;

import android.app.smdt.SmdtManager;
import android.content.Context;
import android.content.Intent;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.data.WorkTimePeriod;
import com.md.dzbp.data.WorkTimePoint;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.tcp.ServerManager;
import com.md.dzbp.ui.activity.ExamActivity;
import com.md.dzbp.ui.activity.InterestClassActivity;
import com.md.dzbp.ui.activity.MainActivity;
import com.md.dzbp.ui.activity.MeetingActivity;
import com.md.dzbp.ui.activity.NoticeActivity;
import com.md.dzbp.ui.activity.SignActivity;
import com.md.dzbp.ui.activity.TeacherActivity;
import com.md.dzbp.utils.ACache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 任务队列
 * 单例模式
 * <p>
 * 任务类型               0　作息时间 1　会议 2 通知 3考试  4 考勤  100开关屏  5走班制
 * <p>
 * 其中 0/4/100 为低级别任务
 * 1/2/3/5为高级别任务
 */

public class SwitchTask extends Timer {

    private static volatile SwitchTask switchTask;
    private static volatile TimerTask timerTask;
    private ArrayList<WorkTimePoint> mList;
    private Context context;
    private int level;//判断是否正在执行高阶人物 0没有 1 有
    private static final String TAG = "SwitchTask-->{}";
    private Logger logger;
    private int CheckTag;
    private final ACache mACache;
    private SmdtManager smdtManager;
    private Timer timer_checkDelay;

    private SwitchTask(Context context) {
        this.context = context;
        logger = LoggerFactory.getLogger(context.getClass());
        mList = new ArrayList<>();
        mACache = ACache.get(context);
        smdtManager = SmdtManager.create(context);
        try {
            ArrayList<WorkTimePeriod> list = (ArrayList<WorkTimePeriod>) mACache.getAsObject("Task");
            if (list != null) {
                ArrayList<WorkTimePoint> list1 = WorkTimePoint.GetWorkTimePointList(list);
                SetTaskList(list1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        timerTask = new TimerTask() {
            public void run() {
                MainTask();
            }
        };
    }

    public static SwitchTask getInstance(Context context) {

        if (switchTask == null) {
            synchronized (SwitchTask.class) {
                if (switchTask == null) {
                    switchTask = new SwitchTask(context);
                }
            }
        }
        return switchTask;
    }

    /**
     * 运行任务
     */
    public void TaskRun() {
        switchTask.scheduleAtFixedRate(timerTask, 1000, 1000);
    }

    /**
     * 设置任务队列
     */
    public void SetTaskList(ArrayList<WorkTimePoint> mList) {
        this.mList = mList;
//        LogUtils.d(mList);
        CheckCurrentTask();
    }

    public void AddTaskList(ArrayList<WorkTimePoint> list) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.addAll(list);
        ListSort(mList);
        CheckCurrentTask();
    }

    public ArrayList<WorkTimePoint> GetTaskList() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList;
    }

    /**
     * 主任务
     */
    private void MainTask() {
        String mCurrentDate = TimeUtils.getCurrentDate();
        String currentTime = TimeUtils.getCurrentTime("yyyy:MM:dd:HH:mm:ss");
        SimpleDateFormat df = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setTime(date.getTime() + 1000);
        String currentTime2 = df.format(date);
        date.setTime(date.getTime() - 2000);
        String currentTime1 = df.format(date);

//        LogUtils.d("开始轮询任务" + currentTime);
        for (WorkTimePoint point : mList) {
            if (TimeUtils.compareDate(currentTime1, point.getTriggerTime()) && TimeUtils.compareDate(point.getTriggerTime(), currentTime2)) {
                logger.debug(TAG, "执行任务:" + point.getName() + point.getStartTask());
                StartTask(currentTime1 + "/" + currentTime2, point);
            }
        }


        if (TimeUtils.compareDate(currentTime1, mCurrentDate + ":01:00:00") && TimeUtils.compareDate(mCurrentDate + ":01:00:00", currentTime2)
                || TimeUtils.compareDate(currentTime1, mCurrentDate + ":05:00:00") && TimeUtils.compareDate(mCurrentDate + ":05:00:00", currentTime2)) {
            logger.debug(TAG, "设备未关机，执行断连刷新课表任务！！！！！！！" + mCurrentDate);

            try {
                ArrayList<WorkTimePeriod> list = (ArrayList<WorkTimePeriod>) mACache.getAsObject("Task");
                if (list != null) {
                    ArrayList<WorkTimePoint> list1 = WorkTimePoint.GetWorkTimePointList(list);
                    SetTaskList(list1);
                }

                ServerManager.getInstance(context).Stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * 隔固定时间对当前切换结果进行强制检查
         */
        if (CheckTag % 700 == 0) {
            CheckTag = 0;
            switchTask.CheckCurrentTask();
        }
        CheckTag++;

    }

    /**
     * 固定时间后强制检查
     *
     * @param seconds 多少秒后执行
     */

    public void CheckCurrentTaskdelay(int seconds) {
        logger.debug(TAG, seconds + "秒后开始执行强切后检查实际页面任务");
        if (timer_checkDelay != null) {
            timer_checkDelay.cancel();
        }
        timer_checkDelay = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                logger.debug(TAG, "开始执行强切后检查实际页面任务");
                switchTask.CheckCurrentTask();
            }
        };

        timer_checkDelay.schedule(task, seconds * 1000);
    }

    /**
     * 根据任务类型执行任务
     *
     * @param point
     */
    private void StartTask(String currentTime, WorkTimePoint point) {
        switch (point.getType()) {
            case 0:
                if (level == 0 && point.getStartTask() == 0 && point.getTaskstate() == 0) {
                    //上课
                    Intent intent = new Intent(context, TeacherActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-上课页面" + currentTime);
                } else if (level == 0 && point.getStartTask() == 1 && point.getTaskstate() == 0) {
                    //下课
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-下课页面" + currentTime);
                } else {
                    logger.debug(TAG, "屏幕切换失败level:" + level + "/" + point.getTaskstate());
                }
                break;
            case 1:
                if (point.getStartTask() == 0 && point.getTaskstate() == 0) {
                    //会议开始
                    Intent intent = new Intent(context, MeetingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    level = 1;
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-会议开始" + currentTime);
                } else if (point.getStartTask() == 1 && point.getTaskstate() == 0) {
                    //会议结束
                    level = 0;
                    WorkTimePoint startTaskPoint = getTobeStartTaskPoint(currentTime, point);
                    if (startTaskPoint != null) {
                        StartTask(currentTime, startTaskPoint);
                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-会议结束" + currentTime);
                } else {
                    logger.debug(TAG, "屏幕切换失败level:" + level + "/" + point.getTaskstate());
                }
                break;
            case 2:
                if (point.getStartTask() == 0 && point.getTaskstate() == 0) {
                    //通知开始
                    Intent intent = new Intent(context, NoticeActivity.class);
                    intent.putExtra("id", point.getTaskTag());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    level = 1;
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-通知开始" + currentTime);
                } else if (point.getStartTask() == 1 && point.getTaskstate() == 0) {
                    //通知结束
                    level = 0;
                    WorkTimePoint startTaskPoint = getTobeStartTaskPoint(currentTime, point);
                    if (startTaskPoint != null) {
                        StartTask(currentTime, startTaskPoint);
                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-通知结束" + currentTime);
                } else {
                    logger.debug(TAG, "屏幕切换失败level:" + level + "/" + point.getTaskstate());
                }

                break;
            case 3:
                if (point.getStartTask() == 0 && point.getTaskstate() == 0) {
                    //考试开始
                    Intent intent = new Intent(context, ExamActivity.class);
                    intent.putExtra("id", point.getTaskTag());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    level = 1;
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-考试开始" + currentTime);
                } else if (point.getStartTask() == 1 && point.getTaskstate() == 0) {
                    //考试结束
                    level = 0;
                    Intent intent = new Intent(context, ExamActivity.class);
                    intent.putExtra("id", point.getTaskTag());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-考试结束" + currentTime);
                } else {
                    logger.debug(TAG, "屏幕切换失败level:" + level + "/" + point.getTaskstate());
                }
                break;
            case 4:
                if (level == 0 && point.getStartTask() == 0 && point.getTaskstate() == 0) {
                    //打卡开始
                    Intent intent = new Intent(context, SignActivity.class);
                    //获取结束打卡的时间，做倒计时
                    for (WorkTimePoint p : mList) {
                        if (point.getTaskTag().equals(p.getTaskTag()) && p.getStartTask() == 1) {
                            String endTime = "";
                            String triggerTime = p.getTriggerTime();
                            if (triggerTime.length()>12){
                                endTime = triggerTime.substring(11);
                            }
                            intent.putExtra("End", endTime);
//                            LogUtils.d("End" + p.getTriggerTime());
                        }
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-考勤模式" + currentTime);
                } else if (level == 0 && point.getStartTask() == 1 && point.getTaskstate() == 0) {
                    //打卡结束
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-考勤模式结束" + currentTime);
                } else {
                    logger.debug(TAG, "屏幕切换失败level:" + level + "/" + point.getTaskstate());
                }
                break;
            case 5:
                if (point.getStartTask() == 0 && point.getTaskstate() == 0) {
                    //走班开始
                    Intent intent = new Intent(context, InterestClassActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    level = 1;
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-走班开始" + currentTime);
                } else if (point.getStartTask() == 1 && point.getTaskstate() == 0) {
                    //走班结束
                    level = 0;
                    WorkTimePoint startTaskPoint = getTobeStartTaskPoint(currentTime, point);
                    if (startTaskPoint != null) {
                        StartTask(currentTime, startTaskPoint);
                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕跳转成功-走班结束" + currentTime);
                } else {
                    logger.debug(TAG, "屏幕切换失败level:" + level + "/" + point.getTaskstate());
                }
                break;
            case 100://开关屏操作
                if (level == 0 && point.getStartTask() == 0 && point.getTaskstate() == 0) {
                    //关屏开始
                    smdtManager.smdtSetLcdBackLight(0);//关闭背光
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕关屏" + currentTime);
                } else if (level == 0 && point.getStartTask() == 1 && point.getTaskstate() == 0) {
                    smdtManager.smdtSetLcdBackLight(1);//打开背光
                    point.setTaskstate(1);
                    logger.debug(TAG, "屏幕开屏" + currentTime);
                } else {
                    logger.debug(TAG, "屏幕开关失败level:" + level + "/" + point.getTaskstate());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 寻找任务结束后需要开始的任务
     *
     * @param point 结束任务
     * @return 需开始任务
     */
    private WorkTimePoint getTobeStartTaskPoint(String currentTime, WorkTimePoint point) {
        if (mList == null || mList.size() == 0) {
            return null;
        }
        int beginTask = 0;
        int endTask = 0;
        for (int i = 0; i < mList.size(); i++) {
            if (point.getTaskTag().equals(mList.get(i).getTaskTag())) {
                if (mList.get(i).getStartTask() == 0) {
                    beginTask = i;
                } else if (mList.get(i).getStartTask() == 1) {
                    endTask = i;
                }
            }
        }
        /**
         * 寻找结束任务之前是否有已开始未结束的高级别任务
         *
         * 有就返回离当前结束任务最近的开始未结束高级别任务
         */
        for (int i = endTask; i >= 0; i--) {
            if ((mList.get(i).getType() == 1 || mList.get(i).getType() == 2 || mList.get(i).getType() == 3 || mList.get(i).getType() == 5) && mList.get(i).getStartTask() == 0 && !mList.get(i).getTaskTag().equals(point.getTaskTag())) {
                if (!IsTaskEnd(currentTime, mList.get(i))) {
                    mList.get(i).setTaskstate(0);
                    return mList.get(i);
                }
            }
        }

        /**
         * 没有已开始未结束高级别任务
         * 寻找离结束任务左侧最近的低级别任务
         */
        level = 0;
        for (int i = endTask; i >= 0; i--) {
            if (mList.get(i).getType() == 0 || mList.get(i).getType() == 4 || mList.get(i).getType() == 100) {
                mList.get(i).setTaskstate(0);
                return mList.get(i);
            }
        }
        return null;
    }

    /**
     * 检查当前任务
     * 登录后检查已开始的任务
     */
    public void CheckCurrentTask() {
        logger.debug(TAG, "开始检查当前时间已执行的任务并执行");
        String currentTime = TimeUtils.getCurrentTime("yyyy:MM:dd:HH:mm:ss");
        if (mList == null || mList.size() == 0) {
            logger.debug(TAG, "时间列表为空");
            return;
        }
        int endTask = 0;
        WorkTimePoint point = null;
        WorkTimePoint toStartPoint = null;

        for (int i = mList.size() - 1; i >= 0; i--) {
            if (TimeUtils.compareDate(mList.get(i).getTriggerTime(), currentTime)) {
                point = mList.get(i);
                endTask = i;
                break;
            }

        }

        if (point != null) {
            logger.debug(TAG, "最近已开始任务为：" + point.getName() + point.getTriggerTime());
            if (point.getType() == 1 || point.getType() == 2 || point.getType() == 3 || point.getType() == 5) {
                if (point.getStartTask() == 0) {//是高级别开始任务--直接执行
                    toStartPoint = point;
                } else if (point.getStartTask() == 1) {//是高级别结束任务--检查未结束任务
                    toStartPoint = getTobeStartTaskPoint(currentTime, point);
                }
            } else {
                /**
                 * 是低级别切换任务
                 * 寻找结束任务之前是否有已开始未结束任务
                 * 有就返回离当前结束任务最近的开始未结束任务
                 */
                for (int i = endTask; i >= 0; i--) {
                    if ((mList.get(i).getType() == 1 || mList.get(i).getType() == 2 || mList.get(i).getType() == 3 || mList.get(i).getType() == 5) && mList.get(i).getStartTask() == 0 && !mList.get(i).getTaskTag().equals(point.getTaskTag())) {
                        if (!IsTaskEnd(currentTime, mList.get(i))) {
                            mList.get(i).setTaskstate(0);
                            toStartPoint = mList.get(i);
                        }
                    }
                }
                //未找到已开始未结束任务，直接返回当前任务
                if (toStartPoint == null) {
                    level = 0;
                    toStartPoint = point;
                }
            }
        } else {
            logger.debug(TAG, "未找到最近的已结束任务");
        }

        if (toStartPoint != null) {
            toStartPoint.setTaskstate(0);//执行前对任务已执行状态置0，不然无法执行
            logger.debug(TAG, "检查结束，开始跳转：" + toStartPoint.getName());
            StartTask(currentTime, toStartPoint);
        } else {
            for (WorkTimePoint workTimePoint : mList) {
                if (workTimePoint.getType() == 3) {
                    logger.debug(TAG, "检查结束，考试未开始状态，回到考试页面");
                    Intent intent = new Intent(context, ExamActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    return;
                }
            }
            logger.debug(TAG, "检查结束，未找到跳转页面，回到下课页面");
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 判断当前开始任务是否已结束
     *
     * @param currentTime
     * @param startTask
     * @return
     */
    private boolean IsTaskEnd(String currentTime, WorkTimePoint startTask) {
        for (WorkTimePoint point : mList) {
            if (point.getTaskTag().equals(startTask.getTaskTag()) && point.getStartTask() == 1) {
                if (TimeUtils.compareDate(point.getTriggerTime(), currentTime)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 对任务按时间排序
     */
    private static void ListSort(List<WorkTimePoint> list) {
        Collections.sort(list, new Comparator<WorkTimePoint>() {
            @Override
            public int compare(WorkTimePoint o1, WorkTimePoint o2) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
                try {
                    Date dt1 = format.parse(o1.getTriggerTime());
                    Date dt2 = format.parse(o2.getTriggerTime());
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }
}
