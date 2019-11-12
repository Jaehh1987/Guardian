package com.portfolio.guardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FromActivity extends AppCompatActivity {

    DatabaseReference databaseCrime;
    ArrayList<JSONObject> jsonCrimeList;
    ArrayList<Crime> crimeList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from);
        databaseCrime = FirebaseDatabase.getInstance().getReference();
        jsonCrimeList = new ArrayList<>();
        crimeList = new ArrayList<>();
        listView = findViewById(R.id.listViewCrime);
    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseCrime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                crimeList.clear();

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

                CrimeListAdapter crimeListAdapter = new CrimeListAdapter(FromActivity.this, crimeList);
                listView.setAdapter(crimeListAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void moveTo(View view) {
        Intent intent = new Intent(this, ToActivity.class);
        startActivity(intent);
    }
}
