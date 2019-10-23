package com.example.getapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

public class SharePrefData  {
    Context context;
    long getTimer;
    public static final String LOCKED_APP = "locked_app";


    public SharePrefData(Context context) {
        this.context = context;
    }

    //     THis method saves the selected package names into Shared Pref.

    public void saveLocked(Context context, List<String> lockedApp) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences("package_names", Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonLockedApp = gson.toJson(lockedApp);
        editor.putString(LOCKED_APP, jsonLockedApp);
        editor.apply();
    }

    public ArrayList<String> getLocked(Context context) {
        SharedPreferences settings;
        List<String> locked;

        settings = context.getSharedPreferences("package_names", Context.MODE_PRIVATE);

        if (settings.contains(LOCKED_APP)) {
            String jsonLocked = settings.getString(LOCKED_APP, null);
            Gson gson = new Gson();
            String[] lockedItems = gson.fromJson(jsonLocked,
                    String[].class);

            locked = Arrays.asList(lockedItems);
            locked = new ArrayList<String>(locked);
        } else
            return null;
        return (ArrayList<String>) locked;
    }


    public void addLocked(Context context, String app) {
        List<String> lockedApp = getLocked(context);
        if (lockedApp == null)
            lockedApp = new ArrayList<String>();

        lockedApp.add(app);
        saveLocked(context, lockedApp);
    }


    public void removeLocked(Context context, String app) {
        ArrayList<String> locked = getLocked(context);
        if (locked != null) {
            locked.remove(app);
            saveLocked(context, locked);
        }
    }


    //   This method is used to save the time in shared pref.
    public void saveTimer(long timerValue, String key){
        SharedPreferences settings = context.getSharedPreferences("SELECTED_TIME", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, (int) timerValue);
        editor.apply();
        Log.e(TAG, "saveTimer: Saved" );

    }


    //  get Timer from shared pref
    public long getTimer(String key){
        SharedPreferences settings = context.getSharedPreferences("SELECTED_TIME", MODE_PRIVATE);
        return  getTimer =  settings.getInt(key,0);
    }

//
//        public void clearPackageName(){
//        SharedPreferences preferences = context.getSharedPreferences("package_names", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();
//    }
//
//    public void clearTimer(){
//        SharedPreferences preferences = context.getSharedPreferences("SELECTED_TIME", MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();
//    }









}
