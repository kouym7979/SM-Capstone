package com.example.sm_capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.util.KakaoParameterException;

import java.util.HashMap;
import java.util.Map;

public class MyPageActivity extends AppCompatActivity {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private EditText nameEdit, phoneEdit, storeNameEdit, storeNumEdit;
    String name, phoneNum, StoreName, StoreNum;
    private ImageButton logout_btn, modify_btn;
    Activity a;
    private Button kakao_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        nameEdit = findViewById(R.id.nameEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        logout_btn = findViewById(R.id.logout_btn);
        modify_btn = findViewById(R.id.modify_btn);
        a = MyPageActivity.this;
        if(user!=null){
            mStore.collection("user").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        System.out.println(user.getUid());
                        DocumentSnapshot document = task.getResult();
                        name = (String) document.getData().get(EmployID.name);
                        phoneNum = (String) document.getData().get(EmployID.phone_number);
                        System.out.println("확인"+name);
                        nameEdit.setText(name);
                        phoneEdit.setText(phoneNum);
                    }
                }
            });

        }

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MyPageActivity.this);
                dlg.setMessage("로그아웃 하시겠습니까?");
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dlg.show();
            }
        });

        modify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MyPageActivity.this);
                dlg.setMessage("수정 하시겠습니까?");
                dlg.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, Object> userMap = new HashMap<>();
                        userMap.put(EmployID.name, nameEdit.getText().toString());
                        userMap.put(EmployID.phone_number, phoneEdit.getText().toString());
                        mStore.collection("user").document(user.getUid()).update(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ((GlobalMethod)getApplicationContext()).modifyOK(a);
                            }
                        });
                    }
                });
                dlg.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dlg.show();
            }
        });
        /*kakao_btn=findViewById(R.id.kakao_btn);
        kakao_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                     KakaoLink kakaolink=KakaoLink.getKakaoLink(this);
                     KakaoTalkLinkMessageBuilder builder=kakaolink.createKakaoTalkLinkMessageBuilder();

                    builder.addText("링크테스트");//
                    builder.addAppButton("앱 실행하기");//앱실행버튼

                    kakaolink.sendMessage(builder,this);//메시지발송

                } catch (KakaoParameterException e) {
                    e.printStackTrace();
                }

            }
        });*/
    }
    public void kakaoLink(View view){
        FeedTemplate params = FeedTemplate.
                newBuilder(ContentObject.newBuilder("Emplo","https://image.genie.co.kr/Y/IMAGE/IMG_ALBUM/081/191/791/81191791_1555664874860_1_600x600.JPG",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build()).build())
                .addButton(new ButtonObject("앱에서보기", LinkObject.newBuilder()
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1").build()))
                .build();
        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {}

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });
    }
}