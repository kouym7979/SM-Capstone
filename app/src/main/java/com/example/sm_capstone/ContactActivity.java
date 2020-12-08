package com.example.sm_capstone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.Distribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ContactActivity extends AppCompatActivity {

    private ArrayList<Contact> mArrayList;
    private ContactAdapter mAdapter;
    private ContactAdapterVerEmployee mAdapterE;
    Activity a;
    private int size = 0;
    private TextView tv;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private String myType;
    private String store_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        a = ContactActivity.this;
        tv = findViewById(R.id.accept_tv);

        SharedPreferences preferences = getSharedPreferences("ContactInfo", MODE_PRIVATE);
        myType = preferences.getString("myType","0");
        SharedPreferences preferences2 = getSharedPreferences("StoreInfo", MODE_PRIVATE);
        store_num = preferences2.getString("StoreNum","0");

        if(myType.equals("manager"))
        {
            tv.setVisibility(View.VISIBLE);
        }
        else
        {
            tv.setVisibility(View.GONE);
        }

        final RecyclerView mRecyclerView = findViewById(R.id.contact_recyclerview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mArrayList = new ArrayList<>();
        mStore.collection("user")
                .whereEqualTo("storeNum", store_num)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null){
                            mArrayList.clear();
                            for (DocumentSnapshot doc : value.getDocuments()){
                                Map<String, Object> map = doc.getData();
                                String name = String.valueOf(map.get(EmployID.name));
                                String phoneNum = String.valueOf(map.get(EmployID.phone_number));
                                String accept = String.valueOf(map.get(EmployID.accept));
                                Contact data = new Contact(name, phoneNum, accept);
                                mArrayList.add(data);
                            }
                            if(myType.equals("manager"))
                            {   mAdapter = new ContactAdapter(mArrayList);
                                mRecyclerView.setAdapter(mAdapter);}
                            else {
                                mAdapterE = new ContactAdapterVerEmployee(mArrayList);
                                mRecyclerView.setAdapter(mAdapterE);
                            }

                        }
                    }
                });

        mAdapter.setOnDeleteClickListener(new ContactAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(View v, int position) {
                Contact item = mAdapter.getItem(position);

            }
        });

        mAdapter.setOnOkClickListener(new ContactAdapter.OnOkClickListener() {
            @Override
            public void onOkClick(View v, int position) {

            }
        });
    }

    public void deleteDialog(){
        Dialog builder = new Dialog(this);
        builder.setContentView(R.layout.custom_dialog);
        builder.show();
        final Button yesbtn=(Button)builder.findViewById(R.id.yesButton);
        final Button nobtn=(Button)builder.findViewById(R.id.noButton);

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}