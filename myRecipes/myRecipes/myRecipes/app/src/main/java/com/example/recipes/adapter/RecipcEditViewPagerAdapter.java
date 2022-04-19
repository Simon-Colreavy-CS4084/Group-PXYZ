package com.example.recipes.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.recipes.fragment.Fragment1;
import com.example.recipes.fragment.recipc.FragmentRecipc1;
import com.example.recipes.fragment.recipc.FragmentRecipc2;
import com.example.recipes.fragment.recipc.FragmentRecipc3;
import com.example.recipes.fragment.recipc.FragmentRecipc4;
import com.example.recipes.fragment.recipc.FragmentRecipc5;

public class RecipcEditViewPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"1", "2", "3", "4", "5"};

    public RecipcEditViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentRecipc1();
          } else if (position == 1) {
            return new FragmentRecipc2();
        } else if (position == 2) {
            return new FragmentRecipc3();
        } else if (position == 3) {
            return new FragmentRecipc4();
        }else if (position == 4) {
            return new FragmentRecipc5();
        }
        return new Fragment1();
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}