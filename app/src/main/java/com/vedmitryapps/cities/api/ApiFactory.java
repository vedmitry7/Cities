package com.vedmitryapps.cities.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiFactory {

    private GeonamesService geonamesServiceClient;

    public static DownloadCountriesService getDownloadCountriesService(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        return retrofit.create(DownloadCountriesService.class);
    }

}
