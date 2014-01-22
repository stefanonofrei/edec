package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import android.util.Log;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.ScanProductFragment;
import edu.uaic.fii.wad.edec.model.Product;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductInfo extends AsyncTask<Void, Void, Void> {

    private String id;

    public ProductInfo(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.scanURL + id + ".json", ServiceHandler.GET);

        MainActivity.tasksNumber = 2;  //just product info and company info (no rules & reasons);
        new ProductLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        if (jsonStr != null) {
            try {
                JSONObject product = new JSONObject(jsonStr);

                String name = product.getString("name");
                String image = product.getString("image");
                JSONArray ingredients = product.getJSONArray("ingredients");

                MainActivity.currentProduct = new Product();
                MainActivity.currentProduct.setBarcode(id);
                MainActivity.currentProduct.setName(name);
                MainActivity.currentProduct.setImage(image);

                JSONObject company = product.getJSONObject("company");
                String id = company.getString("id");

                new CompanyInfo(id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                MainActivity.tasksNumber += ingredients.length();

                for (int i = 0; i < ingredients.length(); i++) {
                    JSONObject ingredient = ingredients.getJSONObject(i);
                    id = ingredient.getString("id");

                    new IngredientInfo(id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        MainActivity.tasksNumber++;
        MainActivity.completedTasks.incrementAndGet();
        new ProductVerdict(this.id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.loading.show();
    }

    private class ProductLoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            while (true) {
                if (MainActivity.tasksNumber == MainActivity.completedTasks.get()) {
                    break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (MainActivity.currentProduct.getStatus().equals("green")) {
                ScanProductFragment.scanPageListener.onSwitchToNextFragment(0, 1);
            } else {
                ScanProductFragment.scanPageListener.onSwitchToNextFragment(0, 2);
            }
            MainActivity.loading.dismiss();
            MainActivity.tasksNumber = -1;
            MainActivity.completedTasks.set(0);
        }
    }
}
