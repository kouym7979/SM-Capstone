package com.example.sm_capstone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.sm_capstone.Board_Post.Post;
import com.example.sm_capstone.adapter.BoardAdapter;
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

    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private Context context;
    private RecyclerView Board;//동적게시판
    private BoardAdapter mAdapter;
    private List<Post> mDatas;
    private String board_part;
    private RecyclerView.LayoutManager mlayoutManager;
    private FirebaseFirestore mStore=FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_board);
        Board=findViewById(R.id.recyclerview);
        FloatingActionButton fab=findViewById(R.id.edit_button);//글 작성
        fab.setOnClickListener(this);

        mlayoutManager = new LinearLayoutManager(this);
        Board.setLayoutManager(mlayoutManager);

    }
    @Override
    protected void onStart() {
        Intent intent=getIntent();
        board_part=intent.getStringExtra("board_part");
        super.onStart();
        mDatas = new ArrayList<>();//
        mStore.collection("Post")//리사이클러뷰에 띄울 파이어베이스 테이블 경로
                .whereEqualTo("board_part","동적게시판")//후에 가게정보에 따른 비교를 추가해야함
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
                                        String writer_name = String.valueOf(shot.get(EmployID.writer_name));
                                        String post_id=String.valueOf(shot.get(EmployID.post_id));
                                        String post_photo=String.valueOf(shot.get(EmployID.post_photo));
                                        String board_part=String.valueOf(shot.get(EmployID.board_part));
                                        Post data = new Post(documentId, title, contents,post_id,writer_name,post_photo,board_part);
                                        mDatas.add(data);//여기까지가 게시글에 해당하는 데이터 적용
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
}
