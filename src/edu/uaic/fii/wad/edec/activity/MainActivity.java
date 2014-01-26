package edu.uaic.fii.wad.edec.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.adapter.TabsPagerAdapter;
import edu.uaic.fii.wad.edec.fragment.*;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.model.Product;
import edu.uaic.fii.wad.edec.service.group.FriendsGroupsListing;
import edu.uaic.fii.wad.edec.service.group.JoinedGroupsListing;
import edu.uaic.fii.wad.edec.service.group.MyGroupsListing;
import edu.uaic.fii.wad.edec.service.group.RecommendedGroupsListing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static ProgressDialog loading;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = { "Scan", "Groups", "Stats" };
    private static final int SELECT_PICTURE = 1;

    public static Group currentGroup;
    public static Product currentProduct;

    /**
     * 0 - add new group
     * 1 - edit group
     * 2 - join suggested group
     * 3 - leave group
     */
    public static int groupState = 0;

    public static List<Group> myGroups = Collections.synchronizedList(new ArrayList<Group>());
    public static List<Group> joinedGroups = Collections.synchronizedList(new ArrayList<Group>());
    public static List<Group> recommendationsGroups = Collections.synchronizedList(new ArrayList<Group>());
    public static List<Group> friendsGroups = Collections.synchronizedList(new ArrayList<Group>());

    public static int tasksNumber = -1;
    public static AtomicInteger completedTasks;
    public static boolean loadAllGroups;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        activity = this;
        completedTasks = new AtomicInteger(0);
        loadAllGroups = true;

        loading = new ProgressDialog(this);
        loading.setTitle("Loading");
        loading.setMessage("Please wait...");
        loading.setCancelable(false);

        currentGroup = new Group();

        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setTheme(android.R.style.Theme_Holo);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);

                if (!TabsPagerAdapter.onResultFragment) {
                    if (position > 0) {
                        ScanProductFragment.stop();
                    } else {
                        ScanProductFragment.start();
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        loadGroups();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() == 0) {
            if (mAdapter.getItem(0) instanceof OkProductFragment) {
                ((OkProductFragment) mAdapter.getItem(0)).backPressed();
            } else if (mAdapter.getItem(0) instanceof BadProductFragment) {
                ((BadProductFragment) mAdapter.getItem(0)).backPressed();
            } else if (mAdapter.getItem(0) instanceof ScanProductFragment) {
                finish();
            }
        } else if (viewPager.getCurrentItem() == 1) {
            if (mAdapter.getItem(1) instanceof GroupDetailsFragment) {
                ((GroupDetailsFragment) mAdapter.getItem(1)).backPressed();
            } else if (mAdapter.getItem(1) instanceof SearchFragment) {
                ((SearchFragment) mAdapter.getItem(1)).backPressed();
            } else if (mAdapter.getItem(1) instanceof GroupsFragment) {
                finish();
            }
        } else if (viewPager.getCurrentItem() == 2) {
            if (mAdapter.getItem(2) instanceof GroupDetailsFragment) {
                ((GroupDetailsFragment) mAdapter.getItem(2)).backPressed();
            } else if (mAdapter.getItem(2) instanceof StatisticsFragment) {
                finish();
            }
        } else {
            finish();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                GroupDetailsFragment.groupLogo.setImageURI(selectedImageUri);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static void loadGroups() {
        MainActivity.tasksNumber = 4;
        new GroupsLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        new MyGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new JoinedGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new RecommendedGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new FriendsGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static class GroupsLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            while (true) {
                if (MainActivity.tasksNumber == MainActivity.completedTasks.get()) {
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            GroupsFragment.addGridViews();
            MainActivity.loading.dismiss();
            MainActivity.tasksNumber = -1;
            MainActivity.completedTasks.set(0);
        }
    }
}