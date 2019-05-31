package com.md.dzbp.model;

import java.io.File;

/**
 * Time:2019/5/14.
 * Author: Administrator
 * Description:
 */
public interface OnCompressListener {
    void onSuccess(File file);
    void onError();
}
