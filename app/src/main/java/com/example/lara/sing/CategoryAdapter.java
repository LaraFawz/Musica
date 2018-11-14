package com.example.lara.sing;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Lara on 5/12/2018.
 */

public class CategoryAdapter extends FragmentPagerAdapter {

    /** Context of the app */
    private Context mContext;

    /**
     * Create a new {@link CategoryAdapter} object.
     *
//     * @param context is the context of the app
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public CategoryAdapter(FragmentManager fm) {
        super(fm);
//        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new AudioFragment();
        } else if (position == 1) {
            return new VideoFragment();
        } else {
            return new TopRankFragment();
        }
    }


    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "AUDIO RECORDS";
//            return mContext.getString(R.string.audio_records);
        } else if (position == 1) {
//            return mContext.getString(R.string.video_records);
            return "VIDEO RECORDS";
        } else {
//            return mContext.getString(R.string.top_ranking);
            return "TOP RANKS";
        }
    }
}
