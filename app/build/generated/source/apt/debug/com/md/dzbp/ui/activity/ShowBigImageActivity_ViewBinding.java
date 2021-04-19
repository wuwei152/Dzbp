// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.md.dzbp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ShowBigImageActivity_ViewBinding implements Unbinder {
  private ShowBigImageActivity target;

  @UiThread
  public ShowBigImageActivity_ViewBinding(ShowBigImageActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ShowBigImageActivity_ViewBinding(ShowBigImageActivity target, View source) {
    this.target = target;

    target.mImg = Utils.findRequiredViewAsType(source, R.id.showimg_img, "field 'mImg'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ShowBigImageActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mImg = null;
  }
}
