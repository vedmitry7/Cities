package com.vedmitryapps.cities.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.vedmitryapps.cities.R;
import com.vedmitryapps.cities.api.GeonamesService;
import com.vedmitryapps.cities.model.GeoList;
import com.vedmitryapps.cities.model.Geoname;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CityDetails extends AppCompatActivity {

    TextView cityName;
    TextView summary;
    TextView wikiUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);

        cityName = (TextView) findViewById(R.id.cityName);
        summary = (TextView) findViewById(R.id.summary);
        wikiUrl = (TextView) findViewById(R.id.wikiUrl);

        String title = getIntent().getStringExtra("cityName");

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://api.geonames.org/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        GeonamesService client = retrofit.create(GeonamesService.class);

        client.getPlace("vedmitry", title).enqueue(new Callback<GeoList>() {
            @Override
            public void onResponse(Call<GeoList> call, Response<GeoList> response) {
                if(response.body().getGeonames().size()==0){
                    summary.setText(R.string.no_information_on_wiki);
                }
                Geoname geoname = response.body().getGeonames().get(0);
                System.out.println("qqqq " + call.toString());
                System.out.println("qqqq " + geoname.getTitle());
                System.out.println("qqqq " + geoname.getSummary());
                System.out.println("qqqq " + geoname.getFeature());
                System.out.println("qqqq " + geoname.getLang());
                System.out.println("qqqq " + geoname.getElevation());
                System.out.println("qqqq " + geoname.getGeoNameId());
                System.out.println("qqqq " + geoname.getCountryCode());
                System.out.println("qqqq " + geoname.getRank());
                System.out.println("qqqq " + geoname.getThumbnailImg());
                System.out.println("qqqq " + geoname.getWikipediaUrl());
                System.out.println("qqqq " + geoname.getTitle());

                cityName.setText(geoname.getTitle()+"");
                summary.setText(geoname.getSummary());
                wikiUrl.setText(geoname.getWikipediaUrl());

            }

            @Override
            public void onFailure(Call<GeoList> call, Throwable t) {
                System.out.println("qqqq " + call.request().toString());
                System.out.println("qqqq " + call.toString());
                System.out.println("qqqq " + t.getLocalizedMessage());
                System.out.println("qqqq " + t.getMessage());
                System.out.println("qqqq " + t.getCause());
                System.out.println("qqqq " + t);


                Toast.makeText(CityDetails.this, "error2", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
