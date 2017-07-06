package com.pangge.moontest.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.pangge.moontest.Weather1ContentProvider;

import com.pangge.moontest.Weather2ContentProvider;
import com.pangge.moontest.Weather3ContentProvider;
import com.pangge.moontest.Weather4ContentProvider;
import com.pangge.moontest.Weather5ContentProvider;

/**
 * Created by iuuu on 17/5/15.
 * 可以删了
 * bunengshan
 */

public class WeatherContract {

    public static final String CONTENT_AUTHORITY = "com.pangge.moontest";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
    public static final String PATH_WEATHER = "weather";

    public static final class WeatherEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_WEATHER)
                .build();
        public static final String TABLE_NAME = "weather";

        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_MIN_TEMP = "low";
        public static final String COLUMN_MAX_TEMP = "high";
        public static final String COLUMN_FX = "fengxiang";
        public static final String COLUMN_FL = "fengli";
        public static final String COLUMN_WENDU = "wendu";
        public static final String COLUMN_TIP = "ganmao";
        public static final String COLUMN_CITY = "city";

        public static Uri buildWeatherUriWithData(long id, int loadId){
            //cursor position 0->


            switch (loadId){
                case 1:
                    return ContentUris.withAppendedId(Weather1ContentProvider.CONTENT_URI, id+1);
                case 2:
                    return ContentUris.withAppendedId(Weather2ContentProvider.CONTENT_URI, id+1);
                case 3:
                    return ContentUris.withAppendedId(Weather3ContentProvider.CONTENT_URI, id+1);
                case 4:
                    return ContentUris.withAppendedId(Weather4ContentProvider.CONTENT_URI, id+1);
                case 5:
                    return ContentUris.withAppendedId(Weather5ContentProvider.CONTENT_URI, id+1);
                default:
                    return ContentUris.withAppendedId(Weather1ContentProvider.CONTENT_URI, id+1);

            }
            //return CONTENT_URI.buildUpon().appendPath(date).build();
        }

        public static String getSqlSelectForTodayOnwards() {
            long normalizedUtcNow = System.currentTimeMillis();
            return WeatherContract.WeatherEntry.COLUMN_DATE + " >= " + normalizedUtcNow;
        }

    }
}
