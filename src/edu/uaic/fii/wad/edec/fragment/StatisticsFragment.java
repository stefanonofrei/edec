package edu.uaic.fii.wad.edec.fragment;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.model.GridItem;
import edu.uaic.fii.wad.edec.view.ExpandableHeightGridView;
import edu.uaic.fii.wad.edec.adapter.GridViewAdapter;
import java.util.ArrayList;
import java.util.Arrays;

public class StatisticsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.statistics_fragment, container, false);

        ExpandableHeightGridView myGridView = (ExpandableHeightGridView) view.findViewById(R.id.stats_trending_groups_grid);
        GridViewAdapter customGridAdapter = new GridViewAdapter(view.getContext(), R.layout.grid_item, getData());
        myGridView.setAdapter(customGridAdapter);
        myGridView.setExpanded(true);


        return view;
    }

    private ArrayList<String> trendingGroupsNames = new ArrayList<String>(
            Arrays.asList("Unhealthy Drinks", "Mushrooms", "High-Sugar", "Sweets", "Evil Companies", "Bad Drinks"));


    private ArrayList getData() {
        final ArrayList imageItems = new ArrayList();
        TypedArray imgs = getResources().obtainTypedArray(R.array.trending_groups);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
                    imgs.getResourceId(i, -1));
            imageItems.add(new GridItem(bitmap, trendingGroupsNames.get(i)));
        }

        return imageItems;

    }
}