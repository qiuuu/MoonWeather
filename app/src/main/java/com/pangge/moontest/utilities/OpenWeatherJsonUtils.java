package com.pangge.moontest.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pangge.moontest.BaseApplication;
import com.pangge.moontest.DaoSession;
<<<<<<< HEAD
import com.pangge.moontest.Weather1;
=======
import com.pangge.moontest.MainActivity;
import com.pangge.moontest.Weather;
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
import com.pangge.moontest.data.WeatherContract;

import org.greenrobot.greendao.rx.RxDao;
import org.json.JSONException;

import io.reactivex.annotations.NonNull;

/**
 * Created by iuuu on 17/5/17.
 */

public class OpenWeatherJsonUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     * <p/>
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param forecastJson JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */

<<<<<<< HEAD
    private static RxDao<Weather1, Long> weatherDao;
=======
    private static RxDao<Weather, Long> weatherDao;
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
    private static DaoSession daoSession;


    public static ContentValues[] getWeatherContentValuesFromJson(Context context, @NonNull JsonObject forecastJson)
            throws JSONException
    {

        daoSession =BaseApplication.getDaoSession();
<<<<<<< HEAD
        weatherDao = daoSession.getWeather1Dao().rx();
        Weather1 weather;
=======
        weatherDao = daoSession.getWeatherDao().rx();
        Weather weather;
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
        JsonObject data = forecastJson.get("data").getAsJsonObject();
        JsonElement wendu = data.get("wendu");
        JsonElement ganmao = data.get("ganmao");
        JsonElement city = data.get("city");
        JsonArray forecast = data.get("forecast").getAsJsonArray();
<<<<<<< HEAD
       // Log.i(wendu.toString(),"------温度---------------");
       /**
        * need or not
        * delete!!
        * weather1 will miss city,wendu
        * Gson gson = new Gson();
        weatherDao.getDao().deleteAll();
        for(JsonElement element:forecast){
            Log.i("---hello--",element.toString());
            weather = gson.fromJson(element, Weather1.class);
            weatherDao.getDao().insert(weather);
           // weatherDao.getDao().q
        }*/
=======
        Log.i(wendu.toString(),"------温度---------------");
        Gson gson = new Gson();
        weatherDao.getDao().deleteAll();
        for(JsonElement element:forecast){
            Log.i("---hello--",element.toString());
            weather = gson.fromJson(element,Weather.class);
            weatherDao.getDao().insert(weather);
           // weatherDao.getDao().q
        }
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e


        ContentValues[] weatherContentValues = new ContentValues[forecast.size()];
        int i=0;
        for (JsonElement element:forecast){
            ContentValues weatherValues = new ContentValues();
            JsonObject object = element.getAsJsonObject();
            //改的是Content Provider中的数据，可是greendao 却也被改了？？
            //only add today temp



            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, object.get("date").getAsString());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_TYPE, object.get("type").getAsString());

            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, object.get("high").getAsString());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, object.get("low").getAsString());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_FX, object.get("fengxiang").getAsString());
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_FL, object.get("fengli").getAsString());


            if(i == 0){
<<<<<<< HEAD

=======
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_CITY, city.getAsString());

                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WENDU, wendu.getAsString());

                weatherValues.put(WeatherContract.WeatherEntry.COLUMN_TIP, ganmao.getAsString());


            }
            weatherContentValues[i] = weatherValues;
            i++;

        }




        return weatherContentValues;
    }

}
