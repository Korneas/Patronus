package com.example.camilomontoya.patronus.Utils;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.camilomontoya.patronus.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class BottomNavigationHelper {
    private static final String TAG = "BottomNavigationHelper";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigation) {
        Log.d(TAG, "setupBottomNavigationView: Setting view");
        bottomNavigation.enableAnimation(false);
        bottomNavigation.enableItemShiftingMode(false);
        bottomNavigation.enableShiftingMode(false);
        bottomNavigation.setTextVisibility(false);
    }

    public static void enableNavigation(BottomNavigationViewEx bottomNavigation) {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        break;
                    case R.id.ic_friends:
                        break;
                    case R.id.ic_history:
                        break;
                    case R.id.ic_settings:
                        break;
                }

                return false;
            }
        });

    }

}
