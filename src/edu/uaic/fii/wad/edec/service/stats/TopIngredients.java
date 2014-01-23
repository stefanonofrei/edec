package edu.uaic.fii.wad.edec.service.stats;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.StatisticsFragment;
import edu.uaic.fii.wad.edec.model.Company;
import edu.uaic.fii.wad.edec.model.Ingredient;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.topURL + "ingredients.json", ServiceHandler.GET);
        System.out.println(jsonStr);
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
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);
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