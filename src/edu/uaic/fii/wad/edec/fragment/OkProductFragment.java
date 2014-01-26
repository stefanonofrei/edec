package edu.uaic.fii.wad.edec.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
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
        setProductInfo();
    }

    private LinearLayout ingredientsLayout;

    public void setProductInfo() {
        TextView productName = (TextView) getActivity().findViewById(R.id.ok_product_name);
        if (MainActivity.currentProduct.getName().length() > 15) {
            productName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        }
        productName.setText(MainActivity.currentProduct.getName());

        byte[] decodedString = Base64.decode(MainActivity.currentProduct.getImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView productLogo = (ImageView) getActivity().findViewById(R.id.ok_product_logo);
        productLogo.setImageBitmap(decodedByte);

        decodedString = Base64.decode(MainActivity.currentProduct.getCompany().getImage(), Base64.DEFAULT);
        decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ImageView companyLogo = (ImageView) getActivity().findViewById(R.id.ok_company_logo);
        companyLogo.setImageBitmap(decodedByte);

        TextView companyName = (TextView) getActivity().findViewById(R.id.ok_company_name);
        companyName.setText(MainActivity.currentProduct.getCompany().getName());

        ingredientsLayout = (LinearLayout) getActivity().findViewById(R.id.ok_ingredients_list);

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
    }
}