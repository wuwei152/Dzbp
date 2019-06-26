package com.md.dzbp.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

public class GlideImgManager {

    /**
     * load normal  for img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv) {
        //原生 API
        Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).into(iv);
    }

    /**
     * load normal  for  circle or round img
     * tag为0原型图片，为1 圆角图片
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag
     */
    public static void glideLoader(Context context, String url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
//            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).apply(RequestOptions.bitmapTransform(new CircleCrop())).into(iv);
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
//            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(iv);
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new CenterCrop(), new GlideRoundTransform(context, 8)).into(iv);
        }
    }

    public static void glideLoader(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).transform(new CenterCrop(), new GlideRoundTransform(context, 10)).into(iv);
    }

    /**
     * load normal  for  circle or round img
     *
     * @param url
     * @param erroImg
     * @param emptyImg
     * @param iv
     * @param tag
     */
    public static void glideLoader(Context context, File url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new CenterCrop(), new GlideRoundTransform(context, 8)).into(iv);
        }
    }

    public static void glideLoader(Context context, int url, int erroImg, int emptyImg, ImageView iv, int tag) {
        if (0 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new GlideCircleTransform(context)).into(iv);
        } else if (1 == tag) {
            Glide.with(context).load(url).placeholder(emptyImg).error(erroImg).transform(new CenterCrop(), new GlideRoundTransform(context, 8)).into(iv);
//            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(new RoundedCorners(10))).into(iv);
        }
    }
}