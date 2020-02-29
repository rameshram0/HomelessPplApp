package com.example.homelesspplapp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Display extends Application {


   public String date;

   public String storedate;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public void onCreate() {
        super.onCreate();


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        long s=System.currentTimeMillis();
        SimpleDateFormat sdf=new SimpleDateFormat( "MMM dd yyyy  hh:mm a");
        Date d=new Date(s);
        date=sdf.format(d);
        Log.d("time", "time: "+date);


        /*Calendar c= Calendar.getInstance();
        c.setTimeInMillis(s);*/
        String s1=sp.getString("date","");

        if(date != s1){
        storedate=s1;
        } if(date.contains("")){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("date",date );
            editor.apply();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        onCreate();

    }

}







