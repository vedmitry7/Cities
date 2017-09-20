package com.vedmitryapps.cities.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.vedmitryapps.cities.App;
import com.vedmitryapps.cities.CountryLoader;
import com.vedmitryapps.cities.R;
import com.vedmitryapps.cities.database.DBHelper;
import com.vedmitryapps.cities.ui.adapter.ListAdapter;
import com.vedmitryapps.cities.ui.dialog.LoadingDialog;
import com.vedmitryapps.cities.ui.dialog.LoadingView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vedmitryapps.cities.App.APP_PREFERENCES;
import static com.vedmitryapps.cities.App.DB_IS_FILLED;
import static com.vedmitryapps.cities.App.DOWNLOAD_ID;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView expListView;
    private ListAdapter expListAdapter;
    private List<String> expListTitle = new ArrayList<>();
    private Map<String, List<String>> expListDetail = new HashMap<>();
    private LoadingView mLoadingView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        initView();

        if(mSharedPreferences.getBoolean(DB_IS_FILLED, false)){
            showList();
        } else {
            mLoadingView.showLoadingIndicator();
            LoaderCountriesCallback callback = new LoaderCountriesCallback();
            getSupportLoaderManager().initLoader(DOWNLOAD_ID, Bundle.EMPTY, callback);
        }
    }

    private void showList() {
        DBHelper dbHelper = new DBHelper(this);
        expListDetail = dbHelper.getDate();
        expListTitle = new ArrayList<>(expListDetail.keySet());
        Collections.sort(expListTitle, new Comparator<String>() {
            @Override
            public int compare(String s, String t1) {
                return s.compareTo(t1);
            }
        });
        expListAdapter.update(expListTitle, expListDetail);
        mLoadingView.hideLoadingIndicator();
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
                intent.putExtra(App.CITY_NAME, expListDetail.get(expListTitle.get(groupPosition))
                        .get(childPosition));
                startActivity(intent);
                return false;
            }
        });

        mLoadingView = LoadingDialog.view(getSupportFragmentManager());
        mLoadingView.showLoadingIndicator();
    }

    private class LoaderCountriesCallback implements LoaderManager.LoaderCallbacks<Map<String, List<String>>>{

        @Override
        public Loader<Map<String, List<String>>> onCreateLoader(int id, Bundle args) {
            if (id == DOWNLOAD_ID){
                return new CountryLoader(getApplicationContext());
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Map<String, List<String>>> loader, Map<String, List<String>> data) {

            if(data==null){
                Toast.makeText(getApplicationContext(), R.string.something_went_wrong, Toast.LENGTH_LONG).show();
                mLoadingView.hideLoadingIndicator();
                return;
            }

            if(loader.getId() == DOWNLOAD_ID){
                saveToDB(data);
                mSharedPreferences.edit().putBoolean(App.DB_IS_FILLED, true).commit();
                showList();
                mLoadingView.hideLoadingIndicator();
            }
        }

        @Override
        public void onLoaderReset(Loader<Map<String, List<String>>> loader) {
        }
    }

    private void saveToDB(Map<String, List<String>> map){
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.clearDB();
        dbHelper.fillDB(map);
    }
}
