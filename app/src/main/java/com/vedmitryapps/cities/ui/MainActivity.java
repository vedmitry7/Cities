package com.vedmitryapps.cities.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.vedmitryapps.cities.LoadData;
import com.vedmitryapps.cities.R;
import com.vedmitryapps.cities.api.service.CityDetails;
import com.vedmitryapps.cities.api.service.DownloadCountries;
import com.vedmitryapps.cities.ui.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    /*private RecyclerView mRecyclerView;
    private List<String> countries = new ArrayList<>();*/

    ExpandableListView expListView;
    ListAdapter expListAdapter;
    List<String> expListTitle;
    Map<String, List<String>> expListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_better);

        /*countries = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.posts_recycle_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);

        CountriesAdapter adapter = new CountriesAdapter(countries);
        mRecyclerView.setAdapter(adapter);*/

        expListView = (ExpandableListView) findViewById(R.id.expListView);

        expListDetail = LoadData.loadData();

        expListTitle = new ArrayList<>(expListDetail.keySet());
        expListAdapter = new ListAdapter(this, expListTitle, expListDetail);

        expListView.setAdapter(expListAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(getApplicationContext(), CityDetails.class);
                intent.putExtra("cityName", expListDetail.get(expListTitle.get(groupPosition))
                        .get(childPosition));
                startActivity(intent);

                Toast.makeText(getApplicationContext(),
                        expListTitle.get(groupPosition)
                                + " : " + expListDetail.get(expListTitle.get(groupPosition))
                                .get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://raw.githubusercontent.com/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        DownloadCountries client = retrofit.create(DownloadCountries.class);

        client.getData().enqueue(new Callback<Map<String, List<String>>>() {
            @Override
            public void onResponse(Call<Map<String, List<String>>> call, Response<Map<String,
                    List<String>>> response) {
                Log.i("TAG22", "Response come.");
                for (String key :response.body().keySet()
                     ) {
                    //countries.add(key);
                    //mRecyclerView.getAdapter().notifyDataSetChanged();
                }
                ArrayList<String> list = new ArrayList<>(response.body().keySet());
                Collections.sort(list, new Comparator<String>() {
                    @Override
                    public int compare(String s, String t1) {
                        return s.compareTo(t1);
                    }
                });
                expListTitle = new ArrayList<>(list);
                expListDetail = response.body();
                expListAdapter.update(expListTitle, expListDetail);
            }

            @Override
            public void onFailure(Call<Map<String, List<String>>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();

            }
        });




    }
}
