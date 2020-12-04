package com.example.sm_capstone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.sm_capstone.EmployID.date;

public class ScheduleRequest extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private EditText request_reference; //요청시 참고사항
    private String writer_id; //작성자 id
    private String writer_name; //작성자 name
    private String schedule_id; //스케줄넘버
    private String start_time, end_time;
    private Button btn_request; //요청하기 버튼
    private TextView date;
    private TextView starttime, endtime;




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        Intent intent = getIntent();
        schedule_id = intent.getStringExtra("schedule_id");
        start_time = intent.getStringExtra("start_time");
        end_time = intent.getStringExtra("end_time");


        date = findViewById(R.id.request_date);     //해당 날짜
        starttime = findViewById(R.id.request_starttime);      //해당 출근 시간
        endtime = findViewById(R.id.request_endtime);          //해당 퇴근 시간
        request_reference = findViewById(R.id.request_reference);         //참고사항
        findViewById(R.id.request_save).setOnClickListener(this);      //완료 버튼

        date.setText(EmployID.date);
        starttime.setText(start_time);
        endtime.setText(end_time);

        if(mAuth.getCurrentUser()!=null){//User에 등록되어있는 작성자를 가져오기 위해서
            mStore.collection("user").document(mAuth.getCurrentUser().getUid())//
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult()!=null){
                                writer_name=(String)task.getResult().getData().get(EmployID.name);//
                                writer_id=(String)task.getResult().getData().get(EmployID.documentId);
                                Log.d("ScheduleRequest","현재 사용자 uid입니다:"+writer_id);
                                Log.d("ScheduleRequest","현재 사용자 이름입니다"+writer_name);
                                Log.d("ScheduleRequest", "현재 스케줄 id입니다:"+schedule_id);
                                Log.d("ScheduleRequest", "현재 스케줄 start_time입니다:"+start_time);
                                Log.d("ScheduleRequest", "현재 스케줄 end_time입니다:"+end_time);
                            }
                        }
                    });
        }


    }


    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser()!=null){
            String ScheduleID = mStore.collection("CalendarPost").document().getId();
            Intent intent = getIntent();
            schedule_id = intent.getStringExtra("schedule_id");

            Log.d("확인", "해당 scheduleid:"+schedule_id);

            Map<String, Object> data = new HashMap<>();
            data.put(EmployID.documentId, mAuth.getCurrentUser().getUid());
            data.put(EmployID.request, "1");
            mStore.collection("CalendarPost").document(schedule_id).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Request complete",Toast.LENGTH_SHORT).show();
                }
            });
            finish();
        }


    }
}
