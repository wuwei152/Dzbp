package com.md.dzbp.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/8/15.
 * 获取时间,并每隔一秒实时更新时间
 */
public class TimeUtils {
    private Context context;
    private TimeListener timeListener;

    public TimeUtils(Context context, TimeListener timeListener) {
        this.context = context;
        this.timeListener = timeListener;
        new TimeThread().start();
    }

    //在主线程里面处理消息并更新UI界面
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    long sysTime = System.currentTimeMillis();
                    CharSequence sysTimeStr = DateFormat.format("HH:mm:ss", sysTime);
                    timeListener.getTime(sysTimeStr.toString());
                    break;
                default:
                    break;

            }
        }
    };

    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");//设置日期格式
        String timeStr = df.format(new Date());
        return timeStr;
    }

    public static String countDown(int mTime) {
        return mTime / 1000 + "";
    }

    public class TimeThread extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = 1;  //消息(一个整型值)
                    mHandler.sendMessage(msg);// 每隔1秒发送一个msg给mHandler
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }


    // 获取当前时间，long型
    public static long currentTimeLong() {
        return new Date().getTime();
    }

    // 获取当前时间，字符串形式
    public static String currentTime() {
        Date date = new Date();
        return toString(date);
    }
    // 获取当前时间，字符串形式
    public static String currentTimelog() {
        Date date = new Date();
        return toString(date).replaceAll(" ","").replaceAll(":","_");
    }

    // 从字符串, 获取日期, 如time = "2016-3-16 4:12:16"
    public static Date toDate(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);

            return date;
        } catch (ParseException e) {
            return null;
        }
    }

    // 从long, 获取日期
    public static Date toDate(long millSec) {
        return new Date(millSec);
    }
    // 从long, 获取日期
    public static String toDateString(long millSec) {

        return toString(toDate(millSec));
    }

    // 日期转化为字符串形式
    public static String toString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String  getStringDate(){
        //获取当前日期
        Calendar ca = Calendar.getInstance();
        int year = ca.get(Calendar.YEAR);//获取年份
        int month = ca.get(Calendar.MONTH) + 1;//获取月份
        int day = ca.get(Calendar.DATE);//获取日
        String weak = String.valueOf(ca.get(Calendar.DAY_OF_WEEK));//获取日
        String monthStr = "" + month;
        String dayStr = "" + day;
        if (month < 10) {
            monthStr = "0" + month;
        }
        if (day < 10) {
            dayStr = "0" + day;
        }
        if ("1".equals(weak)) {
            weak = "天";
        } else if ("2".equals(weak)) {
            weak = "一";
        } else if ("3".equals(weak)) {
            weak = "二";
        } else if ("4".equals(weak)) {
            weak = "三";
        } else if ("5".equals(weak)) {
            weak = "四";
        } else if ("6".equals(weak)) {
            weak = "五";
        } else if ("7".equals(weak)) {
            weak = "六";
        }
        String dateNow = year + "年" + monthStr + "月" + dayStr + "日    " + "星期" + weak;
        return dateNow;
    }

    public static long parseLong(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
