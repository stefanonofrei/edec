package edu.uaic.fii.wad.edec.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.adapter.TabsPagerAdapter;

import java.util.ArrayList;

public class BadProductFragment extends Fragment {

    public static PageFragmentListener scanPageListener;

    public BadProductFragment(PageFragmentListener listener) {
        scanPageListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bad_product_fragment, container, false);
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

    private LinearLayout reasonsLayout;
    private LinearLayout ingredientsLayout;
    private int currentRecommended;
    private TextView recommendedName;
    private ImageView recommendedLogo;
    private ArrayList<String> recommendations;

    public void mockProduct() {
        ImageView productLogo = (ImageView) getActivity().findViewById(R.id.bad_product_logo);
        productLogo.setImageResource(R.drawable.mockup_pepsi);

        TextView productName = (TextView) getActivity().findViewById(R.id.bad_product_name);
        productName.setText("Pepsi Can");

        ImageView companyLogo = (ImageView) getActivity().findViewById(R.id.bad_company_logo);
        companyLogo.setImageResource(R.drawable.mockup_pepsico);

        TextView companyName = (TextView) getActivity().findViewById(R.id.bad_company_name);
        companyName.setText("PepsiCo Inc.");

        boolean hasIngredients = true;

        if (hasIngredients) {
            ingredientsLayout = (LinearLayout) getActivity().findViewById(R.id.bad_ingredients_list);

            LinearLayout.LayoutParams ingredientsLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            ingredientsLayoutParams.setMargins((int) getResources().getDimension(R.dimen.margin),
                    0,
                    (int) getResources().getDimension(R.dimen.margin),
                    (int) getResources().getDimension(R.dimen.space));

            TextView ingredient1 = new TextView(getActivity());
            ingredient1.setText("carbonated water");
            ingredient1.setHeight(50);
            ingredient1.setTextColor(Color.BLACK);
            ingredient1.setBackgroundResource(R.drawable.ingredient_header);
            ingredient1.setGravity(Gravity.CENTER);

            ingredient1.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient1);

            TextView ingredient2 = new TextView(getActivity());
            ingredient2.setText("high fructose corn syrup");
            ingredient2.setHeight(50);
            ingredient2.setTextColor(Color.BLACK);
            ingredient2.setBackgroundResource(R.drawable.ingredient_header);
            ingredient2.setGravity(Gravity.CENTER);

            ingredient2.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient2);

            TextView ingredient3 = new TextView(getActivity());
            ingredient3.setText("caramel color");
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
            ingredient5.setText("caffeine");
            ingredient5.setHeight(50);
            ingredient5.setTextColor(Color.BLACK);
            ingredient5.setBackgroundResource(R.drawable.ingredient_header);
            ingredient5.setGravity(Gravity.CENTER);

            ingredient5.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient5);

            TextView ingredient6 = new TextView(getActivity());
            ingredient6.setText("citric acid");
            ingredient6.setHeight(50);
            ingredient6.setTextColor(Color.BLACK);
            ingredient6.setBackgroundResource(R.drawable.ingredient_header);
            ingredient6.setGravity(Gravity.CENTER);

            ingredient6.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient6);

            TextView ingredient7 = new TextView(getActivity());
            ingredient7.setText("natural flavors");
            ingredient7.setHeight(50);
            ingredient7.setTextColor(Color.BLACK);
            ingredient7.setBackgroundResource(R.drawable.ingredient_header);
            ingredient7.setGravity(Gravity.CENTER);

            ingredient7.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient7);

            ingredientsLayout.setVisibility(LinearLayout.GONE);

            // reasons expand/collapse

            TextView ingredients = (TextView) getActivity().findViewById(R.id.bad_ingredients);

            ingredients.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ingredientsLayout.getVisibility() == View.VISIBLE) {
                        ingredientsLayout.setVisibility(LinearLayout.GONE);
                    } else {
                        ingredientsLayout.setVisibility(LinearLayout.VISIBLE);
                    }
                }
            });
        } else {
            TextView ingredients = (TextView) getActivity().findViewById(R.id.bad_ingredients);
            ingredients.setVisibility(View.GONE);

            ingredientsLayout = (LinearLayout) getActivity().findViewById(R.id.bad_ingredients_list);
            ingredientsLayout.setVisibility(RelativeLayout.GONE);
        }

        reasonsLayout = (LinearLayout) getActivity().findViewById(R.id.bad_reasons_list);

        LinearLayout.LayoutParams reasonsLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        reasonsLayoutParams.setMargins((int) getResources().getDimension(R.dimen.margin),
                0,
                (int) getResources().getDimension(R.dimen.margin),
                (int) getResources().getDimension(R.dimen.space));

        TextView reason1 = new TextView(getActivity());
        reason1.setText("contains caffeine");
        reason1.setHeight(50);
        reason1.setTextColor(Color.BLACK);
        reason1.setBackgroundResource(R.drawable.reason_header);
        reason1.setGravity(Gravity.CENTER);

        reason1.setLayoutParams(reasonsLayoutParams);
        reasonsLayout.addView(reason1);

        TextView reason2 = new TextView(getActivity());
        reason2.setText("made by PepsiCo Inc.");
        reason2.setHeight(50);
        reason2.setTextColor(Color.BLACK);
        reason2.setBackgroundResource(R.drawable.reason_header);
        reason2.setGravity(Gravity.CENTER);

        reason2.setLayoutParams(reasonsLayoutParams);
        reasonsLayout.addView(reason2);

        reasonsLayout.setVisibility(LinearLayout.GONE);

        // reasons expand/collapse

        TextView reasons = (TextView) getActivity().findViewById(R.id.bad_reasons);

        reasons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reasonsLayout.getVisibility() == View.VISIBLE) {
                    reasonsLayout.setVisibility(LinearLayout.GONE);
                } else {
                    reasonsLayout.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        // recommendations

        currentRecommended = 0;

        recommendations = new ArrayList<String>();
        recommendations.add("Sprite");
        recommendations.add("Fanta");
        recommendations.add("7 Up");

        recommendedLogo = (ImageView) getActivity().findViewById(R.id.bad_recommended_logo);
        recommendedLogo.setImageResource(R.drawable.mockup_sprite);

        recommendedName = (TextView) getActivity().findViewById(R.id.bad_recommended_name);
        recommendedName.setText("Sprite");


        ImageView leftRecommended = (ImageView) getActivity().findViewById(R.id.bad_recommended_left);

        leftRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentRecommended == 0) {
                    recommendedName.setText(recommendations.get(2));
                    recommendedLogo.setImageResource(R.drawable.mockup_sevenup);
                    currentRecommended = 2;
                } else if (currentRecommended == 1) {
                    recommendedName.setText(recommendations.get(0));
                    recommendedLogo.setImageResource(R.drawable.mockup_sprite);
                    currentRecommended = 0;
                } else if (currentRecommended == 2) {
                    recommendedName.setText(recommendations.get(1));
                    recommendedLogo.setImageResource(R.drawable.mockup_fanta);
                    currentRecommended = 1;
                }
            }
        });

        ImageView rightRecommended = (ImageView) getActivity().findViewById(R.id.bad_recommended_right);

        rightRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentRecommended == 0) {
                    recommendedName.setText(recommendations.get(1));
                    recommendedLogo.setImageResource(R.drawable.mockup_fanta);
                    currentRecommended = 1;
                } else if (currentRecommended == 1) {
                    recommendedName.setText(recommendations.get(2));
                    recommendedLogo.setImageResource(R.drawable.mockup_sevenup);
                    currentRecommended = 2;
                } else if (currentRecommended == 2) {
                    recommendedName.setText(recommendations.get(0));
                    recommendedLogo.setImageResource(R.drawable.mockup_sprite);
                    currentRecommended = 0;
                }
            }
        });
    }
}