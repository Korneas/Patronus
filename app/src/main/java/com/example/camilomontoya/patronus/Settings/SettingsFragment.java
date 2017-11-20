package com.example.camilomontoya.patronus.Settings;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.camilomontoya.patronus.R;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class SettingsFragment extends Fragment {
    private static final String TAG = "MapFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }
}
