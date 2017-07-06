package com.pangge.moontest.parseCity;



import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by iuuu on 17/6/24.
 */

public interface RequestInterface {
    @GET("js/citylist.xml")
    Observable<XMLCity> getCity();

}
