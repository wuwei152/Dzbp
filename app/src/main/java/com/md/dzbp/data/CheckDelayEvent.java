package com.md.dzbp.data;

/**
 * Created by Administrator on 2018/12/18.
 */

public class CheckDelayEvent {
    private int seconds;

//    public CheckDelayEvent() {
//    }

    public CheckDelayEvent(int seconds) {
        this.seconds = seconds;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
