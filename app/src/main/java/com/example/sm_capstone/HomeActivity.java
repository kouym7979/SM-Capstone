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
    private BoardAdapter mAdapter;
    private List<Home_Post> mDatas;
    private Button btn_Dyboard,btn_logou;//동적게시판으로 이동하는 버튼
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        dynamicBoard=findViewById(R.id.recyclerview);
        btn_Dyboard=findViewById(R.id.btn_board);

        btn_calendar=findViewById(R.id.btn_calendar);




    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onItemClicked(int position) {

    }

}
