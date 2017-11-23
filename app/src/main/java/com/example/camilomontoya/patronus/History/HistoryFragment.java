package com.example.camilomontoya.patronus.History;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.Utils.Typo;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class HistoryFragment extends Fragment {
    private static final String TAG = "MapFragment";

    private TextView titleToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        titleToolbar = view.findViewById(R.id.toolbar_history_title);
        titleToolbar.setTypeface(Typo.getInstance().getTitle());

        return view;
    }
}
