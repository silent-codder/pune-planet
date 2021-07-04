package com.cctpl.puneplanet.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cctpl.puneplanet.SubFragments.CategoryFragment;
import com.cctpl.puneplanet.SubFragments.ForYouFragment;

public class TabAdapter extends FragmentPagerAdapter {

    int totalTabs;
    public TabAdapter(@NonNull FragmentManager fm, int behavior, int tabCount) {
        super(fm, behavior);
        this.totalTabs = tabCount;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0 :
                return new ForYouFragment();
            case 1 :
                return new CategoryFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
