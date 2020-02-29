package com.example.homelesspplapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetail extends AppCompatActivity {

    private long backpress;
    private Toast backToast;

    JSONObject response, profile_pic_data, profile_pic_url;

    Button logout;
    ImageView pic;

    TextView name;
    SharedPreferences preferences;
    Button newsfeed,profile,addpeople,donate,sponsers;

    SharedPreferences sp;

    TextView lastseen,online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Display d=(Display)getApplicationContext();
        String s3=d.storedate;

        String s5=d.date;

        long t=System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MMM dd yyyy  hh:mm a");
        Date date=new Date(t);
        String onlin=simpleDateFormat.format(date);

        if(s5.equals(onlin)){
           online =findViewById(R.id.online);
           // online.setText("You Are In ONLINE");
        }

        lastseen=findViewById(R.id.lastseen);
        lastseen.setText("LAST VISITED "+"   "+s3);


       preferences = PreferenceManager.getDefaultSharedPreferences(UserDetail.this);
        String h1=preferences.getString("name","");
        String h2=preferences.getString("pic","");





        pic=findViewById(R.id.profilepic);
        Glide.with(UserDetail.this).load(h2).into(pic);

        newsfeed=findViewById(R.id.newsfeed);
        profile=findViewById(R.id.profile);
        addpeople=findViewById(R.id.addpeople);
        donate=findViewById(R.id.donate);
        sponsers=findViewById(R.id.sponsers);

        newsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i=new Intent(UserDetail.this,Newsfeed.class);
            startActivity(i);
                    overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
            //finish();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserDetail.this,Profile.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
               // finish();
            }
        });
        addpeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserDetail.this,Addpeople.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
                //finish();
            }
        });
        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserDetail.this,Donate.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
               // finish();
            }
        });
        sponsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(UserDetail.this,Sponscers.class);
                startActivity(i);
                overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
              //  finish();
            }
        });


        TextView tv=(TextView)findViewById(R.id.textView1);
        tv.setSelected(true);

        name=(TextView)findViewById(R.id.name);
        name.setText("Welcome\n"+h1);

       // logout=findViewById(R.id.lgout);
       /* logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginManager.getInstance().logOut();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UserDetail.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("email");
                editor.clear();
                editor.commit();
                Intent i=new Intent(UserDetail.this,MainActivity.class);
                startActivity(i);
                finish();

            }
        });*/

       // TextView user_name = (TextView) findViewById(R.id.profile_name);
        CircleImageView user_picture = (CircleImageView) findViewById(R.id.profilepic);
       // TextView user_email = (TextView) findViewById(R.id.profile_email);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(UserDetail.this);
       String h=preferences.getString("email","");
       //String h1=preferences.getString("name","");
      // String h2=preferences.getString("image","");
       // user_name.setText(h1);
        //user_email.setText(h);
       // Glide.with(UserDetail.this).load(h2).into(user_picture);



    }

    boolean backbtn=false;
    public  void onBackPressed(){

        if (backbtn){
            super.onBackPressed();
        }else{
            backbtn=true;
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backbtn=false;
                }
            },2000);
        }
    }
}
