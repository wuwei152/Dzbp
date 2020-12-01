package com.md.dzbp.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.data.MainData;
import com.md.dzbp.utils.BannerVideoLoader;
import com.md.dzbp.utils.GlideZBImageLoader;
import com.md.dzbp.utils.GlideZBImageLoader2;
import com.zk.banner.Banner;
import com.zk.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;

public class LooperImgPlayActivity extends BaseActivity {

    @BindView(R.id.Loop)
    Banner banner;
    @BindView(R.id.back)
    ImageView back;
    private ArrayList<MainData.PhotosBean> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_looper_img_play;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    protected void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    protected void initUI() {

        photos = (ArrayList<MainData.PhotosBean>) getIntent().getSerializableExtra("photos");

    }

    @Override
    protected void initData() {

        List<String> list = new ArrayList<>();
        for (MainData.PhotosBean photo : photos) {
            list.add(photo.getUrl());
        }

        //设置图片加载器
        banner.setImageLoader(new GlideZBImageLoader2());
        //设置视频加载
        banner.setVideoLoader(new BannerVideoLoader(photos));
        //设置图片和视频集合
        banner.setImages(list);
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(15000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
//        banner.setPageTransformer(true, new RotateUpTransformer());
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Jzvd.releaseAllVideos();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
