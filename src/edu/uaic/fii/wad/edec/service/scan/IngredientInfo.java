package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Ingredient;
import edu.uaic.fii.wad.edec.service.util.Token;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class IngredientInfo extends AsyncTask<Void, Void, Void> {

    private String id;

    public IngredientInfo(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
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
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


}
