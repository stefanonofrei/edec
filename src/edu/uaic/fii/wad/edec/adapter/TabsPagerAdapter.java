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
    public Fragment mFragmentAtPos2;
    private PageListener listener = new PageListener();
    public static boolean onResultFragment = false;
    public static boolean onDetailsSearchFragment = false;

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

                if (mFragmentAtPos1 instanceof GroupsFragment) {
                    if (id == 1) {
                        mFragmentAtPos1 = new GroupDetailsFragment(listener);
                    } else if (id == 2) {
                        mFragmentAtPos1 = new SearchFragment(listener);
                    }

                    onDetailsSearchFragment = true;
                } else {
                    mFragmentAtPos1 = new GroupsFragment(listener);
                    onDetailsSearchFragment = false;
                }
            } else if (fragment == 2) {
                mFragmentManager.beginTransaction().remove(mFragmentAtPos2).commit();

                if (mFragmentAtPos2 instanceof StatisticsFragment) {
                    mFragmentAtPos2 = new GroupDetailsFragment(listener);
                } else {
                    mFragmentAtPos2 = new StatisticsFragment(listener);
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
                if (mFragmentAtPos2 == null) {
                    mFragmentAtPos2 = new StatisticsFragment(listener);
                }
                return mFragmentAtPos2;
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
        if (object instanceof GroupsFragment && mFragmentAtPos1 instanceof SearchFragment) {
            return POSITION_NONE;
        }
        if (object instanceof SearchFragment && mFragmentAtPos1 instanceof GroupsFragment) {
            return POSITION_NONE;
        }

        if (object instanceof StatisticsFragment && mFragmentAtPos2 instanceof GroupDetailsFragment) {
            return POSITION_NONE;
        }
        if (object instanceof GroupDetailsFragment && mFragmentAtPos2 instanceof StatisticsFragment) {
            return POSITION_NONE;
        }

        return POSITION_UNCHANGED;
    }

}