package com.example.camilomontoya.patronus.Settings;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.camilomontoya.patronus.R;
import com.example.camilomontoya.patronus.StartActivity;
import com.example.camilomontoya.patronus.Utils.CurrentUser;
import com.example.camilomontoya.patronus.Utils.Typo;
import com.google.firebase.auth.FirebaseAuth;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class SettingsFragment extends Fragment {
    private static final String TAG = "MapFragment";

    private CircleImageView profilePic;
    private TextView titleToolbar, settingsName, settingsEmail;
    private Button logOut;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mAuth = FirebaseAuth.getInstance();

        titleToolbar = view.findViewById(R.id.toolbar_settings_title);
        titleToolbar.setTypeface(Typo.getInstance().getTitle());

        settingsName = view.findViewById(R.id.settings_name);
        settingsEmail = view.findViewById(R.id.settings_email);
        settingsName.setTypeface(Typo.getInstance().getTitle());
        settingsEmail.setTypeface(Typo.getInstance().getContent());

        settingsName.setText(CurrentUser.getRef().getName());
        settingsEmail.setText(CurrentUser.getRef().getEmail());

        profilePic = view.findViewById(R.id.settings_profile_pic);
        ImageLoader.getInstance().displayImage(CurrentUser.getRef().getProfilePic(),profilePic);

        logOut = (Button) view.findViewById(R.id.logOut);
        logOut.setTypeface(Typo.getInstance().getTitle());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(getActivity(), StartActivity.class));
                getActivity().finish();
            }
        });

        return view;
    }
}
