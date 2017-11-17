package com.md.dzbp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;

public class ShowBigImageActivity extends BaseActivity {

    @BindView(R.id.showimg_img)
    ImageView mImg;
    private Logger logger;
    private String TAG = "ShowBigImageActivity-->{}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setAnim() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_show_big_image;
    }

    @Override
    protected void initUI() {
        logger = LoggerFactory.getLogger(getClass());
        Intent intent = getIntent();
        if (intent.hasExtra("imgUrl")) {
            String imgUrl = intent.getStringExtra("imgUrl");
            Glide.with(ShowBigImageActivity.this)
                    .load(imgUrl)
                    .into(mImg);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG,"查看图片界面");
        Constant.SCREENTYPE = 111;
    }

    @Override
    protected void initData() {
        mImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
