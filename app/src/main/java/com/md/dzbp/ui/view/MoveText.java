package com.md.dzbp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/30.
 */
public class MoveText extends TextView {

    private int lastx;
    private int lasty;

    public MoveText(Context context) {
        super(context);
    }

    public MoveText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastx = x;
                lasty = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = lastx-x;
                int offsetY = lasty-y;
                offsetLeftAndRight(offsetX);
                offsetTopAndBottom(offsetY);
                break;
        }
        return super.onTouchEvent(event);

    }
}
