package com.example.portfoliotracker;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


/**
 * A {@code MainActivity} is the activity that first loads when the user launches the application.
 *
 * {@link #onCreate(Bundle)} which fires when the system creates your activity.
 * {@link #onStart()} which fires when the activity enters the Started state, and the activity becomes visible to the user.
 * {@link #onStop()} which fires when the activity is no longer visible to the user.
 */
public class MainActivity extends AppCompatActivity {
    private EditText textView1,textView2,textView3,textView4,textView5;
    private TextView result1a,result1b, result2a, result2b, result3a, result3b, result4a, result4b, result5a, result5b;
    private BroadcastReceiver myBroadcastReceiver1,myBroadcastReceiver2,myBroadcastReceiver3,myBroadcastReceiver4,myBroadcastReceiver5;

    /**
     * Array of CharSequence to keep track of the 5 stock tickers.
     */
    CharSequence[] listOfStocksChosen = new CharSequence[] {
            "", "", "", "", "" };

    /**
     * Automatically called when the activity is starting.
     *
     * @param savedInstanceState contains the data it most recently supplied in onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.editTicker1);
        textView2 = findViewById(R.id.editTicker2);
        textView3 = findViewById(R.id.editTicker3);
        textView4 = findViewById(R.id.editTicker4);
        textView5 = findViewById(R.id.editTicker5);

        result1a = findViewById(R.id.annualizedreturn1);
        result1b = findViewById(R.id.annualizedvolatility1);
        result2a = findViewById(R.id.annualizedreturn2);
        result2b = findViewById(R.id.annualizedvolatility2);
        result3a = findViewById(R.id.annualizedreturn3);
        result3b = findViewById(R.id.annualizedvolatility3);
        result4a = findViewById(R.id.annualizedreturn4);
        result4b = findViewById(R.id.annualizedvolatility4);
        result5a = findViewById(R.id.annualizedreturn5);
        result5b = findViewById(R.id.annualizedvolatility5);

        Button downloadButton1 = findViewById(R.id.downloadButton1);
        Button downloadButton2 = findViewById(R.id.downloadButton2);
        Button downloadButton3 = findViewById(R.id.downloadButton3);
        Button downloadButton4 = findViewById(R.id.downloadButton4);
        Button downloadButton5 = findViewById(R.id.downloadButton5);

        Button submitButton1 = findViewById(R.id.submitButton1);
        Button submitButton2 = findViewById(R.id.submitButton2);
        Button submitButton3 = findViewById(R.id.submitButton3);
        Button submitButton4 = findViewById(R.id.submitButton4);
        Button submitButton5 = findViewById(R.id.submitButton5);

        Button download = findViewById(R.id.downloadAll);
        Button calcAll = findViewById(R.id.calcAll);

        myBroadcastReceiver1 = new MyBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(myBroadcastReceiver1, new IntentFilter("DOWNLOAD_COMPLETE_1"));

        myBroadcastReceiver2 = new MyBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(myBroadcastReceiver2, new IntentFilter("DOWNLOAD_COMPLETE_2"));

        myBroadcastReceiver3 = new MyBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(myBroadcastReceiver3, new IntentFilter("DOWNLOAD_COMPLETE_3"));

        myBroadcastReceiver4 = new MyBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(myBroadcastReceiver4, new IntentFilter("DOWNLOAD_COMPLETE_4"));

        myBroadcastReceiver5 = new MyBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(myBroadcastReceiver5, new IntentFilter("DOWNLOAD_COMPLETE_5"));

        BroadcastReceiver downloadBroadcastReceiver1 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver1, new IntentFilter("ENABLE_CALC_1"));

        BroadcastReceiver downloadBroadcastReceiver2 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver2, new IntentFilter("ENABLE_CALC_2"));

        BroadcastReceiver downloadBroadcastReceiver3 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver3, new IntentFilter("ENABLE_CALC_3"));

        BroadcastReceiver downloadBroadcastReceiver4 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver4, new IntentFilter("ENABLE_CALC_4"));

        BroadcastReceiver downloadBroadcastReceiver5 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver5, new IntentFilter("ENABLE_CALC_5"));

        BroadcastReceiver errorBroadcastReceiver1 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver1, new IntentFilter("ERROR_1"));

        BroadcastReceiver errorBroadcastReceiver2 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver2, new IntentFilter("ERROR_2"));

        BroadcastReceiver errorBroadcastReceiver3 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver3, new IntentFilter("ERROR_3"));

        BroadcastReceiver errorBroadcastReceiver4 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver4, new IntentFilter("ERROR_4"));

        BroadcastReceiver errorBroadcastReceiver5 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver5, new IntentFilter("ERROR_5"));

        // OnClickEvent for downloadButton1
        downloadButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] tempArray = new CharSequence[] {
                        "", "", "", "", "" };
                // If the textbox for tickers input is empty
                if (textView1.getText().toString().equals("")){
                    textView1.setError("Ticker cannot be empty");
                }
                else{
                    tempArray[0]=textView1.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    Log.v("ClickListener", String.valueOf(tempArray[0]));
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
            }
        });

        // OnClickEvent for downloadButton2
        downloadButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] tempArray = new CharSequence[] {
                        "", "", "", "", "" };
                // If the textbox for tickers input is empty
                if (textView2.getText().toString().equals("")){
                    textView2.setError("Ticker cannot be empty");
                }
                else{
                    tempArray[1]=textView2.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    Log.v("ClickListener", String.valueOf(tempArray[1]));
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
            }
        });

        // OnClickEvent for downloadButton3
        downloadButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] tempArray = new CharSequence[] {
                        "", "", "", "", "" };
                // If the textbox for tickers input is empty
                if (textView3.getText().toString().equals("")){
                    textView3.setError("Ticker cannot be empty");
                }
                else{
                    tempArray[2]=textView3.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    Log.v("ClickListener", String.valueOf(tempArray[2]));
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
            }
        });

        // OnClickEvent for downloadButton4
        downloadButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] tempArray = new CharSequence[] {
                        "", "", "", "", "" };
                // If the textbox for tickers input is empty
                if (textView4.getText().toString().equals("")){
                    textView4.setError("Ticker cannot be empty");
                }
                else{
                    tempArray[3]=textView4.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    Log.v("ClickListener", String.valueOf(tempArray[3]));
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
            }
        });

        // OnClickEvent for downloadButton5
        downloadButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] tempArray = new CharSequence[] {
                        "", "", "", "", "" };
                // If the textbox for tickers input is empty
                if (textView5.getText().toString().equals("")){
                    textView5.setError("Ticker cannot be empty");
                }
                else{
                    tempArray[4]=textView5.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    Log.v("ClickListener", String.valueOf(tempArray[4]));
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
            }
        });


        // OnClickEvent for downloadAll button
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!textView1.getText().toString().equals("")){
                    listOfStocksChosen[0] = textView1.getText().toString();
                }
                if (!textView2.getText().toString().equals("")){
                    listOfStocksChosen[1] = textView2.getText().toString();
                }
                if (!textView3.getText().toString().equals("")){
                    listOfStocksChosen[2] = textView3.getText().toString();

                }
                if (!textView4.getText().toString().equals("")){
                    listOfStocksChosen[3] = textView4.getText().toString();

                }
                if (!textView5.getText().toString().equals("")){
                    listOfStocksChosen[4] = textView5.getText().toString();
                }
                Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                intent.putExtra("tickers", listOfStocksChosen);
                startService(intent);
            }
        });

        // OnClickEvent for submitButton1
        submitButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In case of odd behaviour where the user downloads for a ticker but removes it, disable the calc button
                if (textView1.getText().toString().equals("")){
                    submitButton1.setEnabled(false);
                    return;
                }
                result1a.setText("-");
                result1b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_1");
                sendBroadcast(intent);
            }
        });

        // OnClickEvent for submitButton2
        submitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In case of odd behaviour where the user downloads for a ticker but removes it, disable the calc button
                if (textView2.getText().toString().equals("")){
                    submitButton2.setEnabled(false);
                    return;
                }
                result2a.setText("-");
                result2b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_2");
                sendBroadcast(intent);
            }
        });

        // OnClickEvent for submitButton3
        submitButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In case of odd behaviour where the user downloads for a ticker but removes it, disable the calc button
                if (textView3.getText().toString().equals("")){
                    submitButton3.setEnabled(false);
                    return;
                }
                result3a.setText("-");
                result3b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_3");
                sendBroadcast(intent);
            }
        });

        // OnClickEvent for submitButton4
        submitButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In case of odd behaviour where the user downloads for a ticker but removes it, disable the calc button
                if (textView4.getText().toString().equals("")){
                    submitButton4.setEnabled(false);
                    return;
                }
                result4a.setText("-");
                result4b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_4");
                sendBroadcast(intent);
            }
        });

        // OnClickEvent for submitButton5
        submitButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In case of odd behaviour where the user downloads for a ticker but removes it, disable the calc button
                if (textView5.getText().toString().equals("")){
                    submitButton5.setEnabled(false);
                    return;
                }
                result5a.setText("-");
                result5b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_5");
                sendBroadcast(intent);
            }
        });

        // OnClickEvent for calculateAll button
        // Synchronously
        calcAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitButton1.performClick();
                submitButton2.performClick();
                submitButton3.performClick();
                submitButton4.performClick();
                submitButton5.performClick();
            }
        });

    }

    /**
     * Invoked on exit of {@link #onCreate(Bundle)} function and when activity enters the Started state.
     */
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * Invoked when the activity is no longer visible to the user.
     */
    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReceiver1);
        unregisterReceiver(myBroadcastReceiver2);
        unregisterReceiver(myBroadcastReceiver3);
        unregisterReceiver(myBroadcastReceiver4);
        unregisterReceiver(myBroadcastReceiver5);
    }




}