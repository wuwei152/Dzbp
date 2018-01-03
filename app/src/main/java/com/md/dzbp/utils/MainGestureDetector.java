package com.md.dzbp.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.md.dzbp.ui.view.MainDialog;

/**
 * Created by Administrator on 2017/12/21.
 */

public class MainGestureDetector {

    public static GestureDetector.SimpleOnGestureListener getGestureDetector(final MainDialog mainDialog) {
        return new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                float x = e2.getX() - e1.getX();
                float y = e2.getY() - e1.getY();

                if (x > 300 && Math.abs(x) > Math.abs(y)) {
                    mainDialog.dismiss();
                } else if (x < -300 && Math.abs(x) > Math.abs(y)) {
                    mainDialog.show();
                }
                return true;
            }
        };
    }
}
