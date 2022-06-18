package com.example.parstagram;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.parstagram.activities.LoginActivity;
import com.example.parstagram.activities.MainActivity;

public class SplashScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        finish();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                Intent i=new Intent(SplashScreenActivity.this, LoginActivity.class);
//                startActivity(i);
//            }
//        }, 500);

    }
}

