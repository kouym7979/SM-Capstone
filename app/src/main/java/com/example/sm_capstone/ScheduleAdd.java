package com.example.sm_capstone;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ScheduleAdd extends Dialog implements View.OnClickListener,TimePicker.OnTimeChangedListener {

//    TimePicker.OnTimeChangedListener


    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();

    private Context context;
    private Button btn_add_exit, btn_add_submit; //취소버튼, 완료버튼
    private String writer_id; //작성자넘버
    private String writer_name; //작성자 이름
    private String store_num;
    private String request="0";
    private String date; //날짜
//    private String start_time; //출근시간
//    private String end_time; //퇴근시간
    private EditText mreference; //참고사항
    private int hourOfDay1, hourOfDay2;
    private int minute1, minute2;

    private Calendar c1, c2;
    private TimePicker start_time, end_time;

    DateFormat fmtDateAndTime = DateFormat.getDateInstance(); //기본 날짜 시간 포맷 설정
    TextView dateAndTimeLabel;
    TextView startTimeLabel, endTimeLabel;
    Calendar dateAndTime = Calendar.getInstance(); //현재의 시간으로 Calendar객체 생성
    TextView name;


    //Listener설정과 설정된 날자를 Calendar 객체에 설정 : set눌렀을 때 처리
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateLabel();
        }
    };
    //Listener 설정과 설정된 시간을 Calendar 객체에 설정:set눌렀을 때 처리
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            updateDateLabel();
        }
    };

   /**Called when the activity is first created. */

    private void updateDateLabel() {
        dateAndTimeLabel.setText(fmtDateAndTime.format(dateAndTime.getTime()));
    }



    public ScheduleAdd(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schedule_add);

        mreference=findViewById(R.id.schedule_reference); //참고사항

        btn_add_submit = (Button)findViewById(R.id.btn_add_submit);
        btn_add_submit.setOnClickListener(this);

        if(mAuth.getCurrentUser()!=null){//User에 등록되어있는 작성자를 가져오기 위해서
            mStore.collection("user").document(mAuth.getCurrentUser().getUid())//
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult()!=null){
                                writer_name=(String)task.getResult().getData().get(EmployID.name);//
                                writer_id=(String)task.getResult().getData().get(EmployID.documentId);
                                store_num=(String)task.getResult().getData().get(EmployID.storeNum);
                                Log.d("확인","현재 사용자 uid입니다:"+writer_id);
                                Log.d("확인","현재 사용자 이름입니다"+writer_name);

                                name = (TextView) findViewById(R.id.txt_add_name);
                                name.setText(writer_name);

                            }
                        }
                    });
        }


        Button btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                new DatePickerDialog(context, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        //출근시간 TimePicker
        c1 = Calendar.getInstance();
        hourOfDay1 = c1.get(c1.HOUR_OF_DAY);
        minute1 = c1.get(c1.MINUTE);
        startTimeLabel = (TextView)findViewById(R.id.startTimeLabel);
        start_time = (TimePicker)findViewById(R.id.tp_starttime);
        startTimeLabel.setText(hourOfDay1+":"+minute1);
        start_time.setOnTimeChangedListener(this);

        //퇴근시간 TimePicker
        c2 = Calendar.getInstance();
        hourOfDay2 = c2.get(c2.HOUR_OF_DAY);
        minute2 = c2.get(c2.MINUTE);
        endTimeLabel = (TextView) findViewById(R.id.endTimeLabel);
        end_time = (TimePicker)findViewById(R.id.tp_endtime);
        endTimeLabel.setText(hourOfDay2+":"+minute2);
        end_time.setOnTimeChangedListener(this);




        dateAndTimeLabel = (TextView) findViewById(R.id.dateAndTime);


        updateDateLabel();

    }

    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser() != null){
            String ScheduleID = mStore.collection("CalendarPost").document().getId();

            Map<String, Object> data = new HashMap<>();
            data.put(EmployID.documentId,mAuth.getCurrentUser().getUid()); //유저 고유번호
            data.put(EmployID.writer_name, writer_name); //작성자 이름
            data.put(EmployID.schedule_id, ScheduleID); //스케줄 고유번호
            data.put(EmployID.writer_id,writer_id); //작성자 id
            data.put(EmployID.date, dateAndTimeLabel.getText().toString()); //날짜
            data.put(EmployID.start_time, startTimeLabel.getText().toString()); //출근시간
            data.put(EmployID.end_time, endTimeLabel.getText().toString()); //퇴근시간
            data.put(EmployID.reference, mreference.getText().toString()); //참고사항
            data.put(EmployID.timestamp, FieldValue.serverTimestamp());//파이어베이스 시간을 저장 그래야 게시글 정렬이 시간순가능
            data.put(EmployID.storeNum, store_num);
            data.put(EmployID.request, request);
            mStore.collection("CalendarPost").document(ScheduleID).set(data); //CalendarPost이라는 테이블에 데이터를 입력

        }

        if(v==btn_add_submit){
            dismiss();
        }

    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
       if(view==start_time){
           startTimeLabel.setText(hourOfDay+":"+minute);
       }
       else{
           endTimeLabel.setText(hourOfDay+":"+minute);
       }
    }
}
