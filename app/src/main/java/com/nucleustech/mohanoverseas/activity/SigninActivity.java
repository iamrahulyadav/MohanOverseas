package com.nucleustech.mohanoverseas.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.nucleustech.mohanoverseas.R;
import com.nucleustech.mohanoverseas.model.UserClass;
import com.nucleustech.mohanoverseas.util.Util;

/**
 * Created by ritwik on 28/11/17.
 */

public class SigninActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;

    //Firebase and GoogleApiClient
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN = 9001;
    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = SigninActivity.this;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
    }


    public void onGoogleClick(View v) {
        mFirebaseAuth.signOut();
        //startActivity(new Intent(mContext, WalkthroughActivity.class));

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Toast.makeText(mContext,"Google Account Verified.",Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                Log.e("Signin", "Google Sign In failed.");
                Toast.makeText(mContext,"Google Sign In failed.",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("SignIn", "onConnectionFailed:" + connectionResult);
        Util.initToast(this, "Google Play Services error.");
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(mContext,"Authentication failed",Toast.LENGTH_SHORT).show();
                           // Util.initToast(mContext, "Authentication failed");
                        } else {
                            //Toast.makeText(mContext,"Logging in! Please wait...",Toast.LENGTH_SHORT).show();
                            UserClass userClass= Util.fetchUserClass(mContext);
                            if(userClass==null)
                                userClass= new UserClass();
                            userClass.displayName= "" + acct.getDisplayName();
                            userClass.profileUrl= "" + acct.getPhotoUrl();
                            Util.saveUserClass(mContext,userClass);
                            Intent intent = new Intent(mContext, WalkthroughActivity.class);
                            /*intent.putExtra("name", "" + acct.getDisplayName());
                            intent.putExtra("photo", "" + acct.getPhotoUrl());*/
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
