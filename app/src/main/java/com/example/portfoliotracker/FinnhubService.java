package com.example.portfoliotracker;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class FinnhubService extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    private ArrayList<CharSequence> tickerList = new ArrayList<>();

    // Our interval
    private static final int START_INTERVAL = 1625097601;

    //Changed the interval to reduce load time while debugging
    private static final int END_INTERVAL = 1640995199;
    private String token = "c8sq972ad3ifkeaocsjg"; // put your own token


    private final class ServiceHandler extends Handler {
        // Constructor
        public ServiceHandler(Looper looper) { super(looper); }

        // Implement the handler, handle the message to download
        @Override
        public void handleMessage(Message msg){
            // url to get historical data

            // This arraylist stores the tickers that are also passed into the msg
            System.out.println(msg.getData());
            ArrayList<CharSequence> tickers = msg.getData().getCharSequenceArrayList("tickers");

            // Do the below for all the tickers
            for (int i = 0; i < tickers.size(); i++) {
                String ticker = tickers.get(i).toString();
                if (ticker.equals("")){
                    continue;
                }
                System.out.println(ticker);
                String stringUrl =
                        String.format("https://finnhub.io/api/v1/stock/candle?symbol=%s&resolution=D&from=%s&to=%s&token=%s", ticker, START_INTERVAL, END_INTERVAL, token);
                String result;
                String inputLine;

                try {

                    // make GET requests

                    URL myUrl = new URL(stringUrl);
                    HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();

                    connection.setRequestMethod(REQUEST_METHOD);
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECTION_TIMEOUT);

                    connection.connect();

                    // store json string from GET response

                    InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();

                    while((inputLine = reader.readLine()) != null){
                        stringBuilder.append(inputLine);
                    }

                    reader.close();
                    streamReader.close();

                    result = stringBuilder.toString();

                } catch(IOException e) {
                    e.printStackTrace();
                    result = null;
                    Thread.currentThread().interrupt();
                }

                // parse the json string into 'close' and 'volume' array

                JSONObject jsonObject = null;
                JSONArray jsonArrayClose = null;
                JSONArray jsonArrayVolume = null;

                try {
                    jsonObject = new JSONObject(result);
                    jsonArrayClose = jsonObject.getJSONArray("c");
                    jsonArrayVolume = jsonObject.getJSONArray("v");
                } catch (JSONException e) {e.printStackTrace();}

                // Log number of values obtained
                Log.v("close", String.valueOf(jsonArrayClose.length()));
                Log.v("vol", String.valueOf(jsonArrayVolume.length()));

                try {
                    for (int j = 0; j < jsonArrayClose.length(); j++) {
                        double close = jsonArrayClose.getDouble(j);
                        double volume = jsonArrayVolume.getDouble(j);
                        Log.v("data", j + ":, c: " + close + " v: " + volume);
                        // Persist this into the ContentProvider
                        ContentValues values = new ContentValues();
                        values.put(HistoricalDataProvider.CLOSE, close);
                        values.put(HistoricalDataProvider.VOLUME, volume);
                        getContentResolver().insert(HistoricalDataProvider.CONTENT_URI, values);
                    }
                    Intent intent;
                    switch (i){
                        case 0:
                            intent = new Intent("DOWNLOAD_COMPLETE_1");
                            sendBroadcast(intent);
                            break;
                        case 1:
                            intent = new Intent("DOWNLOAD_COMPLETE_2");
                            sendBroadcast(intent);
                            break;
                        case 2:
                            intent = new Intent("DOWNLOAD_COMPLETE_3");
                            sendBroadcast(intent);
                            break;
                        case 3:
                            intent = new Intent("DOWNLOAD_COMPLETE_4");
                            sendBroadcast(intent);
                            break;
                        case 4:
                            System.out.println("CAsE4");
                            intent = new Intent("DOWNLOAD_COMPLETE_5");
                            sendBroadcast(intent);
                            break;

                    }
                } catch (JSONException e) {e.printStackTrace();}
            }

            // broadcast message that download is complete

//            Intent intent = new Intent("DOWNLOAD_COMPLETE");
//            sendBroadcast(intent);

            stopSelf(msg.arg1);

        }
    }

    @Override
    public void onCreate(){
        // Get looper and handler up, tie them to the Handler thread
        HandlerThread thread = new HandlerThread("Service", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);


    }

    @Override
    // Get the handler to initiate some work (send msg to handler thread)
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Download starting", Toast.LENGTH_SHORT).show();
        tickerList = intent.getCharSequenceArrayListExtra("tickers");
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;

        // In reality take the list of tickers as input
        // Might be variable number of tickers but up to 5
        Bundle bundle = new Bundle();
        // Fill the bundle up with the tickers list
        bundle.putCharSequenceArrayList("tickers", tickerList);
        msg.setData(bundle);
        // Send arbitrary message
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onDestroy(){ Toast.makeText(this, "Download done", Toast.LENGTH_SHORT).show(); }
}
