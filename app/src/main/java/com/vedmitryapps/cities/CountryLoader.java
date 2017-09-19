package com.vedmitryapps.cities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.vedmitryapps.cities.api.ApiFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CountryLoader extends AsyncTaskLoader<Map<String, List<String>>>{

    public CountryLoader(Context context) {
        super(context);
        Log.i("TAG22rt", "create loader");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public Map<String, List<String>> loadInBackground() {
        try {
            return ApiFactory.getDownloadCountriesService().getData().execute().body();
        } catch (IOException e) {
            return null;
        }
    }
}
