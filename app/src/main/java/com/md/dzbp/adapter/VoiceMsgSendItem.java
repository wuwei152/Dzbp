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
import com.md.dzbp.constants.Constant;
import com.md.dzbp.constants.ERRORTYPE;
import com.md.dzbp.data.MessageBase;
import com.md.dzbp.data.VoiceReceiveMessage;
import com.md.dzbp.data.VoiceSendMessage;
import com.md.dzbp.ftp.FTP;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.utils.FileUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 发送语音消息
 * Created by Administrator on 2017/8/24.
 */
public class VoiceMsgSendItem implements ItemViewDelegate<MessageBase> {
    private final Logger logger;
    private Context context;

    public VoiceMsgSendItem(Context context) {
        this.context = context;
        logger = LoggerFactory.getLogger(context.getClass());
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.chatrow_sent_voice;
    }

    @Override
    public boolean isForViewType(MessageBase item, int position) {
        return item instanceof VoiceSendMessage;
    }

    @Override
    public void convert(final ViewHolder holder, final MessageBase message, int position) {
        final VoiceSendMessage chatMessage = (VoiceSendMessage) message;
        GlideImgManager.glideLoader(context, chatMessage.getHeadIcon(), R.drawable.head_icon, R.drawable.head_icon, ((ImageView) holder.getView(R.id.iv_userhead)), 0);
        holder.setText(R.id.timestamp, TimeUtils.toDateString(chatMessage.getCreateTime()));
//        holder.setText(R.id.tv_length, chatMessage.getLength()+"  \"");
        holder.setText(R.id.tv_userid, chatMessage.getUserName());

        if (chatMessage.getCreateTime()==0){
            holder.setVisible(R.id.timestamp,false);
        }else {
            holder.setVisible(R.id.timestamp,true);
        }
        if (chatMessage.getStatus()==0){
            holder.setVisible(R.id.msg_status,false);
            holder.setVisible(R.id.progress_bar,true);
        }else if (chatMessage.getStatus()==1){
            holder.setVisible(R.id.msg_status,false);
            holder.setVisible(R.id.progress_bar,false);
        }if (chatMessage.getStatus()==2){
            holder.setVisible(R.id.msg_status,true);
            holder.setVisible(R.id.progress_bar,false);
        }

        holder.setOnClickListener(R.id.bubble, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationDrawable voiceAnimation;
                holder.setImageResource(R.id.iv_voice,R.anim.voice_to_icon);
                voiceAnimation = (AnimationDrawable) ((ImageView)holder.getView(R.id.iv_voice)).getDrawable();
                voiceAnimation.start();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        holder.setImageResource(R.id.iv_voice,R.drawable.ease_chatto_voice_playing);
                    }
                },2000);
                if (!TextUtils.isEmpty(chatMessage.getVoiceLocalPath())){
                    play(chatMessage.getVoiceLocalPath());
                }else {
                    try {
                        downloadVoice(chatMessage);
                    } catch (Exception e) {
                        logger.error("VoiceMsgReceiveItem--{}",e.getMessage());
                    }
                }
            }
        });
        holder.setOnClickListener(R.id.msg_status, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(chatMessage);
            }
        });
    }

    private void downloadVoice(final VoiceSendMessage chatMessage) {
        final String fileName = chatMessage.getVoicePath();
        File voice = new File(FileUtils.getDiskCacheDir(context) + "SendVoiceCache/" + fileName);
        if (voice.exists()){
            play(voice.getAbsolutePath());
            chatMessage.setVoiceLocalPath(FileUtils.getDiskCacheDir(context)+"SendVoiceCache/" + fileName);
            play(chatMessage.getVoiceLocalPath());
            return;
        }
        LogUtils.d("下载语音："+ Constant.Ftp_Voice + fileName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 下载
                try {
                    //单文件下载
                    new FTP(context).downloadSingleFile(Constant.Ftp_Voice+"/" + fileName, FileUtils.getDiskCacheDir(context)+"SendVoiceCache/", fileName, new FTP.DownLoadProgressListener() {

                        @Override
                        public void onDownLoadProgress(String currentStep, long downProcess, File file) {
                            if (currentStep.equals(ERRORTYPE.FTP_DOWN_SUCCESS)) {
                                logger.debug("VoiceMsgReceiveItem--{}","-----xiazaiyuyin--successful");
                                chatMessage.setVoiceLocalPath(FileUtils.getDiskCacheDir(context)+"SendVoiceCache/" + fileName);
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

    private void play(String path){
        logger.debug("VoiceMsgSendItem--{}","播放"+path);
        SoundPool soundPool;
        soundPool= new SoundPool(21, AudioManager.STREAM_SYSTEM,10);
        soundPool.load(path,1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                logger.debug("VoiceMsgSendItem","开始播放！");
                soundPool.play(1,1, 1, 0, 0,  1f);
            }
        });
    }
}
