package edu.uaic.fii.wad.edec.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import edu.uaic.fii.wad.edec.fragment.*;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;


public class TabsPagerAdapter extends FragmentPagerAdapter {

    private final FragmentManager mFragmentManager;
    public Fragment mFragmentAtPos0;
    public Fragment mFragmentAtPos1;
    private PageListener listener = new PageListener();
    public static boolean onResultFragment = false;

    private final class PageListener implements PageFragmentListener {

        public void onSwitchToNextFragment(int fragment, int id) {

            if (fragment == 0) {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos0).commit();

                if (mFragmentAtPos0 instanceof ScanProductFragment) {
                    if (id == 1) {
                        mFragmentAtPos0 = new OkProductFragment(listener);
                    } else if (id == 2) {
                        mFragmentAtPos0 = new BadProductFragment(listener);
                    }

                    onResultFragment = true;
                } else {
                    mFragmentAtPos0 = new ScanProductFragment(listener);
                    onResultFragment = false;
                }

            } else if (fragment == 1) {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos1).commit();

                if (mFragmentAtPos1 instanceof GroupDetailsFragment) {
                    mFragmentAtPos1 = new GroupsFragment(listener);
                } else {
                    mFragmentAtPos1 = new GroupDetailsFragment(listener);
                }
            }

            notifyDataSetChanged();
        }
    }

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentManager = fm;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                if (mFragmentAtPos0 == null) {
                    mFragmentAtPos0 = new ScanProductFragment(listener);
                }
                return mFragmentAtPos0;

            case 1:
                if (mFragmentAtPos1 == null) {
                    mFragmentAtPos1 = new GroupsFragment(listener);
                }
                return mFragmentAtPos1;
            case 2:
                return new StatisticsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public int getItemPosition(Object object)
    {
        if (object instanceof ScanProductFragment && mFragmentAtPos0 instanceof OkProductFragment) {
            return POSITION_NONE;
        }
        if (object instanceof ScanProductFragment && mFragmentAtPos0 instanceof BadProductFragment) {
            return POSITION_NONE;
        }
        if (object instanceof OkProductFragment && mFragmentAtPos0 instanceof ScanProductFragment) {
            return POSITION_NONE;
        }
        if (object instanceof BadProductFragment && mFragmentAtPos0 instanceof ScanProductFragment) {
            return POSITION_NONE;
        }

        if (object instanceof GroupDetailsFragment && mFragmentAtPos1 instanceof GroupsFragment) {
            return POSITION_NONE;
        }
        if (object instanceof GroupsFragment && mFragmentAtPos1 instanceof GroupDetailsFragment) {
            return POSITION_NONE;
        }

        return POSITION_UNCHANGED;
    }

}