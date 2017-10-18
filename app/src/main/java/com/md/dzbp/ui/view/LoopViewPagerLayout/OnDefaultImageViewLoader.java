package com.md.dzbp.ui.view.LoopViewPagerLayout;

import android.content.Context;
import android.widget.ImageView;

/**
 * Default ImageViewLoader
 *
 * @author Edwin.Wu
 * @version 2016/12/6 14:42
 * @since JDK1.8
 */
public abstract class OnDefaultImageViewLoader implements OnLoadImageViewListener {

    @Override
    public ImageView createImageView(Context context) {
        return new ImageView(context);
    }
}
