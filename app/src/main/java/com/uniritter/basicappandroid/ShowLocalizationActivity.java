package com.uniritter.basicappandroid;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.LocationRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniritter.basicappandroid.entity.Localization;

import java.util.ArrayList;
import java.util.List;

public class ShowLocalizationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private DatabaseReference myRef;


    private LocationRequest mLocationRequest;

    private TextView localizationText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rel);

        String email = mAuth.getCurrentUser().getEmail();
        String child = email.replaceAll("[\\@\\.\\-\\:\\*]", "");

        myRef = FirebaseDatabase.getInstance().getReference("localization").child(child);


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Button logout = findViewById(R.id.logoutShow);

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

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });


    }

    private void printLocalization(List<Localization> localizations) {
        String text = "";
        for (Localization l : localizations) {
            text += "Data: " + l.getDate() + " - Latitude: " + l.getLatitude() + " - Longitude: " + l.getLongitude() + " - Usuário: " + l.getUser() + "\n\n";
        }
        localizationText.setText(text);
    }

    private void userNotFound() {
        localizationText.setText("Usuário não encontrado");
    }

    private void signOut() {
        Log.d("SignOut", "Deslogando");
        mAuth.signOut();

        Intent it = new Intent(ShowLocalizationActivity.this, MainActivity.class);
        startActivity(it);
    }


}

