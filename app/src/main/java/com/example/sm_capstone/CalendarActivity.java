package com.example.sm_capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private RecyclerView scheduleList; //캘린더리스트(스케줄)
    private CalendarAdapter calendarAdapter; //캘린더adapter;
//    private ScheduleAdapter scheduleAdapter; //스케줄adapter
    private TextView monthText; //월 text 표시
    private Button btn_monthPrevious, btn_monthNext;  //월 이동 버튼
    private Button btn_scheduleAdd; //Add버튼 (일정추가)
    private ScheduleAdd scheduleAdd; //일정추가 다이얼로그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        calendarAdapter = new CalendarAdapter(this);
        GridView monthView = this.findViewById(R.id.monthView);
        monthView.setAdapter(calendarAdapter);
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MonthItem selectedItem = (MonthItem) calendarAdapter.getItem(position);
                int day = selectedItem.getDay();
                Log.d(TAG, String.format("Selected Day: %s", day));

                calendarAdapter.setSelectedPosition(position);
                calendarAdapter.notifyDataSetChanged();
            }
        });

        //파라미터에 리스너 등록
//        scheduleAdd = new ScheduleAdd(this, positiveListener, negativeListener);


        monthText = this.findViewById(R.id.monthText);
        this.setMonthText();

        //이전달로 이동 버튼
        btn_monthPrevious=findViewById(R.id.btn_monthPrevious);
        btn_monthPrevious.setOnClickListener(this);

        //다음달로 이동 버튼
        btn_monthNext=findViewById(R.id.btn_monthNext);
        btn_monthNext.setOnClickListener(this);

        //일정추가 버튼
        btn_scheduleAdd=findViewById(R.id.btn_scheduleAdd);
        btn_scheduleAdd.setOnClickListener(this);



    }

    private void setMonthText() {
        int year = calendarAdapter.getYear();
        int month = calendarAdapter.getMonth();
        monthText.setText(String.format("%s-%s", year, month+1));
    }

    public void onClick(View v) {
//        startActivity(new Intent(CalendarActivity.this,PostWrite.class));
//        finish();
        switch(v.getId()){
            case R.id.btn_monthPrevious:
                calendarAdapter.setPreviousMonth();
                calendarAdapter.notifyDataSetChanged();
                setMonthText();
                break;
            case R.id.btn_monthNext:
                calendarAdapter.setNextMonth();
                calendarAdapter.notifyDataSetChanged();
                setMonthText();
                break;
            case R.id.btn_scheduleAdd:
                scheduleAdd = new ScheduleAdd(this);
                scheduleAdd.setCanceledOnTouchOutside(true);
                scheduleAdd.setCancelable(true);
                scheduleAdd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                scheduleAdd.show();
                scheduleAdd = new ScheduleAdd(this, positiveListener, negativeListener);
                break;
        }
    }

    private View.OnClickListener positiveListener = new View.OnClickListener(){
        public void onClick(View v){
            scheduleAdd.dismiss();
        }
    };

    private View.OnClickListener negativeListener = new View.OnClickListener(){
        public void onClick(View v){
            scheduleAdd.dismiss();
        }
    };


}