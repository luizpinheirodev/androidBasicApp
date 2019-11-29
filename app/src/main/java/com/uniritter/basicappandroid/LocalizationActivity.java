package com.uniritter.basicappandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uniritter.basicappandroid.entity.Localization;

import java.text.SimpleDateFormat;
import java.time.Instant;

public class LocalizationActivity extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        Button fab = findViewById(R.id.gps);
        Button rel = findViewById(R.id.rel);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                askPermission();
                serviceConfig();
            }
        });

        rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(LocalizationActivity.this, ShowLocalizationActivity.class);
                startActivity(it);
            }
        });

    }



    private void askPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else
            serviceConfig();
    }

    public void serviceConfig() {
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }

                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                public void onProviderEnabled(String provider) {
                }

                public void onProviderDisabled(String provider) {
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 600, 0, locationListener);
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, locationListener);
        } catch (SecurityException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void atualizar(Location location) {


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


        Localization l = new Localization();
        l.setUser(mAuth.getCurrentUser().getEmail());
        l.setDate(sdf.format(location.getTime()));
        l.setLatitude(location.getLatitude());
        l.setLongitude(location.getLongitude());

        String moment = String.valueOf(Instant.now().getEpochSecond());

        DatabaseReference local = referencia.child("localization");
        local.child(l.getUser().replaceAll("[\\@\\.\\-\\:\\*]", "") + "/" + moment
                .replaceAll("[\\@\\.\\-\\:\\*]", ""))
                .setValue(l);


    }

}

