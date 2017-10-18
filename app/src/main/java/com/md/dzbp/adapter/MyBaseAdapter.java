package com.md.dzbp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ：Administrator on 2016/10/5 10:27
 * describe：Adapter的基类
 */
public abstract class MyBaseAdapter<T> extends android.widget.BaseAdapter {

    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> mList = new ArrayList<T>();

    public MyBaseAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 判断数据是否为空
     *
     * @return 为空返回true，不为空返回false
     */
    public boolean isEmpty() {
        return mList.isEmpty();
    }

    /**
     * 设置为新的数据，旧数据会被清空
     *
     * @param newList
     */
    public void setDatas(List<T> newList) {
//        this.mList.clear();
        this.mList = newList;
        notifyDataSetChanged();
    }

    /**
     * 在原有的数据上添加新数据
     *
     * @param mList
     */
    public void addDatas(List<T> mList) {
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    /**
     * 清空数据
     */
    public void clearDatas() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    abstract public View getView(int position, View convertView, ViewGroup parent);
}
