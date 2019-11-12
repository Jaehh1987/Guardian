package com.portfolio.guardian;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CrimeListAdapter extends ArrayAdapter<Crime> {

    private Activity context;
    private List<Crime> crimeList;

    public CrimeListAdapter(Activity context, List<Crime> crimeList) {
        super(context, R.layout.list_layout, crimeList);
        this.context = context;
        this.crimeList = crimeList;
    }

    public CrimeListAdapter(Activity context, int resource, List<Crime> objects, Activity context1, List<Crime> crimeList) {
        super(context, resource, objects);
        this.context = context1;
        this.crimeList = crimeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView tvListTypeValue = listViewItem.findViewById(R.id.tvListTypeValue);
        TextView tvListDateTimeValue = listViewItem.findViewById(R.id.tvListDateTimeValue);
        TextView tvListNeighborhoodValue = listViewItem.findViewById(R.id.tvListNeighborhoodValue);
        TextView tvListLocationXValue = listViewItem.findViewById(R.id.tvListLocationXValue);
        TextView tvListLocationYValue = listViewItem.findViewById(R.id.tvListLocationYValue);

        Crime crime = crimeList.get(position);

        tvListTypeValue.setText(crime.getType());
        SimpleDateFormat dest = new SimpleDateFormat(("dd/MMM/yyyy hh:mm"), Locale.ENGLISH);
        tvListDateTimeValue.setText(dest.format(crime.getDate()));
        tvListNeighborhoodValue.setText(crime.getNeighborhood());
        tvListLocationXValue.setText(String.valueOf(crime.getX()));
        tvListLocationYValue.setText(String.valueOf(crime.getY()));

        return listViewItem;
    }
}