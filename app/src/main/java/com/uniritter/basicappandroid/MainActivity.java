package com.uniritter.basicappandroid;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private static final int REQUEST_CHECK_SETTINGS = 613;
    private LocationRequest mLocationRequest;

    private EditText user;
    private EditText password;
    private Button loginUser;
    private Button logoutUser;

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        for (String provider : locationManager.getAllProviders()) {
            Toast.makeText(getApplicationContext(), provider, Toast.LENGTH_LONG).show();
        }

        signOut();


        loginUser = findViewById(R.id.login);
        logoutUser = findViewById(R.id.logout);
        loginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = findViewById(R.id.user);
                password = findViewById(R.id.password);

                signIn(user.getText().toString(), password.getText().toString());

                if (mAuth.getCurrentUser() != null) {
                    Log.i("CreateUser", "Usuario logado!");
                    Intent it = new Intent(MainActivity.this, LocalizationActivity.class);
                    startActivity(it);
                } else {
                    Log.i("CreateUser", "Usuario nao logado!");
                }
            }
        });

        logoutUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //  updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {

        } else {

        }
    }


    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void signIn(String email, String password) {
        Log.d("SingIn", "signIn:" + email);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SingIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SingIn", "signInWithEmail:failure", task.getException());

                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Log.w("SingIn", "signInWithEmail:failure", task.getException());
                        }
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void signOut() {
        Log.d("SignOut", "Deslogando");
        mAuth.signOut();
        updateUI(null);
    }


    /////////////////
}

