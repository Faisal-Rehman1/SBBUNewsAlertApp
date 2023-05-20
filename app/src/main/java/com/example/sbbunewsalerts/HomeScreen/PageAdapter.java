package com.example.sbbunewsalerts.HomeScreen;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sbbunewsalerts.Gallery.GallerFragment;
import com.example.sbbunewsalerts.News.All_News_Fragment;
import com.example.sbbunewsalerts.Profile.ProfileFragment;


public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                All_News_Fragment tab1 = new All_News_Fragment();
                return tab1;
            case 1:
                GallerFragment tab2 = new GallerFragment();
                return tab2;
            case 2:
                ProfileFragment tab3 = new ProfileFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}