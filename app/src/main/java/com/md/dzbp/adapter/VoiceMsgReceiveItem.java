package com.md.dzbp.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.md.dzbp.R;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.VoiceReceiveMessage;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.utils.FileUtils;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 接受语音消息
 * Created by Administrator on 2017/8/24.
 */
public class VoiceMsgReceiveItem implements ItemViewDelegate<MessageBase> {
    private final Logger logger;
    private Context context;

    public VoiceMsgReceiveItem(Context context) {
        this.context = context;
        logger = LoggerFactory.getLogger(context.getClass());
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.chatrow_received_voice;
    }

    @Override
    public boolean isForViewType(MessageBase item, int position) {
        return item instanceof VoiceReceiveMessage;
    }

    @Override
    public void convert(final ViewHolder holder, MessageBase message, int position) {
        final VoiceReceiveMessage chatMessage = (VoiceReceiveMessage) message;
        Glide.with(context).load(chatMessage.getHeadIcon()).into((ImageView) holder.getView(R.id.iv_userhead));
        holder.setText(R.id.timestamp, TimeUtils.toDateString(chatMessage.getCreateTime()));
//        holder.setText(R.id.tv_length, chatMessage.getLength()+"  \"");
        holder.setText(R.id.tv_userid, chatMessage.getUserName());
        if (chatMessage.getCreateTime() == 0) {
            holder.setVisible(R.id.timestamp, false);
        } else {
            holder.setVisible(R.id.timestamp, true);
        }
        holder.setOnClickListener(R.id.bubble, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationDrawable voiceAnimation;
                holder.setImageResource(R.id.iv_voice,R.anim.voice_from_icon);
                voiceAnimation = (AnimationDrawable) ((ImageView)holder.getView(R.id.iv_voice)).getDrawable();
                voiceAnimation.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.setImageResource(R.id.iv_voice,R.drawable.ease_chatfrom_voice_playing);
                    }
                },2000);
                if (!TextUtils.isEmpty(chatMessage.getVoiceLocalPath())) {
                    play(chatMessage.getVoiceLocalPath());
                } else {
                    try {
                        downloadVoice(chatMessage);
                    } catch (Exception e) {
                        logger.error("VoiceMsgReceiveItem--{}",e.getMessage());
                    }
                }

            }
        });
    }

    private void downloadVoice(final VoiceReceiveMessage chatMessage) {
        final String fileName = chatMessage.getVoicePath();
        LogUtils.d("下载语音："+Constant.Ftp_Voice + fileName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 下载
                try {
                    //单文件下载
                    new FTP(context).downloadSingleFile(Constant.Ftp_Voice+"/" + fileName, FileUtils.getDiskCacheDir(context)+"receiveVoice/", fileName, new FTP.DownLoadProgressListener() {

                        @Override
                        public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                            if (currentStep.equals(ERRORTYPE.FTP_DOWN_SUCCESS)) {
                                logger.debug("VoiceMsgReceiveItem--{}","-----xiazaiyuyin--successful");
                                chatMessage.setVoiceLocalPath(FileUtils.getDiskCacheDir(context)+"receiveVoice/" + fileName);
                                play(chatMessage.getVoiceLocalPath());
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_LOADING)) {
                                logger.debug("VoiceMsgReceiveItem--{}","-----xiazaiyuyin---" + downProcess + "%");
                            } else if (currentStep.equals(ERRORTYPE.FTP_DOWN_FAIL)) {
                                logger.debug("VoiceMsgReceiveItem--{}","-----xiazaiyuyin--fail---");
                            }
                        }

                    });

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void play(String path) {
        logger.debug("VoiceMsgReceiveItem--{}","播放" + path);
        SoundPool soundPool;
        soundPool = new SoundPool(21, AudioManager.STREAM_SYSTEM, 10);
        soundPool.load(path, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                logger.debug("VoiceMsgReceiveItem--{}","开始播放！");
                soundPool.play(1, 1, 1, 0, 0, 1f);
            }
        });
    }
}
