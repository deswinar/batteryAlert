package com.ar.wins.firebasetraining;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by USER on 13/08/2018.
 */

public class AlertListFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    String batteryThreshold, timeHour, timeMinute, temperatureThreshold, fileName = "myFile", defaultValue = "0";
    int batteryLevel, timeLevel, temperatureLevel, addCount, addStatus;
    boolean toggleBattery, toggleTime, toggleTemperature;

    RelativeLayout rlBattery, rlTime, rlTemperature;
    TextView tvNameBattery, tvNameTime, tvNameTemperature, tvDescBattery, tvDescTime, tvDescTemperature;
    Switch swBattery, swTime, swTemperature;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        rlBattery = view.findViewById(R.id.rl_battery_alert);
        rlTime = view.findViewById(R.id.rl_time_alert);
        rlTemperature = view.findViewById(R.id.rl_temperature_alert);

//        rlBattery.setOnClickListener(this);
//        rlTime.setOnClickListener(this);
//        rlTemperature.setOnClickListener(this);

        tvNameBattery = view.findViewById(R.id.tv_name_battery);
        tvNameTime = view.findViewById(R.id.tv_name_time);
        tvNameTemperature = view.findViewById(R.id.tv_name_temperature);
        tvDescBattery = view.findViewById(R.id.tv_desc_battery);
        tvDescTime = view.findViewById(R.id.tv_desc_time);
        tvDescTemperature = view.findViewById(R.id.tv_desc_temperature);

        swBattery = view.findViewById(R.id.sw_battery);
        swTime = view.findViewById(R.id.sw_time);
        swTemperature = view.findViewById(R.id.sw_temperature);

        swBattery.setOnCheckedChangeListener(this);
        swTime.setOnCheckedChangeListener(this);
        swTemperature.setOnCheckedChangeListener(this);

        // Save data in Shared Preferences
        sharedPreferences = getActivity().getSharedPreferences(fileName, Context.MODE_PRIVATE);
        batteryThreshold = sharedPreferences.getString("batteryThreshold", defaultValue);
        toggleBattery = sharedPreferences.getBoolean("toggleBattery", false);

        timeHour = sharedPreferences.getString("timeHour", defaultValue);
        timeMinute = sharedPreferences.getString("timeMinute", defaultValue);
        toggleTime = sharedPreferences.getBoolean("toggleTime", false);

        temperatureThreshold = sharedPreferences.getString("temperatureThreshold", defaultValue);
        toggleTemperature = sharedPreferences.getBoolean("toggleTemperature", false);

        swBattery.setChecked(toggleBattery);
        swTime.setChecked(toggleTime);
        swTemperature.setChecked(toggleTemperature);

        tvDescBattery.setText(batteryThreshold + "%");
        tvDescTime.setText(timeHour + ":" + timeMinute);
        tvDescTemperature.setText(temperatureThreshold + "°C");

        return view;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String charging_status = "", battery_condition = "", power_source = "Unplugged";
            // Get the battery percentage
            batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);

        }
    };

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), AddAlertActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()){
            case R.id.sw_battery:
                editor = sharedPreferences.edit();
                editor.putBoolean("toggleBattery", checked);
                editor.apply();
                editor.commit();
                break;
            case R.id.sw_time:
                editor = sharedPreferences.edit();
                editor.putBoolean("toggleTime", checked);
                editor.apply();
                editor.commit();
                break;
            case R.id.sw_temperature:
                editor = sharedPreferences.edit();
                editor.putBoolean("toggleTemperature", checked);
                editor.apply();
                editor.commit();
                break;
        }
    }
}
