package com.example.igallery;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    int noOfTabs;

    public PagerAdapter (FragmentManager fm,int numTabs) {
        super(fm);
        this.noOfTabs=numTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0 :
                UserHomeFrag userHomeFrag = new UserHomeFrag();
                return userHomeFrag;
            case 1:
                UserOrderScreen userOrderScreen = new UserOrderScreen();
                return  userOrderScreen;
            case 2:
                UserBlogScreen userBlogScreen = new UserBlogScreen();
                return userBlogScreen;
            default:
            return null;

        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
