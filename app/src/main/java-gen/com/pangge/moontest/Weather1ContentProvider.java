package com.pangge.moontest;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

/* Copy this code snippet into your AndroidManifest.xml inside the <application> element:

    <provider
        android:name="com.pangge.moontest.Weather1ContentProvider"
        android:authorities="com.pangge.moontest.provider" />
*/

public class Weather1ContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.pangge.moontest.provider";
    public static final String BASE_PATH = "weather1";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + BASE_PATH;

    //db
    private SQLiteDatabase db;


    private static final String TABLENAME1 = Weather1Dao.TABLENAME;
    /**
     * single content provider--multi tables
     */
    private static final String TABLENAME2 = Weather2Dao.TABLENAME;
    private static final String TABLENAME3 = Weather3Dao.TABLENAME;
    private static final String TABLENAME4 = Weather4Dao.TABLENAME;
    private static final String TABLENAME5 = Weather5Dao.TABLENAME;

    /**
     * !!!
     */
    private static final String PK = Weather1Dao.Properties.Id.columnName;

    private static final int WEATHER1_DIR = 0;
    private static final int WEATHER1_ID = 1;

    private static final UriMatcher sURIMatcher;

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, WEATHER1_DIR);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", WEATHER1_ID);
        sURIMatcher.addURI(AUTHORITY, Weather2ContentProvider.BASE_PATH, WEATHER1_DIR);
        sURIMatcher.addURI(AUTHORITY, Weather2ContentProvider.BASE_PATH + "/#", WEATHER1_ID);
        sURIMatcher.addURI(AUTHORITY, Weather3ContentProvider.BASE_PATH, WEATHER1_DIR);
        sURIMatcher.addURI(AUTHORITY, Weather3ContentProvider.BASE_PATH + "/#", WEATHER1_ID);

        sURIMatcher.addURI(AUTHORITY, Weather4ContentProvider.BASE_PATH, WEATHER1_DIR);
        sURIMatcher.addURI(AUTHORITY, Weather4ContentProvider.BASE_PATH + "/#", WEATHER1_ID);


        sURIMatcher.addURI(AUTHORITY, Weather5ContentProvider.BASE_PATH, WEATHER1_DIR);
        sURIMatcher.addURI(AUTHORITY, Weather5ContentProvider.BASE_PATH + "/#", WEATHER1_ID);

    }

    /**
     * This must be set from outside, it's recommended to do this inside your Application object.
     * Subject to change (static isn't nice).
     */
    public static DaoSession daoSession;

    @Override
    public boolean onCreate() {
        // if(daoSession == null) {
        //     throw new IllegalStateException("DaoSession must be set before content provider is created");
        // }
        DaoLog.d("Content Provider started: " + CONTENT_URI);

        return true;
    }

    protected Database getDatabase() {
        if(daoSession == null) {
            throw new IllegalStateException("DaoSession must be set during content provider is active");
        }
        return daoSession.getDatabase();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //int uriType = sURIMatcher.match(uri);
        // Log.i("-hello-insert",uri.getPath());
        // SQLiteDatabase db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        // long id = 0;
        String path = "";
        switch (uri.getLastPathSegment()){
            case BASE_PATH:
                path = insertTable(uri,TABLENAME1,values);

                break;
            case Weather2ContentProvider.BASE_PATH:
                path = insertTable(uri,TABLENAME2,values);
                break;

            case Weather3ContentProvider.BASE_PATH:
                path = insertTable(uri,TABLENAME3,values);
                break;

            case Weather4ContentProvider.BASE_PATH:
                path = insertTable(uri,TABLENAME4,values);
                break;

            case Weather5ContentProvider.BASE_PATH:
                path = insertTable(uri,TABLENAME5,values);
                break;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(path);

    }

    private String insertTable(Uri uri, String tableName, ContentValues values){
        int uriType = sURIMatcher.match(uri);
        db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        long id = 0;
        String path = "";
        switch (uriType) {
            case WEATHER1_DIR:
                id = db.insert(tableName, null, values);
                path = Weather2ContentProvider.BASE_PATH + "/" + id;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return path;

    }



    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        int size = 0;
        Log.i("bulkinsert--","----content provider----");
        switch (uri.getLastPathSegment()){
            case BASE_PATH:
                size = bulkTable(uri,TABLENAME1,values);

                break;
            case Weather2ContentProvider.BASE_PATH:
                size = bulkTable(uri,TABLENAME2,values);
                break;

            case Weather3ContentProvider.BASE_PATH:
                size = bulkTable(uri,TABLENAME3,values);
                break;

            case Weather4ContentProvider.BASE_PATH:
                size = bulkTable(uri,TABLENAME4,values);
                break;

            case Weather5ContentProvider.BASE_PATH:
                size = bulkTable(uri,TABLENAME5,values);
                break;
        }
        return size;



    }

    private int bulkTable(Uri uri, String tableName, ContentValues[] values){
        db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case WEATHER1_DIR:
                int rowsInserted = 0;
                try{
                    for (ContentValues value : values) {
                        long _id = db.insert(tableName, null, value);
                        if(_id != -1){
                            rowsInserted++;
                        }

                    }


                }catch (Exception e){
                    e.printStackTrace();

                }
                if(rowsInserted >0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsInserted;
            default:
                //  If the URI does match match WEATHER_DIR, return the super implementation of bulkInsert
                return super.bulkInsert(uri, values);

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // int uriType = sURIMatcher.match(uri);
       // Log.i("---uri---",uri.toString());
        //---uri---: content://com.pangge.moontest.provider/weather2
        //Log.i("-hello-dele",uri.getPath());
      //  -hello-dele: /weather2
        //Log.i("-hello-dele-ok",uri.getLastPathSegment());
        //-hello-dele-ok: weather2
        //Log.i("---suriMat---",sURIMatcher.);
        //  Database db = getDatabase();
        //  SQLiteDatabase db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        int rowsDeleted = 0;
        // String id;
        switch (uri.getLastPathSegment()){
            case BASE_PATH:
                rowsDeleted = deleteTable(uri, TABLENAME1, selection, selectionArgs);
                break;
            case Weather2ContentProvider.BASE_PATH:
                rowsDeleted = deleteTable(uri, TABLENAME2, selection, selectionArgs);
                break;
            case Weather3ContentProvider.BASE_PATH:
                rowsDeleted = deleteTable(uri, TABLENAME3, selection, selectionArgs);
                break;
            case Weather4ContentProvider.BASE_PATH:
                rowsDeleted = deleteTable(uri, TABLENAME4, selection, selectionArgs);
                break;
            case Weather5ContentProvider.BASE_PATH:
                rowsDeleted = deleteTable(uri, TABLENAME5, selection, selectionArgs);
                break;

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }


    private int deleteTable(Uri uri, String tableName, String selection, String[] selectionArgs){
        int uriType = sURIMatcher.match(uri);
        int rowsDeleted = 0;
        String id;
        db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        switch (uriType) {
            case WEATHER1_DIR:
                rowsDeleted = db.delete(tableName, selection, selectionArgs);
                break;
            case WEATHER1_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(tableName, PK + "=" + id, null);
                } else {
                    rowsDeleted = db.delete(tableName, PK + "=" + id + " and "
                            + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        return rowsDeleted;


    }
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        //Database db = getDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
            case WEATHER1_DIR:
                rowsUpdated = db.update(TABLENAME1, values, selection, selectionArgs);
                break;
            case WEATHER1_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(TABLENAME1, values, PK + "=" + id, null);
                } else {
                    rowsUpdated = db.update(TABLENAME1, values, PK + "=" + id
                            + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
       // Log.i("path", uri.getPathSegments().get(0));

        //uri.getPathSegments();
        String tableName = uri.getPathSegments().get(0);

      //  Log.i("last", uri.getLastPathSegment());
        int uriType = sURIMatcher.match(uri);
       // Log.i("uriType--", ""+uriType);
        switch (uriType) {
            case WEATHER1_DIR:
                queryBuilder.setTables(uri.getLastPathSegment());
                // queryTable(uri,queryBuilder, uri.getLastPathSegment());
                break;
            case WEATHER1_ID:
                queryBuilder.setTables(tableName);
                queryBuilder.appendWhere(PK + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        /*switch (uri.getLastPathSegment()){
            case BASE_PATH:
                queryBuilder = queryTable(uri, queryBuilder, TABLENAME1);
                break;
            case Weather2ContentProvider.BASE_PATH:
                queryBuilder = queryTable(uri, queryBuilder, TABLENAME2);
                break;
            case Weather3ContentProvider.BASE_PATH:
                queryBuilder = queryTable(uri, queryBuilder, TABLENAME3);
                break;
            case Weather4ContentProvider.BASE_PATH:
                queryBuilder = queryTable(uri, queryBuilder, TABLENAME4);
                break;
            case Weather5ContentProvider.BASE_PATH:
                queryBuilder = queryTable(uri, queryBuilder, TABLENAME5);
                break;
        }*/

       // Log.i("table--", queryBuilder.getTables());


        Database db = getDatabase();
        Cursor cursor = queryBuilder.query(((StandardDatabase) db).getSQLiteDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }


    @Override
    public final String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case WEATHER1_DIR:
                return CONTENT_TYPE;
            case WEATHER1_ID:
                return CONTENT_ITEM_TYPE;
            default :
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}