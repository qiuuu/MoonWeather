package com.pangge.moontest;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pangge.moontest.utilities.MoonWeatherUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iuuu on 17/5/15.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private final Context mContext;

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private boolean mUseTodayLayout;

    private final ForecastAdapterOnClickHandler mClickHandler;
    public interface ForecastAdapterOnClickHandler{
        void onClick(long date);
    }

    private Cursor mCursor;

    public ForecastAdapter(Context context,ForecastAdapterOnClickHandler clickHandler){

        mContext = context;
        mClickHandler = clickHandler;

        mUseTodayLayout = mContext.getResources().getBoolean(R.bool.use_today_layout);
    }

    @Override
    public void onBindViewHolder(ForecastAdapter.ForecastAdapterViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        int viewType = getItemViewType(position);
        int weatherImageId = 0;
        String weatherName = mCursor.getString(MainActivity.INDEX_WEATHER_TYPE);
        switch (viewType){
            case VIEW_TYPE_TODAY:
                weatherImageId = MoonWeatherUtils
                        .getLargeArtDrawableForWeatherCondition(weatherName);
                break;
            case VIEW_TYPE_FUTURE_DAY:
                weatherImageId = MoonWeatherUtils
                        .getSmallArtDrawableForWeatherCondition(weatherName);
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);

        }

        /****************
                * Weather Icon *
         ****************/
       /* String weatherName = mCursor.getString(MainActivity.INDEX_WEATHER_TYPE);
        int weatherImageId = MoonWeatherUtils
                .getSmallArtDrawableForWeatherCondition(weatherName);*/
        holder.iconView.setImageResource(weatherImageId);

        /****************
         * Weather description-TYPE *
         ****************/
        Log.i(weatherName,"--name");
        holder.descriptionView.setText(weatherName);

        /****************
         * Weather Date *
         ****************/
        String weatherDate = mCursor.getString(MainActivity.INDEX_WEATHER_DATE);
        holder.dateView.setText(weatherDate);
        Log.i(weatherDate,"--date");

        /****************
         * Weather High *
         ****************/
        String weatherHigh = mCursor.getString(MainActivity.INDEX_WEATHER_MAX_TEMP);
        holder.highTempView.setText(weatherHigh);
        Log.i(weatherHigh,"--high");

        /****************
         * Weather Low *
         ****************/
        String weatherLow = mCursor.getString(MainActivity.INDEX_WEATHER_MIN_TEMP);
        holder.lowTempView.setText(weatherLow);
        Log.i(weatherLow,"--low");

    }

    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType){
            case VIEW_TYPE_TODAY:
                layoutId = R.layout.list_item_forecast_today;
                break;
            case VIEW_TYPE_FUTURE_DAY:
                layoutId = R.layout.forecast_list_item;
                break;
            default:
                throw new IllegalArgumentException("Invalid view type, value of "+ viewType);
        }
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_list_item, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);

        view.setFocusable(true);

        ForecastAdapterViewHolder forecastAdapterViewHolder = new ForecastAdapterViewHolder(view);
        return forecastAdapterViewHolder;
    }

    @Override
    public int getItemCount() {
        if(null == mCursor) return 0;

        return mCursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(mUseTodayLayout && position == 0){
            return VIEW_TYPE_TODAY;

        }else {
            return VIEW_TYPE_FUTURE_DAY;
        }
       // return super.getItemViewType(position);
    }

    /**
     -
     +     * Swaps the cursor used by the ForecastAdapter for its weather data. This method is called by
     +     * MainActivity after a load has finished, as well as when the Loader responsible for loading
     +     * the weather data is reset. When this method is called, we assume we have a completely new
     +     * set of data, so we call notifyDataSetChanged to tell the RecyclerView to update.
     *
     -
     +     * @param newCursor the new cursor to use as ForecastAdapter's data source
     */

    void swapCursor(Cursor newCursor){
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_date) TextView dateView;
        @BindView(R.id.tv_weather_description) TextView descriptionView;
        @BindView(R.id.tv_high_temperature) TextView highTempView;
        @BindView(R.id.tv_low_temperature) TextView lowTempView;

        @BindView(R.id.tv_imageView) ImageView iconView;
        public ForecastAdapterViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);


        }




        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
          //  String weatherForDay = m
            mCursor.moveToPosition(adapterPosition);

            int date = mCursor.getPosition();
          //  mCursor.get---test get type
            String column3 = mCursor.getColumnName(3);

            Log.i("-date-adapter-Click-", column3);

            Log.i("---date-Finished---", ""+date);
            mClickHandler.onClick(date);

        }



    }

}
