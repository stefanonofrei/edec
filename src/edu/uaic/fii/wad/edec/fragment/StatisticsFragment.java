package edu.uaic.fii.wad.edec.fragment;

import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.uaic.fii.wad.edec.R;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.*;
import edu.uaic.fii.wad.edec.service.group.GroupDetails;
import edu.uaic.fii.wad.edec.service.stats.TopCompanies;
import edu.uaic.fii.wad.edec.service.stats.TopIngredients;
import edu.uaic.fii.wad.edec.service.stats.TopProducts;
import edu.uaic.fii.wad.edec.service.stats.TrendingGroups;
import edu.uaic.fii.wad.edec.service.util.ImageBase64;
import edu.uaic.fii.wad.edec.view.ExpandableHeightGridView;
import edu.uaic.fii.wad.edec.adapter.GridViewAdapter;
import java.util.ArrayList;
import java.util.Arrays;
import android.os.AsyncTask;

public class StatisticsFragment extends Fragment{

    GridViewAdapter customGridAdapter;
    ProgressDialog barProgressDialog;
    private ViewPager viewPager;
    public static ArrayList<Company> companiesList;
    public static ArrayList<Product> productsList;
    public static ArrayList<Ingredient> ingredientsList;
    public static ArrayList<Group> groupList;

    private ArrayList imageItems = new ArrayList();

    private ImageView firstCompany;
    private ImageView secondCompany;
    private ImageView thirdCompany;

    private ImageView firstProduct;
    private ImageView secondProduct;
    private ImageView thirdProduct;

    private ImageView firstIngredient;
    private ImageView secondIngredient;
    private ImageView thirdIngredient;

    private TextView firstCompanyName;
    private TextView secondCompanyName;
    private TextView thirdCompanyName;

    private TextView firstProductName;
    private TextView secondProductName;
    private TextView thirdProductName;

    private TextView firstIngredientName;
    private TextView secondIngredientName;
    private TextView thirdIngredientName;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment, container, false);

        ExpandableHeightGridView myGridView = (ExpandableHeightGridView) view.findViewById(R.id.stats_trending_groups_grid);
        customGridAdapter = new GridViewAdapter(view.getContext(), R.layout.grid_item, imageItems);
        myGridView.setAdapter(customGridAdapter);
        myGridView.setExpanded(true);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.groupState = setGroupUserAssociation(groupList.get(i).getId());
                new GroupDetails(groupList.get(i).getId()).execute();
                viewPager.setCurrentItem(1);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getViewElements();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            if(productsList == null || companiesList == null || ingredientsList == null){
                barProgressDialog = new ProgressDialog(getActivity());

                barProgressDialog.setTitle("Loading");

                barProgressDialog.setMessage("Please wait...");

                barProgressDialog.setProgress(0);

                barProgressDialog.setMax(20);

                barProgressDialog.show();

                new TopCompanies(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new TopProducts(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new TopIngredients(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                new TrendingGroups(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }


        }
    }

    public void refreshCompaniesData(){
        verifyTasks();
        if(companiesList.size() > 0){
            firstCompany.setImageBitmap(ImageBase64.decodeImage(companiesList.get(0).getImage()));
            firstCompanyName.setText(companiesList.get(0).getName());
        }
        if(companiesList.size() > 1){
            secondCompany.setImageBitmap(ImageBase64.decodeImage(companiesList.get(1).getImage()));
            secondCompanyName.setText(companiesList.get(1).getName());
        }
        if(companiesList.size() > 2){
            thirdCompany.setImageBitmap(ImageBase64.decodeImage(companiesList.get(2).getImage()));
            thirdCompanyName.setText(companiesList.get(2).getName());
        }
    }

    public void refreshProductsData(){
        verifyTasks();
        if(productsList.size() > 0){
            firstProduct.setImageBitmap(ImageBase64.decodeImage(productsList.get(0).getImage()));
            firstProductName.setText(productsList.get(0).getName());
        }
        if(productsList.size() > 1){
            secondProduct.setImageBitmap(ImageBase64.decodeImage(productsList.get(1).getImage()));
            secondProductName.setText(productsList.get(1).getName());
        }
        if(productsList.size() > 2){
            thirdProduct.setImageBitmap(ImageBase64.decodeImage(productsList.get(2).getImage()));
            thirdProductName.setText(productsList.get(2).getName());
        }

    }

    public void refreshIngredientsData(){
        verifyTasks();
        if(ingredientsList.size() > 0){
            firstIngredient.setImageBitmap(ImageBase64.decodeImage(ingredientsList.get(0).getImage()));
            firstIngredientName.setText(ingredientsList.get(0).getName());
        }
        if(ingredientsList.size() > 1){
            secondIngredient.setImageBitmap(ImageBase64.decodeImage(ingredientsList.get(1).getImage()));
            secondIngredientName.setText(ingredientsList.get(1).getName());
        }
        if(ingredientsList.size() > 2){
            thirdIngredient.setImageBitmap(ImageBase64.decodeImage(ingredientsList.get(2).getImage()));
            thirdIngredientName.setText(ingredientsList.get(2).getName());
        }

    }

    public void refreshGroupData(){
        ArrayList newImages = new ArrayList();
        for(int i = 0; i < groupList.size(); i++){

            Bitmap bitmap = ImageBase64.decodeImage(groupList.get(i).getLogo());
            newImages.add(new GridItem(bitmap, groupList.get(i).getName()));
        }
        imageItems.clear();
        imageItems.addAll(newImages);
        customGridAdapter.notifyDataSetChanged();
    }

    public void getViewElements(){

        viewPager = (ViewPager) getActivity().findViewById(edu.uaic.fii.wad.edec.R.id.pager);
        firstCompany = (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_first_company_logo);
        firstCompanyName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_first_company_name);

        secondCompany = (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_second_company_logo);
        secondCompanyName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_second_company_name);

        thirdCompany = (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_third_company_logo);
        thirdCompanyName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_third_company_name);

        firstProduct = (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_first_product_logo);
        firstProductName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_first_product_name);

        secondProduct= (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_second_product_logo);
        secondProductName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_second_product_name);

        thirdProduct = (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_third_product_logo);
        thirdProductName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_third_product_name);

        firstIngredient = (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_first_ingredient_logo);
        firstIngredientName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_first_ingredient_name);

        secondIngredient= (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_second_ingredient_logo);
        secondIngredientName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_second_ingredient_name);

        thirdIngredient = (ImageView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_third_ingredient_logo);
        thirdIngredientName = (TextView) getView().findViewById(edu.uaic.fii.wad.edec.R.id.stats_third_ingredient_name);

    }

    public void verifyTasks(){
        if(productsList != null && companiesList != null && ingredientsList != null && groupList != null) {
            barProgressDialog.dismiss();
        }
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