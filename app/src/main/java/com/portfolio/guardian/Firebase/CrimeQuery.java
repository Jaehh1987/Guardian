package com.portfolio.guardian.Firebase;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.portfolio.guardian.Util.Crime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CrimeQuery extends AsyncTask<String, String, ArrayList<Crime>> {

    private final Activity activity;
    private final ListView listView;
    DatabaseReference databaseCrime;
    ArrayList<Crime> crimeList;

    public CrimeQuery(final Activity activity, final ListView listView) {
        this.activity = activity;
        this.listView = listView;
    }

    protected void onPreExecute() {
        databaseCrime = FirebaseDatabase.getInstance().getReference();
        crimeList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Crime> doInBackground(String... strings) {

        String neighborhood = strings[0];
        crimeList.clear();

        Query testQuery = databaseCrime.orderByChild("NEIGHBOURHOOD").equalTo(neighborhood);
        testQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot crimeSnapshot : dataSnapshot.getChildren()) {

                    Crime crime = new Crime();

                    crime.setType((String)crimeSnapshot.child("TYPE").getValue());
                    crime.setNeighborhood((String)crimeSnapshot.child("NEIGHBOURHOOD").getValue());
                    long year = (Long) crimeSnapshot.child("YEAR").getValue();
                    long month = (Long) crimeSnapshot.child("MONTH").getValue();
                    long day = (Long) crimeSnapshot.child("DAY").getValue();
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    crime.setDate(date);
                    crime.setX(((Number) crimeSnapshot.child("X").getValue()).longValue());
                    crime.setY(((Number) crimeSnapshot.child("X").getValue()).longValue());

                    crimeList.add(crime);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        return crimeList;
    }

    @Override
    protected void onProgressUpdate(String... values) {}

    @Override
    protected void onPostExecute(ArrayList<Crime> crimes) {}
}
