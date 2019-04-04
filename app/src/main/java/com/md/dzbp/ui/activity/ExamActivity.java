package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CourseBean;
import com.md.dzbp.data.ExamBean;
import com.md.dzbp.data.ExamPlan;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.myToast;
import com.md.dzbp.utils.ACache;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.GlideImgManager;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class ExamActivity extends BaseActivity implements TimeListener, UIDataListener {


    @BindView(R.id.title_classAddr)
    TextView mAddress;
    @BindView(R.id.title_cardNum)
    EditText mCardNum;
    @BindView(R.id.title_sclIcon)
    ImageView mSclIcon;
    @BindView(R.id.title_schoolName)
    TextView mSchoolName;
    @BindView(R.id.title_className)
    TextView mClassName;
    @BindView(R.id.title_classAlias)
    TextView mAlias;
    @BindView(R.id.title_time)
    TextView mTime;
    @BindView(R.id.title_week)
    TextView mWeek;
    @BindView(R.id.title_date)
    TextView mDate;

    @BindView(R.id.exam_list)
    ListView mListview;
    @BindView(R.id.exam_name)
    TextView mName;
    @BindView(R.id.exam_subject)
    TextView mSubject;
    @BindView(R.id.exam_states)
    TextView mStates;
    @BindView(R.id.exam_examTime)
    TextView mExamTime;
    @BindView(R.id.exam_proctor)
    TextView mProctor;
    @BindView(R.id.exam_discipline)
    TextView mDiscipline;
    private ACache mAcache;
    private String address;

    private String TAG = "ExamActivity-->{}";
    private Logger logger;
    private Dialog dialog;
    private NetWorkRequest netWorkRequest;
    private ExamBean examBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_exam;
    }

    @Override
    protected void initUI() {
        EventBus.getDefault().register(this);
        mAcache = ACache.get(this);
        logger = LoggerFactory.getLogger(ExamActivity.class);
        dialog = MyProgressDialog.createLoadingDialog(ExamActivity.this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

        try {
            String className = mAcache.getAsString("ClassName");
            String gradeName = mAcache.getAsString("GradeName");
            String address = mAcache.getAsString("Address");
            String schoolName = mAcache.getAsString("SchoolName");
            String logo = mAcache.getAsString("Logo");
            String alias = mAcache.getAsString("Alias");

            mDate.setText(TimeUtils.getStringDate());
            mWeek.setText(TimeUtils.getStringWeek());
            //获取时间日期
            new TimeUtils(ExamActivity.this, this);

            mClassName.setText(gradeName + "\n\n"+ className);
            mAddress.setText("教室编号:" + address);
            mSchoolName.setText(schoolName);
            GlideImgManager.glideLoader(ExamActivity.this, logo, R.drawable.pic_not_found, R.drawable.pic_not_found, mSclIcon, 1);
            if (!TextUtils.isEmpty(alias) && !alias.equals("null")) {
                mAlias.setText("(" + alias + ")");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(TAG, e);
        }
        getUIData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setUiData(examBean);
                getUIData();
            }
        }, 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "考试界面");
        Constant.SCREENTYPE = 4;


    }

    private void getUIData() {

        Map map = new HashMap();
        map.put("deviceId", Constant.getDeviceId(this));
        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_EXAM), false, map);
        mDate.setText(TimeUtils.getStringDate());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void initData() {
        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum, this);
        getCardNumUtils.getNum(new GetCardNumUtils.SetNum() {
            @Override
            public void setNum(String num) {
                if (!TextUtils.isEmpty(num)) {
                    Intent intent = new Intent(ExamActivity.this, TcpService.class);
                    intent.putExtra("Num", num);
                    intent.putExtra("Act", 4);
                    intent.putExtra("ext", "");
                    startService(intent);
                }
            }
        });

        try {
            examBean = (ExamBean) mAcache.getAsObject("Exam");
            setUiData(examBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据时间判断需显示的考试
     *
     * @param examBean
     */
    private void setUiData(ExamBean examBean) {

        examBean = new ExamBean();
        examBean.setExamination(new ExamBean.ExaminationBean("2019届---3月月考", "2019年3月26日", "2019年3月27日"));
        ArrayList<ExamPlan> examPlans = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ExamPlan plan = new ExamPlan("1231"+i,"2019-03-27","09:00:00","19:00:00","语文","李老师");
            examPlans.add(plan);
        }
        examBean.setPlan(examPlans);

        if (examBean != null) {
            mAddress.setText(examBean.getAddress());
            ExamBean.ExaminationBean examination = examBean.getExamination();
            if (examination != null) {
                mName.setText(examination.getName());
                if (!TextUtils.isEmpty(examination.getRemark())) {
                    CharSequence charSequence = Html.fromHtml(examination.getRemark());
                    mDiscipline.setText(charSequence);
                    mDiscipline.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }
            String current = TimeUtils.getCurrentTime2();
            ArrayList<ExamPlan> plan = (ArrayList<ExamPlan>) examBean.getPlan();
            if (plan != null) {
                mListview.setAdapter(new CommonAdapter<ExamPlan>(ExamActivity.this, R.layout.item_exam, plan) {
                    @Override
                    protected void convert(ViewHolder viewHolder, ExamPlan item, int position) {
                        viewHolder.setText(R.id.examitem_date, item.getEdate());
                        viewHolder.setText(R.id.examitem_time, item.getStarttime() + "--" + item.getEndtime());
                        viewHolder.setText(R.id.examitem_course, item.getSubjectname());
                        viewHolder.setText(R.id.examitem_teacher, "监考老师：" + item.getTeachername());
                    }
                });

                ExamPlan plans = null;//当前需显示的计划

//                LogUtils.d(current);
//                LogUtils.d(plan);
                for (ExamPlan examPlan : plan) {//查找当前正在进行的计划
                    if (compareDate(examPlan.getEdate() + " " + examPlan.getStarttime(), current) && compareDate(current, examPlan.getEdate() + " " + examPlan.getEndtime())) {
                        plans = examPlan;
                    }
                }
//                LogUtils.d(plans);
                if (plans != null) {//当前正在进行的计划
                    mStates.setText("考试正在进行");
                    mSubject.setText(plans.getSubjectname());
                    mExamTime.setText(plans.getEdate() + "     " + plans.getStarttime() + "--" + plans.getEndtime());
                    mProctor.setText("监考老师：" + plans.getTeachername());
                } else {//没有当前正在进行的计划，查找即将开始的计划
                    for (ExamPlan examPlan : plan) {
                        if (compareDate(current, examPlan.getEdate() + " " + examPlan.getStarttime())) {
                            plans = examPlan;
                            break;
                        }
                    }

                    if (plans != null) {
                        mStates.setText("考试即将开始");
                        mSubject.setText(plans.getSubjectname());
                        mExamTime.setText(plans.getEdate() + "     " + plans.getStarttime() + "--" + plans.getEndtime());
                        mProctor.setText("监考老师：" + plans.getTeachername());
                    }
                    if (plans == null && plan.size() > 0) {//没有即将开始的计划，计划已全部执行完，显示最后的计划
                        plans = plan.get(plan.size() - 1);
                        mStates.setText("考试已结束");
                        mSubject.setText(plans.getSubjectname());
                        mExamTime.setText(plans.getEdate() + "     " + plans.getStarttime() + "--" + plans.getEndtime());
                        mProctor.setText("监考老师：" + plans.getTeachername());
                    } else if (plans == null && plan.size() == 0) {
                        mStates.setText("当前暂无考试安排");
                    }
                }


            } else {
                mStates.setText("当前暂无考试安排");
            }
        } else {
            mStates.setText("当前暂无考试安排");
        }
    }


    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {

            if (data != null) {
                ExamBean examBean1 = JSON.parseObject(data.toString(), new TypeReference<ExamBean>() {
                });
                if (examBean1 != null) {
//                    LogUtils.d(examBean1);
                    List<ExamPlan> plan = examBean1.getPlan();
                    if (plan != null) {
                        for (ExamPlan examPlan : plan) {
                            String edate = examPlan.getEdate();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            String timeStr = df.format(TimeUtils.toDate(edate));
                            examPlan.setEdate(timeStr);
                        }
                    }
                    examBean1.setPlan(plan);
                    setUiData(examBean1);
                    examBean = examBean1;
                    mAcache.put("Exam", examBean1);
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        myToast.toast(ExamActivity.this, message);
    }

    @Override
    public void showDialog() {
        if (dialog != null && !mainDialog.isShowing()) {
            try {
                dialog.show();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void dismissDialog() {
        if (dialog != null && !isFinishing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(final int errorCode, String errorMessage) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logger.debug(TAG, "重新请求");
                getUIData();
            }
        }, 30000);
    }

    @Override
    public void cancelRequest() {

    }

    public boolean compareDate(String time1, String time2) {
        try {
            //如果想比较日期则写成"yyyy-MM-dd"就可以了
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //将字符串形式的时间转化为Date类型的时间
            Date a = sdf.parse(time1);
            Date b = sdf.parse(time2);
            //Date类的一个方法，如果a早于b返回true，否则返回false
            if (a.before(b))
                return true;
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
