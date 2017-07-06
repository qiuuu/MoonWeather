package com.pangge.moontest.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonObject;


import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.pangge.moontest.Weather1ContentProvider;
import com.pangge.moontest.Weather2ContentProvider;
import com.pangge.moontest.Weather3ContentProvider;
import com.pangge.moontest.Weather4ContentProvider;
import com.pangge.moontest.Weather5ContentProvider;

import com.pangge.moontest.component.AppModule;
import com.pangge.moontest.component.DaggerAppComponent;
import com.pangge.moontest.data.WeatherPreferences;
import com.pangge.moontest.network.WeatherMini;
import com.pangge.moontest.utilities.NotificationUtils;
import com.pangge.moontest.utilities.OpenWeatherJsonUtils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by iuuu on 17/5/16.
 * //  TODO (1) Create a class called WeatherSyncTask
 -//  TODO (2) Within WeatherSyncTask, create a synchronized public static void method called syncWeather
 -//  TODO (3) Within syncWeather, fetch new weather data
 -// TODO (4) If we have valid results, delete the old data and insert the new
 */

public class WeatherSyncTask {
    /**
     +     * Performs the network request for updated weather, parses the JSON from that request, and
     +     * inserts the new weather information into our ContentProvider. Will notify the user that new
     +     * weather has been loaded if the user hasn't been notified of the weather within the last day
     +     * AND they haven't disabled notifications in the preferences screen.
     +     *
     +     * @param context Used to access utility methods and the ContentResolver
     +     */
    private static final String BASE_URL = "http://wthrcdn.etouch.cn/";
    //private static CompositeDisposable compositeDisposable;;
    private static List<String> cityList;
    private static List<JsonObject> bodyList;

    private Context mContext;

    @Inject
    SharedPreferences sharedPreferences;



    //synchronized
    public void syncWeather(Context context){

        //compositeDisposable = new CompositeDisposable();;
        cityList =new ArrayList<>();


        sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(context))
                .build().getsharedPreferences();

       /* SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("北京","101010100");
        editor.apply();*/

        Map<String, ?> allContent = sharedPreferences.getAll();

        //Log.i("-----be focus","--shhare----------"+sharedPreferences.getString(""));

        for(Map.Entry<String, ?> entry : allContent.entrySet()){

            Log.i("sync-value", entry.getKey());
            //cityList.add(entry.getKey());
            cityList.add(entry.getValue().toString());

        }


        try {
            //default location 北京
            String location = WeatherPreferences.getPreferredWeatherLocation(context);


            /*cityList.add("北京");
            cityList.add("武汉");
            cityList.add("上海");
            cityList.add("广州");
            cityList.add("郑州");*/

            /*cityList.add("101010100");
            cityList.add("101010200");
            cityList.add("101010300");
            cityList.add("101010400");
            cityList.add("101010500");*/


//           get 0->5


            Log.i(location,"---------------------");
/**
 * NetworkSecurityConfig: No Network Security Config specified, using platform default
    */
            WeatherMini weatherMini = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                   // .addCallAdapterFactory()
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(WeatherMini.class);
            //JsonObject test1 = weatherMini.query(location).execute().body();
            //String test = test1.get("data").getAsString();


           /* JsonObject bodyTest = weatherMini.query(location).execute().body();
            Log.i("body---",bodyTest.getAsString());
            /***
             * activity && fragment
             * location--->cityList.get(0)
             */
         // weatherMini.query(cityList.get(0)).
            bodyList = new ArrayList<>();
            List<Uri> uris = new ArrayList<>();

            for (String city : cityList){
                Log.i("---syncTask-bodyAdd",city);
                bodyList.add(weatherMini.query(city).clone().execute().body());



            }

           /* bodyList.add(weatherMini.query(cityList.get(0)).execute().body());
            bodyList.add(weatherMini.query(cityList.get(1)).clone().execute().body());
            bodyList.add(weatherMini.query(cityList.get(2)).clone().execute().body());
            bodyList.add(weatherMini.query(cityList.get(3)).clone().execute().body());
            bodyList.add(weatherMini.query(cityList.get(4)).clone().execute().body());
*/
            //JsonObject body = weatherMini.query(cityList.get(0)).execute().body();
            //JsonObject body = weatherMini.query(location).execute().body();

           // weatherMini.query(location).headers().


           // Log.i("ddd---",body.get("data")).toString();






            uris.add(Weather1ContentProvider.CONTENT_URI);
            uris.add(Weather2ContentProvider.CONTENT_URI);
            uris.add(Weather3ContentProvider.CONTENT_URI);
            uris.add(Weather4ContentProvider.CONTENT_URI);
            uris.add(Weather5ContentProvider.CONTENT_URI);
            /* Parse the JSON into a list of weather values*/
            int i = 0;

            ContentResolver moonContentResolver = context.getContentResolver();

            //avoid two same finish-loader
            //delete all--!! Max is 5

            //for (JsonObject body : bodyList){
            for(int j = 0;j<5;j++){
                moonContentResolver.delete(
                        //Weather1ContentProvider.CONTENT_URI,
                        uris.get(j),
                        null,
                        null);
              //  Log.i("contentResolver-delete-"+j,uris.get(j).toString());


            }



            for(JsonObject body : bodyList){
                ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context,body);
                if(weatherValues != null && weatherValues.length != 0){
                /* Get a handle on the ContentResolver to delete and insert data*/

                /*If we have valid results, delete the old data and insert the new
                 Delete old weather data because we don't need to keep multiple days' data*/

                  /*  Log.i("-syncWeather--delete---",uris.get(i).toString());

                    moonContentResolver.delete(uris.get(i),
                            null,
                            null);*/


                    Log.i("-syncWeather--insert---",uris.get(i).toString());

                /* Insert our new weather data into Sunshine's ContentProvider*/

                    moonContentResolver.bulkInsert(
                            //Weather1ContentProvider.CONTENT_URI,
                            uris.get(i),
                            weatherValues);
                }
               // Log.i("-finish insert---",uris.get(i).toString());
              //  Log.i("---i value---",""+i);
                i++;


            }



            /***
             为什么不使用Rx响应式编程。只能在Acitvity中？？？忘了
            compositeDisposable.add(weatherMini.query(location)
                    .flatMap(new Function<Response<JsonObject>, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull Response<JsonObject> jsonObjectResponse) throws Exception {
                            if(jsonObjectResponse.isSuccessful()){
                                Log.i("hello  everyone !!!!!","---------------------");
                                Log.i(jsonObjectResponse.message(),"---------------------");

                                /* Parse the JSON into a list of weather values
                                ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context,jsonObjectResponse.body());
                                if(weatherValues != null && weatherValues.length != 0){

                                /* Get a handle on the ContentResolver to delete and insert data
                                    ContentResolver moonContentResolver = context.getContentResolver();

                                /*If we have valid results, delete the old data and insert the new
                                Delete old weather data because we don't need to keep multiple days' data
                                    moonContentResolver.delete(
                                            WeatherContentProvider.CONTENT_URI,
                                            null,
                                            null);
                                 /* Insert our new weather data into Sunshine's ContentProvider
                                    moonContentResolver.bulkInsert(
                                            WeatherContentProvider.CONTENT_URI,
                                            weatherValues);
                                }

                            }else {
                                String error = jsonObjectResponse.errorBody().toString();
                                Log.i("eeerrr----", error);
                            }
                            return Observable.just("sucess");
                        }
                    })*/
            // .map(jsonObjectResponse -> jsonObjectResponse.body())
            /* .flatMap(new Function<JsonObject, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(@NonNull JsonObject jsonObject) throws Exception {
                            Log.i("hello  everyone !!!!!","---------------------");
                            Log.i(jsonObject.toString(),"---------------------");

                            ContentValues[] weatherValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context,jsonObject);
                            if(weatherValues != null && weatherValues.length != 0){

                                ContentResolver moonContentResolver = context.getContentResolver();

                               urn Observable.just("success");
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .subscribe());*/

            Log.i("---begin notify---","--hello---");

            boolean notificationsEnabled = WeatherPreferences.areNotificationsEnabled(context);

            Log.i("notify--",""+notificationsEnabled);
            if(notificationsEnabled){
                NotificationUtils.notifyUserOfNewWeather(context);

            }







        }catch (Exception e){
             /* Server probably invalid */
            e.printStackTrace();

        }

       // compositeDisposable.clear();

    }
}
