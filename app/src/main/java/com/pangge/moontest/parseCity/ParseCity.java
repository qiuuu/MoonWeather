package com.pangge.moontest.parseCity;


import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.pangge.moontest.BaseApplication;
import com.pangge.moontest.City;
import com.pangge.moontest.DaoSession;

import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * Created by iuuu on 17/6/25.
 */

public class ParseCity {
    private static final String BASE_URL = "http://mobile.weather.com.cn/";

    public CompositeDisposable mCompositeDisposable;

    private List<Code> list;
    private List<City> cityList;

    private RxDao<City, Long> cityDao;
    private RxQuery<City> cityRxQuery;
    private DaoSession daoSession;

    public ParseCity() {



    }
    public Observable<String> parseXml(){


        mCompositeDisposable = new CompositeDisposable();

        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final ObservableEmitter<String> e) throws Exception {

                RequestInterface requestInterface = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(SimpleXmlConverterFactory.create())

                        .build().create(RequestInterface.class);

                Log.i("---－－－－－－","--what up -g贵--where aore table-");



                mCompositeDisposable.add(requestInterface.getCity()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.newThread())
                        .subscribeWith(new DisposableObserver<XMLCity>(){
                            @Override
                            public void onComplete() {
                              //  Log.i("con","ssuccessful");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.i(e.toString(),"error");
                            }

                            @Override
                            public void onNext(XMLCity value) {
                                cityList = value.cities;
                                daoSession = BaseApplication.getDaoSession();
                                cityDao = daoSession.getCityDao().rx();
                                int i =0;
                                City city;


                                Log.i("---gli---delete","--加油加油－－－－ -g贵--where aore table-");

                                // cityList = (List<City>)(List<?>)list;
                                //before insert, need clear the table
                                cityDao.getDao().deleteAll();
                                Log.i("---gli---brary","--加油加油－－－－ -g贵--where aore table-");

                               // cityDao.getDao().insertInTx(list.);
                                cityDao.getDao().insertInTx(cityList);

                                /*while (i < list.size()){
                                    String d1 = list.get(i).d1;
                                    String d2 = list.get(i).d2;
                                    String d3 = list.get(i).d3;
                                    String d4 = list.get(i).d4;
                                    city = new City(null,d1,d2,d3,d4);
                                    cityDao.getDao().insert(city);
                                    Log.i("---g"+i,"--what 巨慢 -g贵--where aore table-");


                                    i++;
                                }*/
                                Log.i("---gli---brary","--what 巨慢 -g贵--where aore table-");


                                e.onNext("list sucess");

                               /* if (list.size() == 0) {
                                    return;
                                }*/
                            }
                        }));



            }
        }).subscribeOn(Schedulers.io());






    }
}
