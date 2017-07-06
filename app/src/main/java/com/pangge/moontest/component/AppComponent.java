package com.pangge.moontest.component;

import android.content.SharedPreferences;

import dagger.Component;

/**
 * Created by iuuu on 17/7/2.
 */
@Component(modules = {AppModule.class})
public interface AppComponent {

    SharedPreferences getsharedPreferences();
}
