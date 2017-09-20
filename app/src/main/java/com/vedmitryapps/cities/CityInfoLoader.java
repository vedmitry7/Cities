package com.vedmitryapps.cities;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.vedmitryapps.cities.api.ApiFactory;
import com.vedmitryapps.cities.model.GeoList;

import java.io.IOException;


public class CityInfoLoader extends AsyncTaskLoader<GeoList> {

    private String cityName;

    public CityInfoLoader(Context context, String cityName) {
        super(context);
        this.cityName = cityName;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public GeoList loadInBackground() {
        try {
            return ApiFactory.getGeonamesService().getPlace(App.USERNAME, cityName).execute().body();
        } catch (IOException e) {
            return null;
        }
    }
}
