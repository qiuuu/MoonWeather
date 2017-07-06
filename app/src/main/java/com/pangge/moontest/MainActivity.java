package com.pangge.moontest;

import android.content.Intent;

import android.content.SharedPreferences;

import android.database.Cursor;

import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;



import com.jakewharton.rxbinding2.view.RxView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.pangge.moontest.component.AppModule;
import com.pangge.moontest.component.DaggerAppComponent;
import com.pangge.moontest.data.WeatherContract;

import com.pangge.moontest.locate.LocateCurrent;
import com.pangge.moontest.pageIndicator.PageIndicatorView;
import com.pangge.moontest.parseCity.ParseCity;
import com.pangge.moontest.setting.LocationActivity;
import com.pangge.moontest.setting.SearchActivity;
import com.pangge.moontest.sync.WeatherSyncUtils;
import com.pangge.moontest.viewpager.ViewPagerAdapter;



import org.greenrobot.greendao.rx.RxDao;
import org.greenrobot.greendao.rx.RxQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.inject.Inject;


import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;

import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{


    public static List<String> selectedCities = new ArrayList<>();


   // private ParseCity parseCity;

    @BindView(R.id.main_type) TextView typeText;
    @BindView(R.id.temp) TextView tempText;
    @BindView(R.id.city) TextView cityText;

    private PageIndicatorView pageIndicatorView;

    private List<View> pageList;

    /**
    *   Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.view.View.setOnClickListener(android.view.View$OnClickListener)' on a null object reference
    *  BindView 没有Bind成功！！为啥
     *  ButterKnife.bind(this) hhhhahahhh
     */

    @BindView(R.id.weatherCard) CardView weatherCard;

    public static final int ID_FORECAST_LOADER1 = 1;
    public static final int ID_FORECAST_LOADER2 = 2;
    public static final int ID_FORECAST_LOADER3 = 3;
    public static final int ID_FORECAST_LOADER4 = 4;
    public static final int ID_FORECAST_LOADER5 = 5;

    /*
     * The columns of data that we are interested in displaying within our MainActivity's list of
     * weather data.
     */
    public static final String[] MAIN_FORECAST_PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_TYPE,
            WeatherContract.WeatherEntry.COLUMN_FX,
            WeatherContract.WeatherEntry.COLUMN_FL,
            WeatherContract.WeatherEntry.COLUMN_CITY,
            WeatherContract.WeatherEntry.COLUMN_WENDU,
            WeatherContract.WeatherEntry.COLUMN_TIP,
    };

    /*
     * We store the indices of the values in the array of Strings above to more quickly be able to
     * access the data from our query. If the order of the Strings above changes, these indices
     * must be adjusted to match the order of the Strings.
     */
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_MAX_TEMP = 1;
    public static final int INDEX_WEATHER_MIN_TEMP = 2;
    public static final int INDEX_WEATHER_TYPE = 3;
    public static final int INDEX_WEATHER_FENGXIANG = 4;
    public static final int INDEX_WEATHER_FENGLI = 5;
    public static final int INDEX_WEATHER_CITY = 6;
    public static final int INDEX_WEATHER_WENDU = 7;
    public static final int INDEX_WEATHER_TIP = 8;



    public static final String BASE_URL = "http://wthrcdn.etouch.cn/";

   // private CompositeDisposable compositeDisposable;
    //private CardView card1;

    private RxDao<City, Long> cityDao;
    private RxQuery<City> cityRxQuery;
    private DaoSession daoSession;
    private CompositeDisposable compositeDisposable;

    @Inject
    SharedPreferences sharedPreferences;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        compositeDisposable = new CompositeDisposable();

        //remember to clear compositiDisposable in parseCity

        //not the first
        //syncTask();


        //card1 = (CardView)findViewById(R.id.weatherCard);
        //card1.setOnClickListener(this);

        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER1, null, this);
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER2, null, this);
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER3, null, this);
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER4, null, this);
        getSupportLoaderManager().initLoader(ID_FORECAST_LOADER5, null, this);


        weatherCard.setOnClickListener(this);
        Log.i("create","MainAcitivity----workd");



    }
    private void syncTask(){
        Log.i("sync－－－","MainAcitivity----workd");
        /**
         * onResume-->back to refresh
         *
         * 下拉或定时刷新 better
         */
        /**WeatherSyncUtils.initialize(this);
         WeatherSyncUtils.startImmediateSync(this);
         */

        /**
         * call initialize instead of startImmediateSync
         */
        WeatherSyncUtils.initialize(this);

        //once open the app, sync weather
        WeatherSyncUtils.startImmediateSync(this);



    }






    @Override
    protected void onResume() {
        super.onResume();
        pageList = new ArrayList<>();
        Log.i("==Main-resume---",pageList.size()+"-------------------");

        //locate current city





    }






    private void initCustomViews(){
        final ViewPagerAdapter adapter = new ViewPagerAdapter();
        //adapter.setData(createPageList());
        adapter.setData(pageList);

        //ViewPager pagerWeather = (ViewPager)findViewById(R.id.viewPager);
        //CustomWeatherView view = new CustomWeatherView();


        ViewPager pager = (ViewPager)findViewById(R.id.viewPager);
        pager.setAdapter(adapter);

        pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(pager);
        /**Basic usage in java
        *
        RxViewPager.pageSelections(pager)
                .subscribe(new Action1<Integer>(){
                    @Override
                    public void call(Integer integer) {

                    }
                });
        */

        /**Basic usage in kotlin
         *
         * viewPager.pageSelections()
                    .subscribe{

         }
         */
        //cannot resolve method ..Integer

       /* RxViewPager.pageSelections(pager)


                .subscribe(integer -> changeToList(integer));*/


        //  pageIndicatorView.setViewPager(pagerWeather);
    }
    private void changeToList(String s){
        Log.i("rxViewPager",s);

    }


    @android.support.annotation.NonNull
    private CustomWeatherView createPageView(int color, int LoaderId) {
        View view = new View(this);
        view.setBackgroundColor(getResources().getColor(color));


        CustomWeatherView v = new CustomWeatherView(this);
        v.setIdForecastLoader(LoaderId);
        v.setBackgroundColor(getResources().getColor(color));
        /**
         * Rxbinding viewpager click
         */

        RxView.clicks(view)
                .subscribe(o -> changeToList(o.toString()));




        return v;
    }


    @Override
    protected void onPause() {
        super.onPause();
      //  compositeDisposable.clear();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri forecastQueryUri;


        switch (id){
            case ID_FORECAST_LOADER1:
                /* URI for all rows of weather data in our weather table */

                forecastQueryUri = Weather1ContentProvider.CONTENT_URI;
                /* Sort order: Ascending by date */

               // String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                //date 格式 并不标准

                //String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();
                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);

            case ID_FORECAST_LOADER2:
                /* URI for all rows of weather data in our weather table */

                forecastQueryUri = Weather2ContentProvider.CONTENT_URI;
                /* Sort order: Ascending by date */

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            case ID_FORECAST_LOADER3:
                /* URI for all rows of weather data in our weather table */

                forecastQueryUri = Weather3ContentProvider.CONTENT_URI;
                /* Sort order: Ascending by date */

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            case ID_FORECAST_LOADER4:
                /* URI for all rows of weather data in our weather table */

                forecastQueryUri = Weather4ContentProvider.CONTENT_URI;
                /* Sort order: Ascending by date */

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            case ID_FORECAST_LOADER5:
                /* URI for all rows of weather data in our weather table */

                forecastQueryUri = Weather5ContentProvider.CONTENT_URI;
                /* Sort order: Ascending by date */

                return new CursorLoader(this,
                        forecastQueryUri,
                        MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        int id = loader.getId();


        switch (id){
            case 1:
                displayViewPager(data, id);
                break;
            case 2:
                displayViewPager(data, id);
                break;
            case 3:
                displayViewPager(data, id);
                break;
            case 4:
                displayViewPager(data, id);
                break;
            case 5:
                displayViewPager(data, id);
                break;
            default:
                break;
        }




        //If mPosition equals RecyclerView.NO_POSITION, set it to 0
        //if(mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        // Smooth scroll the RecyclerView to mPosition
        // mRecyclerView.smoothScrollToPosition(mPosition);
        //If the Cursor's size is not equal to 0, call showWeatherDataView
        // if(data.getCount() != 0) showWeatherDataView();
    }

    private void displayViewPager(Cursor data, int id){

        sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(this))
                .build().getsharedPreferences();

        Map<String, ?> allContent = sharedPreferences.getAll();
        //  holder.citySelected.setText(sharedPreferences.getString());
        int size = 0;

        for(Map.Entry<String, ?> entry : allContent.entrySet()){
            size++;




        }
     //   Log.i("--be-add-pagelist---"+pageList.size(), size+"Shared");

        if(pageList.size() >= size){
            pageList.clear();
          //  Log.i("---load finish test---", "－－－－分界线－－－");

        }


        if(data.moveToFirst()){



            String city = data.getString(INDEX_WEATHER_CITY);
            String wendu = data.getString(INDEX_WEATHER_WENDU);
            String type = data.getString(INDEX_WEATHER_TYPE);
            // wendu = wendu.  "°c"

            String sTempFormat = getResources().getString(R.string.temp);
            String sFinalTemp = String.format(sTempFormat,wendu);
            cityText.setText(city);
            tempText.setText(sFinalTemp);
            typeText.setText(type);



          //  Log.i("---load finish test---", city);




            //List<CustomWeatherView> list = new ArrayList<>();


           /* for(int i = 0 ;i < 6; i++){
                CustomWeatherView custom = createPageView(R.color.google_green);

                list.add(custom);
                list.get(i).cityText.setText(city);
                list.get(i).tempText.setText(sFinalTemp);
                list.get(i).typeText.setText(type);
            }*/
          //  pageList.addAll(list);

            CustomWeatherView custom = createPageView(R.color.google_red,id);
            custom.cityText.setText(city);
            custom.tempText.setText(sFinalTemp);
            custom.typeText.setText(type);


            pageList.add(custom);
           // Log.i("pageListsize--","after add-"+pageList.size());
           /* pageList.add(createPageView(R.color.google_blue));
            pageList.add(createPageView(R.color.google_yellow));
            pageList.add(createPageView(R.color.google_green));
            pageList.add(createPageView(R.color.google_purple));*/




            initCustomViews();
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
         /*
          * Since this Loader's data is now invalid, we need to clear the Adapter that is
          * displaying the data.
          */

        /**
         * app destroy ---> 才调用此函数
         */
        Log.i("pageListsize--","--------------为 is 烦恼--loader---reset");




    }

    @Override
    public void onClick(View view) {

      //  Log.i("hello--","MainAcitivity----work");
        Intent weatherListIntent = new Intent(this,WeatherListActivity.class);
        weatherListIntent.putExtra("LoaderId", 3);
        startActivity(weatherListIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.forecast, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //if(id == R.id.a)
        if(id == R.id.action_map){
            Intent intent = new Intent(this, LocationActivity.class);
            startActivity(intent);



            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
