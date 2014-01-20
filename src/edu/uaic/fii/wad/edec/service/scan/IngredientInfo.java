package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.model.Ingredient;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IngredientInfo extends AsyncTask<Void, Void, Void> {

    private String id;

    public IngredientInfo(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject ingredient = new JSONObject(jsonStr);

                String name = ingredient.getString("name");
                MainActivity.currentProduct.addIngredient(new Ingredient(id, name));

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.completedTasks.incrementAndGet();
        //new MyGroupsGridInfo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


}
