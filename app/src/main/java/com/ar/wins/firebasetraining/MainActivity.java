package com.ar.wins.firebasetraining;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    int level, addCount = 0;
    String batteryThreshold, timeHour, timeMinute, temperatureThreshold, fileName = "myFile", defaultValue = "0";
    boolean toggleBattery, toggleTime, toggleTemperature;
    boolean wifiStatus, ledStatus;

    Context context = this;
    Button buttonEdit, buttonConnect;
    Switch swToggle;

    BottomNavigationView bottomNavigationView;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    WifiManager wifiManager;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new AsyncHttpClient();

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID =  "\"" + "Esp8266TestNet" + "\"";
        wifiConfiguration.preSharedKey = "\"" + "Esp8266Test" + "\"";

        wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(wifiConfiguration);

        int netId = wifiManager.addNetwork(wifiConfiguration);

        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true); // Seconds parm instructs to  disableOtherNetworks
        wifiManager.reconnect(); // Now we will connect to the only available enabled network.

        loadFragment(new AlertListFragment());

        bottomNavigationView = findViewById(R.id.botnav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        batteryThreshold = sharedPreferences.getString("batteryThreshold", defaultValue);
        timeHour = sharedPreferences.getString("timeHour", defaultValue);
        timeMinute = sharedPreferences.getString("timeMinute", defaultValue);
        temperatureThreshold = sharedPreferences.getString("temperatureThreshold", defaultValue);

        toggleBattery = sharedPreferences.getBoolean("toggleBattery", false);
        toggleTime = sharedPreferences.getBoolean("toggleTime", false);
        toggleTemperature = sharedPreferences.getBoolean("toggleTemperature", false);

//        Bundle addStatus = getIntent().getExtras();

        buttonEdit = findViewById(R.id.bt_edit);
        buttonConnect = findViewById(R.id.bt_connect);

        Context mContext = getApplicationContext();

        // Initialize a new IntentFilter instance
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        // Register the broadcast receiver
        mContext.registerReceiver(mBroadcastReceiver, iFilter);
//        Intent intent = new Intent();
//        intent.putExtra("level", level);

        buttonEdit.setOnClickListener(MainActivity.this);
        buttonConnect.setOnClickListener(this);


    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, fragment)
                    .commit();
        }
        return false;
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.bt_edit:
                Intent intent = new Intent(this, AddAlertActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_connect:
                if(wifiStatus == false) {
                    wifiManager.setWifiEnabled(true);
                    buttonConnect.setText("Disconnect");
                }else{
                    wifiManager.setWifiEnabled(false);
                    buttonConnect.setText("Connect");
                }
                break;
        }
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            batteryThreshold = sharedPreferences.getString("batteryThreshold", defaultValue);
            // Get the battery percentage
            level = intent.getIntExtra("level", 0);

            if(level == Integer.valueOf(batteryThreshold)){
                if(ledStatus == false){
                    createNotification(null);
                    Uri uri = Uri.parse("http://192.168.4.1/led/on");

                    client.get(String.valueOf(uri), new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            // called before request is started
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                            // called when response HTTP status is "200 OK"
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                            // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        }

                        @Override
                        public void onRetry(int retryNo) {
                            // called when request is retried
                        }
                    });

//                    Intent bIntent = new Intent();
//                    bIntent.setAction(Intent.ACTION_VIEW);
//                    bIntent.addCategory(Intent.CATEGORY_BROWSABLE);
//                    bIntent.setData(Uri.parse(String.valueOf(uri)));
//                    startActivity(bIntent);
                }
                ledStatus = true;
                Toast.makeText(MainActivity.this, "ON", Toast.LENGTH_SHORT);
                buttonEdit.setText(Boolean.toString(ledStatus));

            }
            if(level > Integer.valueOf(batteryThreshold)){
                if(ledStatus == true){
                    Uri uri1 = Uri.parse("http://192.168.4.1/led/off"); // missing 'http://' will cause crashed

//                    Intent bIntent = new Intent();
//                    bIntent.setAction(Intent.ACTION_VIEW);
//                    bIntent.addCategory(Intent.CATEGORY_BROWSABLE);
//                    bIntent.setData(Uri.parse(String.valueOf(uri1)));
//                    startActivity(bIntent);
                }
                ledStatus = false;
                Toast.makeText(MainActivity.this, "OFF", Toast.LENGTH_SHORT);
                buttonEdit.setText(Boolean.toString(ledStatus));



            }
        }
    };

    public void createNotification(View view) {
        batteryThreshold = sharedPreferences.getString("batteryThreshold", defaultValue);
        // define sound URI, the sound to be played when there's a notification
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //String soundPath = "android.resource://" + view.getContext().getPackageName() + "/" + R.raw.mp3 ;

        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, NotificationReceiverActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Ticker")
                .setSmallIcon(R.drawable.ic_battery_charging_full_black_24dp)
                .setContentTitle("Battery is at " + level + "%")
                .setContentText("Your battery is at " + batteryThreshold + "%, consider to plug out your charger")
                //.setContentIntent(pi)
                //.addAction(R.drawable.ic_snooze_black_24dp, "Snooze", pi)
                .setAutoCancel(true)
                .setSound(soundUri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navigation_alert:
                loadFragment(new AlertListFragment());
                break;
            case R.id.navigation_status:
                loadFragment(new StatusFragment());
                break;
        }
        return true;
    }

}
