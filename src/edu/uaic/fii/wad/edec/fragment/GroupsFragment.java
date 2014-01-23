package edu.uaic.fii.wad.edec.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.adapter.GridViewAdapter;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.listener.ScrollViewListener;
import edu.uaic.fii.wad.edec.model.GridItem;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.service.group.DeleteGroup;
import edu.uaic.fii.wad.edec.view.ExpandableHeightGridView;
import edu.uaic.fii.wad.edec.service.group.GroupDetails;
import edu.uaic.fii.wad.edec.view.ObservableScrollView;

import java.util.ArrayList;
import java.util.Collections;

public class GroupsFragment extends Fragment implements ScrollViewListener {

    private static Fragment fragment = null;
    private static View view;

    public static PageFragmentListener pageListener;

    private TextView my, myTop, myBottom, personal, personalTop, personalBottom, friends, friendsTop,
            friendsBottom, joined, joinedTop, joinedBottom;
    final int[] myCoordinates = new int[2];
    final int[] myTopCoordinates = new int[2];
    final int[] myBottomCoordinates = new int[2];
    final int[] recommendationsCoordinates = new int[2];
    final int[] recommendationsTopCoordinates = new int[2];
    final int[] recommendationsBottomCoordinates = new int[2];
    final int[] friendsCoordinates = new int[2];
    final int[] friendsTopCoordinates = new int[2];
    final int[] friendsBottomCoordinates = new int[2];
    final int[] joinedCoordinates = new int[2];
    final int[] joinedTopCoordinates = new int[2];
    final int[] joinedBottomCoordinates = new int[2];

    public static GridViewAdapter myGroupsCustomGridAdapter;
    public static GridViewAdapter friendsGroupsCustomGridAdapter;
    public static GridViewAdapter joinedGroupsCustomGridAdapter;
    public static GridViewAdapter recommendedGroupsCustomGridAdapter;
    public static Bitmap thumb = null;
    public static Activity activity;

    public GroupsFragment(PageFragmentListener listener) {
        pageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.view_groups_fragment, container, false);

        fragment = this;
        activity = getActivity();

        my = (TextView) view.findViewById(R.id.view_groups_my);
        myTop = (TextView) view.findViewById(R.id.view_groups_my_top);
        myBottom = (TextView) view.findViewById(R.id.view_groups_my_bottom);

        personal = (TextView) view.findViewById(R.id.view_groups_friends);
        personalTop = (TextView) view.findViewById(R.id.view_groups_friends_top);
        personalBottom = (TextView) view.findViewById(R.id.view_groups_friends_bottom);

        friends = (TextView) view.findViewById(R.id.view_groups_recommendations);
        friendsTop = (TextView) view.findViewById(R.id.view_groups_recommendations_top);
        friendsBottom = (TextView) view.findViewById(R.id.view_groups_recommendations_bottom);

        joined = (TextView) view.findViewById(R.id.view_groups_joined);
        joinedTop = (TextView) view.findViewById(R.id.view_groups_joined_top);
        joinedBottom = (TextView) view.findViewById(R.id.view_groups_joined_bottom);


        addGridViews();
        ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.view_groups_scroll);
        scrollView.setScrollViewListener(this);

        return view;
    }

    public static void addGridViews() {
        addMyGridView();
        addFriendsGridView();
        addRecommendedGridView();
        addJoinedGridView();
    }

    private static void addMyGridView() {
        ExpandableHeightGridView myGridView = (ExpandableHeightGridView) view.findViewById(R.id.view_groups_my_grid);

        ArrayList<GridItem> myGroups = new ArrayList<GridItem>();
        byte[] decodedString;
        Bitmap decodedByte;
        for (int i = 0; i < MainActivity.myGroups.size(); i++) {
            decodedString = Base64.decode(MainActivity.myGroups.get(i).getLogo(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            myGroups.add(new GridItem(decodedByte, MainActivity.myGroups.get(i).getName()));
        }

        Bitmap bitmap = BitmapFactory.decodeResource(fragment.getResources(), R.string.add_new_group);

        Collections.reverse(myGroups);
        myGroups.add(new GridItem(bitmap, "Add New Group"));
        Collections.reverse(myGroups);

        myGroupsCustomGridAdapter = new GridViewAdapter(view.getContext(), R.layout.grid_item, myGroups);
        myGridView.setAdapter(myGroupsCustomGridAdapter);
        myGridView.setExpanded(true);
        myGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setMessage("Delete group?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new DeleteGroup(MainActivity.myGroups.get(i - 1).getId()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // ..
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    MainActivity.groupState = 0;
                    MainActivity.currentGroup = new Group();
                    pageListener.onSwitchToNextFragment(1, 0);
                } else {
                    MainActivity.groupState = 1;
                    new GroupDetails(MainActivity.myGroups.get(i - 1).getId()).execute();
                }
            }
        });
    }

    private static void addFriendsGridView() {
        ArrayList<GridItem> friendsGroups = new ArrayList<GridItem>();
        byte[] decodedString;
        Bitmap decodedByte;
        for (int i = 0; i < MainActivity.friendsGroups.size(); i++) {
            decodedString = Base64.decode(MainActivity.friendsGroups.get(i).getLogo(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            friendsGroups.add(new GridItem(decodedByte, MainActivity.friendsGroups.get(i).getName()));
        }

        friendsGroupsCustomGridAdapter = new GridViewAdapter(view.getContext(), R.layout.grid_item, friendsGroups);
        ExpandableHeightGridView friendsGridView = (ExpandableHeightGridView) view.findViewById(R.id.view_groups_friends_grid);
        friendsGridView.setAdapter(friendsGroupsCustomGridAdapter);
        friendsGridView.setExpanded(true);

        friendsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.groupState = 2;
                new GroupDetails(MainActivity.friendsGroups.get(i).getId()).execute();
            }
        });
    }

    private static void addRecommendedGridView() {
        ArrayList<GridItem> recommendedGroups = new ArrayList<GridItem>();
        byte[] decodedString;
        Bitmap decodedByte;
        for (int i = 0; i < MainActivity.recommendationsGroups.size(); i++) {
            decodedString = Base64.decode(MainActivity.recommendationsGroups.get(i).getLogo(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            recommendedGroups.add(new GridItem(decodedByte, MainActivity.recommendationsGroups.get(i).getName()));
        }

        recommendedGroupsCustomGridAdapter = new GridViewAdapter(view.getContext(), R.layout.grid_item, recommendedGroups);
        ExpandableHeightGridView recommendationsGridView = (ExpandableHeightGridView) view.findViewById(R.id.view_groups_recommendations_grid);
        recommendationsGridView.setAdapter(recommendedGroupsCustomGridAdapter);
        recommendationsGridView.setExpanded(true);

        recommendationsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.groupState = 2;
                new GroupDetails(MainActivity.recommendationsGroups.get(i).getId()).execute();
            }
        });
    }

    private static void addJoinedGridView() {
        ArrayList<GridItem> joinedGroups = new ArrayList<GridItem>();
        byte[] decodedString;
        Bitmap decodedByte;
        for (int i = 0; i < MainActivity.joinedGroups.size(); i++) {
            decodedString = Base64.decode(MainActivity.joinedGroups.get(i).getLogo(), Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            joinedGroups.add(new GridItem(decodedByte, MainActivity.joinedGroups.get(i).getName()));
        }

        joinedGroupsCustomGridAdapter = new GridViewAdapter(view.getContext(), R.layout.grid_item, joinedGroups);
        ExpandableHeightGridView joinedGridView = (ExpandableHeightGridView) view.findViewById(R.id.view_groups_joined_grid);
        joinedGridView.setAdapter(joinedGroupsCustomGridAdapter);
        joinedGridView.setExpanded(true);

        joinedGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.groupState = 3;
                new GroupDetails(MainActivity.joinedGroups.get(i).getId()).execute();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        my.getLocationOnScreen(myCoordinates);
        myTop.getLocationOnScreen(myTopCoordinates);
        myBottom.getLocationOnScreen(myBottomCoordinates);

        personal.getLocationOnScreen(recommendationsCoordinates);
        personalTop.getLocationOnScreen(recommendationsTopCoordinates);
        personalBottom.getLocationOnScreen(recommendationsBottomCoordinates);

        friends.getLocationOnScreen(friendsCoordinates);
        friendsTop.getLocationOnScreen(friendsTopCoordinates);
        friendsBottom.getLocationOnScreen(friendsBottomCoordinates);

        joined.getLocationOnScreen(joinedCoordinates);
        joinedTop.getLocationOnScreen(joinedTopCoordinates);
        joinedBottom.getLocationOnScreen(joinedBottomCoordinates);

        if (myCoordinates[1] <= myTopCoordinates[1]) {
            myTop.setVisibility(View.VISIBLE);
        } else {
            myTop.setVisibility(View.GONE);
        }

        if (myCoordinates[1] >= myBottomCoordinates[1]) {
            myBottom.setVisibility(View.VISIBLE);
        } else {
            myBottom.setVisibility(View.GONE);
        }

        if (recommendationsCoordinates[1] <= recommendationsTopCoordinates[1]) {
            personalTop.setVisibility(View.VISIBLE);
        } else {
            personalTop.setVisibility(View.GONE);
        }

        if (recommendationsCoordinates[1] >= recommendationsBottomCoordinates[1]) {
            personalBottom.setVisibility(View.VISIBLE);
        } else {
            personalBottom.setVisibility(View.GONE);
        }

        if (friendsCoordinates[1] <= friendsTopCoordinates[1]) {
            friendsTop.setVisibility(View.VISIBLE);
        } else {
            friendsTop.setVisibility(View.GONE);
        }

        if (friendsCoordinates[1] >= friendsBottomCoordinates[1]) {
            friendsBottom.setVisibility(View.VISIBLE);
        } else {
            friendsBottom.setVisibility(View.GONE);
        }

        if (joinedCoordinates[1] <= joinedTopCoordinates[1]) {
            joinedTop.setVisibility(View.VISIBLE);
        } else {
            joinedTop.setVisibility(View.GONE);
        }

        if (joinedCoordinates[1] >= joinedBottomCoordinates[1]) {
            joinedBottom.setVisibility(View.VISIBLE);
        } else {
            joinedBottom.setVisibility(View.GONE);
        }
    }
}