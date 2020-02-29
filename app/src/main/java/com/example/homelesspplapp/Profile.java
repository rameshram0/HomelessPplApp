package com.example.homelesspplapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;

import de.hdodenhof.circleimageview.CircleImageView;


    public class Profile extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
        Button logoutBtn;
        TextView userName, userEmail, userId;
        CircleImageView profileImage;
        private GoogleApiClient googleApiClient;
        private GoogleSignInOptions gso;
        SharedPreferences sp;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_profile);

         sp =PreferenceManager.getDefaultSharedPreferences(this);
            logoutBtn = (Button) findViewById(R.id.logoutBtn);
            userName = (TextView) findViewById(R.id.name);
            userEmail = (TextView) findViewById(R.id.email);
            userId = (TextView) findViewById(R.id.userId);
            profileImage = findViewById(R.id.profileImage);

            gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            ImageView back=findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(Profile.this, UserDetail.class));
                                            overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
                                            finish();
                                        }
                                    });




            logoutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    if (status.isSuccess()) {
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.remove("personemail");
                                        editor.commit();
                                        gotoMainActivity();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Session not close", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            });
        }


        @Override
        protected void onStart() {
            super.onStart();
            OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            if(opr.isDone()){
                GoogleSignInResult result=opr.get();
                handleSignInResult(result);
            }else{
                opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                    @Override
                    public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                        handleSignInResult(googleSignInResult);
                    }
                });
            }
        }
        private void handleSignInResult(GoogleSignInResult result){
            if(result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();

                if (acct != null){

                    String personName = acct.getDisplayName();
                    //String personGivenName = acct.getGivenName();
                    //String personFamilyName = acct.getFamilyName();
                    String personEmail = acct.getEmail();
                    String personId = acct.getId();
                    Uri personPhoto = acct.getPhotoUrl();


                  /*  SharedPreferences.Editor editor=sp.edit();
                    editor.putString("personemail",personEmail);
                    editor.apply();*/


                    userName.setText(personName);
                    userEmail.setText(personEmail);
                    userId.setText(personId);

                    if (acct.getPhotoUrl() != null){
                        Glide.with(this).load(personPhoto).into(profileImage);
                        Toast.makeText(this, "picture found", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(this, "no picture found", Toast.LENGTH_SHORT).show();
                    }
                }
               /* try {
                  //  Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "image not found", Toast.LENGTH_LONG).show();
                }*/

            }else{
                gotoMainActivity();
            }
        }
        private void gotoMainActivity(){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
            finish();
        }
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        public  void onBackPressed(){
            super.onBackPressed();

            overridePendingTransition(R.anim.fade_in_anim,R.anim.fade_out_anim);
        }
    }

    /*CircleImageView i;
    SharedPreferences sp;
    TextView proname,proemail;
    Button logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,UserDetail.class));
                finish();
            }
        });

        sp= PreferenceManager.getDefaultSharedPreferences(this);
        String u=sp.getString("image","");
        String un=sp.getString("name","");
        String ue=sp.getString("email","");
        i=(CircleImageView) findViewById(R.id.i);
        Glide.with(Profile.this).load(u).into(i);

        proname=findViewById(R.id.profile_name);
        proname.setText(un);
        proemail=findViewById(R.id.profile_email);
        proemail.setText(ue);

        logout=findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LoginManager.getInstance().logOut();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Profile.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("email");
                editor.clear();
                editor.commit();
                Intent i=new Intent(Profile.this,MainActivity.class);
                startActivity(i);
                finish();


            }
        });


    }
}
*/