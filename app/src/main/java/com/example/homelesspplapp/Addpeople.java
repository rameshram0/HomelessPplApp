package com.example.homelesspplapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Delayed;

public class Addpeople extends AppCompatActivity {

    AlertDialog.Builder builder;

    EditText orname, username, userphn;
    TextView userlocation;

    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpeople);




        builder = new AlertDialog.Builder(this);

        orname = findViewById(R.id.opname);
        orname.setText(orname.getText().toString());
        username = findViewById(R.id.username);
        userphn = findViewById(R.id.userphn);
        userlocation = findViewById(R.id.location);
        userlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(Addpeople.this, MapsActivity.class);
                startActivity(i);
                finish();


            }
        });


        Intent i = getIntent();
        final String location = i.getStringExtra("loc");
        userlocation.setText(location);

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location == null) {
                    Toast.makeText(Addpeople.this, "must select location", Toast.LENGTH_SHORT).show();
                    userlocation.setError("hehe");
                } else if (orname.getText().toString().isEmpty()) {
                    Toast.makeText(Addpeople.this, "plz enter orpen name", Toast.LENGTH_SHORT).show();
                    orname.requestFocus();


                } else if (username.getText().toString().isEmpty()) {
                    Toast.makeText(Addpeople.this, "plz enter user name", Toast.LENGTH_SHORT).show();
                    username.requestFocus();
                } else if (userphn.getText().toString().isEmpty()) {
                    Toast.makeText(Addpeople.this, "plz enter user num", Toast.LENGTH_SHORT).show();
                    userphn.requestFocus();
                } else {

                    adduser(orname.getText().toString(), username.getText().toString(), userphn.getText().toString(), userlocation.getText().toString());
                    // Toast.makeText(Addpeople.this, "Success Submitted", Toast.LENGTH_SHORT).show();
                    username.setText("");
                    orname.setText("");
                    username.setText("");
                    userphn.setText("");


                    builder.setMessage("Success Submitted")
                            .setCancelable(false)
                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {


                                    Intent i = new Intent(Addpeople.this, UserDetail.class);
                                    startActivity(i);
                                    finish();

                                }
                            });

                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Hello user!");
                    alert.show();

                }

            }
        });


        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Addpeople.this, UserDetail.class));
                overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
                finish();
            }
        });
    }

    public void adduser(final String ornae, final String usname, final String usphn, final String loc) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(Addpeople.this);
        String url = "https://rameshram.000webhostapp.com/orphendetails.php"; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("ornae", ornae);
                MyData.put("usname", usname);
                MyData.put("usphn", usphn);
                MyData.put("loc", loc);
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);
    }
    public  void onBackPressed(){
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
    }
}


 /*builder.setMessage("Success Submitted")
         .setCancelable(false)
         .setPositiveButton("ok", new DialogInterface.OnClickListener() {
public void onClick(DialogInterface dialog, int id) {


        Intent i = new Intent(Addpeople.this, UserDetail.class);
        startActivity(i);
        finish();

        }
        });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Hello user!");
        alert.show();*/