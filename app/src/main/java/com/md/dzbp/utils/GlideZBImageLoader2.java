package com.md.dzbp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.md.dzbp.R;
import com.zk.banner.listener.OnVideoStateListener;
import com.zk.banner.loader.ImageLoader;

/**
 * Created by Administrator on 2019/2/12.
 */
public class GlideZBImageLoader2 extends ImageLoader {
    @Override
    public void displayView(Context context, Object path, ImageView view, OnVideoStateListener listener) {

        Glide.with(context).load((String)path).error(R.drawable.pic_not_found).diskCacheStrategy(DiskCacheStrategy.ALL).into(view);

    }

    @Override
    public ImageView createView(Context context) {
        ImageView simpleDraweeView = new ImageView(context);
        return simpleDraweeView;
    }

}