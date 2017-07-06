package com.pangge.moontest.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pangge.moontest.R;

/**
 * Created by iuuu on 17/5/15.
 */

public final class WeatherPreferences {
    public static final String PREF_COORD_LAT = "coord_lat";

    public static final String PREF_COORD_LONG = "coord_long";


    public static String getPreferredWeatherLocation(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String keyForLocation = context.getString(R.string.pref_location_key);
        String defaultLocation = context.getString(R.string.pref_location_Id_default);

        return sp.getString(keyForLocation,defaultLocation);
    }

    public static double[] getLocationCoordinates(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        double[] preferredCoordinates = new double[2];

        preferredCoordinates[0] = Double
                .longBitsToDouble(sp.getLong(PREF_COORD_LAT, Double.doubleToRawLongBits(0.0)));
        preferredCoordinates[1] = Double
                .longBitsToDouble(sp.getLong(PREF_COORD_LONG, Double.doubleToRawLongBits(0.0)));

        return preferredCoordinates;
    }


    public static boolean areNotificationsEnabled(Context context){
        String displayNotificationsKey = context.getString(R.string.pref_enable_notifications_key);

        boolean shouldDisplayNotificationsByDefault = context
                .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        boolean shouldDisplayNotifications = sp
                .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

        return shouldDisplayNotifications;
    }

}
