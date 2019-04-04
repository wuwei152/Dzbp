package com.md.dzbp.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.md.dzbp.R;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.TextReceiveMessage;
import com.md.dzbp.data.TextSendMessage;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;

/**
 * 发送文字消息
 * Created by Administrator on 2017/8/24.
 */
public class TxtMsgSendItem implements ItemViewDelegate<MessageBase> {
    private Context context;
    public TxtMsgSendItem(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.chatrow_sent_text;
    }

    @Override
    public boolean isForViewType(MessageBase item, int position) {
        return item instanceof TextSendMessage;
    }

    @Override
    public void convert(ViewHolder holder, MessageBase message, int position) {
        TextSendMessage chatMessage = (TextSendMessage) message;
        holder.setText(R.id.tv_chatcontent, chatMessage.getContent());
        GlideImgManager.glideLoader(context, chatMessage.getHeadIcon(), R.drawable.head_icon, R.drawable.head_icon, ((ImageView) holder.getView(R.id.iv_userhead)), 0);
        holder.setText(R.id.timestamp, TimeUtils.toDateString(chatMessage.getCreateTime()));
        holder.setText(R.id.tv_userid, chatMessage.getUserName());
        if (chatMessage.getCreateTime()==0){
            holder.setVisible(R.id.timestamp,false);
        }else {
            holder.setVisible(R.id.timestamp,true);
        }
    }
}
