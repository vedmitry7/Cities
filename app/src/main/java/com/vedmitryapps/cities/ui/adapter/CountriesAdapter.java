package com.vedmitryapps.cities.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vedmitryapps.cities.R;

import java.util.List;


public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.ViewHolder> {

    private List<String> countries;

    public CountriesAdapter(List<String> countries) {
        this.countries = countries;
    }

    @Override
    public CountriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CountriesAdapter.ViewHolder holder, int position) {

        holder.countryName.setText(countries.get(position));
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView countryName;

        public ViewHolder(View itemView) {
            super(itemView);

            countryName = (TextView) itemView.findViewById(R.id.countryName);
        }
    }
}