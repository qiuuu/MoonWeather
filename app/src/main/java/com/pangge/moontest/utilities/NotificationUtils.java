package com.pangge.moontest.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.pangge.moontest.DetailActivity;
import com.pangge.moontest.MainActivity;
import com.pangge.moontest.R;
import com.pangge.moontest.data.WeatherContract;
import com.pangge.moontest.data.WeatherPreferences;

/**
 * Created by iuuu on 17/6/14.
 */

public class NotificationUtils {

    public static final String[] WEATHER_NOTIFICATION_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_CITY,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
    };


    public static final int INDEX_MAX_TEMP = 1;
    public static final int INDEX_MIN_TEMP = 2;


    private static final int WEATHER_NOTIFICATION_ID = 3004;

    public static void notifyUserOfNewWeather(Context context) {

        Uri todaysWeatherUri = WeatherContract.WeatherEntry
                //id =1 loadid=2
                .buildWeatherUriWithData(1,2);

        Cursor todayWeatherCursor = context.getContentResolver().query(
                todaysWeatherUri,
                WEATHER_NOTIFICATION_PROJECTION,
                null,
                null,
                null);

        if(todayWeatherCursor.moveToFirst()){
            String date = todayWeatherCursor.getString(MainActivity.INDEX_WEATHER_DATE);
            String high = todayWeatherCursor.getString(MainActivity.INDEX_WEATHER_MAX_TEMP);
            String low = todayWeatherCursor.getString(MainActivity.INDEX_WEATHER_MIN_TEMP);

            //Resources resources = context.getResources();

            String notificationTitle = context.getString(R.string.app_name);

            String notificationText = high;

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setColor(ContextCompat.getColor(context, R.color.google_blue))
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setAutoCancel(true);

            Intent detailIntentForToday = new Intent(context, DetailActivity.class);
            detailIntentForToday.setData(todaysWeatherUri);

            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
            taskStackBuilder.addNextIntentWithParentStack(detailIntentForToday);
            PendingIntent resultPendingIntent = taskStackBuilder
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            notificationBuilder.setContentIntent(resultPendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(WEATHER_NOTIFICATION_ID, notificationBuilder.build());


            Log.i("Notification--","--ok----");


        }

        todayWeatherCursor.close();






    }



   /* private static String getNotificationText(Context context, String city, String high, String low) {

         /*
          * Short description of the weather, as provided by the API.
          * e.g "clear" vs "sky is clear".
          */
       /* String shortDescription = SunshineWeatherUtils
                .getStringForWeatherCondition(context, weatherId);*/

      //  String notificationFormat = context.getString(R.string.format_notification);

         /* Using String's format method, we create the forecast summary */
       /* String notificationText = String.format(notificationFormat,
                shortDescription,
                SunshineWeatherUtils.formatTemperature(context, high),
                SunshineWeatherUtils.formatTemperature(context, low));

        return notificationText;
    }*/
}
