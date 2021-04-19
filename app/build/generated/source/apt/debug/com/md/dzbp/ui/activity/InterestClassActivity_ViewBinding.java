// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

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
import com.md.dzbp.ui.view.HorizontalListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class InterestClassActivity_ViewBinding implements Unbinder {
  private InterestClassActivity target;

  @UiThread
  public InterestClassActivity_ViewBinding(InterestClassActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public InterestClassActivity_ViewBinding(InterestClassActivity target, View source) {
    this.target = target;

    target.mAddr = Utils.findRequiredViewAsType(source, R.id.title_classAddr, "field 'mAddr'", TextView.class);
    target.mCardNum = Utils.findRequiredViewAsType(source, R.id.title_cardNum, "field 'mCardNum'", EditText.class);
    target.mSclIcon = Utils.findRequiredViewAsType(source, R.id.title_sclIcon, "field 'mSclIcon'", ImageView.class);
    target.mSchoolName = Utils.findRequiredViewAsType(source, R.id.title_schoolName, "field 'mSchoolName'", TextView.class);
    target.mClassName = Utils.findRequiredViewAsType(source, R.id.title_className, "field 'mClassName'", TextView.class);
    target.mAlias = Utils.findRequiredViewAsType(source, R.id.title_classAlias, "field 'mAlias'", TextView.class);
    target.mTime = Utils.findRequiredViewAsType(source, R.id.title_time, "field 'mTime'", TextView.class);
    target.mWeek = Utils.findRequiredViewAsType(source, R.id.title_week, "field 'mWeek'", TextView.class);
    target.mDate = Utils.findRequiredViewAsType(source, R.id.title_date, "field 'mDate'", TextView.class);
    target.mGridView = Utils.findRequiredViewAsType(source, R.id.interesting_GridView, "field 'mGridView'", GridView.class);
    target.mMainTitle = Utils.findRequiredViewAsType(source, R.id.interest_mainTitle, "field 'mMainTitle'", TextView.class);
    target.mMainSub = Utils.findRequiredViewAsType(source, R.id.interest_mainSub, "field 'mMainSub'", TextView.class);
    target.mMainDate = Utils.findRequiredViewAsType(source, R.id.interest_mainDate, "field 'mMainDate'", TextView.class);
    target.mHost = Utils.findRequiredViewAsType(source, R.id.interest_host, "field 'mHost'", TextView.class);
    target.mNum = Utils.findRequiredViewAsType(source, R.id.interest_Num, "field 'mNum'", TextView.class);
    target.mYingdao = Utils.findRequiredViewAsType(source, R.id.interest_yingdao, "field 'mYingdao'", TextView.class);
    target.mShidao = Utils.findRequiredViewAsType(source, R.id.interest_shidao, "field 'mShidao'", TextView.class);
    target.mWeidao = Utils.findRequiredViewAsType(source, R.id.interest_weidao, "field 'mWeidao'", TextView.class);
    target.mQrcode = Utils.findRequiredViewAsType(source, R.id.interest_qrcode, "field 'mQrcode'", ImageView.class);
    target.mListview = Utils.findRequiredViewAsType(source, R.id.interest_listview, "field 'mListview'", HorizontalListView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    InterestClassActivity target = this.target;
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
    target.mGridView = null;
    target.mMainTitle = null;
    target.mMainSub = null;
    target.mMainDate = null;
    target.mHost = null;
    target.mNum = null;
    target.mYingdao = null;
    target.mShidao = null;
    target.mWeidao = null;
    target.mQrcode = null;
    target.mListview = null;
  }
}
