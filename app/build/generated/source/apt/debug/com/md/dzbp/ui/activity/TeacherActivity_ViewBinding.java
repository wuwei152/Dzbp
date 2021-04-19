// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.md.dzbp.R;
import com.zk.banner.Banner;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TeacherActivity_ViewBinding implements Unbinder {
  private TeacherActivity target;

  private View view7f0801f3;

  @UiThread
  public TeacherActivity_ViewBinding(TeacherActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public TeacherActivity_ViewBinding(final TeacherActivity target, View source) {
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
    target.mCourseName = Utils.findRequiredViewAsType(source, R.id.teacher_courseName, "field 'mCourseName'", TextView.class);
    target.mTeacherName = Utils.findRequiredViewAsType(source, R.id.teacher_teacherName, "field 'mTeacherName'", TextView.class);
    target.mQrcode = Utils.findRequiredViewAsType(source, R.id.teacher_qrcode, "field 'mQrcode'", ImageView.class);
    target.mTeacherIcon = Utils.findRequiredViewAsType(source, R.id.teacher_icon, "field 'mTeacherIcon'", SimpleDraweeView.class);
    target.banner = Utils.findRequiredViewAsType(source, R.id.teacher_Loop, "field 'banner'", Banner.class);
    target.mWeek2 = Utils.findRequiredViewAsType(source, R.id.teacher_week2, "field 'mWeek2'", TextView.class);
    target.mListview = Utils.findRequiredViewAsType(source, R.id.teacher_listview, "field 'mListview'", ListView.class);
    target.mNextCourse = Utils.findRequiredViewAsType(source, R.id.teacher_nextCourse, "field 'mNextCourse'", TextView.class);
    target.mConStatus = Utils.findRequiredViewAsType(source, R.id.main_conStatus, "field 'mConStatus'", ImageView.class);
    target.mStart = Utils.findRequiredViewAsType(source, R.id.teacher_start, "field 'mStart'", TextView.class);
    target.mEnd = Utils.findRequiredViewAsType(source, R.id.teacher_end, "field 'mEnd'", TextView.class);
    target.mSeekbar = Utils.findRequiredViewAsType(source, R.id.teacher_seekbar, "field 'mSeekbar'", SeekBar.class);
    view = Utils.findRequiredView(source, R.id.teacher_back, "method 'onViewClicked'");
    view7f0801f3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    TeacherActivity target = this.target;
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
    target.mCourseName = null;
    target.mTeacherName = null;
    target.mQrcode = null;
    target.mTeacherIcon = null;
    target.banner = null;
    target.mWeek2 = null;
    target.mListview = null;
    target.mNextCourse = null;
    target.mConStatus = null;
    target.mStart = null;
    target.mEnd = null;
    target.mSeekbar = null;

    view7f0801f3.setOnClickListener(null);
    view7f0801f3 = null;
  }
}
