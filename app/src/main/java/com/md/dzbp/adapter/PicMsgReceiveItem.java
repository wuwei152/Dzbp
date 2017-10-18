package com.md.dzbp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.md.dzbp.R;
import com.md.dzbp.data.ImageReceiveMessage;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.ui.activity.ShowBigImageActivity;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;

/**
 * 接受图片消息
 */
public class PicMsgReceiveItem implements ItemViewDelegate<MessageBase> {

    private Context context;

    public PicMsgReceiveItem(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.chatrow_received_picture;
    }

    @Override
    public boolean isForViewType(MessageBase item, int position) {
        return item instanceof ImageReceiveMessage;
    }

    @Override
    public void convert(ViewHolder holder, MessageBase message, int position) {
        final ImageReceiveMessage chatMessage = (ImageReceiveMessage) message;
        Glide.with(context).load(chatMessage.getPicUrl()).into((ImageView) holder.getView(R.id.image));
        Glide.with(context).load(chatMessage.getHeadIcon()).into((ImageView) holder.getView(R.id.iv_userhead));
        holder.setText(R.id.timestamp, TimeUtils.toDateString(chatMessage.getCreateTime()));
        holder.setText(R.id.tv_userid, chatMessage.getUserName());
        if (chatMessage.getCreateTime()==0){
            holder.setVisible(R.id.timestamp,false);
        }else {
            holder.setVisible(R.id.timestamp,true);
        }

        holder.setOnClickListener(R.id.image, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShowBigImageActivity.class);
                intent.putExtra("imgUrl",chatMessage.getPicUrl());
                context.startActivity(intent);
            }
        });
    }
}
