package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Product;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONException;
import org.json.JSONObject;

public class SimilarProductInfo extends AsyncTask<Void, Void, Void> {

    private String id;

    public SimilarProductInfo(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject product = new JSONObject(jsonStr);

                String name = product.getString("name");
                String image = product.getString("image");
                Product recommendedProduct = new Product();
                recommendedProduct.setName(name);
                recommendedProduct.setImage(image);
                MainActivity.currentProduct.addRecommended(recommendedProduct);

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