package com.example.homelesspplapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Donate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setSelected(true);


        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Donate.this, UserDetail.class));
                overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
                finish();
            }
        });

        Button donate = findViewById(R.id.donate);
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent i = new Intent(Donate.this, BottomSheetFragment.class);
                startActivity(i);
                finish();*/
               showBottomSheetDialogFragment();
            }
        });
    }
        public void showBottomSheetDialogFragment() {
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
        }

    public  void onBackPressed(){
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
    }
    }

