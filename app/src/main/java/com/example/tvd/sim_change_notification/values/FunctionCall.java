package com.example.tvd.sim_change_notification.values;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FunctionCall {
    public void logStatus(String message) {
        Log.d("debug", message);
    }
    public String currentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.US);
        return sdf.format(new Date());
    }
}
