package com.example.sm_capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener{

    private Button val_Button;//중복확인버튼
    private Button regButton;
    private EditText nameEdit, passEdit,passChkEdit,phoneNumEdit,emailEdit;
    private EditText numLayout, nameLayout;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore mstore = FirebaseFirestore.getInstance();
    Activity a;
    private CheckBox manager;//관리자인지 직원인지
    private CheckBox employee;
    int dep;
    Boolean check = false;
    String type;//관리자, 직원 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_acitivity);
        setTitle("회원가입");
        a = SignupActivity.this;
        //getSupportActionBar().hide();

        val_Button = findViewById(R.id.valButton);
        regButton = findViewById(R.id.regButton);

        nameEdit = findViewById(R.id.username);
        passEdit = findViewById(R.id.password);
        passChkEdit = findViewById(R.id.passCheck);
        phoneNumEdit = findViewById(R.id.phoneNum);
        emailEdit = findViewById(R.id.email);
        nameLayout = findViewById(R.id.storeNameLayout);
        numLayout = findViewById(R.id.storeNumLayout);

        //checkBox
        manager = (CheckBox) findViewById(R.id.manager);
        employee = (CheckBox) findViewById(R.id.employee);

        manager.setOnCheckedChangeListener(this);
        employee.setOnCheckedChangeListener(this);

        val_Button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDuplicate();
            }
        });

        //regButton ->회원가입 완료버튼
        regButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInfo();
            }
        });

    }

    public void checkDuplicate(){//아이디 중복확인 메소드
        mAuth.fetchSignInMethodsForEmail(emailEdit.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if(task.getResult().getSignInMethods().size()==0){
                            ((GlobalMethod)getApplicationContext()).idPossible(a);
                            val_Button.setEnabled(false);
                            val_Button.setTextColor(Color.GREEN);
                            val_Button.setText("사용가능");
                        }
                        else{
                            ((GlobalMethod)getApplicationContext()).idDuplicate(a);
                        }

                    }
                });
    }

    public void registerInfo() {//회원가입 등록 메소드
        if (val_Button.isEnabled()) {
            ((GlobalMethod)getApplicationContext()).idChkPlz(a);
        } else {
            Toast.makeText(getApplicationContext(), "잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
            mAuth.createUserWithEmailAndPassword(emailEdit.getText().toString(), passEdit.getText().toString()).
                    addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null) {
                                    Map<String, Object> userMap = new HashMap<>();
                                    //EmploID란 클래스를 통해서 사용자별 정보를 객체별로 저장하는 클래스
                                    userMap.put(EmployID.documentId, user.getUid());//고유 식별번호
                                    userMap.put(EmployID.name, nameEdit.getText().toString());
                                    userMap.put(EmployID.phone_number, phoneNumEdit.getText().toString());
                                    userMap.put(EmployID.email, emailEdit.getText().toString());
                                    userMap.put(EmployID.password, passEdit.getText().toString());
                                    userMap.put(EmployID.type, type);
                                    userMap.put(EmployID.storeName,nameLayout.getText().toString());
                                    userMap.put(EmployID.storeNum,numLayout.getText().toString());
                                    Log.d("확인","가입자 형식:"+type);
                                    Log.d("확인","documentId:"+mAuth.getCurrentUser().getUid());
                                    Log.d("확인","name:"+nameEdit.getText().toString());
                                    Log.d("확인","phone_number:"+phoneNumEdit.getText().toString());
                                    Log.d("확인","email:"+ emailEdit.getText().toString());
                                    Log.d("확인","password:"+ passEdit.getText().toString());
                                    Log.d("확인","employ:"+ EmployID.user);
                                    mstore.collection(EmployID.user).document(user.getUid()).set(userMap, SetOptions.merge());
                                    finish();
                                }
                            } else {
                                Toast.makeText(SignupActivity.this, "error.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(manager.isChecked()==true)
        {
            employee.setChecked(false);
            type="manager";
        }
        else if(employee.isChecked()==true)
        {
            manager.setChecked(false);
            type="employee";
        }
    }
}