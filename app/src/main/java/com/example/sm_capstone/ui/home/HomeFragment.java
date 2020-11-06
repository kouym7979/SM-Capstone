package com.example.sm_capstone.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_capstone.Board_Post.Post;
import com.example.sm_capstone.DynamicBoard;
import com.example.sm_capstone.EmployID;
import com.example.sm_capstone.HomeActivity;
import com.example.sm_capstone.R;
import com.example.sm_capstone.adapter.BoardAdapter;
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

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FirebaseAuth Auth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private Context context;
    private RecyclerView dynamicBoard;//동적게시판
    private BoardAdapter mAdapter;
    private List<Post> mDatas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.activity_home, container, false);

        context =container.getContext();

        Button.OnClickListener onClickListener=new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.btn_board:
                       Intent intent=new Intent(getActivity(),DynamicBoard.class);
                       intent.putExtra("post_num","1");//1은 동적게시판,2는 정적게시판
                        startActivity(intent);
                        break;

                }
            }
        };
        Button D_board = (Button) root.findViewById(R.id.btn_board) ;
        D_board.setOnClickListener(onClickListener) ;
        dynamicBoard=(RecyclerView)root.findViewById(R.id.recyclerview);

        return root;
    }
    @Override
    public void onStart() {
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
                                    mAdapter = new BoardAdapter(getContext(),mDatas);//mDatas라는 생성자를 넣어줌
                                    dynamicBoard.setAdapter(mAdapter);
                                }
                            }
                        });
    }
}