package com.pangge.moontest.setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.pangge.moontest.MainActivity;
import com.pangge.moontest.R;
import com.pangge.moontest.sync.WeatherSyncUtils;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by iuuu on 17/6/26.
 */

public class LocationActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.citiesSelected)
            RecyclerView mRecyclerViewSelected;

    RecyclerView.Adapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        ButterKnife.bind(this);




        fab.setOnClickListener(this);

        Log.i("0---------the message","---------location-------");



       /* sharedPreferences = DaggerAppComponent.builder().appModule(new AppModule(getApplication()))
                .build().getsharedPreferences();

        String message = sharedPreferences.getString("test","");

        Log.i("0---------the message","----------------"+message);*/
    }



    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        adapter = new LocationAdapter(this);

        mRecyclerViewSelected.setHasFixedSize(true);

        mRecyclerViewSelected.setLayoutManager(layoutManager);
        mRecyclerViewSelected.setAdapter(adapter);
        Log.i("0---------resume","---------location-------");
        WeatherSyncUtils.startImmediateSync(this);
    }



    @Override
    public void onClick(View view) {
        int count = adapter.getItemCount();
        //限制五个城市
        if(count<5 && count>0){
            Intent intent = new Intent(this,SearchActivity.class);

            startActivity(intent);
        }else if(count==5){
            Toast.makeText(this,"最多添加五个城市",Toast.LENGTH_SHORT).show();
        }

    }
}
