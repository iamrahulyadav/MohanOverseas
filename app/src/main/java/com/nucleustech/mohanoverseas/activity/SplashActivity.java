package com.nucleustech.mohanoverseas.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nucleustech.mohanoverseas.R;
import com.nucleustech.mohanoverseas.model.UserClass;
import com.nucleustech.mohanoverseas.model.UserModel;
import com.nucleustech.mohanoverseas.util.Util;

/**
 * Created by ritwik on 28/11/17.
 */

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new SplashTimerTask().execute();

    }

    /**
     * The Splash Timer. duration ---> 2345ms
     *
     * @params {@link AsyncTask}
     */
    private class SplashTimerTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SplashActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading! Please wait...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2345);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            verifyUserLogin();
        }
    }

    /**
     * Open IntroActivity.
     */
    private void openIntroActivity() {

        Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Open LoginActivity.
     */
   /* public void openLoginActivity() {

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }*/

    /**
     * Verify user is logged in
     */
    private void verifyUserLogin() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            openIntroActivity();
        } else {
            Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
            UserClass userClass= Util.fetchUserClass(SplashActivity.this);
            if(userClass==null)
                userClass= new UserClass();
            userClass.displayName= "" + mFirebaseUser.getDisplayName();
            userClass.profileUrl= "" + mFirebaseUser.getPhotoUrl();
            Util.saveUserClass(SplashActivity.this,userClass);
            intent.putExtra("name",""+mFirebaseUser.getDisplayName());
            startActivity(intent);
            finish();
        }
    }

}
