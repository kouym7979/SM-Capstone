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
import android.widget.GridView;
import android.widget.TextView;

import com.example.sm_capstone.Board_Post.Post;
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

//    private static final String TAG = MainActivity.class.getSimpleName();

//    private FirebaseDatabase database;       //realtime database
//    private DatabaseReference databaseReference;  //realtime database

    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();

    private Context context;
    private RecyclerView Schedule; //캘린더리스트(스케줄)
    private ScheduleAdapter scheduleAdapter;
    private RecyclerView.LayoutManager ScheduleLayoutManager;
    private List<CalendarPost> datas;

    private CalendarAdapter calendarAdapter; //캘린더adapter;
    private TextView monthText; //월 text 표시
    private Button btn_monthPrevious, btn_monthNext;  //월 이동 버튼
    private Button btn_scheduleAdd; //Add버튼 (일정추가)
    private ScheduleAdd scheduleAdd; //일정추가 다이얼로그
    private Button btn_modify, btn_delete; //일정 수정, 삭제버튼
    private Button btn_request; //대타 요청 버튼

    // day=27  date=2020. 11. 27
    String year; //클릭된 날짜에 해당 년도
    String month; //클릭된 날짜의 해당 달
    String day;  //클릭된 날짜
    String selectedDate; //클릭된 날짜 문자열



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        ///////////////////////
//        Schedule = findViewById(R.id.recycler_schedule);  //리싸이클러뷰
//        mlayoutManager = new LinearLayoutManager(this);
//        Schedule.setLayoutManager(mlayoutManager);
        ////////////////////////

        /////////////////////////캘린더//////////////////////////////////////
        calendarAdapter = new CalendarAdapter(this);
        GridView monthView = this.findViewById(R.id.monthView);
        monthView.setAdapter(calendarAdapter);
        monthView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MonthItem selectedItem = (MonthItem) calendarAdapter.getItem(position);
                day = String.valueOf(selectedItem.getDay());
                month = String.valueOf(calendarAdapter.getMonth()+1);
                year = String.valueOf(calendarAdapter.getYear());
                selectedDate = (year+". "+month+". "+day+".");
                Log.d("선택된날짜",selectedDate);

//                Log.d(TAG, String.format("Selected Day: %s", day));

                calendarAdapter.setSelectedPosition(position);
                calendarAdapter.notifyDataSetChanged();
            }
        });

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

        /////////////////////////////스케줄/////////////////////////////////////
        Schedule = findViewById(R.id.recycler_schedule); //아이디 연결

        ScheduleLayoutManager = new LinearLayoutManager(this);
        Schedule.setLayoutManager(ScheduleLayoutManager);

//        //수정버튼
//        btn_modify = findViewById(R.id.btn_modify);
//        btn_modify.setOnClickListener(this);
//
//        //삭제버튼
//        btn_delete = findViewById(R.id.btn_delete);
//        btn_delete.setOnClickListener(this);
//
//        //대타 요청 버튼
//        btn_request = findViewById(R.id.btn_request);
//        btn_request.setOnClickListener(this);



        //realtime database test
//        database = FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
//        databaseReference = database.getReference("CalendarPost"); //DB테이블 연결
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                datas.clear(); //기존 배열리스트가 존재하지 않게 초기화
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){ //반복문으로 데이터 List 추출
//                    CalendarPost calendarpost = snapshot.getValue(CalendarPost.class);  //만들어뒀던 CalendarPost객체에 데이터를 담음
//                    datas.add(calendarpost);  //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
//                }
//                scheduleAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                //DB를 가져오던 중 에러 발생 시
//                Log.e("CalendarActivity", String.valueOf(error.toException()));  //에러문 출력
//            }
//        });
//        scheduleAdapter = new ScheduleAdapter(datas, CalendarActivity.this);
//        Schedule.setAdapter(scheduleAdapter);  //리사이클러뷰에 어댑터 연결



    }


    protected void onStart(){
        Intent intent = getIntent(); //데이터 전달받기
        super.onStart();


        datas = new ArrayList<>();  //Calendar Post 객체를 담을 ArrayList (어댑터쪽으로)
        mStore.collection("CalendarPost")
                .whereEqualTo("date", selectedDate)
                .orderBy(EmployID.writer_name, Query.Direction.DESCENDING)//시간정렬순으로
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
                                    scheduleAdapter = new ScheduleAdapter(datas, CalendarActivity.this);
                                    Schedule.setAdapter(scheduleAdapter);
                                }
                            }
                        }
                );
    }


    private void setMonthText() {
        int year = calendarAdapter.getYear();
        int month = calendarAdapter.getMonth();
        monthText.setText(String.format("%s-%s", year, month+1));
    }

    public void onClick(View v) {
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
                scheduleAdd = new ScheduleAdd(this);
                break;
//            case R.id.btn_modify:
//                break;
//            case R.id.btn_delete:
//                break;
//            case R.id.btn_request:
//                break;
        }
    }


}