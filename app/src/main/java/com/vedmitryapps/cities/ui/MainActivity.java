package com.vedmitryapps.cities.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.vedmitryapps.cities.CountryLoader;
import com.vedmitryapps.cities.DBHelper;
import com.vedmitryapps.cities.R;
import com.vedmitryapps.cities.ui.adapter.ListAdapter;
import com.vedmitryapps.cities.ui.dialog.LoadingDialog;
import com.vedmitryapps.cities.ui.dialog.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String APP_PREFERENCES = "prefs";
    ExpandableListView expListView;
    ListAdapter expListAdapter;
    List<String> expListTitle = new ArrayList<>();
    Map<String, List<String>> expListDetail = new HashMap<>();
    LoadingView mLoadingView;
    public static final int downloadId = 234567;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_better);
        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        initView();

        if(sharedPreferences.getBoolean("downloaded", false)){
            Log.i("TAG22rt", "downloaded - true");
            showList();
        } else {
            Log.i("TAG22rt", "downloaded - false");
            mLoadingView.showLoadingIndicator();
            LoaderCountriesCallbacks callback = new LoaderCountriesCallbacks();
            getSupportLoaderManager().initLoader(downloadId, Bundle.EMPTY, callback);
        }
    }

    private void showList() {
        DBHelper dbHelper = new DBHelper(this);
        expListDetail = dbHelper.getDate();
        expListTitle = new ArrayList<>(expListDetail.keySet());
        showList(expListTitle, expListDetail);
    }

    private void initView() {
        expListView = (ExpandableListView) findViewById(R.id.expListView);
        expListAdapter = new ListAdapter(this, expListTitle, expListDetail);
        expListView.setAdapter(expListAdapter);
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                Intent intent = new Intent(getApplicationContext(), CityDetailsActivity.class);
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

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mLoadingView.showLoadingIndicator();
    }

    private class LoaderCountriesCallbacks implements LoaderManager.LoaderCallbacks<Map<String, List<String>>>{

        @Override
        public Loader<Map<String, List<String>>> onCreateLoader(int id, Bundle args) {
            if (id == downloadId){
                return new CountryLoader(getApplicationContext());
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Map<String, List<String>>> loader, Map<String, List<String>> data) {
            if(loader.getId() == downloadId){
                saveToDB(data);
                sharedPreferences.edit().putBoolean("downloaded", true).commit();
                showList();
                mLoadingView.hideLoadingIndicator();
            }
        }

        @Override
        public void onLoaderReset(Loader<Map<String, List<String>>> loader) {
        }
    }

    public void showList(List<String> listTitle, Map listCities) {
        expListAdapter.update(listTitle, listCities);
        mLoadingView.hideLoadingIndicator();
    }



    private void saveToDB(Map<String, List<String>> map){
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.clearDB();
        dbHelper.fillDB(map);
    }
}
