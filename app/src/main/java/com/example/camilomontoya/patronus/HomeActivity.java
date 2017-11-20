package com.example.camilomontoya.patronus;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.camilomontoya.patronus.Utils.BottomNavigationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(Color.parseColor("#1C2335"));
        setupBottomNavigation();

        mAuth = FirebaseAuth.getInstance();

        home = (Button) findViewById(R.id.homeBtn);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this,StartActivity.class));
                finish();
            }
        });
    }

    private void setupBottomNavigation() {
        Log.d(TAG, "setupBottomNavigation: setting up BottomNavigation");

        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bottomNav);
        BottomNavigationHelper.setupBottomNavigationView(bottomNav);
    }
}
