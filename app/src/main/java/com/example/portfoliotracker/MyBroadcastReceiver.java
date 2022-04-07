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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class MyBroadcastReceiver extends BroadcastReceiver {

    static final String PROVIDER_NAME = "com.example.portfoliotracker.HistoricalDataProvider";

    private final Handler handler;

    public MyBroadcastReceiver(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(intent.getAction());
        if (intent.getAction().indexOf("DOWNLOAD_COMPLETE")!=-1) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/history");
                    CharSequence ticker="";
                    TextView result = (TextView) ((Activity)context).findViewById(R.id.textView7);
                    switch (intent.getAction()){
                        case "DOWNLOAD_COMPLETE_1":
                            result = (TextView) ((Activity)context).findViewById(R.id.textView7);
                            ticker = ((EditText) ((Activity)context).findViewById(R.id.editTicker1)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_2":
                            result = (TextView) ((Activity)context).findViewById(R.id.textView8);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker2)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_3":
                            result = (TextView) ((Activity)context).findViewById(R.id.textView9);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker3)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_4":
                            result = (TextView) ((Activity)context).findViewById(R.id.textView10);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker4)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_5":
                            result = (TextView) ((Activity)context).findViewById(R.id.textView11);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker5)).getText().toString();
                            break;
                    }
                    result.setText("Calculating...");
                    double sumReturns = 0;
                    double period = 0;
                    ArrayList<Double> dailyReturnData = new ArrayList<>();
                    Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, String.format("ticker =  '%s'",ticker.toString()), null, null);

                    if (cursor.moveToFirst()) {

                        double close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                        double previousDay = close;
                        while (!cursor.isAfterLast()) {
                            period+=1;
                            previousDay = close;
                            close = cursor.getDouble(cursor.getColumnIndexOrThrow("close"));
                            sumReturns += (close - previousDay)/previousDay;
                            dailyReturnData.add((close - previousDay)/previousDay);
                            cursor.moveToNext();
                        }
                    }
                    else {
                        result.setText("No Records Found");
                    }
                    double dailyReturnMean = sumReturns/period;
                    double summationNumerator = 0;
                    for (int i = 0; i < dailyReturnData.size(); i++) {
                        double dailyReturn = dailyReturnData.get(i);
                        double diff = Math.pow(dailyReturn-dailyReturnMean,2);
                        summationNumerator += diff;
                    }
                    double sd = Math.pow(250,0.5)*Math.pow(summationNumerator/dailyReturnData.size(),0.5);

                    double annualisedReturns = sumReturns/period * 250;
                    Log.v("Period",String.valueOf(period));
                    Log.v("annualisedReturns",String.valueOf(annualisedReturns));
                    result.setText(String.format("%.2f, SD = %.2f", annualisedReturns,sd));
                }
            });
        }
    }
}