package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/11/27.
 */

public class FileInfo implements Serializable {

    private String filepath;
    private int fileLocal;
    private String name;
    private String desc;

    public FileInfo() {
    }

    public FileInfo(int fileLocal, String name) {
        this.fileLocal = fileLocal;
        this.name = name;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public int getFileLocal() {
        return fileLocal;
    }

    public void setFileLocal(int fileLocal) {
        this.fileLocal = fileLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
