package com.md.dzbp.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Time:2019/4/3.
 * Author: Administrator
 * Description:
 */
public class MyViewPagerAdapter extends PagerAdapter {

    private ArrayList<View> mList = null;
    private OnItemClickPosition onclick = null;

    public void setData(ArrayList<View> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClickPosition onclick) {
        this.onclick = onclick;
    }

    public MyViewPagerAdapter(ArrayList<View> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
//        return Integer.MAX_VALUE;//返回图片的个数

        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        if (view == object) {
            return true;
        } else {
            return false;
        }//固定写法
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = mList.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick.onclick(position);
            }
        });
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(view);
        return mList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        object = null;
    }

    public interface OnItemClickPosition {
        void onclick(int position);
    }
}
