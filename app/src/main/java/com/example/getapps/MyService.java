package com.example.getapps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.security.auth.login.LoginException;

import static android.content.ContentValues.TAG;

public class MyService extends Service {

    public static String str_receiver = "com.example.getapps.receiver";
    public static final long NOTIFY_INTERVAL = 1000;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    Intent lockIntent;
    SharePrefData sharePrefData;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimer = new Timer();
        lockIntent = new Intent(this, LockScreen.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);
        sharePrefData = new SharePrefData(this);
        sharePrefData.getLocked(getApplicationContext());
        sharePrefData.getTimer("SELECTED_TIME");

    }

//    @Override
//    public void onStart(Intent intent, int startid)
//    {
//        //try noz  ok
//        Intent intents = new Intent(getBaseContext(),SelectTimer.class);
//        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intents);
//        Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
//        Log.d(TAG, "onStart");
//    }


//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Intent intents = new Intent(getBaseContext(),SelectTimer.class);
//         intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//         startActivity(intents);
//
//
//
//        return super.onStartCommand(intent, flags, startId);
//    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        assert alarmService != null;
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 200,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
        Log.e("Service_Auto_Restart", "ON");
    }

     // Compares the foreground app with selected app
    public void lockTheApp(){
        int count = 0;
        try {
            count = sharePrefData.getLocked(getApplicationContext()).size();
        } catch (Exception e) {

        }
        for (int i=0;i<count;i++){
            if (printForegroundTask().equalsIgnoreCase(sharePrefData.getLocked(getApplicationContext()).get(i))){
                startActivity(lockIntent);
            }
        }
    }

    private String printForegroundTask() {
        String currentApp = "";
        @SuppressLint("WrongConstant") UsageStatsManager usm = (UsageStatsManager) this.getSystemService("usagestats");
        long time = System.currentTimeMillis();
        assert usm != null;
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            assert am != null;
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }
       // Log.e("FOREGROUND", "Current App in foreground is: " + currentApp);
        return currentApp;
    }

//    public void clearPackageName(){
//        SharedPreferences preferences = getSharedPreferences("package_names", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();
//    }

    private void clearAppData() {
        try {
            // clearing app data
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData(); // note: it has a return value!
            } else {
                String packageName = getApplicationContext().getPackageName();
                Runtime runtime = Runtime.getRuntime();
                runtime.exec("pm clear "+packageName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideApp(){
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this,MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,  PackageManager.DONT_KILL_APP);
    }

    public   void showApp(){
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this,MainActivity.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,  PackageManager.DONT_KILL_APP);
    }

    public void Timer(){
        CountDownTimer countDownTimer = new CountDownTimer(sharePrefData.getTimer("theTime"),500) {
            @Override
            public void onTick(long l) {
                lockTheApp();
                hideApp();
            }
            @Override
            public void onFinish() {
                showApp();
                sharePrefData.removeLocked(getApplicationContext(),"");
                clearAppData();


//                sharePrefData.clearPackageName();
//                sharePrefData.clearTimer();
//                Toast.makeText(MyService.this, "FINISHED", Toast.LENGTH_SHORT).show();
//               // stopSelf();
//                stopService(new Intent(getApplicationContext(),MyService.class));

            }
        }.start();
    }

//-------------- TimerTask Which runs in the background-----------------------

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Timer();
                    }
            });
        }

    }
}
