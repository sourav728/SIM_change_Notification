package com.example.tvd.sim_change_notification;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.tvd.sim_change_notification.receiver.AirphoneMode;
import com.example.tvd.sim_change_notification.service.MyService;
import com.example.tvd.sim_change_notification.values.FunctionCall;

import static com.example.tvd.sim_change_notification.constant.PREF_NAME;

public class MainActivity extends AppCompatActivity {
    String mPhoneNumber="";
    AirphoneMode reciver;
    FunctionCall functionCalls;


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        functionCalls = new FunctionCall();
        TelephonyManager tm = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        String simID = tm.getSimSerialNumber();
        String telNumber = tm.getLine1Number();
        SavePreferences("SIM_NUMBER",simID);

        Toast.makeText(this, "SIM Number"+simID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Phone Number"+telNumber, Toast.LENGTH_SHORT).show();
        Log.d("debug","SIM no "+simID);

        //start_receiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        reciver = new AirphoneMode();
        registerReceiver(reciver, intentFilter);*/

        if (!isMyServiceRunning(MyService.class)) {
            functionCalls.logStatus("MR Service not running");
            Intent service = new Intent(MainActivity.this, MyService.class);
            startService(service);
        } else functionCalls.logStatus("MR Service Running in background");

    }

    @Override
    protected void onStop() {
        super.onStop();
       // unregisterReceiver(reciver);
    }

    private void SavePreferences(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

   /* //code for starting broadcast receiver
    private void start_receiver() {
        functionCalls.logStatus("AirphoneMode Checking...");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AirphoneMode.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            functionCalls.logStatus("AirphoneMode Started..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (10000), pendingIntent);
            }
        } else functionCalls.logStatus("AirphoneMode Already running..");
    }*/

}
