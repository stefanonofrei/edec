package edu.uaic.fii.wad.edec.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.adapter.TabsPagerAdapter;

public class OkProductFragment extends Fragment {

    static PageFragmentListener scanPageListener;

    public OkProductFragment(PageFragmentListener listener) {
        scanPageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.ok_product_fragment, container, false);
    }

    public void backPressed() {
        TabsPagerAdapter.onResultFragment = false;
        ScanProductFragment.previewStopped = false;
        scanPageListener.onSwitchToNextFragment(0, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        mockProduct();
    }

    private LinearLayout ingredientsLayout;

    public void mockProduct() {
        ImageView productLogo = (ImageView) getActivity().findViewById(R.id.ok_product_logo);
        productLogo.setImageResource(R.drawable.mockup_snickers);

        TextView productName = (TextView) getActivity().findViewById(R.id.ok_product_name);
        productName.setText("Snickers Bar");

        ImageView companyLogo = (ImageView) getActivity().findViewById(R.id.ok_company_logo);
        companyLogo.setImageResource(R.drawable.mockup_mars);

        TextView companyName = (TextView) getActivity().findViewById(R.id.ok_company_name);
        companyName.setText("Mars, Incorporated");

        boolean hasIngredients = true;

        if (hasIngredients) {
            ingredientsLayout = (LinearLayout) getActivity().findViewById(R.id.ok_ingredients_list);

            LinearLayout.LayoutParams ingredientsLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            ingredientsLayoutParams.setMargins((int) getResources().getDimension(R.dimen.margin),
                    0,
                    (int) getResources().getDimension(R.dimen.margin),
                    (int) getResources().getDimension(R.dimen.space));

            TextView ingredient1 = new TextView(getActivity());
            ingredient1.setText("milk chocolate");
            ingredient1.setHeight(50);
            ingredient1.setTextColor(Color.BLACK);
            ingredient1.setBackgroundResource(R.drawable.ingredient_header);
            ingredient1.setGravity(Gravity.CENTER);

            ingredient1.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient1);

            TextView ingredient2 = new TextView(getActivity());
            ingredient2.setText("peanuts");
            ingredient2.setHeight(50);
            ingredient2.setTextColor(Color.BLACK);
            ingredient2.setBackgroundResource(R.drawable.ingredient_header);
            ingredient2.setGravity(Gravity.CENTER);

            ingredient2.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient2);

            TextView ingredient3 = new TextView(getActivity());
            ingredient3.setText("corn syrup");
            ingredient3.setHeight(50);
            ingredient3.setTextColor(Color.BLACK);
            ingredient3.setBackgroundResource(R.drawable.ingredient_header);
            ingredient3.setGravity(Gravity.CENTER);

            ingredient3.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient3);

            TextView ingredient4 = new TextView(getActivity());
            ingredient4.setText("sugar");
            ingredient4.setHeight(50);
            ingredient4.setTextColor(Color.BLACK);
            ingredient4.setBackgroundResource(R.drawable.ingredient_header);
            ingredient4.setGravity(Gravity.CENTER);

            ingredient4.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient4);

            TextView ingredient5 = new TextView(getActivity());
            ingredient5.setText("milkfat");
            ingredient5.setHeight(50);
            ingredient5.setTextColor(Color.BLACK);
            ingredient5.setBackgroundResource(R.drawable.ingredient_header);
            ingredient5.setGravity(Gravity.CENTER);

            ingredient5.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient5);

            TextView ingredient6 = new TextView(getActivity());
            ingredient6.setText("skim milk");
            ingredient6.setHeight(50);
            ingredient6.setTextColor(Color.BLACK);
            ingredient6.setBackgroundResource(R.drawable.ingredient_header);
            ingredient6.setGravity(Gravity.CENTER);

            ingredient6.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient6);

            TextView ingredient7 = new TextView(getActivity());
            ingredient7.setText("lactose");
            ingredient7.setHeight(50);
            ingredient7.setTextColor(Color.BLACK);
            ingredient7.setBackgroundResource(R.drawable.ingredient_header);
            ingredient7.setGravity(Gravity.CENTER);

            ingredient7.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient7);

            ingredientsLayout.setVisibility(LinearLayout.GONE);

            // reasons expand/collapse

            TextView ingredients = (TextView) getActivity().findViewById(R.id.ok_ingredients);

            ingredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ingredientsLayout.getVisibility() == View.VISIBLE) {
                        ingredientsLayout.setVisibility(LinearLayout.GONE);
                    } else {
                        ingredientsLayout.setVisibility(LinearLayout.VISIBLE);

                        final ScrollView scrollView = (ScrollView) getActivity().findViewById(R.id.ok_scroll);
                        scrollView.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                            }
                        });
                    }
                }
            });


        } else {
            TextView ingredients = (TextView) getActivity().findViewById(R.id.ok_ingredients);
            ingredients.setVisibility(View.GONE);

            ingredientsLayout = (LinearLayout) getActivity().findViewById(R.id.bad_ingredients_list);
            ingredientsLayout.setVisibility(RelativeLayout.GONE);
        }
    }

}