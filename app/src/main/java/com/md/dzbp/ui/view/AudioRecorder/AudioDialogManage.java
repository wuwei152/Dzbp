package com.md.dzbp.ui.view.AudioRecorder;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.md.dzbp.R;

/**
 * 录制语音弹窗管理类
 */

public class AudioDialogManage {
    private Dialog mDialog;
    public ImageView mIcon;     //麦克风及删除图标
    private TextView mTime;     //录音时长
    private TextView mLabel;    //录音提示文字
    private Context mContext;


    public AudioDialogManage(Context context) {
        this.mContext = context;
    }

    /**
     * 默认的对话框的显示
     */
    public void showRecorderingDialog() {
        mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(
                R.layout.voicenotes_recorder_dialog, null);
        mDialog.setContentView(view);

        mIcon = (ImageView) mDialog.findViewById(R.id.recorder_dialog_icon);
        mTime = (TextView) mDialog.findViewById(R.id.recorder_dialog_time_tv);
        mLabel = (TextView) mDialog.findViewById(R.id.recorder_dialog_label);

        mDialog.show();
    }

    //下面在显示各种对话框时，mDialog已经被构造，只需要控制ImageView、TextView的显示即可
    /**
     * 正在录音时，Dialog的显示
     */
    public void recording() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.VISIBLE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.record_microphone);
            mLabel.setBackgroundColor(Color.parseColor("#00000000"));
            mLabel.setText("松开发送");
        }
    }

    /**
     * 取消录音提示对话框
     */
    public void wantToCancel() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.delete_speech_anim_list);
            mLabel.setBackgroundColor(Color.parseColor("#AF2831"));
            mLabel.setText("松开取消发送");
        }
    }

    /**
     * 录音时间过短
     */
    public void tooShort() {
        if (mDialog != null && mDialog.isShowing()) {
            mIcon.setVisibility(View.VISIBLE);
            mTime.setVisibility(View.GONE);
            mLabel.setVisibility(View.VISIBLE);

            mIcon.setImageResource(R.drawable.speech_is_too_short);
            mLabel.setBackgroundColor(Color.parseColor("#00000000"));
            mLabel.setText("说话时间太短");
        }
    }

    /**
     * mDialog.dismiss();
     */
    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog = null;
        }
    }

    /**
     * 更新显示当前录音秒数
     * @param time
     */
    public void updateCurTime(String time) {
        if (mDialog != null && mDialog.isShowing()) {

            mTime.setText(time);
        }
    }
}