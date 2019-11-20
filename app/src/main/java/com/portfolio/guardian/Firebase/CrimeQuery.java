package com.portfolio.guardian.Firebase;

import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.portfolio.guardian.DirectionFinder.Route;
import com.portfolio.guardian.Util.Crime;
import com.portfolio.guardian.Util.UTM;
import com.portfolio.guardian.Util.WGS84;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CrimeQuery extends AsyncTask<Route, String, ArrayList<Crime>> {

    private final GoogleMap mMap;
    private final ArrayList<Marker> markers;
    DatabaseReference databaseCrime;
    ArrayList<Crime> crimeList;

    public CrimeQuery(final GoogleMap mMap, final ArrayList<Marker> markers) {
        this.mMap = mMap;
        this.markers = markers;
    }

    protected void onPreExecute() {
        databaseCrime = FirebaseDatabase.getInstance().getReference();
        crimeList = new ArrayList<>();
    }

    @Override
    protected ArrayList<Crime> doInBackground(Route... route) {

        // convert global latitude & longitude in route to UTM 10 U coordinate

        LatLng start = route[0].startLocation;
        LatLng end = route[0].endLocation;

        double minLat, maxLat, minLng, maxLng;

        if (start.latitude < end.latitude) {
            minLat = start.latitude;
            maxLat = end.latitude;
        } else {
            minLat = end.latitude;
            maxLat = start.latitude;
        }

        if (start.longitude < end.longitude) {
            minLng = start.longitude;
            maxLng = end.longitude;
        } else {
            minLng = end.longitude;
            maxLng = start.longitude;
        }

        WGS84 wgsMin = new WGS84(minLat, minLng);
        WGS84 wgsMax = new WGS84(maxLat, maxLng);
        final UTM utmMin = new UTM(wgsMin);
        final UTM utmMax = new UTM(wgsMax);

        // query crime data in specific area

        crimeList.clear();

        Query testQuery = databaseCrime
                .orderByChild("X").startAt(utmMin.getEasting() - 100).endAt(utmMax.getEasting() + 100);

        testQuery.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot crimeSnapshot : dataSnapshot.getChildren()) {

                    Crime crime = new Crime();

                    crime.setType((String)crimeSnapshot.child("TYPE").getValue());
                    crime.setNeighborhood((String)crimeSnapshot.child("NEIGHBOURHOOD").getValue());
                    crime.setBlock((String)crimeSnapshot.child("HUNDRED_BLOCK").getValue());
                    long year = (Long) crimeSnapshot.child("YEAR").getValue();
                    long month = (Long) crimeSnapshot.child("MONTH").getValue();
                    long day = (Long) crimeSnapshot.child("DAY").getValue();
                    long hour = (Long) crimeSnapshot.child("HOUR").getValue();
                    long minute = (Long) crimeSnapshot.child("MINUTE").getValue();
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    crime.setDate(date);
                    crime.setX(((Number) crimeSnapshot.child("X").getValue()).longValue());
                    crime.setY(((Number) crimeSnapshot.child("Y").getValue()).longValue());

                    if (crime.getY() > utmMin.getNorthing() - 100 && crime.getY() < utmMax.getNorthing() + 100) {
                        crimeList.add(crime);
                    }
                }
                for (Crime c : crimeList) {
                    UTM utm = new UTM(10, 'U', c.getX(), c.getY());
                    WGS84 wgs = new WGS84(utm);
                    markers.add(mMap.addMarker((new MarkerOptions()
                            .position(new LatLng(wgs.getLatitude(), wgs.getLongitude())))));
                    markers.get(markers.size() - 1).setTag(c);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
        return crimeList;
    }

    @Override
    protected void onProgressUpdate(String... values) {}

    @Override
    protected void onPostExecute(ArrayList<Crime> crimes) {}
}
