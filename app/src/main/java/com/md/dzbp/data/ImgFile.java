package com.md.dzbp.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/15.
 */
public class ImgFile implements Serializable {
    private int img;
    private String name;

    public ImgFile() {
    }

    public ImgFile(int img, String name) {
        this.img = img;
        this.name = name;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
