package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/9/13.
 */
public class SignEvent implements Serializable {
    private int type;
    private boolean status;
    private String id;
    private String name;

    public SignEvent() {
    }

    public SignEvent(int type, boolean status, String id, String name) {
        this.type = type;
        this.status = status;
        this.id = id;
        this.name=name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
