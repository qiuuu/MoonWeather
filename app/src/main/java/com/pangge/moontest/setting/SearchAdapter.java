package com.pangge.moontest.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.pangge.moontest.City;
import com.pangge.moontest.MainActivity;
import com.pangge.moontest.R;
import com.pangge.moontest.component.AppModule;
import com.pangge.moontest.component.DaggerAppComponent;


import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import rx.functions.Action1;

/**
 * Created by iuuu on 17/6/28.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchAdapterViewHolder> {

    private final Context mContext;
    private List<City> cities;

    @Inject
    SharedPreferences sharedPreferences;



    public SearchAdapter(Context context, List<City> cities) {
        mContext = context;
        this.cities = cities;



        sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(context))
                .build().getsharedPreferences();


    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchAdapterViewHolder holder, int position) {

        holder.searchText.setText(cities.get(position).getD2());


        RxView.clicks(holder.searchText)

                //.takeUntil(RxView.detaches(parent))
                // .map(aVoid -> view)
                .flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Object o) throws Exception {
                        //    Log.i("sdfsdf",parent.ge);
                     //   String s = searchAdapterViewHolder.searchText.getText().toString();


                        String s = cities.get(position).getD2();
                        String cityCode = cities.get(position).getD1();
                        Log.i("sdfsdf-------"+cityCode,s);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(s, cityCode);

                        editor.apply();
                        Log.i("search-selected",s);

                        Intent intent = new Intent(mContext, LocationActivity.class);
                        mContext.startActivity(intent);
                       return Observable.just("success");
                    }
                })

                .subscribe();


    }



    @Override
    public int getItemCount() {

        return cities.size();
    }

    @Override
    public SearchAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item, parent, false);

       // view.setFocusable(true);
        SearchAdapterViewHolder searchAdapterViewHolder = new SearchAdapterViewHolder(view);


        return searchAdapterViewHolder;
    }

    public class SearchAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.search_cityname)
        TextView searchText;

        public SearchAdapterViewHolder(View view){

            super(view);
            ButterKnife.bind(this, view);
           // view.setOnClickListener(this);
        }


    }


}
