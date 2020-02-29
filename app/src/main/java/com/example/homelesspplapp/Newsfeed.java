package com.example.homelesspplapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

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

public class Newsfeed extends AppCompatActivity implements NewsfeedAdapter.OnNoteListener{

    private RecyclerView mList;

    private LinearLayoutManager linearLayoutManager;
    //private DividerItemDecoration dividerItemDecoration;
    private List<Shoplist> movieList;
    private RecyclerView.Adapter adapter;

    ImageView like, comment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);




        mList = findViewById(R.id.list);

        movieList = new ArrayList<>();
        adapter = new NewsfeedAdapter(getApplicationContext(), movieList, this);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        mList.setHasFixedSize(true);
        mList.setLayoutManager(linearLayoutManager);
        // mList.addItemDecoration(dividerItemDecoration);
        mList.setAdapter(adapter);

        zing();

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Newsfeed.this, UserDetail.class));
                overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
              //  finish();

            }
        });

    }




    public void getData() {

        StringRequest request = new StringRequest(Request.Method.POST, "https://rameshram.000webhostapp.com/getnewsfeed.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonObject = array.getJSONObject(i);

                        Shoplist movie = new Shoplist();
                        movie.setTitle(jsonObject.optString("title").replaceAll("\"", ""));

                        movie.setImage(jsonObject.optString("image"));

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




    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private void zing() {
        if (isNetworkConnected()) {
            getData();
        }
    }
    public void showBottomSheetDialogFragment() {
        ChatFragment chatFragment = new ChatFragment();
        chatFragment.show(getSupportFragmentManager(),chatFragment.getTag());
    }


    @Override
    public void onLike(int p) {

    }

    @Override
    public void onComment(int p) {

        showBottomSheetDialogFragment();
    }

    public  void onBackPressed(){
        super.onBackPressed();

        overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
    }
}
