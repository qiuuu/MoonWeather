package com.pangge.moontest.network;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by iuuu on 17/5/15.
 */

public interface WeatherMini {

    /*@GET("weather_mini")
    Observable<Response<JsonObject>> query(
            @Query("city") String city
    );*/
<<<<<<< HEAD

    /**
     * http://wthrcdn.etouch.cn/weather_mini?city=北京
       http://wthrcdn.etouch.cn/weather_mini?citykey=101010100
     *
     */
   /* @GET("weather_mini")
    Call<JsonObject> query(
            @Query("city") String city
    );*/

=======
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
    @GET("weather_mini")
    Call<JsonObject> query(
            @Query("citykey") String citykey
    );

<<<<<<< HEAD
    /*@GET("weather_mini")
    Response<JsonObject> query(
            @Query("citykey") String citykey
    );
    @GET("weather_mini")
    Observable<Response<JsonObject>> query(
            @Query("citykey") String citykey
    );*/
=======
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e

}
