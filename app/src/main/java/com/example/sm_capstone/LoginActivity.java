package com.example.sm_capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity<CheckB> extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private Button btn_register;
    private EditText emailEdit,passEdit;
    private CheckBox autoLogin;

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);


        emailEdit = findViewById(R.id.emailEdit);
        passEdit = findViewById(R.id.passEdit);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_login:
                String u_id=emailEdit.getText().toString();
                String u_pw=passEdit.getText().toString();
                if(u_id==null || u_pw==null)
                    Toast.makeText(LoginActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                if(u_id!=null && u_pw!=null){
                    loginStart(u_id,u_pw);
                }
                break;
            case R.id.btn_register:
                startActivity(new Intent(this,SignupActivity.class));
                break;
        }
    }
    public void loginStart(String id,String pw){
        Toast.makeText(LoginActivity.this,"Login",Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(id,pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(LoginActivity.this,"로그인 확인 중",Toast.LENGTH_SHORT).show();
                if(!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
                else{
                    currentUser =mAuth.getCurrentUser();//현재 유저확인
                    Toast.makeText(LoginActivity.this,"로그인 성공",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });
    }


   /* @Override// 자동로그인 메소드 추가예정->xml linearlayout형태로 바꿔야함
    public void onStart(){
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        autoLogin = (CheckBox)findViewById(R.id.chk_autologin);
        if(autoLogin.isChecked()==true)
        {
            if (currentUser != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        }
    }*/

}
