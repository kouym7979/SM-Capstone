package com.example.sm_capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class SplashActivity extends AppCompatActivity {
    //GetKeyHash getKeyHash =new GetKeyHash(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        getSupportActionBar().hide();

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
