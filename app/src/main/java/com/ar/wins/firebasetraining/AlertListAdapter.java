package com.ar.wins.firebasetraining;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 12/08/2018.
 */

public class AlertListAdapter extends ArrayAdapter<AlertData> {

    List<AlertData> alertList;
    Context context;
    int res;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String fileName = "myFile", itemList[];
    Boolean toggle, defaultValue = false;

    TextView tvName, tvDesc;
    Switch swToggle;

    public AlertListAdapter(Context context, int res, List<AlertData> alertList){
        super(context, res, alertList);
        this.context = context;
        this.res = res;
        this.alertList = alertList;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(res, null, false);

        AlertData alertData = alertList.get(position);

        sharedPreferences = getContext().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        toggle = sharedPreferences.getBoolean("toggle", false);

        tvName = view.findViewById(R.id.tv_name);
        tvDesc = view.findViewById(R.id.tv_desc);
        swToggle = view.findViewById(R.id.sw_toggle);

        swToggle.setChecked(toggle);

        swToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(position == 0) {
                    editor = sharedPreferences.edit();
                    editor.putBoolean("toggleBattery", isChecked);
                    editor.apply();
                    editor.commit();
                }else if(position == 1){
                    editor = sharedPreferences.edit();
                    editor.putBoolean("toggleTime", isChecked);
                    editor.apply();
                    editor.commit();
                }else if(position == 2){
                    editor = sharedPreferences.edit();
                    editor.putBoolean("toggleTemperature", isChecked);
                    editor.apply();
                    editor.commit();
                }
            }
        });
        //holder.swToggle.setChecked();



//        swToggle.setChecked(toggle);
//
//        swToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    editor = sharedPreferences.edit();
//                    editor.putBoolean("toggle", true);
//                    editor.apply();
//                    editor.commit();
//                }else{
//                    editor = sharedPreferences.edit();
//                    editor.putBoolean("toggle", false);
//                    editor.apply();
//                    editor.commit();
//                }
//            }
//        });

        tvName.setText(alertData.getName());
        tvDesc.setText(alertData.getDesc());

        return view;
    }

}
