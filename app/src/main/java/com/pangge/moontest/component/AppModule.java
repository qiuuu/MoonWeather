package com.pangge.moontest.component;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import dagger.Module;
import dagger.Provides;

/**
 * Created by iuuu on 17/7/2.
 */
@Module
public class AppModule {
    Context mcontext;

    public AppModule(Context context) {
        this.mcontext = context;
    }

    @Provides
    Context providesContext(){
        return mcontext;
    }

    @Provides
    public SharedPreferences provideSharedPreferences(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
