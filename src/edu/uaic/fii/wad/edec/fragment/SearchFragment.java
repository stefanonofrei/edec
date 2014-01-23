package edu.uaic.fii.wad.edec.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.adapter.TabsPagerAdapter;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.view.ExpandableHeightGridView;

public class SearchFragment extends Fragment {

    public static PageFragmentListener pageListener;
    private static View view;

    public SearchFragment(PageFragmentListener listener) {
        pageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);

        ExpandableHeightGridView groupsGridView = (ExpandableHeightGridView) view.findViewById(R.id.search_groups_grid);

        Toast.makeText(view.getContext(), "Search query: " + GroupsFragment.searchQuery, Toast.LENGTH_LONG).show();

        return view;
    }

    public void backPressed() {
        TabsPagerAdapter.onDetailsSearchFragment = false;
        pageListener.onSwitchToNextFragment(1, 0);
    }
}
