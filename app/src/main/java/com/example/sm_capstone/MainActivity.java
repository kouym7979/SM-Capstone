package com.example.sm_capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.sm_capstone.Board_Post.Post;
import com.example.sm_capstone.adapter.BoardAdapter;
import com.example.sm_capstone.ui.home.HomeViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BoardAdapter.EventListener{

    private AppBarConfiguration mAppBarConfiguration;
    private HomeViewModel homeViewModel;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private Context context;
    private RecyclerView dynamicBoard;//동적게시판
    private BoardAdapter mAdapter;
    private List<Post> mDatas;
    private Button btn_Dyboard,btn_logout;//동적게시판으로 이동하는 버튼
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_logout, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        dynamicBoard=findViewById(R.id.recyclerview);
        btn_Dyboard=findViewById(R.id.btn_board);

        btn_Dyboard.setOnClickListener(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();//
        mStore.collection("Post")//리사이클러뷰에 띄울 파이어베이스 테이블 경로
                //.whereEqualTo("post_num",)//후에 가게정보에 따른 비교를 추가해야함
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

                                        Post data = new Post(documentId, title, contents,post_id,writer_name);
                                        mDatas.add(data);//여기까지가 게시글에 해당하는 데이터 적용

                                    }
                                    mAdapter = new BoardAdapter(MainActivity.this,mDatas);//mDatas라는 생성자를 넣어줌
                                    dynamicBoard.setAdapter(mAdapter);
                                }
                            }
                        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_board:
                startActivity(new Intent(MainActivity.this, DynamicBoard.class));
                finish();

        }
    }
}

