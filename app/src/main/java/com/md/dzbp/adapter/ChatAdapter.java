package com.md.dzbp.adapter;

import android.content.Context;

import com.zhy.adapter.abslistview.MultiItemTypeAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/8/24.
 */
public class ChatAdapter<T> extends MultiItemTypeAdapter {
    private List<T> datas;
    public ChatAdapter(Context context, List datas) {
        super(context, datas);
        this.datas = datas;
        addItemViewDelegate(new PicMsgReceiveItem(context));
        addItemViewDelegate(new TxtMsgReceiveItem(context));
        addItemViewDelegate(new VoiceMsgReceiveItem(context));
        addItemViewDelegate(new VoiceMsgSendItem(context));
    }

    public void addData(T data){
        if (datas!=null){
            datas.add(data);
            notifyDataSetChanged();
        }
    }
    public void clearData(){
        if (datas!=null){
            datas.clear();
            notifyDataSetChanged();
        }
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }
}
