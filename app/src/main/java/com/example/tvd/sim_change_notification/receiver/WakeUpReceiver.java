package com.example.tvd.sim_change_notification.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.tvd.sim_change_notification.service.MyService;

public class WakeUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent tracking_service = new Intent(context, MyService.class);
        context.startService(tracking_service);
    }
}
