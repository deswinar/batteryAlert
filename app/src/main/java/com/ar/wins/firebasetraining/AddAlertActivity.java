package com.ar.wins.firebasetraining;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by USER on 14/08/2018.
 */

public class AddAlertActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener{

    RelativeLayout rlBattery, rlTime, rlTemperature;
    TextView tvBattery, tvTime, tvTemperature;
    Button btSave, btCancel;
    SeekBar seekBattery, seekTemperature;
    TimePicker tpTime;

    RelativeLayout.LayoutParams lpBattery, lpTime, lpTemperature;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String fileName = "myFile", defaultValue = "0", batteryThreshold, timeHour, timeMinute, temperatureThreshold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alert);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        batteryThreshold = sharedPreferences.getString("batteryThreshold", defaultValue);
        timeHour = sharedPreferences.getString("timeHour", defaultValue);
        timeMinute = sharedPreferences.getString("timeMinute", defaultValue);
        temperatureThreshold = sharedPreferences.getString("temperatureThreshold", defaultValue);

        rlBattery = findViewById(R.id.rl_battery_alert);
        rlTime = findViewById(R.id.rl_time_alert);
        rlTemperature = findViewById(R.id.rl_temperature_alert);

        tvBattery = findViewById(R.id.tv_battery_alert);
        tvTime = findViewById(R.id.tv_time_alert);
        tvTemperature = findViewById(R.id.tv_temperature_alert);

        btSave = findViewById(R.id.bt_save);
        btCancel = findViewById(R.id.bt_cancel);

        lpTime = (RelativeLayout.LayoutParams) rlTime.getLayoutParams();
        lpTemperature = (RelativeLayout.LayoutParams) rlTemperature.getLayoutParams();

        seekBattery = findViewById(R.id.seek_battery);
        tpTime = findViewById(R.id.tp_time);
        tpTime.setIs24HourView(true);
        seekTemperature = findViewById(R.id.seek_temperature);

        seekBattery.setVisibility(View.GONE);
        seekBattery.setProgress(Integer.valueOf(batteryThreshold));
        tpTime.setVisibility(View.GONE);

        seekTemperature.setVisibility(View.GONE);
        seekTemperature.setProgress(Integer.valueOf(temperatureThreshold));

        seekBattery.setOnSeekBarChangeListener(this);
        seekTemperature.setOnSeekBarChangeListener(this);

        rlBattery.setOnClickListener(this);
        rlTime.setOnClickListener(this);
        rlTemperature.setOnClickListener(this);btSave.setOnClickListener(this);

        btCancel.setOnClickListener(this);

        tvBattery.setText(seekBattery.getProgress() +   "%");
        tvTime.setText(tpTime.getHour() + ":" + tpTime.getMinute());
        tvTemperature.setText(seekTemperature.getProgress() +   "°C");

        tpTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {
                tvTime.setText(hour + ":" + minute);
            }
        });

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
        // TODO Auto-generated method stub
        switch (seekBar.getId()){
            case R.id.seek_battery:
                tvBattery.setText(String.valueOf(progress) + "%");
                break;
            case R.id.seek_temperature:
                tvTemperature.setText(String.valueOf(progress) + "°C");
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.rl_battery_alert:
                seekBattery.setVisibility(seekBattery.isShown()? View.GONE : View.VISIBLE);
                lpTime.addRule(RelativeLayout.BELOW, seekBattery.isShown()? seekBattery.getId() : rlBattery.getId());
                break;
            case R.id.rl_time_alert:
                tpTime.setVisibility(tpTime.isShown()? View.GONE : View.VISIBLE);
                lpTemperature.addRule(RelativeLayout.BELOW, tpTime.isShown()? tpTime.getId() : rlTime.getId());
                break;
            case R.id.rl_temperature_alert:
                seekTemperature.setVisibility(seekTemperature.isShown()? View.GONE : View.VISIBLE);
                break;
            case R.id.bt_save:
                editor = sharedPreferences.edit();
                editor.putString("batteryThreshold", String.valueOf(seekBattery.getProgress()));
                editor.putString("timeHour", String.valueOf(tpTime.getHour()));
                editor.putString("timeMinute", String.valueOf(tpTime.getMinute()));
                editor.putString("temperatureThreshold", String.valueOf(seekTemperature.getProgress()));
                editor.apply();
                editor.commit();
                Intent intent1 = new Intent(this, MainActivity.class);
//                intent1.putExtra("seekBattery", seekBattery.getProgress());
//                intent1.putExtra("tpHour", tpTime.getHour());
//                intent1.putExtra("tpMinute", tpTime.getMinute());
//                intent1.putExtra("seekTemperature", seekTemperature.getProgress());
                startActivity(intent1);
                finish();
                break;
            case R.id.bt_cancel:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }
}
