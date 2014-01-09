package edu.uaic.fii.wad.edec.activity;

import android.app.ActionBar;
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
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.service.group.FriendsGroupsListing;
import edu.uaic.fii.wad.edec.service.group.JoinedGroupsListing;
import edu.uaic.fii.wad.edec.service.group.MyGroupsListing;
import edu.uaic.fii.wad.edec.service.group.RecommendedGroupsListing;
import edu.uaic.fii.wad.edec.adapter.TabsPagerAdapter;
import edu.uaic.fii.wad.edec.fragment.*;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    public static ProgressDialog loading;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    private String[] tabs = { "Scan", "Groups", "Stats" };
    private static final int SELECT_PICTURE = 1;

    public static Group currentGroup;

    /**
     * 0 - add new group
     * 1 - edit group
     * 2 - join suggested group
     * 3 - leave group
     */
    public static int groupState = 0;

    public static ArrayList<Group> myGroups = new ArrayList<Group>();
    public static ArrayList<Group> joinedGroups = new ArrayList<Group>();
    public static ArrayList<Group> recommendationsGroups = new ArrayList<Group>();
    public static ArrayList<Group> friendsGroups = new ArrayList<Group>();

    public static int tasksNumber = -1;
    public static AtomicInteger completedTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        completedTasks = new AtomicInteger(0);

        loading = new ProgressDialog(this);
        loading.setTitle("Loading");
        loading.setMessage("Please wait...");

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

        new MyGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new JoinedGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new RecommendedGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new FriendsGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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
            }
            else if (mAdapter.getItem(0) instanceof ScanProductFragment) {
                finish();
            }
        } else if (viewPager.getCurrentItem() == 1) {
            if (mAdapter.getItem(1) instanceof GroupDetailsFragment) {
                ((GroupDetailsFragment) mAdapter.getItem(1)).backPressed();
            } else {
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
}