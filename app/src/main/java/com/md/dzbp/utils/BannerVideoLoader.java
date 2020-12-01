package com.md.dzbp.utils;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.md.dzbp.data.MainData;
import com.zk.banner.listener.OnVideoStateListener;
import com.zk.banner.loader.VideoLoader;

import java.util.List;

import cn.jzvd.Jzvd;

/**
 * Created by Administrator on 2019/2/12.
 */

public class BannerVideoLoader extends VideoLoader {

    List<MainData.PhotosBean> beans;

    public BannerVideoLoader() {
    }

    public BannerVideoLoader(List<MainData.PhotosBean> beans) {
        this.beans = beans;
    }


    @Override
    public void displayView(Context context, Object path, View view, OnVideoStateListener listener) {
        /**
         注意：
         1.播放加载器由自己选择，这里不限制，这里使用JzvdStd播放
         2.返回的视频路径为Object类型,传输的到的是什么格式，那么这种就使用Object接收和返回，
         你只需要强转成你传输的类型就行，
         切记不要胡乱强转！
         */

//        SCREEN_ORIENTATION_PORTRAIT    SCREEN_ORIENTATION_LANDSCAPE
        MyJzvdStd jzVideo = (MyJzvdStd) view;
        Jzvd.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;// 非全屏是竖屏
        Jzvd.NORMAL_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;// 进入全屏后是竖屏
        jzVideo.setUp((String) path, "");


        String thumb = "";
        if (beans != null) {
            for (MainData.PhotosBean bean : beans) {
                if (bean.getUrl().equals((String) path)) {
                    thumb = bean.getThumburl();
                }
            }
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(context).load(thumb).apply(options).into(jzVideo.posterImageView);
        jzVideo.setOnVideoStateListener(listener);

    }

    //提供createImageView 方法，如果不用可以不重写这个方法，主要是方便自定义播放view的创建
    @Override
    public View createView(Context context) {
        MyJzvdStd view = new MyJzvdStd(context);
        return view;
    }
}
