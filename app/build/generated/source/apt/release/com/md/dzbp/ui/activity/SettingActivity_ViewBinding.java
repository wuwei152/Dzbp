// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.md.dzbp.R;
import com.md.dzbp.ui.view.dropdownmenu.DropdownMenu;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SettingActivity_ViewBinding implements Unbinder {
  private SettingActivity target;

  private View view7f0801a9;

  private View view7f0801ab;

  @UiThread
  public SettingActivity_ViewBinding(SettingActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SettingActivity_ViewBinding(final SettingActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.set_back, "field 'mBack' and method 'onViewClicked'");
    target.mBack = Utils.castView(view, R.id.set_back, "field 'mBack'", LinearLayout.class);
    view7f0801a9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mSchool = Utils.findRequiredViewAsType(source, R.id.set_school, "field 'mSchool'", DropdownMenu.class);
    target.mArea = Utils.findRequiredViewAsType(source, R.id.set_area, "field 'mArea'", DropdownMenu.class);
    view = Utils.findRequiredView(source, R.id.set_confirm, "field 'mConfirm' and method 'onViewClicked'");
    target.mConfirm = Utils.castView(view, R.id.set_confirm, "field 'mConfirm'", TextView.class);
    view7f0801ab = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mIp = Utils.findRequiredViewAsType(source, R.id.set_ip, "field 'mIp'", EditText.class);
    target.mport = Utils.findRequiredViewAsType(source, R.id.set_port, "field 'mport'", EditText.class);
    target.mPsw = Utils.findRequiredViewAsType(source, R.id.set_psw, "field 'mPsw'", EditText.class);
    target.cameratype = Utils.findRequiredViewAsType(source, R.id.set_cameratype, "field 'cameratype'", RadioGroup.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SettingActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mBack = null;
    target.mSchool = null;
    target.mArea = null;
    target.mConfirm = null;
    target.mIp = null;
    target.mport = null;
    target.mPsw = null;
    target.cameratype = null;

    view7f0801a9.setOnClickListener(null);
    view7f0801a9 = null;
    view7f0801ab.setOnClickListener(null);
    view7f0801ab = null;
  }
}
