package com.example.fitting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.fitting.service.AutoRemindService;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
        Intent service = new Intent(context, AutoRemindService.class);

            context.startService(service);
    }
    }
}
