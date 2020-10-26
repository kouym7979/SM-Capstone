package com.example.sm_capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText id,pw;
    private Button login;
    private Button os;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id=findViewById(R.id.id);
        pw=findViewById(R.id.password);
       findViewById(R.id.login_btn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
