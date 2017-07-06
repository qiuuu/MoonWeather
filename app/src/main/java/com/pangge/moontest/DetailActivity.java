package com.pangge.moontest;


<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
<<<<<<< HEAD

=======
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

<<<<<<< HEAD
=======
import com.pangge.moontest.data.WeatherContract;
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
import com.pangge.moontest.databinding.ActivityDetailBinding;
import com.pangge.moontest.utilities.MoonWeatherUtils;

/**
 * Created by iuuu on 17/5/15.
 */

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private Uri mUri;

    private static final int ID_DETAIL_LOADER = 35;

    private ActivityDetailBinding mDetailBinding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       // setContentView(R.layout.activity_detail);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

<<<<<<< HEAD
       /* Intent intent = getIntent();
        int loadId = intent.getIntExtra("LoaderId", 0);
        Log.i("-detail---loadid",""+loadId);*/



=======
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e

        mUri = getIntent().getData();
        if(mUri == null) throw new NullPointerException("URI for DetailActivity cannot be null");

<<<<<<< HEAD
        Log.i("detail --date--uri---",mUri.toString());
       // getSupportLoaderManager().initLoader(ID_DETAIL_LOADER,null,this);
=======
        Log.i("detail --date-----","----onCreate");
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
        getSupportLoaderManager().initLoader(ID_DETAIL_LOADER,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.i("date-----","-----onCreateLoader");
        switch (id){
            case ID_DETAIL_LOADER:
                return new CursorLoader(this,
                        mUri,
                        MainActivity.MAIN_FORECAST_PROJECTION,
                        null,
                        null,
                        null);
<<<<<<< HEAD

            default:
                throw new RuntimeException("Loader not Implemented:" + id);
        }

=======
            default:
                throw new RuntimeException("Loader not Implemented:" + id);
        }
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.i("date-----","-------onLoaderFinished");
        boolean cursorHasValidData = false;
        if(data != null && data.moveToFirst()){
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }
        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;

        }

        /****************
         * Weather Icon *
         ****************/
        String weatherName = data.getString(MainActivity.INDEX_WEATHER_TYPE);
        int weatherImageId = MoonWeatherUtils
                .getSmallArtDrawableForWeatherCondition(weatherName);
        mDetailBinding.primaryInfo.imageView.setImageResource(weatherImageId);

        /****************
         * Weather date *
         ****************/

        String weatherDate = data.getString(MainActivity.INDEX_WEATHER_DATE);
        Log.i("Detail-date--",weatherDate);
        mDetailBinding.primaryInfo.date.setText(weatherDate);
        /****************
         * Weather type *
         ****************/

        String weatherType = data.getString(MainActivity.INDEX_WEATHER_TYPE);

        mDetailBinding.primaryInfo.type.setText(weatherType);
        /****************
         * Weather high *
         ****************/

        String weatherHigh = data.getString(MainActivity.INDEX_WEATHER_MAX_TEMP);

        mDetailBinding.primaryInfo.high.setText(weatherHigh);
        /****************
         * Weather low *
         ****************/

        String weatherLow = data.getString(MainActivity.INDEX_WEATHER_MIN_TEMP);

        mDetailBinding.primaryInfo.low.setText(weatherLow);
        /****************
         * Weather fengxiang *
         ****************/

        String weatherFengXiang = data.getString(MainActivity.INDEX_WEATHER_FENGXIANG);
        Log.i("index----",data.getCount()+"");

        mDetailBinding.extraDetails.fengxiangLabel.setText(weatherFengXiang);
        /****************
         * Weather fengli*
         ****************/
        String weatherFengLi = data.getString(MainActivity.INDEX_WEATHER_FENGLI);
        mDetailBinding.extraDetails.fengliLabel.setText(weatherFengLi);




    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate();
        //hello git

        return super.onCreateOptionsMenu(menu);
    }
}
