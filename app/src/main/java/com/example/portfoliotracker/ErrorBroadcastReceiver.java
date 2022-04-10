package com.example.portfoliotracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;

/**
 *  Receives and handles broadcast intents sent
 */
public class ErrorBroadcastReceiver extends BroadcastReceiver {
    static final String PROVIDER_NAME = "com.example.portfoliotracker.HistoricalDataProvider";

    private final Handler handler;
    EditText editText;

    public ErrorBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    /**
     * Method is called when the ErrorBroadcastReceiver is receiving an Intent broadcast.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        // For logging purposes
        Log.v("Intent", intent.getAction());
        if (intent.getAction().contains("ERROR_")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    switch (intent.getAction()){
                        case "ERROR_1":
                            editText = ((Activity)context).findViewById(R.id.editTicker1);
                            editText.setError("Ticker does not exist");
                            break;
                        case "ERROR_2":
                            editText = ((Activity)context).findViewById(R.id.editTicker2);
                            editText.setError("Ticker does not exist");
                            break;
                        case "ERROR_3":
                            editText = ((Activity)context).findViewById(R.id.editTicker3);
                            editText.setError("Ticker does not exist");
                            break;
                        case "ERROR_4":
                            editText = ((Activity)context).findViewById(R.id.editTicker4);
                            editText.setError("Ticker does not exist");
                            break;
                        case "ERROR_5":
                            editText = ((Activity)context).findViewById(R.id.editTicker5);
                            editText.setError("Ticker does not exist");
                            break;
                    }
                }
            });
        }
    }
}