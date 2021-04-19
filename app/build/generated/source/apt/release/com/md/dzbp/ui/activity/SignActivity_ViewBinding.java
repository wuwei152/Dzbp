// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.md.dzbp.R;
import com.md.dzbp.ui.view.ProgressCustomView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SignActivity_ViewBinding implements Unbinder {
  private SignActivity target;

  @UiThread
  public SignActivity_ViewBinding(SignActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SignActivity_ViewBinding(SignActivity target, View source) {
    this.target = target;

    target.mMainAddr = Utils.findRequiredViewAsType(source, R.id.title_classAddr, "field 'mMainAddr'", TextView.class);
    target.mCardNum = Utils.findRequiredViewAsType(source, R.id.title_cardNum, "field 'mCardNum'", EditText.class);
    target.mSclIcon = Utils.findRequiredViewAsType(source, R.id.title_sclIcon, "field 'mSclIcon'", ImageView.class);
    target.mSchoolName = Utils.findRequiredViewAsType(source, R.id.title_schoolName, "field 'mSchoolName'", TextView.class);
    target.mClassName = Utils.findRequiredViewAsType(source, R.id.title_className, "field 'mClassName'", TextView.class);
    target.mAlias = Utils.findRequiredViewAsType(source, R.id.title_classAlias, "field 'mAlias'", TextView.class);
    target.mTime = Utils.findRequiredViewAsType(source, R.id.title_time, "field 'mTime'", TextView.class);
    target.mWeek = Utils.findRequiredViewAsType(source, R.id.title_week, "field 'mWeek'", TextView.class);
    target.mDate = Utils.findRequiredViewAsType(source, R.id.title_date, "field 'mDate'", TextView.class);
    target.mSurface = Utils.findRequiredViewAsType(source, R.id.sign_surface, "field 'mSurface'", SurfaceView.class);
    target.mGridView = Utils.findRequiredViewAsType(source, R.id.signing_GridView, "field 'mGridView'", GridView.class);
    target.mConStatus = Utils.findRequiredViewAsType(source, R.id.sign_conStatus, "field 'mConStatus'", ImageView.class);
    target.mClock = Utils.findRequiredViewAsType(source, R.id.sign_clock, "field 'mClock'", TextView.class);
    target.mProgress = Utils.findRequiredViewAsType(source, R.id.sign_progress, "field 'mProgress'", ProgressCustomView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SignActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mMainAddr = null;
    target.mCardNum = null;
    target.mSclIcon = null;
    target.mSchoolName = null;
    target.mClassName = null;
    target.mAlias = null;
    target.mTime = null;
    target.mWeek = null;
    target.mDate = null;
    target.mSurface = null;
    target.mGridView = null;
    target.mConStatus = null;
    target.mClock = null;
    target.mProgress = null;
  }
}
