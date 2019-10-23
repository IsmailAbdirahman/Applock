package com.example.getapps;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectTimer extends AppCompatActivity {

    private static final String TAG = "SelectTimer";
    Button fiveMinBtn ;
    Button thirtyminBtn ;
    Button oneHourBtn ;
    Button oneAndHalfBtn ;
    Button twoHourBtn ;
    Button threeHourBtn ;
    Button startBtn ;
    long theTime;
    SharePrefData sharePrefData ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_timer);
        fiveMinBtn = findViewById(R.id.fiveMinBtn);
        thirtyminBtn = findViewById(R.id.thirtyminBtn);
        oneHourBtn = findViewById(R.id.oneHourBtn);
        oneAndHalfBtn = findViewById(R.id.oneAndHalfBtn);
        twoHourBtn = findViewById(R.id.twoHourBtn);
        threeHourBtn = findViewById(R.id.threeHourBtn);
        startBtn = findViewById(R.id.startBtn);
        sharePrefData = new SharePrefData(this);

    }

    public void clearTimer(){
        SharedPreferences preferences = getSharedPreferences("SELECTED_TIME", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }

    public void fiveMinBtn(View view){
        clearTimer();
        theTime =300000;
        sharePrefData.saveTimer(theTime,"theTime");
        if (fiveMinBtn.isEnabled()){
            fiveMinBtn.setEnabled(false);
            thirtyminBtn.setEnabled(true);
            oneHourBtn.setEnabled(true);
            oneAndHalfBtn.setEnabled(true);
            twoHourBtn.setEnabled(true);
            threeHourBtn.setEnabled(true);
        }
    }

    public void thirtyminBtn(View view){
        clearTimer();
        theTime = 1800000 ;
        sharePrefData.saveTimer(theTime,"theTime");
        if (thirtyminBtn.isEnabled()){
            thirtyminBtn.setEnabled(false);
            fiveMinBtn.setEnabled(true);
            oneHourBtn.setEnabled(true);
            oneAndHalfBtn.setEnabled(true);
            twoHourBtn.setEnabled(true);
            threeHourBtn.setEnabled(true);
        }
    }

    public void oneHourBtn(View view){
        clearTimer();
        theTime =3600000;
        sharePrefData.saveTimer(theTime,"theTime");
        if (oneHourBtn.isEnabled()){
            oneHourBtn.setEnabled(false);
            fiveMinBtn.setEnabled(true);
            thirtyminBtn.setEnabled(true);
            oneAndHalfBtn.setEnabled(true);
            twoHourBtn.setEnabled(true);
            threeHourBtn.setEnabled(true);
        }
    }

    public void oneAndHalfBtn(View view){
        clearTimer();
        theTime =5400000 ;
        sharePrefData.saveTimer(theTime,"theTime");
        if (oneAndHalfBtn.isEnabled()){
            oneAndHalfBtn.setEnabled(false);
            fiveMinBtn.setEnabled(true);
            thirtyminBtn.setEnabled(true);
            oneHourBtn.setEnabled(true);
            twoHourBtn.setEnabled(true);
            threeHourBtn.setEnabled(true);
        }
    }

    public void twoHourBtn(View view){
        clearTimer();
        theTime =7200000 ;
        sharePrefData.saveTimer(theTime,"theTime");
        if (twoHourBtn.isEnabled()){
            twoHourBtn.setEnabled(false);
            oneAndHalfBtn.setEnabled(true);
            fiveMinBtn.setEnabled(true);
            thirtyminBtn.setEnabled(true);
            oneHourBtn.setEnabled(true);
            threeHourBtn.setEnabled(true);
        }
    }

    public void threeHourBtn(View view){
        clearTimer();
        theTime =10800000 ;
        sharePrefData.saveTimer(theTime,"theTime");
        if (threeHourBtn.isEnabled()){
            threeHourBtn.setEnabled(false);
            twoHourBtn.setEnabled(true);
            oneAndHalfBtn.setEnabled(true);
            fiveMinBtn.setEnabled(true);
            thirtyminBtn.setEnabled(true);
            oneHourBtn.setEnabled(true);
        }
    }

    public  void showApp(){
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this,MainActivity.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,  PackageManager.DONT_KILL_APP);
    }


    public void hideApp(){
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this,MainActivity.class);
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,  PackageManager.DONT_KILL_APP);
    }


    public void startBtn(View view){
        startService(new Intent(getApplicationContext(),MyService.class));
        //hideApp();

        startBtn.setEnabled(false);
        oneAndHalfBtn.setEnabled(false);
        fiveMinBtn.setEnabled(false);
        thirtyminBtn.setEnabled(false);
        oneHourBtn.setEnabled(false);
        twoHourBtn.setEnabled(false);
        threeHourBtn.setEnabled(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startBtn.setEnabled(false);
        oneAndHalfBtn.setEnabled(false);
        fiveMinBtn.setEnabled(false);
        thirtyminBtn.setEnabled(false);
        oneHourBtn.setEnabled(false);
        twoHourBtn.setEnabled(false);
        threeHourBtn.setEnabled(false);
    }
}
