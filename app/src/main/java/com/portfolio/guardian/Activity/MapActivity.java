package com.portfolio.guardian.Activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
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
import com.portfolio.guardian.Util.Crime;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private Button btnFindPath;
    private EditText startAddress;
    private EditText destinationAddress;

    private List<Marker> startingPointMarker = new ArrayList<>();
    private List<Marker> destinationPointMarker = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private List<Route> userRoutes = new ArrayList<>();

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
        String start = intent.getStringExtra("start");
        String destination = intent.getStringExtra("destination");

        btnFindPath = findViewById(R.id.btnPathFinder);
        startAddress = findViewById(R.id.etStart);
        destinationAddress = findViewById(R.id.etDest);

        startAddress.setText(start);
        destinationAddress.setText(destination);
        sendRequestFromGuardian();
        startAddress.setText("");
        destinationAddress.setText("");
        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startingPointMarker != null) {
                    for (Marker m : startingPointMarker) {
                        m.remove();
                    }
                }

                if (polylinePaths != null) {
                    for (Polyline p : polylinePaths) {
                        p.remove();
                    }
                }
                sendRequestFromGuardian();
            }
        });
    }

    private void sendRequestFromGuardian() {
        String origin = startAddress.getText().toString();
        String destination = destinationAddress.getText().toString();
        if (origin.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Opps! Maybe you forgot to add something!", Toast.LENGTH_SHORT).show();
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
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        polylinePaths = new ArrayList<>();
        startingPointMarker = new ArrayList<>();
        destinationPointMarker = new ArrayList<>();
        userRoutes = new ArrayList<>();

        String origin = startAddress.getText().toString();
        String destination = destinationAddress.getText().toString();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            startingPointMarker.add(mMap.addMarker(new MarkerOptions()
                    .title(origin)
                    .position(route.startLocation)));

            destinationPointMarker.add(mMap.addMarker(new MarkerOptions()
                    .title(destination)
                    .position(route.endLocation)));


            PolylineOptions polylineOptions =
                    new PolylineOptions().geodesic(true).color(Color.GREEN).width(7);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));
            polylinePaths.add(mMap.addPolyline(polylineOptions));

            userRoutes.add(route);
        }
        CrimeQuery crimeQuery = new CrimeQuery(mMap, crimeMarkers);
        crimeQuery.execute(userRoutes.get(0));
    }

    public List<Route> GetUserRoute(List<Route> r) {
        for (int i = 0; i < userRoutes.size(); i++) {
            System.out.println(userRoutes.get(i).startAddress);
            System.out.println(userRoutes.get(i).endAddress);
            System.out.println(userRoutes.get(i).startLocation);
            System.out.println(userRoutes.get(i).endLocation);
        }
        return r;
    }
}



