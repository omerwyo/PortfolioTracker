package com.example.portfoliotracker;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.Looper;
import android.content.IntentFilter;


public class MainActivity extends AppCompatActivity {
    private Button submitButton1,submitButton2,submitButton3,submitButton4,submitButton5,download;
    private Button downloadButton1, downloadButton2,downloadButton3,downloadButton4,downloadButton5;
    private EditText textView1,textView2,textView3,textView4,textView5;
    private TextView result1a,result1b, result2a, result2b, result3a, result3b, result4a, result4b, result5a, result5b;
    private BroadcastReceiver myBroadcastReceiver1,myBroadcastReceiver2,myBroadcastReceiver3,myBroadcastReceiver4,myBroadcastReceiver5;
    private BroadcastReceiver downloadBroadcastReceiver1,downloadBroadcastReceiver2,downloadBroadcastReceiver3,downloadBroadcastReceiver4,downloadBroadcastReceiver5;
    private BroadcastReceiver errorBroadcastReceiver1,errorBroadcastReceiver2,errorBroadcastReceiver3,errorBroadcastReceiver4,errorBroadcastReceiver5;
    CharSequence listOfStocksChosen[] = new CharSequence[] {
            "", "", "", "", "" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String[] language =getResources().getStringArray(R.array.languages);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>
//                (this,android.R.layout.select_dialog_item, language);
//        AutoCompleteTextView actv =  (AutoCompleteTextView)findViewById(R.id.autoCompleteStockOptions);
//        actv.setThreshold(1);//will start working from first character
//        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
//        actv.setTextColor(Color.RED);
        submitButton1 = findViewById(R.id.submitButton1);
        submitButton2 = findViewById(R.id.submitButton2);
        submitButton3 = findViewById(R.id.submitButton3);
        submitButton4 = findViewById(R.id.submitButton4);
        submitButton5 = findViewById(R.id.submitButton5);

        textView1 = findViewById(R.id.editTicker1);
        textView2 = findViewById(R.id.editTicker2);
        textView3 = findViewById(R.id.editTicker3);
        textView4 = findViewById(R.id.editTicker4);
        textView5 = findViewById(R.id.editTicker5);

        download = findViewById(R.id.downloadAll);

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

        downloadButton1 = findViewById(R.id.downloadButton1);
        downloadButton2 = findViewById(R.id.downloadButton2);
        downloadButton3 = findViewById(R.id.downloadButton3);
        downloadButton4 = findViewById(R.id.downloadButton4);
        downloadButton5 = findViewById(R.id.downloadButton5);

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

        downloadBroadcastReceiver1 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver1, new IntentFilter("ENABLE_CALC_1"));

        downloadBroadcastReceiver2 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver2, new IntentFilter("ENABLE_CALC_2"));

        downloadBroadcastReceiver3 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver3, new IntentFilter("ENABLE_CALC_3"));

        downloadBroadcastReceiver4 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver4, new IntentFilter("ENABLE_CALC_4"));

        downloadBroadcastReceiver5 = new DownloadBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(downloadBroadcastReceiver5, new IntentFilter("ENABLE_CALC_5"));

        errorBroadcastReceiver1 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver1, new IntentFilter("ERROR_1"));

        errorBroadcastReceiver2 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver2, new IntentFilter("ERROR_2"));

        errorBroadcastReceiver3 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver3, new IntentFilter("ERROR_3"));

        errorBroadcastReceiver4 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver4, new IntentFilter("ERROR_4"));

        errorBroadcastReceiver5 = new ErrorBroadcastReceiver(new Handler(Looper.getMainLooper()));
        registerReceiver(errorBroadcastReceiver5, new IntentFilter("ERROR_5"));

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(textView1.getText());
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
                System.out.println(listOfStocksChosen);
                intent.putExtra("tickers", listOfStocksChosen);
                startService(intent);
            }

        });



        submitButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result1a.setText("-");
                result1b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_1");
                sendBroadcast(intent);
            }

        });
        submitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result2a.setText("-");
                result2b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_2");
                sendBroadcast(intent);
            }
        });
        submitButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result3a.setText("-");
                result3b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_3");
                sendBroadcast(intent);
            }
        });
        submitButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result4a.setText("-");
                result4b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_4");
                sendBroadcast(intent);
            }
        });
        submitButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result5a.setText("-");
                result5b.setText("-");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_5");
                sendBroadcast(intent);
            }
        });

        downloadButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("c",textView5.getText().toString());
                CharSequence tempArray[] = new CharSequence[] {
                        "", "", "", "", "" };
                if (!textView1.getText().toString().equals("")){
                    tempArray[0] = textView1.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    System.out.println(tempArray);
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
                else{
                    textView1.setError("Ticker cannot be empty");
                }

            }
        });

        downloadButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence tempArray[] = new CharSequence[] {
                        "", "", "", "", "" };
                if (!textView2.getText().toString().equals("")){
                    tempArray[1]=textView2.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
                else{
                    textView2.setError("Ticker cannot be empty");
                }

            }
        });

        downloadButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("c",textView5.getText().toString());
                CharSequence tempArray[] = new CharSequence[] {
                        "", "", "", "", "" };
                if (!textView3.getText().toString().equals("")){
                    tempArray[2] = textView3.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    System.out.println(tempArray);
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
                else{
                    textView3.setError("Ticker cannot be empty");
                }

            }
        });

        downloadButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("c",textView5.getText().toString());
                CharSequence tempArray[] = new CharSequence[] {
                        "", "", "", "", "" };
                if (!textView4.getText().toString().equals("")){
                    tempArray[3]=textView4.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    System.out.println(tempArray);
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
                else{
                    textView4.setError("Ticker cannot be empty");
                }

            }
        });

        downloadButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence tempArray[] = new CharSequence[] {
                        "", "", "", "", "" };
                if (!textView5.getText().toString().equals("")){
                    tempArray[4]=textView5.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                    System.out.println(tempArray);
                    intent.putExtra("tickers", tempArray);
                    startService(intent);
                }
                else{
                    textView5.setError("Ticker cannot be empty");
                }

            }
        });




    }
    @Override
    protected void onStart() {
        super.onStart();
    }

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