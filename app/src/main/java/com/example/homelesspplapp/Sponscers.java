package com.example.homelesspplapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Sponscers extends AppCompatActivity implements SponsorsAdapter.OnNoteListener {

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    //private DividerItemDecoration dividerItemDecoration;
    private List<Sponsorslist> movieList;
    private RecyclerView.Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponscers);



        mList = findViewById(R.id.list);

        movieList = new ArrayList<>();
        adapter = new SponsorsAdapter(getApplicationContext(), movieList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        // mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);


        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sponscers.this, UserDetail.class));
                overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
                finish();
            }
        });
        getData();

    }
    public void getData() {

        StringRequest request = new StringRequest(Request.Method.POST, "https://rameshram.000webhostapp.com/getsponsors.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);

                        Sponsorslist movie = new Sponsorslist();
                        movie.setName(jsonObject.optString("name").replaceAll("\"", ""));
                        movie.setOccupation(jsonObject.optString("occupation").replaceAll("\"", ""));
                        movie.setEmail(jsonObject.optString("email").replaceAll("\"", ""));



                        movieList.add(movie);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                // progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                //progressDialog.dismiss();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }



    @Override
    public void onLike(int p) {

    }

    @Override
    public void onComment(int p) {

    }
    public  void onBackPressed(){
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
    }
}
