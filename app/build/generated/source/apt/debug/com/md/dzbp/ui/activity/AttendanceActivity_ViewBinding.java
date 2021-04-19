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
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.google.android.material.tabs.TabLayout;
import com.md.dzbp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AttendanceActivity_ViewBinding implements Unbinder {
  private AttendanceActivity target;

  private View view7f08005b;

  private View view7f08004d;

  private View view7f08004e;

  private View view7f08004f;

  private View view7f080050;

  private View view7f080051;

  @UiThread
  public AttendanceActivity_ViewBinding(AttendanceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public AttendanceActivity_ViewBinding(final AttendanceActivity target, View source) {
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
    target.tab = Utils.findRequiredViewAsType(source, R.id.tab, "field 'tab'", TabLayout.class);
    target.text1 = Utils.findRequiredViewAsType(source, R.id.text1, "field 'text1'", TextView.class);
    target.add1 = Utils.findRequiredViewAsType(source, R.id.add1, "field 'add1'", TextView.class);
    target.listview1 = Utils.findRequiredViewAsType(source, R.id.listview1, "field 'listview1'", GridView.class);
    target.text2 = Utils.findRequiredViewAsType(source, R.id.text2, "field 'text2'", TextView.class);
    target.add2 = Utils.findRequiredViewAsType(source, R.id.add2, "field 'add2'", TextView.class);
    target.listview2 = Utils.findRequiredViewAsType(source, R.id.listview2, "field 'listview2'", GridView.class);
    target.text3 = Utils.findRequiredViewAsType(source, R.id.text3, "field 'text3'", TextView.class);
    target.add3 = Utils.findRequiredViewAsType(source, R.id.add3, "field 'add3'", TextView.class);
    target.listview3 = Utils.findRequiredViewAsType(source, R.id.listview3, "field 'listview3'", GridView.class);
    target.text4 = Utils.findRequiredViewAsType(source, R.id.text4, "field 'text4'", TextView.class);
    target.add4 = Utils.findRequiredViewAsType(source, R.id.add4, "field 'add4'", TextView.class);
    target.listview4 = Utils.findRequiredViewAsType(source, R.id.listview4, "field 'listview4'", GridView.class);
    target.text5 = Utils.findRequiredViewAsType(source, R.id.text5, "field 'text5'", TextView.class);
    target.add5 = Utils.findRequiredViewAsType(source, R.id.add5, "field 'add5'", TextView.class);
    target.listview5 = Utils.findRequiredViewAsType(source, R.id.listview5, "field 'listview5'", GridView.class);
    view = Utils.findRequiredView(source, R.id.back, "method 'onViewClicked'");
    view7f08005b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.add11, "method 'onViewClicked'");
    view7f08004d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.add12, "method 'onViewClicked'");
    view7f08004e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.add13, "method 'onViewClicked'");
    view7f08004f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.add14, "method 'onViewClicked'");
    view7f080050 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.add15, "method 'onViewClicked'");
    view7f080051 = view;
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
    AttendanceActivity target = this.target;
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
    target.tab = null;
    target.text1 = null;
    target.add1 = null;
    target.listview1 = null;
    target.text2 = null;
    target.add2 = null;
    target.listview2 = null;
    target.text3 = null;
    target.add3 = null;
    target.listview3 = null;
    target.text4 = null;
    target.add4 = null;
    target.listview4 = null;
    target.text5 = null;
    target.add5 = null;
    target.listview5 = null;

    view7f08005b.setOnClickListener(null);
    view7f08005b = null;
    view7f08004d.setOnClickListener(null);
    view7f08004d = null;
    view7f08004e.setOnClickListener(null);
    view7f08004e = null;
    view7f08004f.setOnClickListener(null);
    view7f08004f = null;
    view7f080050.setOnClickListener(null);
    view7f080050 = null;
    view7f080051.setOnClickListener(null);
    view7f080051 = null;
  }
}
