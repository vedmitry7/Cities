package com.vedmitryapps.cities.ui;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.vedmitryapps.cities.CityInfoLoader;
import com.vedmitryapps.cities.R;
import com.vedmitryapps.cities.model.GeoList;
import com.vedmitryapps.cities.model.Geoname;
import com.vedmitryapps.cities.ui.dialog.LoadingDialog;
import com.vedmitryapps.cities.ui.dialog.LoadingView;

import static com.vedmitryapps.cities.App.CITY_NAME;
import static com.vedmitryapps.cities.App.DOWNLOAD_ID;
import static com.vedmitryapps.cities.R.id.cityName;

public class CityDetailsActivity extends AppCompatActivity {

    private TextView mCityName;
    private TextView mSummary;
    private TextView mWikiUrl;
    private LoadingView mLoadingView;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_details);

        initView();
        mTitle = getIntent().getStringExtra(CITY_NAME);

        LoaderCityCallback callback = new LoaderCityCallback(mTitle);
        getSupportLoaderManager().initLoader(DOWNLOAD_ID, Bundle.EMPTY, callback);

    }
    private void initView() {
        mCityName = (TextView) findViewById(cityName);
        mSummary = (TextView) findViewById(R.id.summary);
        mWikiUrl = (TextView) findViewById(R.id.wikiUrl);
        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mLoadingView.showLoadingIndicator();
    }


    private class LoaderCityCallback implements LoaderManager.LoaderCallbacks<GeoList> {

        String cityName;

        public LoaderCityCallback(String cityName) {
            this.cityName = cityName;
        }

        @Override
        public Loader<GeoList> onCreateLoader(int id, Bundle args) {
            if (id == DOWNLOAD_ID){
                return new CityInfoLoader(getApplicationContext(), cityName);
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<GeoList> loader, GeoList data) {
            if(data==null){
                Toast.makeText(getApplicationContext(), R.string.error,Toast.LENGTH_SHORT).show();
                mCityName.setText(mTitle);
                mSummary.setText(R.string.error);
                mLoadingView.hideLoadingIndicator();
                return;
            }

            if (loader.getId() == DOWNLOAD_ID) {
                if (data.getGeonames().size() == 0) {
                    mCityName.setText(mTitle);
                    mSummary.setText(R.string.no_information_on_wiki);
                    mLoadingView.hideLoadingIndicator();
                    return;
                }
                Geoname geoname = data.getGeonames().get(0);
                mCityName.setText(geoname.getTitle() + "");
                mSummary.setText(geoname.getSummary());
                mWikiUrl.setText("https://" + geoname.getWikipediaUrl());
                mLoadingView.hideLoadingIndicator();
            }
        }

        @Override
        public void onLoaderReset(Loader<GeoList> loader) {
        }
    }
}