package com.example.recipes.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.recipes.fragment.Fragment1;
import com.example.recipes.fragment.user.FragmentUser1;
import com.example.recipes.fragment.user.FragmentUser2;
import com.example.recipes.fragment.user.FragmentUser3;

public class MeViewPagerAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{ "Posts", "Recipe", "Like Recipe"};
    private Context context;
    private String userId = "";

    public MeViewPagerAdapter(FragmentManager fm, Context context,String userId) {
        super(fm);
        this.context = context;
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return FragmentUser2.newInstance(userId);
        }else if (position == 2) {
            return FragmentUser3.newInstance(userId);
        }
        return FragmentUser1.newInstance(userId);
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