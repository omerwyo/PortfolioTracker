package com.example.portfoliotracker;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class HistoricalDataProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.portfoliotracker.HistoricalDataProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/history";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "id";
    static final String CLOSE = "close";
    static final String VOLUME = "volume";

    private static HashMap<String, String> HISTORY_PROJECTION_MAP;

    static final int HISTORY = 1;
    static final int HISTORY_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "history", HISTORY);
        uriMatcher.addURI(PROVIDER_NAME, "history/#", HISTORY_ID);
    }


    // Database specific constant declarations

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Historical_Data";
    static final String TABLE_NAME = "history";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " close DECIMAL(5,3) NOT NULL, " +
                    " volume DECIMAL(10,1) NOT NULL);";


    // helper class creates repo

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        // create db if not exists
        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;

    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {

        long rowID = db.insert(TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case HISTORY:
                qb.setProjectionMap(HISTORY_PROJECTION_MAP);
                break;
            case HISTORY_ID:
                qb.appendWhere( ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
        }

        if (sortOrder == null || sortOrder == ""){
            sortOrder = ID;
        }

        Cursor c = qb.query(db, projection, selection, selectionArgs,null, null, sortOrder);

        // register to watch a content URI for changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            // all records
            case HISTORY:
                return "vnd.android.cursor.dir/vnd.com.example.provider.history";
            // a particular record
            case HISTORY_ID:
                return "vnd.android.cursor.item/vnd.com.example.provider.history";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
