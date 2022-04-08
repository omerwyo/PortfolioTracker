package com.example.portfoliotracker;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private Button submitButton,submitButton2,submitButton3,submitButton4,submitButton5,download;
    private EditText textView1,textView2,textView3,textView4,textView5;
    private TextView result1,result2,result3,result4,result5;
    private BroadcastReceiver myBroadcastReceiver1,myBroadcastReceiver2,myBroadcastReceiver3,myBroadcastReceiver4,myBroadcastReceiver5;
    private BroadcastReceiver downloadBroadcastReceiver1,downloadBroadcastReceiver2,downloadBroadcastReceiver3,downloadBroadcastReceiver4,downloadBroadcastReceiver5;
    private BroadcastReceiver errorBroadcastReceiver1,errorBroadcastReceiver2,errorBroadcastReceiver3,errorBroadcastReceiver4,errorBroadcastReceiver5;

    ArrayList<CharSequence> listOfStocksChosen = new ArrayList<>(5);

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
        submitButton = findViewById(R.id.submitButton);
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

        result1 = findViewById(R.id.textView7);
        result2 = findViewById(R.id.textView8);
        result3 = findViewById(R.id.textView9);
        result4 = findViewById(R.id.textView10);
        result5 = findViewById(R.id.textView11);

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
                if (textView1.getText().toString()!=""){
                    listOfStocksChosen.add(0,textView1.getText().toString());
                }
                if (textView2.getText().toString()!=""){
                    listOfStocksChosen.add(1,textView2.getText().toString());
                }
                if (textView3.getText().toString()!=""){
                    listOfStocksChosen.add(2,textView3.getText().toString());

                }
                if (textView4.getText().toString()!=""){
                    listOfStocksChosen.add(3,textView4.getText().toString());

                }
                if (textView5.getText().toString()!=""){
                    listOfStocksChosen.add(4,textView5.getText().toString());
                }

                Intent intent = new Intent(getApplicationContext(), FinnhubService.class);
                System.out.println(listOfStocksChosen);
                intent.putExtra("tickers", listOfStocksChosen);
                startService(intent);
            }

        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result1.setText("Waiting for data");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_1");
                sendBroadcast(intent);
            }

        });
        submitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result2.setText("Waiting for data");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_2");
                sendBroadcast(intent);
            }
        });
        submitButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result3.setText("Waiting for data");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_3");
                sendBroadcast(intent);
            }
        });
        submitButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result4.setText("Waiting for data");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_4");
                sendBroadcast(intent);
            }
        });
        submitButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result5.setText("Waiting for data");
                Intent intent = new Intent("DOWNLOAD_COMPLETE_5");
                sendBroadcast(intent);
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