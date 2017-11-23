package com.example.camilomontoya.patronus;

import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.camilomontoya.patronus.Friends.FriendsFragment;
import com.example.camilomontoya.patronus.History.HistoryFragment;
import com.example.camilomontoya.patronus.Map.MapFragment;
import com.example.camilomontoya.patronus.Settings.SettingsFragment;
import com.example.camilomontoya.patronus.Utils.BottomNavigationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupViewPager();
        setupBottomNavigation();

        /*
        home = (Button) findViewById(R.id.homeBtn);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(HomeActivity.this,StartActivity.class));
                finish();
            }
        });*/
    }

    private void setupViewPager() {
        NavPagerAdapter navPager = new NavPagerAdapter(getSupportFragmentManager());
        navPager.addFragment(new MapFragment());
        navPager.addFragment(new FriendsFragment());
        navPager.addFragment(new HistoryFragment());
        navPager.addFragment(new SettingsFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerHome);
        viewPager.setAdapter(navPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        getWindow().setStatusBarColor(Color.parseColor("#1C2335"));
                        break;
                    case 1:
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkPatronus));
                        break;
                    case 2:
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkPatronus));
                        break;
                    case 3:
                        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDarkPatronus));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupBottomNavigation() {
        Log.d(TAG, "setupBottomNavigation: setting up BottomNavigation");

        BottomNavigationViewEx bottomNav = (BottomNavigationViewEx) findViewById(R.id.bottomNav);
        BottomNavigationHelper.setupBottomNavigationView(bottomNav);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerHome);
        bottomNav.setupWithViewPager(viewPager);
    }

    private void aviso(String txt) {
        Toast.makeText(getApplicationContext(), txt, Toast.LENGTH_SHORT).show();
    }
}
