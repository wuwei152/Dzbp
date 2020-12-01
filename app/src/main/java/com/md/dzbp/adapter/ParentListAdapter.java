package com.md.dzbp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.md.dzbp.R;
import com.md.dzbp.data.StudentInfoBean;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/9/14.
 */
public class ParentListAdapter extends RecyclerView.Adapter<ParentListAdapter.ViewHolder> {

    private Context context;
    private Logger logger  ;

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
    private ArrayList<StudentInfoBean.ParentsBean> mDatas = new ArrayList<>();

    public ParentListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        logger = LoggerFactory.getLogger(context.getClass());
    }

    public ParentListAdapter(Context context, ArrayList mDatas) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = mDatas;
        logger = LoggerFactory.getLogger(context.getClass());
    }

    public void setDatas(ArrayList<StudentInfoBean.ParentsBean> mDatas) {
        this.mDatas = mDatas;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        SimpleDraweeView mImg;
        TextView mNum;
        TextView mName;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.item_main_grid,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mNum = (TextView) view
                .findViewById(R.id.item_num);
        viewHolder.mName = (TextView) view
                .findViewById(R.id.item_name);
        viewHolder.mImg = (SimpleDraweeView) view
                .findViewById(R.id.item_img);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        StudentInfoBean.ParentsBean parentsBean = mDatas.get(i);
        viewHolder.mName.setText(parentsBean.getAccountname());
        if (parentsBean.isSelect()){
            viewHolder.mName.setTextColor(context.getResources().getColor(R.color.green));
        }else {
            viewHolder.mName.setTextColor(context.getResources().getColor(R.color.text_black));
        }

        if (parentsBean.getMsgcount() > 99) {
            viewHolder.mNum.setVisibility(View.VISIBLE);
            viewHolder.mNum.setText("99+");
            setFlickerAnimation(500, viewHolder.mNum);
        } else if (parentsBean.getMsgcount() == 0){
            viewHolder.mNum.setVisibility(View.GONE);
        }else {
            viewHolder.mNum.setVisibility(View.VISIBLE);
            viewHolder.mNum.setText(parentsBean.getMsgcount() + "");
            setFlickerAnimation(500, viewHolder.mNum);
        }
        try {
            viewHolder.mImg.setImageURI(Uri.parse(parentsBean.getPhoto()));
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                    for (int k=0;k<mDatas.size();k++){
                        if (k==i){
                            mDatas.get(k).setSelect(true);
                        }else {
                            mDatas.get(k).setSelect(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void setFlickerAnimation(int offset, View iv_chat_head) {
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(50); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE); //
        animation.setStartOffset(offset);
        iv_chat_head.setAnimation(animation);
    }
}
