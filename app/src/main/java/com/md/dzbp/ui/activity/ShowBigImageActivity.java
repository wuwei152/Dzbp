package com.md.dzbp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;

import butterknife.BindView;

public class ShowBigImageActivity extends BaseActivity {

    @BindView(R.id.showimg_img)
    ImageView mImg;

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
        Intent intent = getIntent();
        if (intent.hasExtra("imgUrl")) {
            String imgUrl = intent.getStringExtra("imgUrl");
            Glide.with(ShowBigImageActivity.this)
                    .load(imgUrl)
                    .into(mImg);
        }
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
