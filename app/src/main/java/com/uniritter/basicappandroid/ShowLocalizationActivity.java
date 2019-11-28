package com.uniritter.basicappandroid;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniritter.basicappandroid.entity.Localization;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowLocalizationActivity extends AppCompatActivity {
    private DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("localization").child("luizemailcom");

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private LocationRequest mLocationRequest;

    private TextView localizationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        for (String provider : locationManager.getAllProviders()) {
            Toast.makeText(getApplicationContext(), provider, Toast.LENGTH_LONG).show();
        }

        localizationText = findViewById(R.id.localization);

        Query lastQuery = myRef.orderByKey().limitToLast(5);


        lastQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Localization> localizations = new ArrayList<>();
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    localizations.add(dsp.getValue(Localization.class));
                }

                System.out.println(localizations);
                printLocalization(localizations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void printLocalization(List<Localization> localizations) {
        String text = "";
        for (Localization l : localizations){
            text += "Data: " + l.getDate() + " - Latitude: " + l.getLatitude() + " - Longitude: " + l.getLongitude() + " - Usuário: " + l.getUser() + "\n\n";
        }
        localizationText.setText(text);
    }

    private void userNotFound() {
        localizationText.setText("Usuário não encontrado");
    }




}

