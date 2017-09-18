package com.vedmitryapps.cities.api.service;


import com.vedmitryapps.cities.api.model.GeoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeonamesService {

    @GET("/wikipediaSearchJSON")
    Call<GeoList> getPalce(@Query("username") String username,
                           @Query("title") String title);

}

