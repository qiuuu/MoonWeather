package com.pangge.moontest.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.pangge.moontest.R;
import com.pangge.moontest.component.AppModule;
import com.pangge.moontest.component.DaggerAppComponent;
import com.pangge.moontest.sync.WeatherSyncUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx.functions.Action1;

/**
 * Created by iuuu on 17/6/29.
 */

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationAdapterViewHolder> {
    private Context mContext;
   // private List<String> citiesName;
    private List<String> sharedCities = new ArrayList<>();

    @Inject
    SharedPreferences sharedPreferences;

    public LocationAdapter(Context mContext) {
        this.mContext = mContext;


        sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(mContext))
                .build().getsharedPreferences();

        Map<String, ?> allContent = sharedPreferences.getAll();
        //  holder.citySelected.setText(sharedPreferences.getString());

        for(Map.Entry<String, ?> entry : allContent.entrySet()){

            // content+=(entry.getKey()+entry.getValue());
            //Log.i("location-bind-key", entry.getKey());
            Log.i("location-bind-value", entry.getValue().toString());
            sharedCities.add(entry.getKey());
            // Log.i("location-bind", content);
        }


    }

    @Override
    public void onBindViewHolder(LocationAdapterViewHolder holder, int position) {
       // holder.citySelected.setText(citiesName.get(position));
        holder.citySelected.setText(sharedCities.get(position));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        /*editor.putString(citiesName.get(position), citiesName.get(position));
        editor.apply();*/



        RxView.clicks(holder.selectDelete)
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object o) throws Exception {

                        editor.remove(sharedCities.get(position));
                        Log.i("--------",sharedCities.get(position));

                        editor.apply();
                        sharedCities.remove(position);
                        if(sharedCities.size()==0){
                            Intent intent = new Intent(mContext,SearchActivity.class);

                            mContext.startActivity(intent);
                        }
                        notifyDataSetChanged();
                        return Observable.just("sucess");
                    }
                })
                .observeOn(Schedulers.io())
                .subscribe(o -> WeatherSyncUtils.startImmediateSync(mContext));


    }

    @Override
    public LocationAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_location, parent, false);

        LocationAdapterViewHolder viewHolder = new LocationAdapterViewHolder(view);



        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return sharedCities.size();
    }

    public class LocationAdapterViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.citySelected)
        TextView citySelected;
        @BindView(R.id.deleteButton)
        Button selectDelete;

        public LocationAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
