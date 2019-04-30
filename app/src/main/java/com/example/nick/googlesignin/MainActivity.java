package com.example.nick.googlesignin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private LinearLayout user_section;
    private Button signOut;
    private SignInButton signIn;
    private TextView name, email;
    private ImageView profile_pic;
//    private GoogleApiClient googleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQ_CODE = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user_section = findViewById(R.id.user_layout);
        signIn = findViewById(R.id.google_signIn);
        signOut = findViewById(R.id.google_logout);
        name = findViewById(R.id.add_name);
        email = findViewById(R.id.add_email);
        profile_pic = findViewById(R.id.add_profile_pic);

        signIn.setOnClickListener(this);
        signOut.setOnClickListener(this);


        user_section.setVisibility(View.GONE);

        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile().build();


        mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);

//        googleApiClient = new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();



    }



    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.google_signIn:
                signIn();
                break;

            case R.id.google_logout:
                signOut();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,REQ_CODE);

    }

    private void signOut()
    {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                        updateUI(false);
                    }

                });
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//            @Override
//            public void onResult(@NonNull Status status) {
//                updateUI(false);
//            }
//        });

    }

    private void updateUI(boolean isLogin)
    {
        if(isLogin)
        {
            user_section.setVisibility(View.VISIBLE);
            signIn.setVisibility(View.GONE);
        }
        else
        {
            user_section.setVisibility(View.GONE);
            signIn.setVisibility(View.VISIBLE);

        }

    }

    private void handleResult(GoogleSignInResult result)
    {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            String displayName = account.getDisplayName();
            String displayEmail = account.getEmail();
            String img_url = account.getPhotoUrl().toString();

            name.setText(displayName);
            email.setText(displayEmail);
            Glide.with(this).load(img_url).into(profile_pic);
            updateUI(true);

        }
        else
        {
            updateUI(false);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE)
        {
            GoogleSignInResult  result  = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
