package com.example.sm_capstone;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;

public class ScheduleAdd extends Dialog implements View.OnClickListener {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();

    private Context context;
    private Button btn_add_exit, btn_add_submit; //취소버튼, 완료버튼
    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;
    private String writer; //작성자
    private String time; //시간
    private String date; //날짜

    DateFormat fmtDateAndTime = DateFormat.getDateInstance(); //기본 날짜 시간 포맷 설정
    TextView dateAndTimeLabel;
    Calendar dateAndTime = Calendar.getInstance(); //현재의 시간으로 Calendar객체 생성
    //Listener설정과 설정된 날자를 Calendar 개게에 설정 : set눌렀을 때 처리
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener(){
        
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };
    //Listener 설정과 설정된 시간을 Calendar 객체에 설정:set눌렀을 때 처리
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            updateLabel();
        }
    };

   /**Called when the activity is first created. */

    private void updateLabel() {
        dateAndTimeLabel.setText(fmtDateAndTime.format(dateAndTime.getTime()));
    }


    public ScheduleAdd(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public ScheduleAdd(Context context, View.OnClickListener positiveListener, View.OnClickListener negativeListener){
        super(context);
        this.mPositiveListener = positiveListener;
        this.mNegativeListener = negativeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schedule_add);

        btn_add_exit = (Button) findViewById(R.id.btn_add_exit);
        btn_add_submit = (Button) findViewById(R.id.btn_add_submit);

        //클릭 리스너 셋팅 (클릭 버튼이 동작하도록)
        btn_add_exit.setOnClickListener(mNegativeListener);
        btn_add_submit.setOnClickListener(mPositiveListener);

        Button btn_date = (Button) findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                new DatePickerDialog(context, d,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        Button btn_time = (Button) findViewById(R.id.btn_time);
        btn_time.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new TimePickerDialog(context, t,
                        dateAndTime.get(Calendar.HOUR_OF_DAY),
                        dateAndTime.get(Calendar.MINUTE),
                        true).show();
            }
        });
        dateAndTimeLabel = (TextView) findViewById(R.id.dateAndTime);
        updateLabel();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_add_exit:
                this.dismiss();
                break;
            case R.id.btn_add_submit:
                dismiss();
                break;
        }
    }
}
