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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

/**
 * A {@code HistoricalDataProvider} encapsulate data and provide it to applications.
 * The data can also be shared between multiple applications.
 *
 * {@link #onCreate()} which is called to initialize the provider
 * {@link #query(Uri, String[], String, String[], String)} which returns data to the caller
 * {@link #insert(Uri, ContentValues)} which inserts new data into the content provider
 * {@link #update(Uri, ContentValues, String, String[])} which updates existing data in the content provider
 * {@link #delete(Uri, String, String[])} which deletes data from the content provider
 * {@link #getType(Uri)} which returns the MIME type of data in the content provider
 */
public class HistoricalDataProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.portfoliotracker.HistoricalDataProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/history";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String ID = "id";
    static final String CLOSE = "close";
    static final String VOLUME = "volume";
    static final String TICKER = "ticker";


    private static HashMap<String, String> HISTORY_PROJECTION_MAP;

    static final int HISTORY = 1;
    static final int HISTORY_ID = 2;

    static final UriMatcher uriMatcher;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "history", HISTORY);
        uriMatcher.addURI(PROVIDER_NAME, "history/#", HISTORY_ID);
    }


    /**
     * Database specific constant declarations.
     */
    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Historical_Data";
    static final String TABLE_NAME = "history";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " ticker STRING NOT NULL, " +
                    " close DECIMAL(5,3) NOT NULL, " +
                    " volume DECIMAL(10,1) NOT NULL);";


    /**
     * Helper class which extends SQLiteOpenHelper.
     */
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

    /**
     * Primary method which is called to initialize the provider.
     * @return boolean value true if provider is successfully created.
     */
    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        // create db if not exists
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    /**
     * Insert new data into the content provider.
     *
     * @param uri the content:// URI of the insertion request. This value cannot be null.
     * @param values a set of column_name/value pairs to add to the database. This value may be null.
     *
     * @return The URI for the newly inserted item. This value may be null.
     * @throws SQLException if rowID is less than or equals to 0 after insertion.
     */
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

    /**
     * Function which returns queried data to the caller.
     *
     * @param uri The URI to query. This will be the full URI sent by the client. This value cannot be null.
     * @param projection String[] describing which columns to return.
     * @param selection The query criteria.
     * @param selectionArgs The query arguments.
     * @param sortOrder The order to sort for the return data to be sorted in.
     *
     * @return Uri on successful insertion into the database.
     * @throws SQLException if rowID is less than or equals to 0 after insertion.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
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

        // Register to watch a content URI for changes
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    /**
     * Returns the MIME type of data in the content provider
     *
     * @param uri the URI to query. This value cannot be null.
     *
     * @return a MIME type string, or null if there is no type.
     * @throws IllegalArgumentException if URI is not supported by function.
     */
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

    /**
     * Application specific implementation of database insertion function.
     *
     * @param uri The full URI to query, including a row ID (if a specific record is requested). This value cannot be null.
     * @param s deletion selection query.  This value may be null.
     * @param strings deletion selection arguments. This value may be null.
     *
     * @return int 0 on successful deletion of data.
     */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    /**
     * Application specific implementation of database update function.
     *
     * @param uri The full URI to query, including a row ID (if a specific record is requested). This value cannot be null.
     * @param contentValues A set of column_name/value pairs to update in the database. This value may be null.
     * @param s deletion selection query.  This value may be null.
     * @param strings deletion selection arguments. This value may be null.
     *
     * @return int 0 on successful update of data.
     */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
