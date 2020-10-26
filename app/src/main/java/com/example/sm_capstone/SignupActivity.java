package com.example.sm_capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity {

    private Button valButton;
    private Button regButton;
    private Button depButton;
    private EditText idEdit;
    private EditText nameEdit;
    private EditText passEdit;
    private EditText passChkEdit;
    private EditText phoneNumEdit;
    private EditText emailEdit;

    int dep;
    Boolean check = false;

    String id;
    String name;
    String password;
    String phoneNum;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_acitivity);
        setTitle("회원가입");

        valButton = findViewById(R.id.valButton);
        regButton = findViewById(R.id.regButton);
        depButton = findViewById(R.id.depButton);

        idEdit = findViewById(R.id.userId);
        nameEdit = findViewById(R.id.username);
        passEdit = findViewById(R.id.password);
        passChkEdit = findViewById(R.id.passCheck);
        phoneNumEdit = findViewById(R.id.phoneNum);
        emailEdit = findViewById(R.id.email);



    }
}