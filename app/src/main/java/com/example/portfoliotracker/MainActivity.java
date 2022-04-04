package com.example.portfoliotracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.os.Handler;
import android.os.Looper;
import android.content.IntentFilter;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    private Button submitButton,submitButton2,submitButton3,submitButton4,submitButton5,download;
    private TextView textView1,textView2,textView3,textView4,textView5;
    private TextView result1,result2,result3,result4,result5;
    private BroadcastReceiver myBroadcastReceiver1,myBroadcastReceiver2,myBroadcastReceiver3,myBroadcastReceiver4,myBroadcastReceiver5;

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

        textView1 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);
        textView3 = findViewById(R.id.textView4);
        textView4 = findViewById(R.id.textView5);
        textView5 = findViewById(R.id.textView6);

        download = findViewById(R.id.download);

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

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                submitButton.setEnabled(true);
//                submitButton2.setEnabled(true);
//                submitButton3.setEnabled(true);
//                submitButton4.setEnabled(true);
//                submitButton5.setEnabled(true);
                if (textView1.getText()!=""){
                    listOfStocksChosen.add(0,textView1.getText());
                }
                if (textView2.getText()!=""){
                    listOfStocksChosen.add(1,textView2.getText());
                }
                if (textView3.getText()!=""){
                    listOfStocksChosen.add(2,textView3.getText());

                }
                if (textView4.getText()!=""){
                    listOfStocksChosen.add(3,textView4.getText());

                }
                if (textView5.getText()!=""){
                    listOfStocksChosen.add(4,textView5.getText());
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
            }
        });
        submitButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result2.setText("Waiting for data");
            }
        });
        submitButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result3.setText("Waiting for data");

            }
        });
        submitButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result4.setText("Waiting for data");
            }
        });
        submitButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                result5.setText("Waiting for data");
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