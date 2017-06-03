package com.example.fitting;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.fitting.db.PlanItem;

import org.litepal.crud.DataSupport;

import java.util.List;


public class Remind2Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.USER_PRESENT")) {
            int time2 = 0;
            List<PlanItem> planItems = DataSupport.findAll(PlanItem.class);
            for (PlanItem planItem : planItems) {
                if (planItem.getIntension() != 0) {
                    time2 = planItem.getIntension();
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
                    editor.putString("date33", planItem.getPlanName());
                    editor.apply();
                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    int anMinute = time2*5*60*1000;
                    long triggerAtTime = SystemClock.elapsedRealtime() + anMinute;
                    Intent i = new Intent(context, MyReceiver.class);
                    PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
                    manager.cancel(pi);
                    manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
                }
            }

        }
    }
}
