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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private final Handler handler;

    public MyBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("DOWNLOAD_COMPLETE")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Uri CONTENT_URI = Uri.parse("content://com.example.serviceexample.HistoricalDataProvider/history");
                    TextView result = (TextView) ((Activity)context).findViewById(R.id.textview_result);
                    result.setText("Calculating...");
                    double sum_price = 0.0;
                    double sum_volume = 0.0;
                    Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, null, null, null);
                    if (cursor.moveToFirst()) {
                        double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                        double volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
                        sum_price += close * volume;
                        sum_volume += volume;
                        while (!cursor.isAfterLast()) {
                            int id = cursor.getColumnIndex("id");
                            close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            volume = cursor.getDouble(cursor.getColumnIndexOrThrow("volume"));
                            sum_price += close * volume;
                            sum_volume += volume;
                            cursor.moveToNext();
                            Log.v("data", close + "");
                        }
                    }
                    else {
                        result.setText("No Records Found");
                    }

                    double vwap = sum_price / sum_volume;
                    result.setText(String.format("%.2f", vwap));

                }
            });
        }
    }
}