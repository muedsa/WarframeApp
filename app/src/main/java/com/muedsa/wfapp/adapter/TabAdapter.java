package com.muedsa.wfapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.muedsa.wfapp.fragment.AlertFragment;

public class TabAdapter extends FragmentPagerAdapter{

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new AlertFragment();
                break;
            case 1:
                fragment = new AlertFragment();
                break;
            case 2:
                fragment = new AlertFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = null;
        switch (position){
            case 0:
                title = "警报";
                break;
            case 1:
                title = "入侵";
                break;
            case 2:
                title = "虚空遗物";
                break;
        }
        return title;
    }
}
