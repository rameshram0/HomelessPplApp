package com.example.homelesspplapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class Splash extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(Splash.this);
                String mm = sp.getString("personemail", "");
                if (!mm.isEmpty()) {

                    Intent intent=new Intent(Splash.this,UserDetail.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent=new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }




               /* Intent intent=new Intent(Splash.this,MainActivity.class);
                startActivity(intent);
                finish();*/

            }
        }, 1000);
    }

}