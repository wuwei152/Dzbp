// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.md.dzbp.R;
import com.zk.banner.Banner;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LooperImgPlayActivity_ViewBinding implements Unbinder {
  private LooperImgPlayActivity target;

  private View view7f08005b;

  @UiThread
  public LooperImgPlayActivity_ViewBinding(LooperImgPlayActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LooperImgPlayActivity_ViewBinding(final LooperImgPlayActivity target, View source) {
    this.target = target;

    View view;
    target.banner = Utils.findRequiredViewAsType(source, R.id.Loop, "field 'banner'", Banner.class);
    view = Utils.findRequiredView(source, R.id.back, "field 'back' and method 'onViewClicked'");
    target.back = Utils.castView(view, R.id.back, "field 'back'", ImageView.class);
    view7f08005b = view;
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
    LooperImgPlayActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.banner = null;
    target.back = null;

    view7f08005b.setOnClickListener(null);
    view7f08005b = null;
  }
}
