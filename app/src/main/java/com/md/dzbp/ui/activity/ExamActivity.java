package com.md.dzbp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.Exam;
import com.md.dzbp.data.ScreenShotEvent;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.md.dzbp.utils.GetCardNumUtils;
import com.md.dzbp.utils.MainGestureDetector;
import com.md.dzbp.utils.SnapUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import butterknife.BindView;

public class ExamActivity extends BaseActivity implements TimeListener {

    @BindView(R.id.exam_cardNum)
    EditText mCardNum;
    @BindView(R.id.exam_time)
    TextView mTime;
    @BindView(R.id.exam_date)
    TextView mDate;
    @BindView(R.id.exam_temp)
    TextView mTemp;
    @BindView(R.id.exam_list)
    ListView mListview;

    private MainDialog mainDialog;
    private GestureDetector gestureDetector;
    private String TAG = "ExamActivity-->{}";
    private Logger logger;

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
        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(ExamActivity.this, MainGestureDetector.getGestureDetector(mainDialog));

        logger = LoggerFactory.getLogger(ExamActivity.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("onNewIntent");
//        if (intent.hasExtra("id")) {
//            noticeId = intent.getStringExtra("id");
//        }
//        getUIdata();
    }

    @Override
    protected void onResume() {
        super.onResume();
        logger.debug(TAG, "考试界面");
        Constant.SCREENTYPE = 4;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mainDialog != null && mainDialog.isShowing()) {
            mainDialog.dismiss();
        }
    }

    @Override
    protected void initData() {
        GetCardNumUtils getCardNumUtils = new GetCardNumUtils(mCardNum);
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

        new TimeUtils(ExamActivity.this, this);
        mDate.setText(TimeUtils.getStringDate());

        ArrayList<Exam> mList = new ArrayList<>();

        mList.add(new Exam("09月11日", "08:00--10:00", "语文", "陈丽丽"));
        mList.add(new Exam("09月11日", "10:10--12:10", "数学", "刘芳"));
        mList.add(new Exam("09月11日", "14:00--16:00", "物理", "张丽"));
        mList.add(new Exam("09月11日", "16:10--18:10", "化学", "赵丹"));
        mList.add(new Exam("09月12日", "08:00--10:00", "历史", "陈丽丽"));
        mList.add(new Exam("09月12日", "10:10--12:10", "政治", "刘芳"));

        mListview.setAdapter(new CommonAdapter<Exam>(ExamActivity.this, R.layout.item_exam, mList) {
            @Override
            protected void convert(ViewHolder viewHolder, Exam item, int position) {
                viewHolder.setText(R.id.examitem_date, item.getDate());
                viewHolder.setText(R.id.examitem_time, item.getTime());
                viewHolder.setText(R.id.examitem_course, "科目：" + item.getCourse());
                viewHolder.setText(R.id.examitem_teacher, "监考老师：" + item.getTeacher());
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
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
}
