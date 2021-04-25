package com.md.dzbp.data;

/**
 * Created by Administrator on 2018/12/18.
 */

public class MainDialogEvent {
    private String num;
    private int type;

    public MainDialogEvent() {
    }

    public MainDialogEvent(String num, int type) {
        this.num = num;
        this.type = type;
    }

    public MainDialogEvent(String num) {
        this.num = num;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
