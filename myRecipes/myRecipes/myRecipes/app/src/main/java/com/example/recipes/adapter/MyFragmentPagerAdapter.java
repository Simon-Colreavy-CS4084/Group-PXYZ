package com.example.recipes.adapter;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.recipes.fragment.Fragment1;
import com.example.recipes.fragment.Fragment2;
import com.example.recipes.fragment.Fragment3;


  public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private String[] mTitles = new String[]{"News Feed", "Recipe", "Profile"};

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return new Fragment2();
        } else if (position == 2) {
            return new Fragment3();
        }
        return new Fragment1();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

          @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
