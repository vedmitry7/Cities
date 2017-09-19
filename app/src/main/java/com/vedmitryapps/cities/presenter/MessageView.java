package com.vedmitryapps.cities.presenter;

import android.content.Context;

import java.util.List;
import java.util.Map;


public interface MessageView {

    void showList(List<String> listTitle, Map listCities);
    void showError();
    Context getContext();
}
