package com.portfolio.guardian.Activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import com.google.android.gms.maps.model.PolylineOptions;
import com.portfolio.guardian.Firebase.CrimeQuery;
import com.portfolio.guardian.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.portfolio.guardian.DirectionFinder.Route;
import com.portfolio.guardian.DirectionFinder.DirectionFinderListener;
import com.portfolio.guardian.DirectionFinder.DirectionFinder;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback,DirectionFinderListener{

    private GoogleMap mMap;
    // private Button btnFindPath;
    private EditText startaddress;
    private EditText destinationaddress;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ArrayList<String> startEnd = new ArrayList<>();

    private ArrayList<Marker> crimeMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String start =intent.getStringExtra("start");
        String destination =intent.getStringExtra("destination");

        //btnFindPath = findViewById(R.id.btnFindPath);
        startaddress = findViewById(R.id.etOrigin);
        destinationaddress = findViewById(R.id.etDestination);

        startaddress.setText(start);
        destinationaddress.setText(destination);
        sendRequest();

//        btnFindPath.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendRequest();
//            }
//        });


    }

    private void sendRequest() {
        String origin = startaddress.getText().toString();
        String destination = destinationaddress.getText().toString();
        if (origin.isEmpty()) {
            Toast.makeText(this, "Your current location", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Where would you like to go?", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng vancouverDowntown = new LatLng(49.282637, -123.118569);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vancouverDowntown, 16));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
//            CrimeQuery crimeQuery = new CrimeQuery(mMap, crimeMarkers);
//            Route test = new Route();
//            test.startLocation = new LatLng(49.26380906, -123.04123053);
//            test.endLocation = new LatLng(49.25969263, -123.17263202);
//            crimeQuery.execute(test);
            return;
        }
        mMap.setMyLocationEnabled(true);


    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }
        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }
        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();
        startEnd = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(route.endAddress)
                    .position(route.endLocation)));

//            startEnd.add(route.startAddress.toString());
//            System.out.println(route.startAddress.toString());
//            startEnd.add(route.endAddress.toString());
//            System.out.println(route.endAddress.toString());
//


            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}