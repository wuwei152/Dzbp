// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.webkit.WebView;
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
import java.lang.IllegalStateException;
import java.lang.Override;

public class NoticeActivity_ViewBinding implements Unbinder {
  private NoticeActivity target;

  private View view7f08015b;

  @UiThread
  public NoticeActivity_ViewBinding(NoticeActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public NoticeActivity_ViewBinding(final NoticeActivity target, View source) {
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
    target.mContent = Utils.findRequiredViewAsType(source, R.id.notice_content, "field 'mContent'", WebView.class);
    target.mTitle = Utils.findRequiredViewAsType(source, R.id.notice_title, "field 'mTitle'", TextView.class);
    target.mCreate = Utils.findRequiredViewAsType(source, R.id.notice_create, "field 'mCreate'", TextView.class);
    target.mCreateTime = Utils.findRequiredViewAsType(source, R.id.notice_createtime, "field 'mCreateTime'", TextView.class);
    view = Utils.findRequiredView(source, R.id.notice_back, "field 'mClose' and method 'onViewClicked'");
    target.mClose = Utils.castView(view, R.id.notice_back, "field 'mClose'", TextView.class);
    view7f08015b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mList = Utils.findRequiredViewAsType(source, R.id.notice_list, "field 'mList'", ListView.class);
    target.mHead = Utils.findRequiredViewAsType(source, R.id.notice_head, "field 'mHead'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    NoticeActivity target = this.target;
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
    target.mContent = null;
    target.mTitle = null;
    target.mCreate = null;
    target.mCreateTime = null;
    target.mClose = null;
    target.mList = null;
    target.mHead = null;

    view7f08015b.setOnClickListener(null);
    view7f08015b = null;
  }
}
