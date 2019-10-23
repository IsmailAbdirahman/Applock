package com.example.getapps;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity{
    //============UI=============//
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecyclerView recyclerView;
    CardView cardView;
    AppsAdapter adapter;
    SharePrefData sharePrefData;
    //=======VARIABLES================//
    String  ApplicationPackageName=null;
    static Context context;
    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardView = findViewById(R.id.card_view);
        recyclerView = findViewById(R.id.recycler_view);
        sharePrefData = new SharePrefData(this);
        init();
        isIgnoringBattery();
        permission_check();
        context = this;

    }
     //TODO: WHAT TO DO TOMORROW ?
    //TODO: re-open the app, after reboot, and hide within few seconds.
    //TODO: solve this problem by looking at service class/showApp and hideApp Methods.



    // recyclerViewLayoutManager
    public void init(){
        recyclerViewLayoutManager = new GridLayoutManager(MainActivity.this, 1);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new AppsAdapter(MainActivity.this, new ApkInfoExtractor(MainActivity.this).GetAllInstalledApkInfo());
        recyclerView.setAdapter(adapter);
        recyclerView.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                        for (int i = 0; i < recyclerView.getChildCount(); i++) {
                            View v = recyclerView.getChildAt(i);
                            v.setAlpha(0.0f);
                            v.animate().alpha(1.0f)
                                    .setDuration(1000)
                                    .setStartDelay(i * 50)
                                    .start();
                        }

                        return true;
                    }
                });
    }

    //Check if battery permissions given
    public void isIgnoringBattery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(ApplicationPackageName)) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Disable Battery Optimization ")
                        .setMessage("I STRONGLY RECOMMEND DISABLING THIS.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .show();

            }

        }
    }

    //Check if app usage access is granted
    public boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode;
            assert appOpsManager != null;
            mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    //Show dialog if usage access permission not given
    public void permission_check() {
        //Usage Permission
        if (!isAccessGranted()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Allow to give usage permission ?")
                    .setMessage("The app won't work without giving permission")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_USAGE_ACCESS_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
    }








    //Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case R.id.add:
            ArrayList<String> apps = sharePrefData.getLocked(context);
            if (apps!=null){
                Intent selectTimerIntent = new Intent(MainActivity.this,SelectTimer.class);
                startActivity(selectTimerIntent);
                return(true);
            }else {
                Toast.makeText(context, "Select App", Toast.LENGTH_SHORT).show();
            }
            case R.id.help:
                Intent selectTimerIntent = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(selectTimerIntent);
                return(true);


    }
        return(super.onOptionsItemSelected(item));
    }

}
