// Generated code from Butter Knife. Do not modify!
package com.md.dzbp.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.md.dzbp.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ExamActivity_ViewBinding implements Unbinder {
  private ExamActivity target;

  @UiThread
  public ExamActivity_ViewBinding(ExamActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public ExamActivity_ViewBinding(ExamActivity target, View source) {
    this.target = target;

    target.mAddress = Utils.findRequiredViewAsType(source, R.id.title_classAddr, "field 'mAddress'", TextView.class);
    target.mCardNum = Utils.findRequiredViewAsType(source, R.id.title_cardNum, "field 'mCardNum'", EditText.class);
    target.mSclIcon = Utils.findRequiredViewAsType(source, R.id.title_sclIcon, "field 'mSclIcon'", ImageView.class);
    target.mSchoolName = Utils.findRequiredViewAsType(source, R.id.title_schoolName, "field 'mSchoolName'", TextView.class);
    target.mClassName = Utils.findRequiredViewAsType(source, R.id.title_className, "field 'mClassName'", TextView.class);
    target.mAlias = Utils.findRequiredViewAsType(source, R.id.title_classAlias, "field 'mAlias'", TextView.class);
    target.mTime = Utils.findRequiredViewAsType(source, R.id.title_time, "field 'mTime'", TextView.class);
    target.mWeek = Utils.findRequiredViewAsType(source, R.id.title_week, "field 'mWeek'", TextView.class);
    target.mDate = Utils.findRequiredViewAsType(source, R.id.title_date, "field 'mDate'", TextView.class);
    target.mListview = Utils.findRequiredViewAsType(source, R.id.exam_list, "field 'mListview'", ListView.class);
    target.mName = Utils.findRequiredViewAsType(source, R.id.exam_name, "field 'mName'", TextView.class);
    target.mSubject = Utils.findRequiredViewAsType(source, R.id.exam_subject, "field 'mSubject'", TextView.class);
    target.mStates = Utils.findRequiredViewAsType(source, R.id.exam_states, "field 'mStates'", TextView.class);
    target.mExamTime = Utils.findRequiredViewAsType(source, R.id.exam_examTime, "field 'mExamTime'", TextView.class);
    target.mProctor = Utils.findRequiredViewAsType(source, R.id.exam_proctor, "field 'mProctor'", TextView.class);
    target.mDiscipline = Utils.findRequiredViewAsType(source, R.id.exam_discipline, "field 'mDiscipline'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    ExamActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mAddress = null;
    target.mCardNum = null;
    target.mSclIcon = null;
    target.mSchoolName = null;
    target.mClassName = null;
    target.mAlias = null;
    target.mTime = null;
    target.mWeek = null;
    target.mDate = null;
    target.mListview = null;
    target.mName = null;
    target.mSubject = null;
    target.mStates = null;
    target.mExamTime = null;
    target.mProctor = null;
    target.mDiscipline = null;
  }
}
