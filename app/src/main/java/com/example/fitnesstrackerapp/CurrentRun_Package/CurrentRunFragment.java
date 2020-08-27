package com.example.fitnesstrackerapp.CurrentRun_Package;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CurrentRunFragment extends Fragment implements OnMapReadyCallback, CurrentRunContract.View {

    private static final int TAG_CODE_PERMISSION_LOCATION = 1;
    private static CurrentRunFragment INSTANCE = null;
    CurrentRunPresenter presenter;
    FusedLocationProviderClient client;
    Handler handler = new Handler();
    ArrayList<Location> locations = new ArrayList<>();
    Task<Location> locationTask;
    Location currentLocation;
    View view;
    GoogleMap map;
    MapView mapView;
    TextView distanceTextView, speedTextView, caloriesTextView;
    double distance = 0, speed = 0, speedOnTime, calories = 0;
    String time;
    long sTime, dTime;
    Button startButton, stopButton;
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
        view = inflater.inflate(R.layout.fragment_current_run, container, false);
        return view;
    }

    public static CurrentRunFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CurrentRunFragment();
        }
        return INSTANCE;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        presenter = new CurrentRunPresenter(this, getContext());

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
                if(stopButton.getText() == "Stop" ){
                    stopChronometer();
                }
                else if(stopButton.getText() == "Finish"){
                    finishChronometer();
                }
            }
        });

        distanceTextView.setText("0.0");
        speedTextView.setText("0.0");
        caloriesTextView.setText("0");

    }

    private void startChronometer() {
        if (!chronometerIsOn) {
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            stopButton.setText("Stop");
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            chronometerIsOn = true;
            getLocation();
            locations.add(currentLocation);
            //runnable.run();
        }
    }

//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//
//            handler.postDelayed(this, 5000);
//        }
//    };

    private void getLocation(){
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    mapView.getMapAsync(CurrentRunFragment.this);
                }
            }
        });
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
            //handler.removeCallbacks(runnable);
        }
        dTime = pauseOffset - sTime;
        long hours = TimeUnit.MILLISECONDS.toHours(dTime);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(dTime) - hours *60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(dTime) - hours *60 * 60 - minutes * 60 ;
        time = hours+"h:" + minutes +"m:" + seconds + "s";
        speedOnTime = (int) dTime / 1000;
        getLocation();
        locations.add(currentLocation);

        double startLat = locations.get(0).getLatitude();
        double startLong = locations.get(0).getLongitude();
        double endLat = locations.get(1).getLatitude();
        double endLong = locations.get(1).getLongitude();
        float result = 0;
        Location.distanceBetween(startLat, startLong, endLat, endLong, new float[]{result});

        int length = locations.size();
        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.fillColor(Color.RED);
        polygonOptions.strokeWidth(5);
        for (int i = 0; i < length; i++ ){
            polygonOptions.add(new LatLng(locations.get(i).getLatitude(), locations.get(i).getLongitude()));
        }
        map.addPolygon(polygonOptions);

        distance = distance + result;
        if (distance == 0){
            speed = 0;
            calories = 0;
        }
        else {
            speed = ((distance * 1000) / speedOnTime);
            calories = distance * 65;
            distanceTextView.setText(distance + " km");
            speedTextView.setText(speed + " m/sec");
            caloriesTextView.setText(calories + "cal");
        }
    }

    private void finishChronometer() {
        startButton.setText("Start");
        stopButton.setVisibility(View.GONE);
        stopButton.setText("Stop");
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        Date date = new Date();
        Item item = new Item(distance, time, speed, calories, formatter.format(date));
        presenter.insert(item);
        Toast.makeText(getContext(), "Your run's data is saved", Toast.LENGTH_SHORT).show();
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
            locationTask = client.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(final Location location) {
                    if (location != null) {
                        currentLocation = location;
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                MarkerOptions options = new MarkerOptions().position(latLng).title("I am here");
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
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