package com.example.portfoliotracker;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

/**
 * An application component that perform download and calculations operations in the background.
 */
public class FinnhubService extends Service {
    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    private static final String REQUEST_METHOD = "GET";
    private static final int READ_TIMEOUT = 15000;
    private static final int CONNECTION_TIMEOUT = 15000;

    private CharSequence[] tickerList = new CharSequence[5];

    // Our intervals
    private static final int START_INTERVAL = 1625097601;
    private static final int END_INTERVAL = 1640995199;
    private String token = "c8sq972ad3ifkeaocsjg"; // put your own token

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) { super(looper); }
        static final String PROVIDER_NAME = "com.example.portfoliotracker.HistoricalDataProvider";

        /**
         * Handler of incoming messages from clients.
         * @param msg message to be passed.
         */
        @Override
        public void handleMessage(Message msg){
            // This arraylist stores the tickers that are also passed into the msg
            Log.v("HandleMessage", String.valueOf(msg.getData()));
            CharSequence[] tickers = msg.getData().getCharSequenceArray("tickers");
            Boolean hasFailed = false;
            // Do the below for all the tickers
            for (int i = 0; i < tickers.length; i++) {
                String ticker = tickers[i].toString();
                if (ticker.equals("")){
                    continue;
                }
                // Check if ticker already exists in database
                Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/history");
                Context context = getApplicationContext();
                Cursor cursor = context.getContentResolver().query(CONTENT_URI, null, String.format("ticker =  '%s'",ticker.toString()), null, null);
                if (cursor.getCount() > 0){
                    Intent intent;
                    switch (i){
                        case 0:
                            intent = new Intent("ENABLE_CALC_1");
                            sendBroadcast(intent);
                            break;
                        case 1:
                            intent = new Intent("ENABLE_CALC_2");
                            sendBroadcast(intent);
                            break;
                        case 2:
                            intent = new Intent("ENABLE_CALC_3");
                            sendBroadcast(intent);
                            break;
                        case 3:
                            intent = new Intent("ENABLE_CALC_4");
                            sendBroadcast(intent);
                            break;
                        case 4:
                            intent = new Intent("ENABLE_CALC_5");
                            sendBroadcast(intent);
                            break;
                    }
                    continue;
                }
                Log.v("Ticker", ticker);
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

                // Parse the json string into 'close' and 'volume' array
                JSONObject jsonObject = null;
                JSONArray jsonArrayClose = null;
                JSONArray jsonArrayVolume = null;

                try {
                    jsonObject = new JSONObject(result);
                    jsonArrayClose = jsonObject.getJSONArray("c");
                    jsonArrayVolume = jsonObject.getJSONArray("v");
                } catch (JSONException e) {
                    Intent intent;
                    switch (i){
                        case 0:
                            intent = new Intent("ERROR_1");
                            sendBroadcast(intent);
                            break;
                        case 1:
                            intent = new Intent("ERROR_2");
                            sendBroadcast(intent);
                            break;
                        case 2:
                            intent = new Intent("ERROR_3");
                            sendBroadcast(intent);
                            break;
                        case 3:
                            intent = new Intent("ERROR_4");
                            sendBroadcast(intent);
                            break;
                        case 4:
                            intent = new Intent("ERROR_5");
                            sendBroadcast(intent);
                            break;
                    }
                    continue;
                }

                // Log number of values obtained
                Log.v("close", String.valueOf(jsonArrayClose.length()));
                Log.v("vol", String.valueOf(jsonArrayVolume.length()));

                try {
                    for (int j = 0; j < jsonArrayClose.length(); j++) {
                        double close = jsonArrayClose.getDouble(j);
                        double volume = jsonArrayVolume.getDouble(j);

                        // Persist this into the ContentProvider
                        ContentValues values = new ContentValues();
                        values.put(HistoricalDataProvider.CLOSE, close);
                        values.put(HistoricalDataProvider.VOLUME, volume);
                        values.put(HistoricalDataProvider.TICKER, ticker);
                        getContentResolver().insert(HistoricalDataProvider.CONTENT_URI, values);
                    }
                    Intent intent;
                    switch (i){
                        case 0:
                            intent = new Intent("ENABLE_CALC_1");
                            sendBroadcast(intent);
                            break;
                        case 1:
                            intent = new Intent("ENABLE_CALC_2");
                            sendBroadcast(intent);
                            break;
                        case 2:
                            intent = new Intent("ENABLE_CALC_3");
                            sendBroadcast(intent);
                            break;
                        case 3:
                            intent = new Intent("ENABLE_CALC_4");
                            sendBroadcast(intent);
                            break;
                        case 4:
                            intent = new Intent("ENABLE_CALC_5");
                            sendBroadcast(intent);
                            break;
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }
            stopSelf(msg.arg1);
        }
    }

    /**
     * Called by the system when the service is first created. Do not call this method directly.
     */
    @Override
    public void onCreate(){
        // Get looper and handler up, tie them to the Handler thread
        HandlerThread thread = new HandlerThread("Service", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);
    }

    /**
     * Called by the system every time a client explicitly starts the service by calling Context.startService(Intent)
     *
     * @param intent The intent as given. This may be null if the service is being restarted after its process has gone away
     * @param flags Additional data about this start request.
     * @param startId A unique integer representing this specific request to start.
     *
     * @return Integer value that indicates what semantics the system should use for the service's current started state.
     */
    @Override
    // Get the handler to initiate some work (send msg to handler thread)
    public int onStartCommand(Intent intent, int flags, int startId){
        Toast.makeText(this, "Download starting", Toast.LENGTH_SHORT).show();
        tickerList = intent.getCharSequenceArrayExtra("tickers");
        Message msg = serviceHandler.obtainMessage();
        msg.arg1 = startId;

        // We pass the ticker inputs into the message using a bundle
        // Might be variable number of tickers but up to 5
        Bundle bundle = new Bundle();
        // Fill the bundle up with the tickers list
        bundle.putCharSequenceArray("tickers", tickerList);
        msg.setData(bundle);
        // Send arbitrary message
        serviceHandler.sendMessage(msg);

        return START_STICKY;
    }

    /**
     * @param intent The Intent that was used to bind to this service, as given.
     * @return null value.
     */
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    /**
     * Called by the system to notify that the service that it is no longer used and is being removed.
     * Create a Toast to indicate completion of download.
     */
    @Override
    public void onDestroy(){ Toast.makeText(this, "Download done", Toast.LENGTH_SHORT).show(); }
}
