package com.example.tvd.sim_change_notification.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.tvd.sim_change_notification.receiver.AirphoneMode;
import com.example.tvd.sim_change_notification.values.FunctionCall;

public class MyService extends Service {
    FunctionCall functionCalls;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        functionCalls = new FunctionCall();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        start_receiver();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop_receiver();
    }

    @SuppressLint("ShortAlarm")
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
    }

    private void stop_receiver() {
        functionCalls.logStatus("AirphoneMode Checking..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AirphoneMode.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning) {
            functionCalls.logStatus("AirphoneMode Stopping..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        } else functionCalls.logStatus("AirphoneMode Not yet Started..");
    }
}
