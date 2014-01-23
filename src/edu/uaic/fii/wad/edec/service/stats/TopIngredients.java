package edu.uaic.fii.wad.edec.service.stats;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.fragment.StatisticsFragment;
import edu.uaic.fii.wad.edec.model.Ingredient;
import edu.uaic.fii.wad.edec.service.util.Token;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * User: irina
 * Date: 1/22/14
 * Time: 5:44 PM
 */
public class TopIngredients extends AsyncTask<Void, Void, Void> {

    private ArrayList<Ingredient> ingredientsList = new ArrayList<Ingredient>();

    private StatisticsFragment statisticsFragment;

    public TopIngredients(StatisticsFragment fragment){
        statisticsFragment = fragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;
        HttpGet httpGet = new HttpGet(URLs.topURL + "ingredients.json");

        httpGet.setHeader("Authorization", Token.CURRENT);

        String jsonStr = null;

        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
            }

            jsonStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (jsonStr != null) {
            try {
                JSONArray ingredients = new JSONArray(jsonStr);
                for (int i = 0; i < ingredients.length(); i++){
                    JSONObject ingredient = ingredients.getJSONObject(i);
                    String id = ingredient.getString("id");
                    addIngredientInfo(id);
                }

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        StatisticsFragment.ingredientsList = ingredientsList;
        statisticsFragment.refreshIngredientsData();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private void addIngredientInfo(String id){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;
        HttpGet httpGet = new HttpGet(URLs.baseURL + id + ".json");

        httpGet.setHeader("Authorization", Token.CURRENT);

        String jsonStr = null;

        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
            }

            jsonStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (jsonStr != null) {
            try {
                JSONObject ingredient = new JSONObject(jsonStr);

                String name = ingredient.getString("name");
                String image =  ingredient.getString("image");
                ingredientsList.add(new Ingredient(id, name, image));

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

}