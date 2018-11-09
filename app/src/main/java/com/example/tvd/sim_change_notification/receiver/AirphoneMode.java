package com.example.tvd.sim_change_notification.receiver;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.tvd.sim_change_notification.values.FunctionCall;

import org.apache.commons.lang3.StringUtils;

import static android.content.Context.MODE_PRIVATE;
import static com.example.tvd.sim_change_notification.constant.SIM_CARD_CHANGED;
import static com.example.tvd.sim_change_notification.constant.SIM_CARD_NOT_CHANGED;

public class AirphoneMode extends BroadcastReceiver {
    Dialog mDialog;
    Context Notification_context;
    FunctionCall functionCall;
    private static Handler handler = null;
    {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SIM_CARD_CHANGED:
                        //Toast.makeText(Notification_context, "SIM Card Has Changed", Toast.LENGTH_LONG).show();
                        break;
                    case SIM_CARD_NOT_CHANGED:
                        //Toast.makeText(Notification_context, "SIM Card Not Changed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        functionCall = new FunctionCall();
        Notification_context = context;
        functionCall.logStatus("Airphone Mode Receiver Current Time:" + functionCall.currentTime());
        SharedPreferences sharedPreferences = context.getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        String prev_simid = sharedPreferences.getString("SIM_NUMBER","");
        Log.d("debug","PREV_SIM_NUMBER"+prev_simid);
        if (Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 0) {
            Toast.makeText(context, "AIRPLANE MODE Off", Toast.LENGTH_SHORT).show();
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            String simID = tm.getSimSerialNumber();
            if (!StringUtils.equalsIgnoreCase(simID,prev_simid))
               // Toast.makeText(context, "Sim card has changed..", Toast.LENGTH_SHORT).show();
                handler.sendEmptyMessage(SIM_CARD_CHANGED);
            else  handler.sendEmptyMessage(SIM_CARD_NOT_CHANGED);
        }
        else Toast.makeText(context, "AIRPLANE MODE On", Toast.LENGTH_SHORT).show();

        if (wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(false);
            Toast.makeText(Notification_context, "Wifi Disabled", Toast.LENGTH_SHORT).show();
        }

    }

}
