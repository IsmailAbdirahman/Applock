package com.example.getapps;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import java.util.Objects;


public class Boot_Com extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // SHOW THE APP
        PackageManager pp = context.getPackageManager();
        ComponentName componentNamee = new ComponentName(context,MainActivity.class);
        pp.setComponentEnabledSetting(componentNamee, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,  PackageManager.DONT_KILL_APP);

        //-----------re-open the App -------------
        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);


        PackageManager p = context.getPackageManager();
        ComponentName componentName = new ComponentName(context,MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,  PackageManager.DONT_KILL_APP);



//        try {
//            Thread.sleep(400);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        context.startService(new Intent(context, MyService.class));
        /*-------alarm setting after boot again--------*/
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 999, alarmIntent, 0);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = (60000 * 1000) / 4;
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);




    }
}
