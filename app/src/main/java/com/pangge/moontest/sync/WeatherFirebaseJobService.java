package com.pangge.moontest.sync;

import android.content.Context;
import android.os.AsyncTask;
<<<<<<< HEAD
import android.util.Log;
=======
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

/**
 * Created by iuuu on 17/5/21.
 */

public class WeatherFirebaseJobService extends JobService {

    private AsyncTask<Void, Void, Void> mFetchWeatherTask;

    @Override
    public boolean onStartJob(JobParameters job) {
        mFetchWeatherTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Context context = getApplicationContext();
<<<<<<< HEAD
               // WeatherSyncTask.syncWeather(context);
                WeatherSyncTask task = new WeatherSyncTask();

                task.syncWeather(context);
                Log.i("after 3 hour","query syncWeather");
=======
                WeatherSyncTask.syncWeather(context);
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                jobFinished(job, false);

            }
        };
        mFetchWeatherTask.execute();
<<<<<<< HEAD
        return true;
=======
        return false;
>>>>>>> 880b76cfb78fe7545fb118abe68f626a27df344e
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if(mFetchWeatherTask != null){
            mFetchWeatherTask.cancel(true);
        }
        return true;
    }
}
