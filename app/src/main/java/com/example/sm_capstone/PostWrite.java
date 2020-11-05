package com.example.sm_capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PostWrite extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth=FirebaseAuth.getInstance();//사용자 정보 가져오기
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private EditText mTitle,mContents;//제목, 내용
    private ImageButton post_photo;
    private ProgressBar post_progressBar;
    private String photoUrl; //사진 저장 변수
    private String post_num,post_id,writer_id;
    private Uri uriProfileImage;
    private ImageView post_imageView;
    private String postImageUrl;
    private static final int CHOOSE_IMAGE = 101;
    private String writer_name;//작성자

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_write);

        mTitle=findViewById(R.id.Post_write_title);//제목 , item_post.xml의 변수와 혼동주의
        mContents=findViewById(R.id.Post_write_contents);
        findViewById(R.id.Post_save).setOnClickListener(this);
        post_photo =findViewById(R.id.Post_photo);
        post_imageView = findViewById(R.id.post_imageview);
        post_imageView.setVisibility(View.INVISIBLE);
        post_progressBar = findViewById(R.id.post_progressbar);

        if(mAuth.getCurrentUser()!=null){//User에 등록되어있는 작성자를 가져오기 위해서
            mStore.collection("user").document(mAuth.getCurrentUser().getUid())//
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult()!=null){
                                writer_name=(String)task.getResult().getData().get(EmployID.name);//
                                //닉네임 뿐만아니라 여기서 FirebaseID.password를 하면 비밀번호도 받아올 수 있음. 즉 원하는 것을 넣으면 됨
                                //파이어베이스에 등록된 닉네임을 불러옴
                                writer_id=(String)task.getResult().getData().get(EmployID.documentId);
                                Log.d("확인","현재 사용자 uid입니다:"+writer_id);
                            }
                        }
                    });
        }

    }

    @Override
    public void onClick(View v) {
        if(mAuth.getCurrentUser()!=null){
            String PostID=mStore.collection("Post").document().getId();//제목이 같아도 게시글이 겹치지않게
            Intent intent=getIntent();
            post_num=intent.getStringExtra("post_num");//동적게시판은 1 정적게시판은 2 예정

            Log.d("확인","여기는 게시글 작성:"+post_num);
            Map<String,Object> data=new HashMap<>();
            data.put(EmployID.documentId,mAuth.getCurrentUser().getUid());//유저 고유번호
            data.put(EmployID.title,mTitle.getText().toString());//게시글제목
            data.put(EmployID.contents,mContents.getText().toString());//게시글 내용
            data.put(EmployID.timestamp, FieldValue.serverTimestamp());//파이어베이스 시간을 저장 그래야 게시글 정렬이 시간순가능
            data.put(EmployID.name,writer_name);
            data.put(EmployID.post_id,PostID);//게시글 ID번호
            data.put(EmployID.post_num,post_num);
            data.put(EmployID.writer_id,writer_id);

            mStore.collection("Post").document(PostID).set(data);//Post라는 테이블에 데이터를 입력하는것/
            finish();
        }
    }
}
