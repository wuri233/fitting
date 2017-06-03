package com.example.fitting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.fitting.service.AutoRemindService;

public class NewPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plan);
    }
    public void openRemind(){
        Intent intent=new Intent(this, AutoRemindService.class);
        startService(intent);
    }
    public void stopRemind(){
        Intent intent=new Intent(this, AutoRemindService.class);
        stopService(intent);
    }
}
