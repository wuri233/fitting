package com.example.fitting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prefs= PreferenceManager.getDefaultSharedPreferences(context);
        String date=prefs.getString("date33","");
        Toast.makeText(context, "完成你的计划:"+date, Toast.LENGTH_SHORT).show();
    }
}
