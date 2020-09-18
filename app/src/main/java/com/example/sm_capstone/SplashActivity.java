package com.example.sm_capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler hd=new Handler();
        hd.postDelayed(new splashhandler(),1000);
    }

    private class splashhandler implements Runnable {
        public void run(){
            startActivity(new Intent(getApplication(),LoginActivity.class));
            SplashActivity.this.finish();
        }
    }
}
