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
        // For logging purposes
        Log.v("Intent", intent.getAction());
        if (intent.getAction().contains("DOWNLOAD_COMPLETE")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/history");
                    CharSequence ticker="";
                    TextView resulta = (TextView) ((Activity)context).findViewById(R.id.annualizedreturn1);
                    TextView resultb = (TextView) ((Activity)context).findViewById(R.id.annualizedvolatility1);
                    switch (intent.getAction()){
                        case "DOWNLOAD_COMPLETE_1":
                            resulta = (TextView) ((Activity)context).findViewById(R.id.annualizedreturn1);
                            resultb = (TextView) ((Activity)context).findViewById(R.id.annualizedvolatility1);
                            ticker = ((EditText) ((Activity)context).findViewById(R.id.editTicker1)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_2":
                            resulta = (TextView) ((Activity)context).findViewById(R.id.annualizedreturn2);
                            resultb = (TextView) ((Activity)context).findViewById(R.id.annualizedvolatility2);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker2)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_3":
                            resulta = (TextView) ((Activity)context).findViewById(R.id.annualizedreturn3);
                            resultb = (TextView) ((Activity)context).findViewById(R.id.annualizedvolatility3);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker3)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_4":
                            resulta = (TextView) ((Activity)context).findViewById(R.id.annualizedreturn4);
                            resultb = (TextView) ((Activity)context).findViewById(R.id.annualizedvolatility4);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker4)).getText().toString();
                            break;
                        case "DOWNLOAD_COMPLETE_5":
                            resulta = (TextView) ((Activity)context).findViewById(R.id.annualizedreturn5);
                            resultb = (TextView) ((Activity)context).findViewById(R.id.annualizedvolatility5);
                            ticker = ((TextView) ((Activity)context).findViewById(R.id.editTicker5)).getText().toString();
                            break;
                    }
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
                        resulta.setText("N/A");
                        resultb.setText("N/A");
                    }

                    // Calculate the 2 metrics
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

                    // Set them in %
                    resulta.setText(String.format("%.1f %%", annualisedReturns * 100));
                    resultb.setText(String.format("%.1f %%", sd * 100));
                }
            });
        }
    }
}