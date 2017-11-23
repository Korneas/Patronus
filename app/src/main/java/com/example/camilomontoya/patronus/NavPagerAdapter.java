package com.example.camilomontoya.patronus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo Montoya on 11/19/2017.
 */

public class NavPagerAdapter extends FragmentPagerAdapter{

    private static final String TAG = "NavPagerAdapter";
    private List<Fragment> mFragmentList = new ArrayList<>();

    public NavPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment frag) {
        mFragmentList.add(frag);
    }
}
