package com.pangge.moontest;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.pangge.moontest.data.WeatherContract;
import com.pangge.moontest.setting.LocationActivity;
import com.pangge.moontest.sync.WeatherSyncUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iuuu on 17/5/18.
 */

public class WeatherListActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>, ForecastAdapter.ForecastAdapterOnClickHandler{

    @BindView(R.id.recyclerview_forecast) RecyclerView mRecyclerView;
    // private RecyclerView mRecyclerView;
    private ForecastAdapter mForecastAdapter;

    private int mPosition = RecyclerView.NO_POSITION;

    private int loadId;

    private static final int ID_FORECAST_LOADER = 22;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mForecastAdapter = new ForecastAdapter(this, this);
        mRecyclerView.setAdapter(mForecastAdapter);

        //compositeDisposable = new CompositeDisposable();
        Log.i("enter retrofit","---------!!!!!!!");
        // loadWeatherData();
        loadId = getIntent().getIntExtra("LoaderId", 0);

       // getSupportLoaderManager().initLoader(ID_FORECAST_LOADER, null, this);
        getSupportLoaderManager().initLoader(loadId, null, this);




}


    private void openPreferredLocationInMap(){

    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri forecastQueryUri;
        switch (id){
            case MainActivity.ID_FORECAST_LOADER1:
                /* URI for all rows of weather data in our weather table */
                forecastQueryUri = Weather1ContentProvider.CONTENT_URI;
                /* Sort order: Ascending by date */

                // String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
                //date 格式 并不标准

                //String selection = WeatherContract.WeatherEntry.getSqlSelectForTodayOnwards();
                return new CursorLoader(this,
                        forecastQueryUri,
                        MainActivity.MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            case MainActivity.ID_FORECAST_LOADER2:
                /* URI for all rows of weather data in our weather table */
                forecastQueryUri = Weather2ContentProvider.CONTENT_URI;

                return new CursorLoader(this,
                        forecastQueryUri,
                        MainActivity.MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            case MainActivity.ID_FORECAST_LOADER3:
                /* URI for all rows of weather data in our weather table */
                forecastQueryUri = Weather3ContentProvider.CONTENT_URI;
                return new CursorLoader(this,
                        forecastQueryUri,
                        MainActivity.MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            case MainActivity.ID_FORECAST_LOADER4:
                /* URI for all rows of weather data in our weather table */
                forecastQueryUri = Weather4ContentProvider.CONTENT_URI;

                return new CursorLoader(this,
                        forecastQueryUri,
                        MainActivity.MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            case MainActivity.ID_FORECAST_LOADER5:
                /* URI for all rows of weather data in our weather table */
                forecastQueryUri = Weather5ContentProvider.CONTENT_URI;

                return new CursorLoader(this,
                        forecastQueryUri,
                        MainActivity.MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Call mForecastAdapter's swapCursor method and pass in the new Cursor
        mForecastAdapter.swapCursor(data);
        //If mPosition equals RecyclerView.NO_POSITION, set it to 0
        if(mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        // Smooth scroll the RecyclerView to mPosition
        mRecyclerView.smoothScrollToPosition(mPosition);
        //If the Cursor's size is not equal to 0, call showWeatherDataView
        if(data.getCount() != 0) showWeatherDataView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /*
          * Since this Loader's data is now invalid, we need to clear the Adapter that is
          * displaying the data.
          */
        mForecastAdapter.swapCursor(null);
    }

    /**
     * not useful
     * @param date
     */
    @Override
    public void onClick(long date) {

        Intent weatherDetailIntent = new Intent(WeatherListActivity.this,DetailActivity.class);

        Uri uriForDateClicked = WeatherContract.WeatherEntry.buildWeatherUriWithData(date, loadId);
        weatherDetailIntent.setData(uriForDateClicked);

        Log.i("---to detail Act", date+"");
        startActivity(weatherDetailIntent);
    }
    private void showWeatherDataView(){
        mRecyclerView.setVisibility(View.VISIBLE);

    }


}
