package com.pangge.moontest;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import org.greenrobot.greendao.DaoLog;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.StandardDatabase;

/* Copy this code snippet into your AndroidManifest.xml inside the <application> element:

    <provider
        android:name="com.pangge.moontest.Weather3ContentProvider"
        android:authorities="com.pangge.moontest.provider" />
*/

public class Weather3ContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.pangge.moontest.provider";
    public static final String BASE_PATH = "weather3";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/" + BASE_PATH;
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/" + BASE_PATH;

    private static final String TABLENAME = Weather3Dao.TABLENAME;
    private static final String PK = Weather3Dao.Properties.Id.columnName;

    private static final int WEATHER3_DIR = 0;
    private static final int WEATHER3_ID = 1;

    private static final UriMatcher sURIMatcher;

    static {
        sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, WEATHER3_DIR);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", WEATHER3_ID);
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
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        long id = 0;
        String path = "";
        switch (uriType) {
        case WEATHER3_DIR:
            id = db.insert(TABLENAME, null, values);
            path = BASE_PATH + "/" + id;
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(path);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        //Database db = getDatabase();
        SQLiteDatabase db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        int rowsDeleted = 0;
        String id;
        switch (uriType) {
        case WEATHER3_DIR:
                rowsDeleted = db.delete(TABLENAME, selection, selectionArgs);
                break;
        case WEATHER3_ID:
            id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                rowsDeleted = db.delete(TABLENAME, PK + "=" + id, null);
            } else {
                rowsDeleted = db.delete(TABLENAME, PK + "=" + id + " and "
                                + selection, selectionArgs);
            }
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        //Database db = getDatabase();
        SQLiteDatabase db = ((StandardDatabase)getDatabase()).getSQLiteDatabase();
        int rowsUpdated = 0;
        String id;
        switch (uriType) {
        case WEATHER3_DIR:
            rowsUpdated = db.update(TABLENAME, values, selection, selectionArgs);
            break;
        case WEATHER3_ID:
            id = uri.getLastPathSegment();
            if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(TABLENAME, values, PK + "=" + id, null);
            } else {
                    rowsUpdated = db.update(TABLENAME, values, PK + "=" + id
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
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
        case WEATHER3_DIR:
            queryBuilder.setTables(TABLENAME);
            break;
        case WEATHER3_ID:
            queryBuilder.setTables(TABLENAME);
            queryBuilder.appendWhere(PK + "="
                    + uri.getLastPathSegment());
            break;
        default:
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        Database db = getDatabase();
        Cursor cursor = queryBuilder.query(((StandardDatabase) db).getSQLiteDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public final String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
        case WEATHER3_DIR:
            return CONTENT_TYPE;
        case WEATHER3_ID:
            return CONTENT_ITEM_TYPE;
        default :
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}
