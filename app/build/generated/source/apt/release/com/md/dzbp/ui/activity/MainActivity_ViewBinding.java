// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.tabs.TabLayout;
import com.md.dzbp.R;
import com.md.dzbp.ui.view.AutoScrollViewPager;
import com.md.dzbp.ui.view.MyRecyclerView;
import com.zk.banner.Banner;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view7f08011e;

  private View view7f08011c;

  private View view7f080124;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target.mLoction = Utils.findRequiredViewAsType(source, R.id.title_classAddr, "field 'mLoction'", TextView.class);
    target.mCardNum = Utils.findRequiredViewAsType(source, R.id.title_cardNum, "field 'mCardNum'", EditText.class);
    target.mSclIcon = Utils.findRequiredViewAsType(source, R.id.title_sclIcon, "field 'mSclIcon'", ImageView.class);
    target.mSchoolName = Utils.findRequiredViewAsType(source, R.id.title_schoolName, "field 'mSchoolName'", TextView.class);
    target.mClassName = Utils.findRequiredViewAsType(source, R.id.title_className, "field 'mClassName'", TextView.class);
    target.mAlias = Utils.findRequiredViewAsType(source, R.id.title_classAlias, "field 'mAlias'", TextView.class);
    target.mTime = Utils.findRequiredViewAsType(source, R.id.title_time, "field 'mTime'", TextView.class);
    target.mWeek = Utils.findRequiredViewAsType(source, R.id.title_week, "field 'mWeek'", TextView.class);
    target.mDate = Utils.findRequiredViewAsType(source, R.id.title_date, "field 'mDate'", TextView.class);
    target.mListview = Utils.findRequiredViewAsType(source, R.id.main_listview, "field 'mListview'", ListView.class);
    target.banner = Utils.findRequiredViewAsType(source, R.id.main_Loop, "field 'banner'", Banner.class);
    target.mStuListRecycler = Utils.findRequiredViewAsType(source, R.id.main_stuList, "field 'mStuListRecycler'", MyRecyclerView.class);
    target.mClassMngIcon = Utils.findRequiredViewAsType(source, R.id.main_classMng_icon, "field 'mClassMngIcon'", SimpleDraweeView.class);
    target.mClassMngName = Utils.findRequiredViewAsType(source, R.id.main_classMng_name, "field 'mClassMngName'", TextView.class);
    target.mClassMngCourse = Utils.findRequiredViewAsType(source, R.id.main_classMng_course, "field 'mClassMngCourse'", TextView.class);
    target.mHonorListView = Utils.findRequiredViewAsType(source, R.id.main_honorListView, "field 'mHonorListView'", ListView.class);
    target.mRecyclerEmpty = Utils.findRequiredViewAsType(source, R.id.main_recycler_Empty, "field 'mRecyclerEmpty'", TextView.class);
    target.mConStatus = Utils.findRequiredViewAsType(source, R.id.main_conStatus, "field 'mConStatus'", ImageView.class);
    target.mYingdao = Utils.findRequiredViewAsType(source, R.id.main_yingdao, "field 'mYingdao'", TextView.class);
    target.mShidao = Utils.findRequiredViewAsType(source, R.id.main_shidao, "field 'mShidao'", TextView.class);
    target.mWeidao = Utils.findRequiredViewAsType(source, R.id.main_weidao, "field 'mWeidao'", TextView.class);
    target.mMoto = Utils.findRequiredViewAsType(source, R.id.main_moto, "field 'mMoto'", TextView.class);
    target.mNoticeTitle = Utils.findRequiredViewAsType(source, R.id.main_noticeTitle, "field 'mNoticeTitle'", TextView.class);
    target.mNoticeTime = Utils.findRequiredViewAsType(source, R.id.main_noticeTime, "field 'mNoticeTime'", TextView.class);
    target.mWeek2 = Utils.findRequiredViewAsType(source, R.id.main_week2, "field 'mWeek2'", TextView.class);
    target.mNextCourse = Utils.findRequiredViewAsType(source, R.id.main_nextCourse, "field 'mNextCourse'", TextView.class);
    target.mViewPager = Utils.findRequiredViewAsType(source, R.id.main_viewPager, "field 'mViewPager'", AutoScrollViewPager.class);
    target.mTab = Utils.findRequiredViewAsType(source, R.id.main_tab, "field 'mTab'", TabLayout.class);
    target.ydList = Utils.findRequiredViewAsType(source, R.id.main_ydList, "field 'ydList'", ListView.class);
    target.tab1 = Utils.findRequiredViewAsType(source, R.id.main_tab1, "field 'tab1'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.main_more, "field 'more' and method 'onViewClicked'");
    target.more = Utils.castView(view, R.id.main_more, "field 'more'", TextView.class);
    view7f08011e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.main_left, "method 'onViewClicked'");
    view7f08011c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.main_right, "method 'onViewClicked'");
    view7f080124 = view;
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
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mLoction = null;
    target.mCardNum = null;
    target.mSclIcon = null;
    target.mSchoolName = null;
    target.mClassName = null;
    target.mAlias = null;
    target.mTime = null;
    target.mWeek = null;
    target.mDate = null;
    target.mListview = null;
    target.banner = null;
    target.mStuListRecycler = null;
    target.mClassMngIcon = null;
    target.mClassMngName = null;
    target.mClassMngCourse = null;
    target.mHonorListView = null;
    target.mRecyclerEmpty = null;
    target.mConStatus = null;
    target.mYingdao = null;
    target.mShidao = null;
    target.mWeidao = null;
    target.mMoto = null;
    target.mNoticeTitle = null;
    target.mNoticeTime = null;
    target.mWeek2 = null;
    target.mNextCourse = null;
    target.mViewPager = null;
    target.mTab = null;
    target.ydList = null;
    target.tab1 = null;
    target.more = null;

    view7f08011e.setOnClickListener(null);
    view7f08011e = null;
    view7f08011c.setOnClickListener(null);
    view7f08011c = null;
    view7f080124.setOnClickListener(null);
    view7f080124 = null;
  }
}
