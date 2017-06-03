package com.example.fitting.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.fitting.MainActivity;
import com.example.fitting.MyReceiver;
import com.example.fitting.R;
import com.example.fitting.db.PlanItem;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AutoRemindService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

                Log.d("AutoRemindService", "executed at " + new Date().
                        toString());
         checkTiming();
        AlarmManager manager =(AlarmManager)getSystemService(ALARM_SERVICE);
        int anMinute=60*1000;
        long triggerAtTime= SystemClock.elapsedRealtime()+anMinute;
        Intent i=new Intent(this,AutoRemindService.class);
        PendingIntent pi=PendingIntent.getService(this,0,i,0);
        manager.cancel(pi);
        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
    private void checkTiming(){
        SimpleDateFormat sHourFormat = new SimpleDateFormat("HH");
        final String nowHour = sHourFormat.format(new Date());
        SimpleDateFormat sMinuteFormat = new SimpleDateFormat("mm");
        final String nowMinute = sMinuteFormat.format(new Date());
        List<PlanItem> planItems = DataSupport.findAll(PlanItem.class);
        for (PlanItem planItem : planItems) {
            if (planItem.isTimingCheck()) {
                if (planItem.getTimeHour() == Integer.parseInt(nowHour)) {
                    if (planItem.getTimeMinute() == Integer.parseInt(nowMinute)) {
                        Intent intent = new Intent(this, MainActivity.class);
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
                        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notification = new NotificationCompat.Builder(this)
                                .setContentTitle(planItem.getPlanName())
                                .setContentText("完成他吧")
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                                .setWhen(System.currentTimeMillis())
                                .setDefaults(NotificationCompat.DEFAULT_ALL)
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .build();
                        manager.notify(1, notification);
                        Toast.makeText(this, "完成他吧", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent=new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =PendingIntent.getActivity(this,0,intent,0);
        Notification notification=new NotificationCompat.Builder(this)
                .setContentTitle("要完成的计划")
                .setContentText(stringName())
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(2,notification);
    }

    public String stringName(){
        StringBuffer names = new StringBuffer();
        List<PlanItem> planItems = DataSupport.findAll(PlanItem.class);
        for (PlanItem planItem : planItems){
            names.append(planItem.getPlanName()+";");
        }
        String strNames = names.toString();
        return strNames;
    }


}

