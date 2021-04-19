// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

public class PatrolActivity_ViewBinding implements Unbinder {
  private PatrolActivity target;

  private View view7f080171;

  private View view7f080170;

  @UiThread
  public PatrolActivity_ViewBinding(PatrolActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PatrolActivity_ViewBinding(final PatrolActivity target, View source) {
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
    target.mTeacherIcon = Utils.findRequiredViewAsType(source, R.id.patrol_teacherIcon, "field 'mTeacherIcon'", ImageView.class);
    target.mTeacherName = Utils.findRequiredViewAsType(source, R.id.patrol_teacherName, "field 'mTeacherName'", TextView.class);
    target.mTeacherCourse = Utils.findRequiredViewAsType(source, R.id.patrol_teacherCourse, "field 'mTeacherCourse'", TextView.class);
    target.mTeacherPeroid = Utils.findRequiredViewAsType(source, R.id.patrol_teacherPeroid, "field 'mTeacherPeroid'", TextView.class);
    target.mMngIcon = Utils.findRequiredViewAsType(source, R.id.patrol_mngIcon, "field 'mMngIcon'", ImageView.class);
    target.mMngName = Utils.findRequiredViewAsType(source, R.id.patrol_mngName, "field 'mMngName'", TextView.class);
    target.mListview = Utils.findRequiredViewAsType(source, R.id.patrol_listview, "field 'mListview'", ListView.class);
    target.mYingdao = Utils.findRequiredViewAsType(source, R.id.patrol_yingdao, "field 'mYingdao'", TextView.class);
    target.mShidao = Utils.findRequiredViewAsType(source, R.id.patrol_shidao, "field 'mShidao'", TextView.class);
    target.mWeidao = Utils.findRequiredViewAsType(source, R.id.patrol_weidao, "field 'mWeidao'", TextView.class);
    view = Utils.findRequiredView(source, R.id.patrol_confrim, "field 'mConfrim' and method 'onViewClicked'");
    target.mConfrim = Utils.castView(view, R.id.patrol_confrim, "field 'mConfrim'", TextView.class);
    view7f080171 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mSurface = Utils.findRequiredViewAsType(source, R.id.patrol_mSurface, "field 'mSurface'", SurfaceView.class);
    target.mVideoList = Utils.findRequiredViewAsType(source, R.id.patrol_videoList, "field 'mVideoList'", HorizontalListView.class);
    view = Utils.findRequiredView(source, R.id.patrol_back, "method 'onViewClicked'");
    view7f080170 = view;
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
    PatrolActivity target = this.target;
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
    target.mTeacherIcon = null;
    target.mTeacherName = null;
    target.mTeacherCourse = null;
    target.mTeacherPeroid = null;
    target.mMngIcon = null;
    target.mMngName = null;
    target.mListview = null;
    target.mYingdao = null;
    target.mShidao = null;
    target.mWeidao = null;
    target.mConfrim = null;
    target.mSurface = null;
    target.mVideoList = null;

    view7f080171.setOnClickListener(null);
    view7f080171 = null;
    view7f080170.setOnClickListener(null);
    view7f080170 = null;
  }
}
