package com.nucleustech.mohanoverseas.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleustech.mohanoverseas.R;
import com.nucleustech.mohanoverseas.util.Util;
import com.nucleustech.mohanoverseas.volley.ServerResponseCallback;
import com.nucleustech.mohanoverseas.volley.VolleyTaskManager;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ritwik on 28/11/17.
 */

public class OTPActivity extends AppCompatActivity implements ServerResponseCallback{

    private EditText otp;
    private VolleyTaskManager volleyTaskManager;
    private static String otpp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        otp = (EditText) findViewById(R.id.otp);
        volleyTaskManager = new VolleyTaskManager(OTPActivity.this);
    }

    public void onOTPClick(View v) {
        String otpString = otp.getText().toString().trim();
        if (TextUtils.isEmpty(otpString)) {
            Util.showMessageWithOk(OTPActivity.this, "Please enter your phone number.");
            return;
        } else if (otpString.length() < 10) {
            Util.showMessageWithOk(OTPActivity.this, "Please enter a correct phone number.");
            return;
        }
        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("mobile", "" + otpString.trim());
        volleyTaskManager.doGetOtp(requestMap,true);

    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {


        if(resultJsonObject.optString("code").trim().equalsIgnoreCase("200")){
            otpp= resultJsonObject.optString("auth");
            final Dialog dialog = new Dialog(OTPActivity.this);
            dialog.setContentView(R.layout.dialog_verify_otp);
            dialog.setCancelable(false);
            Button btnOk = (Button) dialog.findViewById(R.id.btn_verify);
            final EditText tv_userType = (EditText) dialog.findViewById(R.id.etOTPRecived);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String receiverOTP= tv_userType.getText().toString().trim();
                    if(TextUtils.isEmpty(receiverOTP)){
                        Toast.makeText(OTPActivity.this, "Please enter the OTP.",Toast.LENGTH_SHORT).show();
                    }
                    else if(receiverOTP.length()<6){
                        Toast.makeText(OTPActivity.this, "Please enter the correct OTP.",Toast.LENGTH_SHORT).show();
                    }
                    else if(!receiverOTP.equalsIgnoreCase(otpp)){
                        Toast.makeText(OTPActivity.this, "Please enter the correct OTP.",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dialog.dismiss();
                        gotoNextActivity();
                    }
                }
            });
            dialog.show();
        }
    }

    private void gotoNextActivity() {
        startActivity(new Intent(OTPActivity.this, SigninActivity.class));

    }

    @Override
    public void onError() {

    }
}
