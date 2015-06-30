package com.gingerwolfmc.gingerwolfmc;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.WindowManager;


public class splashscreen extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_spashscreen);
        Thread logotimer = new Thread(){
            public void run(){
                try{
                    sleep(5000);
                    Intent menuIntent = new Intent("com.gingerwolfmc.MainActivity");
                    startActivity(menuIntent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally{
                    finish();
                }
            }
        };
            logotimer.start();
    }
}
