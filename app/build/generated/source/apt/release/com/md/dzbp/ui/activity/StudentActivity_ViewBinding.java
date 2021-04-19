// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.md.dzbp.R;
import com.md.dzbp.ui.view.AudioRecorder.AudioRecorderButton;
import com.md.dzbp.ui.view.MyRecyclerView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class StudentActivity_ViewBinding implements Unbinder {
  private StudentActivity target;

  private View view7f0801d9;

  private View view7f0801dd;

  private View view7f0801d5;

  @UiThread
  public StudentActivity_ViewBinding(StudentActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public StudentActivity_ViewBinding(final StudentActivity target, View source) {
    this.target = target;

    View view;
    target.mAddr = Utils.findRequiredViewAsType(source, R.id.title_classAddr, "field 'mAddr'", TextView.class);
    target.mCardNum = Utils.findRequiredViewAsType(source, R.id.title_cardNum, "field 'mCardNum'", EditText.class);
    target.mSclIcon = Utils.findRequiredViewAsType(source, R.id.title_sclIcon, "field 'mSclIcon'", ImageView.class);
    target.mSchoolName = Utils.findRequiredViewAsType(source, R.id.title_schoolName, "field 'mSchoolName'", TextView.class);
    target.mClassName = Utils.findRequiredViewAsType(source, R.id.title_className, "field 'mClassName'", TextView.class);
    target.mAlias = Utils.findRequiredViewAsType(source, R.id.title_classAlias, "field 'mAlias'", TextView.class);
    target.mTime = Utils.findRequiredViewAsType(source, R.id.title_time, "field 'mTime'", TextView.class);
    target.mWeek = Utils.findRequiredViewAsType(source, R.id.title_week, "field 'mWeek'", TextView.class);
    target.mDate = Utils.findRequiredViewAsType(source, R.id.title_date, "field 'mDate'", TextView.class);
    target.mChatlist = Utils.findRequiredViewAsType(source, R.id.student_chatlist, "field 'mChatlist'", ListView.class);
    target.mAudioRecorder = Utils.findRequiredViewAsType(source, R.id.student_audioRecorder, "field 'mAudioRecorder'", AudioRecorderButton.class);
    target.mIcon = Utils.findRequiredViewAsType(source, R.id.student_icon, "field 'mIcon'", SimpleDraweeView.class);
    target.mClass = Utils.findRequiredViewAsType(source, R.id.student_class, "field 'mClass'", TextView.class);
    target.mName = Utils.findRequiredViewAsType(source, R.id.student_name, "field 'mName'", TextView.class);
    target.mRecycle = Utils.findRequiredViewAsType(source, R.id.student_recycle, "field 'mRecycle'", RecyclerView.class);
    target.mHonorRecyclerView = Utils.findRequiredViewAsType(source, R.id.stu_honorRecyclerView, "field 'mHonorRecyclerView'", MyRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.student_inputType, "field 'mInputType' and method 'onViewClicked'");
    target.mInputType = Utils.castView(view, R.id.student_inputType, "field 'mInputType'", ImageView.class);
    view7f0801d9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mTextInput = Utils.findRequiredViewAsType(source, R.id.student_textInput, "field 'mTextInput'", EditText.class);
    view = Utils.findRequiredView(source, R.id.student_textSend, "field 'mTextSend' and method 'onViewClicked'");
    target.mTextSend = Utils.castView(view, R.id.student_textSend, "field 'mTextSend'", TextView.class);
    view7f0801dd = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mYue = Utils.findRequiredViewAsType(source, R.id.student_yue, "field 'mYue'", TextView.class);
    target.mBookListView = Utils.findRequiredViewAsType(source, R.id.stu_BookListView, "field 'mBookListView'", ListView.class);
    view = Utils.findRequiredView(source, R.id.student_back, "method 'onViewClicked'");
    view7f0801d5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    StudentActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mAddr = null;
    target.mCardNum = null;
    target.mSclIcon = null;
    target.mSchoolName = null;
    target.mClassName = null;
    target.mAlias = null;
    target.mTime = null;
    target.mWeek = null;
    target.mDate = null;
    target.mChatlist = null;
    target.mAudioRecorder = null;
    target.mIcon = null;
    target.mClass = null;
    target.mName = null;
    target.mRecycle = null;
    target.mHonorRecyclerView = null;
    target.mInputType = null;
    target.mTextInput = null;
    target.mTextSend = null;
    target.mYue = null;
    target.mBookListView = null;

    view7f0801d9.setOnClickListener(null);
    view7f0801d9 = null;
    view7f0801dd.setOnClickListener(null);
    view7f0801dd = null;
    view7f0801d5.setOnClickListener(null);
    view7f0801d5 = null;
  }
}
