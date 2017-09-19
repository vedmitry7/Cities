package com.vedmitryapps.cities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Test {
    public static void main(String[] args) {

        List<String> oopLanguages = new ArrayList<>();
        oopLanguages.add("Java");
        oopLanguages.add("C++");
        oopLanguages.add("C#");
        oopLanguages.add("Python");
        oopLanguages.add("Scala");

        Type itemsArrType = new TypeToken<List<String>>() {}.getType();

        String s = new Gson().toJson(oopLanguages);

        List<String> list = new Gson().fromJson(s, itemsArrType);



        System.out.println(list.get(1));
        System.out.println(list.size());
    }
}
