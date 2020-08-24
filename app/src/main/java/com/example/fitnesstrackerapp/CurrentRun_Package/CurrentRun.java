package com.example.fitnesstrackerapp.CurrentRun_Package;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.fitnesstrackerapp.Item;
import com.example.fitnesstrackerapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class CurrentRun extends Fragment implements OnMapReadyCallback, CurrentRunContract.View {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private static CurrentRun INSTANCE = null;
    CurrentRunPresenter presenter;
    FusedLocationProviderClient client;
    View view;
    GoogleMap map;
    MapView mapView;
    TextView distanceTextView;
    TextView speedTextView;
    TextView caloriesTextView;
    double distance = 0;
    String time;
    double speed = 0;
    double calories = 0;
    Button startButton;
    Button stopButton;
    Chronometer chronometer;
    private boolean chronometerIsOn;
    private long pauseOffset;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_run, container, false);
        return view;
    }

    public static CurrentRun getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CurrentRun();
        }
        return INSTANCE;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //presenter = new CurrentRunPresenter(this, );

        mapView = view.findViewById(R.id.mapView);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        distanceTextView = view.findViewById(R.id.distance_textView);
        chronometer = view.findViewById(R.id.chronometer);
        speedTextView = view.findViewById(R.id.speed_textView);
        caloriesTextView = view.findViewById(R.id.calories_textView);
        startButton = view.findViewById(R.id.resume_btn);
        startButton.setVisibility(View.VISIBLE);
        stopButton = view.findViewById(R.id.stop_btn);
        stopButton.setVisibility(View.GONE);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startChronometer();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopChronometer();
            }
        });
    }

    private void startChronometer() {
        if (!chronometerIsOn) {
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            chronometerIsOn = true;
        }
    }

    private void stopChronometer() {
        if(chronometerIsOn) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometerIsOn = false;
            startButton.setVisibility(View.VISIBLE);
            startButton.setText("Resume");
            stopButton.setVisibility(View.VISIBLE);
            stopButton.setText("Finish");
            if (stopButton.isPressed()){
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MMM dd");
                Date date = new Date();
                presenter.insert(new Item(distance, time, speed, calories, formatter.format(date)));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            client = LocationServices.getFusedLocationProviderClient(getContext());

            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    if (location != null) {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                                MarkerOptions options = new MarkerOptions().position(latLng).title("I am there");

                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));

                                googleMap.addMarker(options);
                            }
                        });
                    }
                }
            });
            map.setMyLocationEnabled(true);


        } else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    TAG_CODE_PERMISSION_LOCATION);
        }

    }
}