package com.example.getapps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder>{
      Context context1;
     List<String> stringList;
     SharePrefData sharePrefData;
  //  onClickClass onClickClass;
    public AppsAdapter(Context context, List<String> stringList) {
        this.context1 = context;
        this.stringList = stringList;
        //this.onClickClass = onClickClass1;
        sharePrefData = new SharePrefData(context);

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public ImageView imageView;
        public TextView textView_App_Name;
        public Switch switchView;
       // onClickClass onClickClass2;
        public ViewHolder (View itemView){
            super(itemView);
           // this.onClickClass2 =onClickClass3;
            cardView =  itemView.findViewById(R.id.card_view);
            imageView =  itemView.findViewById(R.id.imageview);
            textView_App_Name =  itemView.findViewById(R.id.Apk_Name);
            switchView = itemView.findViewById(R.id.switch_id);
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view2 = LayoutInflater.from(context1).inflate(R.layout.cardview_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(view2);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(context1);
        final String ApplicationPackageName =  stringList.get(position);
        final String ApplicationLabelName = apkInfoExtractor.GetAppName(ApplicationPackageName);
        Drawable drawable = apkInfoExtractor.getAppIconByPackageName(ApplicationPackageName);
        viewHolder.textView_App_Name.setText(ApplicationLabelName);
        viewHolder.imageView.setImageDrawable(drawable);
        viewHolder.switchView.setOnCheckedChangeListener(null);
        viewHolder.cardView.setOnClickListener(null);
         final List<String> ApplicationPackageNameList= new ArrayList<>();
        ApplicationPackageNameList.add(ApplicationPackageName);

        if (checkLockedItem(ApplicationPackageName)) {
            viewHolder.switchView.setChecked(true);
        } else {
            viewHolder.switchView.setChecked(false);
        }
        viewHolder.switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //AppLockLogEvents.logEvents(AppLockConstants.MAIN_SCREEN, "Lock Clicked", "lock_clicked", appInfo.getPackageName());
                    sharePrefData.addLocked(context1,ApplicationPackageName);
                } else {
                    //AppLockLogEvents.logEvents(AppLockConstants.MAIN_SCREEN, "Unlock Clicked", "unlock_clicked", appInfo.getPackageName());
                    sharePrefData.removeLocked(context1,ApplicationPackageName);
                }
            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.switchView.performClick();
            }
        });
    }



    @Override
    public int getItemCount() {
        return stringList.size();
    }


    /*Checks whether a particular app exists in SharedPreferences*/
    public boolean checkLockedItem(String checkApp) {
        boolean check = false;
        List<String> locked = sharePrefData.getLocked(context1);
        if (locked != null) {
            for (String lock : locked) {
                if (lock.equals(checkApp)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

//  public interface onClickClass{
//        void onClickMethod(int position);
//    }
}
