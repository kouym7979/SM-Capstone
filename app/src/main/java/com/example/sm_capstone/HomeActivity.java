package com.example.sm_capstone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sm_capstone.Board_Post.Home_Post;
import com.example.sm_capstone.Board_Post.Post;
import com.example.sm_capstone.adapter.BoardAdapter;
import com.example.sm_capstone.adapter.HomeAdapter;

import com.example.sm_capstone.ui.home.HomeFragment;

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

public class HomeActivity extends AppCompatActivity  implements  BoardAdapter.EventListener{

    private Button btn_home,btn_mypage,btn_calendar,btn_static;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private Context context;
    private RecyclerView dynamicBoard;//동적게시판

    private HomeAdapter mAdapter;
    private List<Home_Post> mDatas, sDatas;

    private Button btn_Dyboard,btn_logou;//동적게시판으로 이동하는 버튼
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private TextView dynamic,staticboard;
    private RecyclerView h_dynamicBoard,static_board;//동적게시판

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dynamic=findViewById(R.id.dynamic);
        staticboard=findViewById(R.id.staticboard);

        dynamicBoard=findViewById(R.id.recyclerview);
        btn_Dyboard=findViewById(R.id.btn_board);

        btn_calendar=findViewById(R.id.btn_calendar);


        Button.OnClickListener onClickListener=new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.btn_board:
                        Intent intent=new Intent(HomeActivity.this,DynamicBoard.class);
                        intent.putExtra("board_part","동적게시판");//1은 동적게시판,2는 정적게시판
                        startActivity(intent);
                        break;
                    case R.id.btn_calendar:
                        Intent intent2=new Intent(HomeActivity.this, CalendarActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.btn_staticboard:
                        Intent intent3=new Intent(HomeActivity.this,DynamicBoard.class);
                        intent3.putExtra("board_part","정적게시판");//1은 동적게시판,2는 정적게시판
                        startActivity(intent3);
                        break;
                    case R.id.btn_my_page:
                        Intent intent4=new Intent(HomeActivity.this,MyPageActivity.class);
                        startActivity(intent4);
                        break;
                }
            }
        };
        Button D_board = (Button)findViewById(R.id.btn_board) ;
        Button Calendar=(Button)findViewById(R.id.btn_calendar);
        Button S_board=findViewById(R.id.btn_staticboard);
        Calendar.setOnClickListener(onClickListener);
        D_board.setOnClickListener(onClickListener) ;
        S_board.setOnClickListener(onClickListener);
        h_dynamicBoard=(RecyclerView)findViewById(R.id.home_recyclerview2);
        static_board=(RecyclerView)findViewById(R.id.home_recyclerview3);

    }

    @Override
    public void onStart() {
        super.onStart();
        mDatas = new ArrayList<>();//동적게시판 용
        sDatas=new ArrayList<>();//정적게시판용
        mStore.collection("Post")//리사이클러뷰에 띄울 파이어베이스 테이블 경로
                // .whereEqualTo("board_part","동적게시판")//1은 동적, 2는 정적 게시판
                .orderBy(EmployID.timestamp, Query.Direction.DESCENDING)//시간정렬순으로
                .addSnapshotListener(
                        new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                if (queryDocumentSnapshots != null) {
                                    mDatas.clear();//미리 생성된 게시글들을 다시 불러오지않게 데이터를 한번 정리
                                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                        Map<String, Object> shot = snap.getData();
                                        String title = String.valueOf(shot.get(EmployID.title));
                                        //String board_part=String.valueOf(shot.get(EmployID.board_part));
                                        String writer_name=String.valueOf(shot.get(EmployID.name));
                                        Home_Post data = new Home_Post(writer_name,title);
                                        mDatas.add(data);//여기까지가 게시글에 해당하는 데이터 적용

                                    }
                                    mAdapter = new HomeAdapter(getApplicationContext(),mDatas);//mDatas라는 생성자를 넣어줌
                                    h_dynamicBoard.setAdapter(mAdapter);

                                }
                            }
                        });


    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onItemClicked(int position) {

    }

}
