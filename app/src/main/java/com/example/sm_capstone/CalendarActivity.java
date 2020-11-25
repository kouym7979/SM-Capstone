package com.example.sm_capstone;

import androidx.annotation.NonNull;
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
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sm_capstone.Board_Post.Post;
import com.example.sm_capstone.adapter.subCalendarAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalendarActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();

    private Context context;
    private RecyclerView Schedule; //캘린더리스트(스케줄)
    private ScheduleAdapter scheduleAdapter;
    private RecyclerView.LayoutManager ScheduleLayoutManager;
    private List<CalendarPost> datas;
    private subCalendarAdapter _calendarAdapter;
    private CalendarAdapter calendarAdapter; //캘린더adapter;
    private TextView monthText; //월 text 표시
    private Button btn_monthPrevious, btn_monthNext;  //월 이동 버튼
    private Button btn_scheduleAdd; //Add버튼 (일정추가)
    private ScheduleAdd scheduleAdd; //일정추가 다이얼로그
    private Button btn_modify, btn_delete; //일정 수정, 삭제버튼
    private Button btn_request; //대타 요청 버튼
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarAdapter = new CalendarAdapter(this);

        CalendarView calendar=findViewById(R.id.calendar);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {//선택한 날짜를 문자열 형태로 전환
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 date=String.format("%d. %d. %d",year,month+1,dayOfMonth);
                Log.d("확인",date);
               loadSchedule(date);
            }
        });
        //monthText = this.findViewById(R.id.monthText);
        //this.setMonthText();
        loadSchedule(date);

        //일정추가 버튼
        btn_scheduleAdd=findViewById(R.id.btn_scheduleAdd);
        btn_scheduleAdd.setOnClickListener(this);

        /////////////////////////////스케줄/////////////////////////////////////
        Schedule = findViewById(R.id.recycler_schedule); //아이디 연결

        ScheduleLayoutManager = new LinearLayoutManager(this);
        Schedule.setLayoutManager(ScheduleLayoutManager);

       /* //수정버튼
        btn_modify = findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);

        //삭제버튼
        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

        //대타 요청 버튼
        btn_request = findViewById(R.id.btn_request);
        btn_request.setOnClickListener(this);
*/

    }

    private void setMonthText() {
        int year = calendarAdapter.getYear();
        int month = calendarAdapter.getMonth();
        monthText.setText(String.format("%s-%s", year, month+1));
    }

    public void onClick(View v) {
        switch(v.getId()){

            case R.id.btn_scheduleAdd:
                scheduleAdd = new ScheduleAdd(this);
                scheduleAdd.setCanceledOnTouchOutside(true);
                scheduleAdd.setCancelable(true);
                scheduleAdd.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                scheduleAdd.show();
                scheduleAdd = new ScheduleAdd(this);
                break;
            case R.id.btn_modify:
                break;
            case R.id.btn_delete:
                break;
            case R.id.btn_request:
                break;
        }
    }

    public void loadSchedule(String date){
        datas = new ArrayList<>();  //Calendar Post 객체를 담을 ArrayList (어댑터쪽으로)
        Log.d("확인","확인날짜:"+date);
        mStore.collection("CalendarPost")
                //.whereEqualTo("date", date)
                //.orderBy(EmployID.timestamp, Query.Direction.DESCENDING)//시간정렬순으로
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if(value != null){
                                    datas.clear();
                                    for(DocumentSnapshot snap : value.getDocuments()){
                                        Map<String, Object> shot = snap.getData();
                                        String documentId = String.valueOf(shot.get(EmployID.documentId));
                                        String writer_name = String.valueOf(shot.get(EmployID.name));
                                        String schedule_id = String.valueOf(shot.get(EmployID.schedule_id));
                                        String date = String.valueOf(shot.get(EmployID.date));
                                        String start_time = String.valueOf(shot.get(EmployID.start_time));
                                        String end_time = String.valueOf(shot.get(EmployID.end_time));
                                        String reference = String.valueOf(shot.get(EmployID.reference));

                                        CalendarPost data = new CalendarPost(documentId, writer_name, schedule_id, date, start_time, end_time, reference);
                                        datas.add(data);
                                    }
                                    _calendarAdapter = new subCalendarAdapter(CalendarActivity.this,datas);
                                    Schedule.setAdapter(_calendarAdapter);
                                }
                            }
                        }
                );
    }
}