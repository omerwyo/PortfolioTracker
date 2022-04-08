package com.example.portfoliotracker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ErrorBroadcastReceiver extends BroadcastReceiver {

    static final String PROVIDER_NAME = "com.example.portfoliotracker.HistoricalDataProvider";

    private final Handler handler;
    EditText editText;
    public ErrorBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction());
        if (intent.getAction().indexOf("ERROR_")!=-1) {

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