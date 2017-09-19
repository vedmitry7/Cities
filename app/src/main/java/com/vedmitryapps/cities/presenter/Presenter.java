package com.vedmitryapps.cities.presenter;

import android.util.Log;

import com.vedmitryapps.cities.DBHelper;
import com.vedmitryapps.cities.api.DownloadCountriesService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Presenter implements MessagePresenter {

    private MessageView view;

    public Presenter(MessageView view) {
        this.view = view;

        if(false){
            downloadDate();
        }

    }

    private void downloadDate(){

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        retrofit.create(DownloadCountriesService.class)
                .getData().enqueue(new Callback<Map<String, List<String>>>() {
            @Override
            public void onResponse(Call<Map<String, List<String>>> call, Response<Map<String,
                                List<String>>> response) {

                for (String s:response.body().keySet()
                        ) {
                    Log.i("TAG22rt", "foreach - " + s);
                }
                saveToDB(response.body());
            }
            @Override
            public void onFailure(Call<Map<String, List<String>>> call, Throwable t) {
                view.showError();
            }
        });
    }

    @Override
    public void onItemClick(int pos) {
    }

    private void saveToDB(Map<String, List<String>> map){
        DBHelper dbHelper = new DBHelper(view.getContext());
        dbHelper.clearDB();
        dbHelper.fillDB(map);
    }
}
