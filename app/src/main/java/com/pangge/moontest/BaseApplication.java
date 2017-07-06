package com.pangge.moontest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pangge.moontest.component.AppModule;
import com.pangge.moontest.component.DaggerAppComponent;
import com.pangge.moontest.locate.LocateCurrent;
import com.pangge.moontest.parseCity.ParseCity;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by iuuu on 17/5/16.
 */

public class BaseApplication extends Application {
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private static DaoSession daoSession;

    private ParseCity parseCity;

    private CompositeDisposable compositeDisposable;

    @Inject
    SharedPreferences sharedPreferences;



    private static DaoMaster.DevOpenHelper mInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        compositeDisposable = new CompositeDisposable();

        //create database db file
        //SQLiteOpenHelper
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "weathers-db", null);
        //get the created database db file
        db = helper.getWritableDatabase();
        Log.i("hel-app","Main---workd");

        if(null == daoMaster){
            //create masterDao
            daoMaster = new DaoMaster(db);
            //just test getInstance
            //daoMaster = new DaoMaster(helper.getWritableDatabase());
            /**
             *
              *
            daoMaster = new DaoMaster(getInstance(this).getWritableDatabase());
            */
            //create Session session

            daoSession = daoMaster.newSession();
        }


        Weather1ContentProvider.daoSession = daoSession;


        compositeDisposable.add(getCity()
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull String s) throws Exception {
                        //first locate,---after get city code , then query
                        Log.i("getcity---after","MainAcitivity----workd");
                        parseCity.mCompositeDisposable.clear();

                        sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(getApplicationContext()))
                                .build().getsharedPreferences();
                        if(sharedPreferences.getAll().size()==0) {
                            compositeDisposable.add(doLocate()

                                    .subscribe());

                        }
                        return Observable.just("sucess locate");
                    }
                })
                .subscribeOn(Schedulers.newThread())


                .observeOn(Schedulers.io())
                .subscribe());

    }



    public static DaoSession getDaoSession(){
        return daoSession;
    }

  /*  public synchronized static DaoMaster.DevOpenHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new DaoMaster.DevOpenHelper(context,"weathers-db", null);

        }
        return mInstance;
    }*/

    private Observable<String> getCity(){


        Log.i("-main-get city--)(","--what up ----");
        return Observable.create(new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                /**
                 * library-->
                 */
                //parseCity = new ParseCity();
                parseCity = new ParseCity();


                Log.i("---getcity","--what up ---where aore table-");

                // List<Code> list= parseCity.parseXml().blockingFirst();
                String s = parseCity.parseXml().blockingFirst();

                // parseCity.parseXml().blockingFirst()

              /*  daoSession = BaseApplication.getDaoSession();
                cityDao = daoSession.getCityDao().rx();
                int i =0;
                City city;*/
                //before insert, need clear the table
                // cityDao.getDao().deleteAll();
                Log.i("---before insert","--what up ---where aore table-");

              /*  while (i < list.size()){
                    String d1 = list.get(i).d1;
                    String d2 = list.get(i).d2;
                    String d3 = list.get(i).d3;
                    String d4 = list.get(i).d4;
                    city = new City(null,d1,d2,d3,d4);
                    cityDao.getDao().insert(city);

                    i++;
                }*/
                Log.i("---after insert","--what up ---where aore table-");

                e.onNext(s);

            }
        }).subscribeOn(Schedulers.io());




    }
    private Observable<String> doLocate(){
        Log.i("-main-Do--locate--)(","----AaaaAAAAAAAAAwhat up ----");
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.i("current---","before");
                LocateCurrent current = new LocateCurrent(getApplicationContext());


                e.onNext("doLocate sucess");


            }

        }).subscribeOn(Schedulers.newThread());
    }
}
