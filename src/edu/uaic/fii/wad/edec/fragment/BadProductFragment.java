package edu.uaic.fii.wad.edec.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.*;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.listener.PageFragmentListener;
import edu.uaic.fii.wad.edec.adapter.TabsPagerAdapter;

import java.util.ArrayList;

public class BadProductFragment extends Fragment {

    public static PageFragmentListener scanPageListener;
    private LinearLayout reasonsLayout;
    private LinearLayout ingredientsLayout;
    private int currentRecommended;
    private ImageView recommendedLogo;
    private TextView recommendedName;

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
        setProductInfo();
    }

    public void setProductInfo() {
        TextView productName = (TextView) getActivity().findViewById(R.id.bad_product_name);
        productName.setText(MainActivity.currentProduct.getName());

        byte[] decodedString = Base64.decode(MainActivity.currentProduct.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView productLogo = (ImageView) getActivity().findViewById(R.id.bad_product_logo);
        productLogo.setImageBitmap(decodedByte);

        decodedString = Base64.decode(MainActivity.currentProduct.getCompany().getImage(), Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView companyLogo = (ImageView) getActivity().findViewById(R.id.bad_company_logo);
        companyLogo.setImageBitmap(decodedByte);

        TextView companyName = (TextView) getActivity().findViewById(R.id.bad_company_name);
        companyName.setText(MainActivity.currentProduct.getCompany().getName());

        ingredientsLayout = (LinearLayout) getActivity().findViewById(R.id.bad_ingredients_list);

        LinearLayout.LayoutParams ingredientsLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ingredientsLayoutParams.setMargins((int) getResources().getDimension(R.dimen.margin),
                0,
                (int) getResources().getDimension(R.dimen.margin),
                (int) getResources().getDimension(R.dimen.space));

        for (int i = 0; i < MainActivity.currentProduct.getIngredients().size(); i++) {
            TextView ingredient = new TextView(getActivity());
            ingredient.setText(MainActivity.currentProduct.getIngredient(i).getName());
            ingredient.setHeight(50);
            ingredient.setTextColor(Color.BLACK);
            ingredient.setBackgroundResource(R.drawable.ingredient_header);
            ingredient.setGravity(Gravity.CENTER);

            ingredient.setLayoutParams(ingredientsLayoutParams);
            ingredientsLayout.addView(ingredient);
        }

        ingredientsLayout.setVisibility(LinearLayout.GONE);

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

        reasonsLayout = (LinearLayout) getActivity().findViewById(R.id.bad_reasons_list);

        LinearLayout.LayoutParams reasonsLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        reasonsLayoutParams.setMargins((int) getResources().getDimension(R.dimen.margin),
                0,
                (int) getResources().getDimension(R.dimen.margin),
                (int) getResources().getDimension(R.dimen.space));

        for (int i = 0; i < MainActivity.currentProduct.getReasons().size(); i++) {
            TextView reason = new TextView(getActivity());
            reason.setText(MainActivity.currentProduct.getReason(i).getName());
            reason.setHeight(50);
            reason.setTextColor(Color.BLACK);
            reason.setBackgroundResource(R.drawable.reason_header);
            reason.setGravity(Gravity.CENTER);

            reason.setLayoutParams(reasonsLayoutParams);
            reasonsLayout.addView(reason);
        }

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

        currentRecommended = 0;
        decodedString = Base64.decode(MainActivity.currentProduct.getRecommended(currentRecommended).getImage(), Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


        recommendedLogo = (ImageView) getActivity().findViewById(R.id.bad_recommended_logo);
        recommendedLogo.setImageBitmap(decodedByte);
        recommendedName = (TextView) getActivity().findViewById(R.id.bad_recommended_name);
        recommendedName.setText(MainActivity.currentProduct.getRecommended(currentRecommended).getName());

        ImageView leftRecommended = (ImageView) getActivity().findViewById(R.id.bad_recommended_left);

        leftRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentRecommended--;

                if (currentRecommended < 0) {
                    currentRecommended = MainActivity.currentProduct.getRecommended().size() - 1;
                }

                byte[] decodedString = Base64.decode(MainActivity.currentProduct.getRecommended(currentRecommended).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                recommendedLogo.setImageBitmap(decodedByte);
                recommendedName.setText(MainActivity.currentProduct.getRecommended(currentRecommended).getName());
            }
        });

        ImageView rightRecommended = (ImageView) getActivity().findViewById(R.id.bad_recommended_right);

        rightRecommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentRecommended++;

                if (currentRecommended == MainActivity.currentProduct.getRecommended().size()) {
                    currentRecommended = 0;
                }

                byte[] decodedString = Base64.decode(MainActivity.currentProduct.getRecommended(currentRecommended).getImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                recommendedLogo.setImageBitmap(decodedByte);
                recommendedName.setText(MainActivity.currentProduct.getRecommended(currentRecommended).getName());
            }
        });
    }
}