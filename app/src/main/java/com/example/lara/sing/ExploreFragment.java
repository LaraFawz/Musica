package com.example.lara.sing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;


public class ExploreFragment extends Fragment {




    /**
     * Context of the app
     */
    private Context mContext;


    public static final String TAG = ExploreFragment.class.getName();
//    private static final String SINGITA22_EXPLORE_HASHTAG = " #SingaholicApp";


    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        ViewPager viewPager = (ViewPager) root.findViewById(R.id.viewpager);
        /** Important: Must use the child FragmentManager or you will see side effects. */
        viewPager.setAdapter(new CategoryAdapter(getChildFragmentManager()));



        return root;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_explore, menu);
//        super.onCreateOptionsMenu(menu,inflater);
//   }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_explore) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_explore, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_explore) {
            String url = ("http://www.youtube.com");
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(intent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }




//    public static class MyAdapter extends FragmentPagerAdapter {
//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            if (position == 0) {
//                return new AudioFragment();
//            } else if (position == 1) {
//                return new VideoFragment();
//            } else {
//                return new TopRankFragment();
//            }
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            if (position == 0) {
//                return "Audio";
//            } else if (position == 1) {
//                return "Video";
//            } else {
//                return "Top Ranking";
//            }
//        }
//        }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class
                    .getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}