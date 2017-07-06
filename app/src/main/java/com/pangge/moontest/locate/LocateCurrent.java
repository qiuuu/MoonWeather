package com.pangge.moontest.locate;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.pangge.moontest.BaseApplication;
import com.pangge.moontest.City;
import com.pangge.moontest.CityDao;
import com.pangge.moontest.DaoSession;
import com.pangge.moontest.MainActivity;
import com.pangge.moontest.component.AppModule;
import com.pangge.moontest.component.DaggerAppComponent;
import com.pangge.moontest.setting.SearchActivity;
import com.pangge.moontest.sync.WeatherSyncUtils;

import org.greenrobot.greendao.rx.RxQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by iuuu on 17/7/4.
 */

public class LocateCurrent {
    private LocationClient mLocationClient = null;
    @Inject
    SharedPreferences sharedPreferences;

    private RxQuery<City> cityRxQuery;
    private DaoSession daoSession;

    public String district;
    private Context mContext;



    public BDLocationListener myListener = new MyLocationListener();
    private CompositeDisposable compositeDisposable;

    private static List<City> arrayList = new ArrayList<>();

    public LocateCurrent(Context mContext) {
        this.mContext = mContext;
        mLocationClient = new LocationClient(mContext);
        daoSession = BaseApplication.getDaoSession();

        mLocationClient.registerLocationListener(myListener);
        compositeDisposable = new CompositeDisposable();

        sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(mContext))
                .build().getsharedPreferences();
        initLocation();
        Log.i("locate","---constructor");


    }

    public void initLocation(){
        Log.i("init-locate","---constructor");

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        int span=100000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mLocationClient.requestLocation();
    }

    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);


            if(location.getLocType() == BDLocation.TypeNetWorkLocation){

                int length = location.getDistrict().length();
                district = location.getDistrict().substring(0,length-1);

                int cityLength = location.getCity().length();
                String city = location.getCity().substring(0,cityLength-1);

                if(district.length()==0){

                    Log.i("first?-city","---------------------first!!----");

                    compositeDisposable.add(doRxQueryCity(city)

                            .observeOn(Schedulers.io())
                            .subscribe(s ->  WeatherSyncUtils.startImmediateSync(mContext)));

                }else {
                    Log.i("first?-district"+district,"---------------------first!!-test 上海---");

                    compositeDisposable.add(doRxQueryCity(district)
                            .observeOn(Schedulers.io())
                            .subscribe(s ->  WeatherSyncUtils.startImmediateSync(mContext)));

                }


                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            }else if (location.getLocType() == BDLocation.TypeNetWorkException){
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            }else if(location.getLocType() == BDLocation.TypeCriteriaException){
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般" +
                        "会造成这种结果，可以试着重启手机");
            }


            Log.i("BaiduLocationApiDem", sb.toString());
        }



        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }

    public Observable<String> doRxQueryCity(String s){
        return Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {

                arrayList.clear();


                Pattern name = Pattern.compile("[\u4e00-\u9fa5]+");



                if (name.matcher(s).matches()) {
                    //input is chinese
                    cityRxQuery = daoSession.getCityDao()
                            .queryBuilder()
                            .where(CityDao.Properties.D2.like(s + "%"))

                            //   .whereOr(CityDao.Properties.D4.eq(s), CityDao.Properties.D2.eq(s))

                            .rx();
                    cityRxQuery.list()
                            .filter(City -> City.size() > 0)
                            // .map(City -> City.get(0).getD1())


                            .flatMap(new Func1<List<City>, rx.Observable<?>>() {
                                @Override
                                public rx.Observable<?> call(List<City> cities) {
                                    Log.i("input-new-"+s,"----input is chinest－－－"+cities.get(0).getD1());


                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(cities.get(0).getD2(), cities.get(0).getD1());

                                    editor.apply();


                                    e.onNext("sucess");
                                  //  WeatherSyncUtils.startImmediateSync();
                                    return null;
                                }
                            })

                            .subscribe();



                }

            }
        }).subscribeOn(Schedulers.newThread());
    }

}
