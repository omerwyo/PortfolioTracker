package com.example.portfoliotracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

public class DownloadBroadcastReceiver extends BroadcastReceiver {

    static final String PROVIDER_NAME = "com.example.portfoliotracker.HistoricalDataProvider";

    private final Handler handler;

    public DownloadBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // For logging purposes
        Log.v("Intent", intent.getAction());
        if (intent.getAction().contains("ENABLE_CALC_")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    switch (intent.getAction()){
                        case "ENABLE_CALC_1":
                            ((Activity)context).findViewById(R.id.submitButton1).setEnabled(true);
                            break;
                        case "ENABLE_CALC_2":
                            ((Activity)context).findViewById(R.id.submitButton2).setEnabled(true);
                            break;
                        case "ENABLE_CALC_3":
                            ((Activity)context).findViewById(R.id.submitButton3).setEnabled(true);
                            break;
                        case "ENABLE_CALC_4":
                            ((Activity)context).findViewById(R.id.submitButton4).setEnabled(true);
                            break;
                        case "ENABLE_CALC_5":
                            ((Activity)context).findViewById(R.id.submitButton5).setEnabled(true);
                            break;
                    }
                }
            });
        }
    }
}