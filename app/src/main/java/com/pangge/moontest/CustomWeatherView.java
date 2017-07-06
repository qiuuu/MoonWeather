package com.pangge.moontest;

import android.content.Context;


import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iuuu on 17/5/13.
 * 可以删了--此自定义组合视图
 * 因为merge外面不知道怎么加CardView!!
<<<<<<< HEAD
 *
 * !!!不用card View了，用view Pager！！
=======
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
 */

public class CustomWeatherView extends RelativeLayout implements
        View.OnClickListener {
    //@BindViews()
    private View rootView;
   //@BindView(R.id.temp_image) ImageView weatherImage;
    @BindView(R.id.main_type) TextView typeText;
    @BindView(R.id.temp) TextView tempText;
    @BindView(R.id.city) TextView cityText;


    private int id;


    private static final int ID_FORECAST_LOADER = 26;



    public CustomWeatherView(Context context){
        super(context);
        init(context);
    }

    public CustomWeatherView(Context context,AttributeSet attrs){
        super(context,attrs);
        init(context);
    }

    public CustomWeatherView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){

        rootView = inflate(context, R.layout.pager_weather, this);
        ButterKnife.bind(this, rootView);

        setOnClickListener(this);

    }


    /*public void getIdForecastLoader(){
        id = this.id;

    }*/

    public void setIdForecastLoader(int id){
        this.id = id;
    }


    @Override
    public void onClick(View view) {
        Log.i("hello","----workd");
        Intent weatherListIntent = new Intent(getContext(),WeatherListActivity.class);

        weatherListIntent.putExtra("LoaderId", id);

        getContext().startActivity(weatherListIntent);
    }
}
