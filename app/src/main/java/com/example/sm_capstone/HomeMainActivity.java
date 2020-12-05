package com.example.sm_capstone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeMainActivity extends AppCompatActivity {

    private ImageView homeBtn, boardBtn, calendarBtn, myPageBtn;
    private ImageView staticImage, dynamicImage;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    private String name, phoneNum, storeName, store_num, type; //매장번호
    private long backKeyPressedTime = 0;
    private Toast toast;
    static RequestQueue requestQueue;
    static String regId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main);

        if(mAuth.getCurrentUser()!=null){//User에 등록되어있는 작성자를 가져오기 위해서
            mStore.collection("user").document(mAuth.getCurrentUser().getUid())//
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.getResult()!=null){
                                store_num=(String)task.getResult().getData().get(EmployID.storeNum);
                                name=(String)task.getResult().getData().get(EmployID.name);
                                phoneNum=(String)task.getResult().getData().get(EmployID.phone_number);
                                storeName=(String)task.getResult().getData().get(EmployID.storeName);
                                type = (String)task.getResult().getData().get(EmployID.type);
                                SharedPreferences preferences = getSharedPreferences("StoreInfo",MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("StoreNum",store_num);
                                editor.putString("StoreName",storeName);
                                editor.putString("Name",name);
                                editor.putString("PhoneNum",phoneNum);
                                editor.putString("Type",type);
                                editor.commit();
                            }
                        }
                    });
        }

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("확인", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("확인", token);
                        Toast.makeText(HomeMainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });

        staticImage = findViewById(R.id.staticImage);
        dynamicImage = findViewById(R.id.dynamicImage);
        homeBtn = findViewById(R.id.home_btn);
        boardBtn = findViewById(R.id.board_btn);
        calendarBtn = findViewById(R.id.calendar_btn);
        myPageBtn = findViewById(R.id.mypage_btn);

        staticImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMainActivity.this, DynamicBoard.class);
                intent.putExtra("board_part","정적게시판");
                startActivity(intent);
            }
        });

        dynamicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMainActivity.this, DynamicBoard.class);
                intent.putExtra("board_part","동적게시판");
                startActivity(intent);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMainActivity.this, HomeMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMainActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        calendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMainActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMainActivity.this, MyPageActivity.class);
                startActivity(intent);
                finish();
            }
        });


        Button pushTest = findViewById(R.id.pushTest);
        pushTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send("하이");
            }
        });

        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public void send(String input){
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("priority","high");

            JSONObject dataObj = new JSONObject();
            dataObj.put("contents", input);
            requestData.put("data",dataObj);

            JSONArray idArray = new JSONArray();
            idArray.put(0, regId);
            requestData.put("registration_ids",idArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                "https://fcm.googleapis.com/fcm/send",
                requestData,
                new Response.Listener<JSONObject>(){

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "key=AAAAAjmqEkM:APA91bE7afelkMdIe7zS11X5i1oiXsjDf1OGhYLBda6_fZYNzmvoHMWYx_baBLulkzQQz2JqmH6jZm8TSVhPzxMrAbsvJSRJJeTk0gVAWalOFiFh-VBmZMBduJ5xR9JwVoD5l6iGYbHb");

                return headers;
            }

            @Override
            protected String getParamsEncoding() {
                Map<String, String> params = new HashMap<String, String>();
                return super.getParamsEncoding();
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed(){
        if (System.currentTimeMillis() > backKeyPressedTime + 2500) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "뒤로 가기 버튼을 한 번 더 누르시면 종료됩니다.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2500) {
            finish();
            toast.cancel();
        }
    }
}