package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class PatrolEditEvent implements Serializable {
    private int type;
    private boolean status;
    private String file;

    public PatrolEditEvent() {
    }

    public PatrolEditEvent(int type, String file) {
        this.type = type;
        this.file = file;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
