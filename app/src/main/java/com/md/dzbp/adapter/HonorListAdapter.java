package com.md.dzbp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.md.dzbp.R;
import com.md.dzbp.data.StudentInfoBean;
import com.md.dzbp.utils.GlideImgManager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/14.
 */
public class HonorListAdapter extends RecyclerView.Adapter<HonorListAdapter.ViewHolder> {

    private Context context;

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private ArrayList<StudentInfoBean.HonorBean> mDatas = new ArrayList<>();

    public HonorListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public HonorListAdapter(Context context, ArrayList mDatas) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
    }

    public void setDatas(ArrayList<StudentInfoBean.HonorBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        TextView mName;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_honor_grid,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mName = (TextView) view
                .findViewById(R.id.item_name);
        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.item_img);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        StudentInfoBean.HonorBean honorBean = mDatas.get(i);
        viewHolder.mName.setText(honorBean.getName());
        try {
            if (honorBean.getImagelist() != null && honorBean.getImagelist().size() > 0)
                GlideImgManager.glideLoader(context, honorBean.getImagelist().get(0).getThumburl(), R.drawable.pic_not_found, R.drawable.pic_not_found, viewHolder.mImg, 1);
        } catch (Exception e) {
        }
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });
        }
    }
}
