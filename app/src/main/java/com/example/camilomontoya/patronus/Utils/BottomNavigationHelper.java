package com.example.camilomontoya.patronus.Utils;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.camilomontoya.patronus.Friends.FriendsFragment;
import com.example.camilomontoya.patronus.History.HistoryFragment;
import com.example.camilomontoya.patronus.Map.MapFragment;
import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.Settings.SettingsFragment;
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
}
