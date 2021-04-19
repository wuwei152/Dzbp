// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.md.dzbp.R;
import com.md.dzbp.ui.view.HorizontalListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VideoShowActivity_ViewBinding implements Unbinder {
  private VideoShowActivity target;

  private View view7f08023f;

  @UiThread
  public VideoShowActivity_ViewBinding(VideoShowActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VideoShowActivity_ViewBinding(final VideoShowActivity target, View source) {
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
    target.mCourseName = Utils.findRequiredViewAsType(source, R.id.videoshow_courseName, "field 'mCourseName'", TextView.class);
    target.mTeacherName = Utils.findRequiredViewAsType(source, R.id.videoshow_teacherName, "field 'mTeacherName'", TextView.class);
    target.mPeriodName = Utils.findRequiredViewAsType(source, R.id.videoshow_periodName, "field 'mPeriodName'", TextView.class);
    target.mAddr2 = Utils.findRequiredViewAsType(source, R.id.videoshow_addr, "field 'mAddr2'", TextView.class);
    target.mSurface = Utils.findRequiredViewAsType(source, R.id.videoshow_mSurface, "field 'mSurface'", SurfaceView.class);
    target.mVideoList = Utils.findRequiredViewAsType(source, R.id.videoshow_videoList, "field 'mVideoList'", HorizontalListView.class);
    view = Utils.findRequiredView(source, R.id.videoshow_back, "method 'onViewClicked'");
    view7f08023f = view;
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
    VideoShowActivity target = this.target;
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
    target.mPeriodName = null;
    target.mAddr2 = null;
    target.mSurface = null;
    target.mVideoList = null;

    view7f08023f.setOnClickListener(null);
    view7f08023f = null;
  }
}
