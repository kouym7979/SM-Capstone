package com.example.sm_capstone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sm_capstone.Board_Post.Post;
import com.example.sm_capstone.adapter.BoardAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicBoard extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private Context context;
    private RecyclerView Board;//동적게시판
    private BoardAdapter mAdapter;
    private List<Post> mDatas;
    private String board_part;
    private RecyclerView.LayoutManager mlayoutManager;
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    private Button write_btn;
    private String store_num;
    private TextView tv;
    private ImageView standingImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_board);
        Board=findViewById(R.id.recyclerview);
        write_btn=findViewById(R.id.write_btn);
        tv=findViewById(R.id.dynamic_text);
        standingImage = findViewById(R.id.standingPicture);

        SharedPreferences preferences = getSharedPreferences("StoreInfo",MODE_PRIVATE);
        store_num = preferences.getString("StoreNum","0");

        mlayoutManager = new LinearLayoutManager(this);
        Board.setLayoutManager(mlayoutManager);

        write_btn.setOnClickListener(this);

    }
    @Override
    protected void onStart() {
        Intent intent=getIntent();
        board_part=intent.getStringExtra("board_part");//정적 또는 동적게시판 데이터를 불러옴
        if(board_part.equals("정적게시판"))
        {
            tv.setText("고정 게시판 Fixed Bulletin Board");
            standingImage.setImageDrawable(getResources().getDrawable(R.drawable.standing2));
        }
        super.onStart();
        mDatas = new ArrayList<>();//
        mStore.collection("Post")//리사이클러뷰에 띄울 파이어베이스 테이블 경로
                .whereEqualTo("board_part",board_part)//후에 가게정보에 따른 비교를 추가해야함
                .orderBy(EmployID.timestamp, Query.Direction.DESCENDING)//시간정렬순으로
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (queryDocumentSnapshots != null) {
                                   mDatas.clear();//미리 생성된 게시글들을 다시 불러오지않게 데이터를 한번 정리
                                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                        Map<String, Object> shot = snap.getData();
                                        String documentId = String.valueOf(shot.get(EmployID.documentId));
                                        String title = String.valueOf(shot.get(EmployID.title));
                                        String contents = String.valueOf(shot.get(EmployID.contents));
                                        String writer_name = String.valueOf(shot.get(EmployID.name));
                                        String post_id=String.valueOf(shot.get(EmployID.post_id));
                                        String post_photo=String.valueOf(shot.get(EmployID.post_photo));
                                        String board_part=String.valueOf(shot.get(EmployID.board_part));
                                        String post_storenum=String.valueOf(shot.get(EmployID.storeNum));
                                        Post data = new Post(documentId, title, contents,post_id,writer_name,post_photo,board_part,post_storenum);
                                        System.out.println("스토어 넘버는:"+post_storenum);
                                        if(store_num.equals(post_storenum))
                                        {
                                            mDatas.add(data);
                                        }
                                        //여기까지가 게시글에 해당하는 데이터 적용
                                    }
                                    mAdapter = new BoardAdapter(DynamicBoard.this,mDatas);//mDatas라는 생성자를 넣어줌
                                    Board.setAdapter(mAdapter);
                                }
                            }
                        });
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(DynamicBoard.this,PostWrite.class);
        intent.putExtra("board_part",board_part);
        startActivity(intent);
        finish();
    }


    public void onBackPressed(){
        Intent intent = new Intent(DynamicBoard.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    public void checkNum(){
        if(mAuth.getCurrentUser()!=null){//User에 등록되어있는 작성자를 가져오기 위해서
            mStore.collection("user").document(mAuth.getCurrentUser().getUid())//
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult()!=null){
                                store_num=(String)task.getResult().getData().get(EmployID.storeNum);//
                                System.out.println("여기");
                                Log.d("확인","store_num"+store_num);
                            }
                        }
                    });
        }
    }


    public void search(final String s_text){
        mDatas.clear();//문자 입력시새로 리스트 목록 활성화를 위해서

        if(s_text.length()==0)
            mDatas.addAll(sub);//다시 기존의 데이터를 넣어줌
        else{
                for (int i=0;i<sub.size();i++) {
                    String s_title=sub.get(i).getTitle();
                    Log.d("확인","제목:"+s_title);
                    Log.d("확인","s_text: "+s_text);
                    if (s_title.contains(s_text)) {
                        String documentId = String.valueOf(sub.get(i).getDocumentId());
                        String title = String.valueOf(sub.get(i).getTitle());
                        String contents = String.valueOf(sub.get(i).getContents());
                        String writer_name = String.valueOf(sub.get(i).getWriter_name());
                        String post_id=String.valueOf(sub.get(i).getPost_id());
                        String post_photo=String.valueOf(sub.get(i).getPost_photo());
                        String board_part=String.valueOf(sub.get(i).getBoard_part());
                        Post data = new Post(documentId, title, contents,post_id,writer_name,post_photo,board_part, store_num);
                        mDatas.add(data);
                        Log.d("확인","포함되어있습니다"+s_title);
                    }
                    else Log.d("확인","확인이 안됩니다");
                }
            }

        mAdapter.notifyDataSetChanged();
    }


}
