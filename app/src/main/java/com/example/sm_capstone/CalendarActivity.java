package com.example.sm_capstone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sm_capstone.Board_Post.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private Context context;
    private RecyclerView Schedule; //캘린더리스트(스케줄)
    private List<CalendarPost> mDatas;
    private RecyclerView.LayoutManager mlayoutManager;
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private CalendarAdapter calendarAdapter; //캘린더adapter;
    private ScheduleAdapter scheduleAdapter; //스케줄adapter
    private TextView monthText; //월 text 표시
    private Button btn_monthPrevious, btn_monthNext;  //월 이동 버튼
    private Button btn_scheduleAdd; //Add버튼 (일정추가)
    private ScheduleAdd scheduleAdd; //일정추가 다이얼로그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ///////////////////////
        Schedule = findViewById(R.id.schedule);  //리싸이클러뷰
        mlayoutManager = new LinearLayoutManager(this);
        Schedule.setLayoutManager(mlayoutManager);
        ////////////////////////

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

    protected void onStart() {
        Intent intent = getIntent();
        super.onStart();

        mDatas = new ArrayList<>();
//        mStore.collection("CalendarPost") //리사이클러뷰에 띄울 파이어베이스 테이블 경로
//                .addSnapshotListener(
//                        new EventListener<QuerySnapshot>() {
//                            @Override
//                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
//                                if(queryDocumentSnapshots != null){
//                                    mDatas.clear();
//                                    for(DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()){
//                                        Map<String, Object> shot = snap.getData();
//                                        String documentId = String.valueOf(shot.get(EmployID.documentId));
//                                        String writer_name = String.valueOf(shot.get(EmployID.writer_name));
//                                        String schedule_id = String.valueOf(shot.get(EmployID.schedule_id));
//                                        String writer_id = String.valueOf(shot.get(EmployID.writer_id));
//                                        String date = String.valueOf(shot.get(EmployID.date));
//                                        String start_time = String.valueOf(shot.get(EmployID.start_time));
//                                        String end_time = String.valueOf(shot.get(EmployID.end_time));
//                                        String mreference = String.valueOf(shot.get(EmployID.reference));
//                                        CalendarPost data = new CalendarPost(documentId, writer_name, schedule_id, date, start_time, end_time, mreference);
//                                        mDatas.add(data);
//                                    }
//                                    scheduleAdapter = new ScheduleAdapter(CalendarActivity.this, mDatas);
//                                    Schedule.setAdapter(scheduleAdapter);
//                                }
//                            }
//                        }
//                );
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