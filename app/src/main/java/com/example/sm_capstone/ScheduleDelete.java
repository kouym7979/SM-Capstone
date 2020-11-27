package com.example.sm_capstone;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ScheduleDelete extends Dialog implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    public ScheduleDelete(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_schedule_delete);

        Button delete_yes = (Button)findViewById(R.id.delete_yes);
        Button delete_no = (Button)findViewById(R.id.delete_no);
    }


    @Override
    public void onClick(View v) {

    }
}
