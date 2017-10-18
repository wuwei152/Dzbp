package com.md.dzbp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.md.dzbp.R;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.TextReceiveMessage;
import com.md.dzbp.model.TimeUtils;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;

/**
 * 接受文字消息
 * Created by Administrator on 2017/8/24.
 */
public class TxtMsgReceiveItem implements ItemViewDelegate<MessageBase> {
    private Context context;
    public TxtMsgReceiveItem(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.chatrow_received_message;
    }

    @Override
    public boolean isForViewType(MessageBase item, int position) {
        return item instanceof TextReceiveMessage;
    }

    @Override
    public void convert(ViewHolder holder, MessageBase message, int position) {
        TextReceiveMessage chatMessage = (TextReceiveMessage) message;
        holder.setText(R.id.tv_chatcontent, chatMessage.getContent());
        Glide.with(context).load(chatMessage.getHeadIcon()).into((ImageView) holder.getView(R.id.iv_userhead));
        holder.setText(R.id.timestamp, TimeUtils.toDateString(chatMessage.getCreateTime()));
        holder.setText(R.id.tv_userid, chatMessage.getUserName());
        if (chatMessage.getCreateTime()==0){
            holder.setVisible(R.id.timestamp,false);
        }else {
            holder.setVisible(R.id.timestamp,true);
        }
    }
}
