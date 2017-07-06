package com.pangge.moontest.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerViewAdapter;
import com.jakewharton.rxbinding2.widget.RxAdapterView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.pangge.moontest.BaseApplication;
import com.pangge.moontest.City;
import com.pangge.moontest.CityDao;
import com.pangge.moontest.DaoSession;
import com.pangge.moontest.R;


import org.greenrobot.greendao.rx.RxQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by iuuu on 17/6/26.
 */

public class SearchActivity extends AppCompatActivity {
    @BindView(R.id.editSearch)
    EditText editText;

    @BindView(R.id.cityList)
    RecyclerView mRecyclerView;

    private RxQuery<City> cityRxQuery;
    private DaoSession daoSession = BaseApplication.getDaoSession();


    private CompositeDisposable compositeDisposable;


    RecyclerView.Adapter adapter;
    //private SearchAdapter adapter;





    private static List<City> arrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();





        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        adapter = new SearchAdapter(this, arrayList);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter);

        //cityRxQuery = new RxQuery<City>();

        getSearchCity();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();

    }

    private void getSearchCity(){



        compositeDisposable.add(RxTextView.textChanges(editText)
                // .map(CharSequence.->String)
                //  .map(CharSequence->CharSequence.toString())
               // .observeOn(Schedulers.io())

                .flatMap(this::doRxQueryCity)

                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<City>>(){
                    @Override
                    public void onComplete() {
                        Log.i("complete","--whyyyyy-----");

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(e.toString(),"error");
                    }

                    @Override
                    public void onNext(@NonNull List<City> o) {


                        /*if(arrayList.size()>0){
                            for(int i = 0 ; i< arrayList.size();i++){

                                String s = arrayList.get(i).getD2();
                                String cityCode = arrayList.get(i).getD1();
                                Log.i("search-result",s);


                                //e.onNext(o)  －－method 2
                               // String s2 = o.get(i).getD2();
                            }
                        }*/

                        adapter.notifyDataSetChanged();
                    }
                }));

    }
    public Observable<List<City>> doRxQueryCity(CharSequence s){
        return Observable.create(new ObservableOnSubscribe<List<City>>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<List<City>> e) throws Exception {

                arrayList.clear();

                Pattern pinyin = Pattern.compile("[a-z]+");
                Pattern name = Pattern.compile("[\u4e00-\u9fa5]+");
                Log.i("I--input-size>", s.length() + "");


                if (pinyin.matcher(s).matches() && s.length() > 2) {
                    //input is pinyin
                    //模糊查询－－like("%你想匹配的%");
                    //when >=3 ,then query-->防止数据过多
                    //   Log.i("I--input->",s.);

                    cityRxQuery = daoSession.getCityDao()

                            .queryBuilder()
                            .where(CityDao.Properties.D3.like(s + "%"))
                            .rx();



                    cityRxQuery.list()
                            .filter(City -> City.size() > 0)
                            //.map(City -> City.get(0).getD1())
                            .flatMap(new Func1<List<City>, rx.Observable<?>>() {
                                @Override
                                public rx.Observable<?> call(List<City> cities) {
                                    Log.i("I--pinyinlist->size", "" + cities.size());
                                    for (int i = 0; i < cities.size(); i++) {
                                        City city = new City();

                                        Log.i("I-ok2-pin", cities.get(i).getD2());
                                        city.setD1(cities.get(i).getD1());
                                        city.setD2(cities.get(i).getD2());
                                        city.setD3(cities.get(i).getD3());
                                        city.setD4(cities.get(i).getD4());

                                        arrayList.add(city);
                                        // arrayList.set(i,city);



                                    }

                                    e.onNext(arrayList);
                                    return null;
                                }
                            })
                            .subscribe();

                } else if (name.matcher(s).matches()) {
                    //input is chinese
                    Log.i("input--"+s,"----input is chinest");
                    cityRxQuery = daoSession.getCityDao()
                            .queryBuilder()
                            .whereOr(CityDao.Properties.D4.like(s + "%"), CityDao.Properties.D2.like(s + "%"))

                            //   .whereOr(CityDao.Properties.D4.eq(s), CityDao.Properties.D2.eq(s))

                            .rx();
                    cityRxQuery.list()
                            .filter(City -> City.size() > 0)
                            // .map(City -> City.get(0).getD1())

                            .flatMap(new Func1<List<City>, rx.Observable<?>>() {
                                @Override
                                public rx.Observable<?> call(List<City> cities) {
                                    Log.i("->size", "" + cities.size());
                                    Log.i("I-ok1-chinese", cities.get(0).getD1());

                                    for (int i = 0; i < cities.size(); i++) {
                                        City city = new City();

                                        Log.i("I-d1-chinese", cities.get(i).getD1());
                                        Log.i("I-d2-chinese", cities.get(i).getD2());
                                        city.setD1(cities.get(i).getD1());
                                        city.setD2(cities.get(i).getD2());
                                        city.setD3(cities.get(i).getD3());
                                        city.setD4(cities.get(i).getD4());

                                        arrayList.add(city);

                                    }
                                    e.onNext(arrayList);
                                    return null;
                                }
                            })
                            .subscribe();



                }

            }
        }).subscribeOn(Schedulers.newThread());
    }






}
