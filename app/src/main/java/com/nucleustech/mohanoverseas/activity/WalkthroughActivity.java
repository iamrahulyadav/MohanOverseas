package com.nucleustech.mohanoverseas.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.nucleustech.mohanoverseas.R;
import com.nucleustech.mohanoverseas.adapter.WalkthroughAdapter;

/**
 * Created by knowalladmin on 28/11/17.
 */

public class WalkthroughActivity extends AppCompatActivity {

    ViewPager myPager;
    private Context mContext;
    private int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        mContext = WalkthroughActivity.this;

        WalkthroughAdapter adapter = new WalkthroughAdapter();
        myPager = (ViewPager) findViewById(R.id.viewpager_layout);

        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);

    }

    public void jumpToNextPage(View view) {

        myPager.setCurrentItem(myPager.getCurrentItem() + 1, true);
        //Toast.makeText(mContext, "Current Item: " + myPager.getCurrentItem(), Toast.LENGTH_SHORT).show();

        if (myPager.getCurrentItem() == 3&& count!=1) {
            //Toast.makeText(mContext, "Move to the next Activity.", Toast.LENGTH_SHORT).show();
            //Move to the next Activity
            count=1;


        }
        else if(myPager.getCurrentItem()==3 && count==1){
            Intent intent= new Intent(mContext, ChatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }

    public void jumpToPreviousPage(View view) {

        myPager.setCurrentItem(myPager.getCurrentItem() - 1, true);
    }


    public void onSkipClicked(View view) {

        //Move to the next Activity
        Intent intent= new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
