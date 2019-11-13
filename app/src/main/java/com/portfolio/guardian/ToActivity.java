package com.portfolio.guardian;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ToActivity extends AppCompatActivity {

    ListView listView;
    DatabaseReference databaseCrime;
    ArrayList<Crime> crimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to);

        databaseCrime = FirebaseDatabase.getInstance().getReference();
        crimeList = new ArrayList<>();
        listView = findViewById(R.id.listViewCrime2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Query testQuery = databaseCrime.orderByChild("NEIGHBOURHOOD").equalTo("West End");
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

                CrimeListAdapter crimeListAdapter = new CrimeListAdapter(ToActivity.this, crimeList);
                listView.setAdapter(crimeListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void moveMap(View view) {
        Intent intent = new Intent(this, NavigateActivity.class);
        startActivity(intent);
    }
}
