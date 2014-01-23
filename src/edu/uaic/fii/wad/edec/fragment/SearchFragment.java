package edu.uaic.fii.wad.edec.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.adapter.GridViewAdapter;
import edu.uaic.fii.wad.edec.adapter.TabsPagerAdapter;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.model.GridItem;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.service.group.GroupDetails;
import edu.uaic.fii.wad.edec.service.group.SearchGroup;
import edu.uaic.fii.wad.edec.service.stats.TopCompanies;
import edu.uaic.fii.wad.edec.service.stats.TopIngredients;
import edu.uaic.fii.wad.edec.service.stats.TopProducts;
import edu.uaic.fii.wad.edec.service.stats.TrendingGroups;
import edu.uaic.fii.wad.edec.service.util.ImageBase64;
import edu.uaic.fii.wad.edec.view.ExpandableHeightGridView;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    public static PageFragmentListener pageListener;
    private static View view;
    public static ArrayList<Group> groupList;

    private ArrayList<GridItem> imageItems = new ArrayList<GridItem>();
    private ProgressDialog barProgressDialog;
    private GridViewAdapter customGridAdapter;

    public SearchFragment(PageFragmentListener listener) {
        pageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);

        ExpandableHeightGridView groupsGridView = (ExpandableHeightGridView) view.findViewById(R.id.search_groups_grid);
        customGridAdapter = new GridViewAdapter(view.getContext(), R.layout.grid_item, imageItems);
        groupsGridView.setAdapter(customGridAdapter);
        groupsGridView.setExpanded(true);

        groupsGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.groupState = setGroupUserAssociation(groupList.get(i).getId());
                new GroupDetails(groupList.get(i).getId(), 1).execute();
            }
        });

        Toast.makeText(view.getContext(), "Search query: " + GroupsFragment.searchQuery, Toast.LENGTH_LONG).show();

        return view;
    }

    public void backPressed() {
        TabsPagerAdapter.onDetailsSearchFragment = false;
        pageListener.onSwitchToNextFragment(1, 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
                barProgressDialog = new ProgressDialog(getActivity());
                barProgressDialog.setTitle("Loading");
                barProgressDialog.setMessage("Please wait...");
                barProgressDialog.setProgress(0);
                barProgressDialog.setMax(20);
                barProgressDialog.show();

                new SearchGroup(this,GroupsFragment.searchQuery).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        }
    }

    public void refreshGroupData(){
        ArrayList<GridItem> newImages = new ArrayList<GridItem>();
        for(int i = 0; i < groupList.size(); i++){

            Bitmap bitmap = ImageBase64.decodeImage(groupList.get(i).getLogo());
            newImages.add(new GridItem(bitmap, groupList.get(i).getName()));
        }
        imageItems.clear();
        imageItems.addAll(newImages);
        customGridAdapter.notifyDataSetChanged();
        barProgressDialog.dismiss();
    }

    public int setGroupUserAssociation(String id){
        for(Group group:MainActivity.myGroups){
            if(group.getId().equals(id)){
                return 1;
            }
        }
        for(Group group:MainActivity.joinedGroups){
            if(group.getId().equals(id)){
                return 3;
            }
        }
        return 2;
    }
}
