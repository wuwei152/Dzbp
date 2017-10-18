package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class SignEvent implements Serializable{
    private boolean status;
    private String id;

    public SignEvent() {
    }

    public SignEvent(boolean status, String id) {
        this.status = status;
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
