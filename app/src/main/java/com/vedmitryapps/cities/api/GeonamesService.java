package com.vedmitryapps.cities.api;


import com.vedmitryapps.cities.model.GeoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeonamesService {

    @GET("/wikipediaSearchJSON")
    Call<GeoList> getPlace(@Query("username") String username,
                           @Query("title") String title);

}

