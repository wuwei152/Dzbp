package com.md.dzbp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.data.Exam;
import com.md.dzbp.model.TimeListener;
import com.md.dzbp.model.TimeUtils;
import com.md.dzbp.tcp.TcpService;
import com.md.dzbp.ui.view.MainDialog;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

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

    private Handler card_handler = null;
    private Handler foucus_handler = null;
    private String card_stringTemp;
    private MainDialog mainDialog;
    private GestureDetector gestureDetector;

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

        mainDialog = new MainDialog(this);
        gestureDetector = new GestureDetector(ExamActivity.this, onGestureListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        if (intent.hasExtra("id")) {
//            noticeId = intent.getStringExtra("id");
//        }
//        getUIdata();
    }

    @Override
    protected void initData() {
        mCardNum.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                card_stringTemp = arg0.toString();
//                LogUtils.i("卡号"+arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                if (card_handler == null) {
                    card_handler = new Handler();
                    card_handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            LogUtils.d(card_stringTemp);
                            if (!TextUtils.isEmpty(card_stringTemp)) {
                                Intent intent = new Intent(ExamActivity.this, TcpService.class);
                                intent.putExtra("Num", card_stringTemp);
                                intent.putExtra("Act", 4);
                                intent.putExtra("ext", "");
                                startService(intent);
                            }
                            mCardNum.setText("");
                            card_handler = null;

                        }
                    }, 1000);
                }
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//                LogUtils.d(arg0);
            }
        });
        foucus_handler = new Handler();
        foucus_handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCardNum.requestFocus();
                foucus_handler.postDelayed(this, 1000);
            }
        }, 1000);
        new TimeUtils(ExamActivity.this, this);
        mDate.setText(TimeUtils.getStringDate());

        ArrayList<Exam> mList = new ArrayList<>();

        mList.add(new Exam("09月11日","08:00--10:00","语文","陈丽丽"));
        mList.add(new Exam("09月11日","10:10--12:10","数学","刘芳"));
        mList.add(new Exam("09月11日","14:00--16:00","物理","张丽"));
        mList.add(new Exam("09月11日","16:10--18:10","化学","赵丹"));
        mList.add(new Exam("09月12日","08:00--10:00","历史","陈丽丽"));
        mList.add(new Exam("09月12日","10:10--12:10","政治","刘芳"));

        mListview.setAdapter(new CommonAdapter<Exam>(ExamActivity.this,R.layout.item_exam,mList) {
            @Override
            protected void convert(ViewHolder viewHolder, Exam item, int position) {
                viewHolder.setText(R.id.examitem_date,item.getDate());
                viewHolder.setText(R.id.examitem_time,item.getTime());
                viewHolder.setText(R.id.examitem_course,"科目："+item.getCourse());
                viewHolder.setText(R.id.examitem_teacher,"监考老师："+item.getTeacher());
            }
        });
    }

    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 500 && Math.abs(x)>Math.abs(y)) {
                        mainDialog.dismiss();
                    } else if (x < -500&& Math.abs(x)>Math.abs(y)) {
                        mainDialog.show();
                    }
                    return true;
                }
            };

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public void getTime(String time) {
        mTime.setText(time);
    }
}
