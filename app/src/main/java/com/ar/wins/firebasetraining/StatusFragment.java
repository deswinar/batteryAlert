package com.ar.wins.firebasetraining;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by USER on 27/08/2018.
 */

public class StatusFragment extends Fragment {

    String technology;
    boolean isPresent;
    int plugged, scale, health, status, rawlevel, voltage, temperature;

    TextView tvStatus, tvHealth, tvTemperature;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        Context mContext = getActivity().getApplicationContext();
        // Initialize a new IntentFilter instance
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // Register the broadcast receiver
        mContext.registerReceiver(mBroadcastReceiver, iFilter);

        tvStatus = view.findViewById(R.id.tv_battery_status);
        tvHealth = view.findViewById(R.id.tv_battery_health);
        tvTemperature = view.findViewById(R.id.tv_battery_temperature);

        tvStatus.setText(String.valueOf(status));
        tvHealth.setText(String.valueOf(health));
        tvTemperature.setText(String.valueOf(temperature));

        return  view;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String batteryStatus[] = {"Unknown", "Charging", "Discharging", "Not Charging", "Full"};
            String batteryHealth[] = {"Unknown","Good", "Overheated", "Dead", "Overvoltage", "Failed"};
            // Get the battery percentage
            isPresent = intent.getBooleanExtra("present", false);
            technology = intent.getStringExtra("technology");
            plugged = intent.getIntExtra("plugged", -1);
            scale = intent.getIntExtra("scale", -1);
            health = intent.getIntExtra("health", 0);
            status = intent.getIntExtra("status", 0);
            rawlevel = intent.getIntExtra("level", -1);
            voltage = intent.getIntExtra("voltage", 0);
            temperature = intent.getIntExtra("temperature", 0);

            tvStatus.setText(batteryStatus[status-1]);
            tvHealth.setText(batteryHealth[health-1]);
            tvTemperature.setText(String.valueOf(temperature));
        }
    };
}