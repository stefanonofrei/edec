package edu.uaic.fii.wad.edec.service.stats;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.StatisticsFragment;
import edu.uaic.fii.wad.edec.model.Product;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * User: irina
 * Date: 1/22/14
 * Time: 5:15 PM
 */
public class TopProducts extends AsyncTask<Void, Void, Void> {

    private ArrayList<Product> productList = new ArrayList<Product>();
    private StatisticsFragment statisticsFragment;

    public TopProducts(StatisticsFragment fragment){
        statisticsFragment = fragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.topURL + "products.json", ServiceHandler.GET);
        System.out.println(jsonStr);
        if (jsonStr != null) {
            try {
                JSONArray products = new JSONArray(jsonStr);
                for (int i = 0; i < products.length(); i++){
                    JSONObject product = products.getJSONObject(i);
                    String id = product.getString("id");
                    addProductInfo(id);
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
        StatisticsFragment.productsList = productList;
        statisticsFragment.refreshProductsData();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private void addProductInfo(String id){
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);
        if (jsonStr != null) {
            try {
                JSONObject company = new JSONObject(jsonStr);
                String name = company.getString("name");
                String image = company.getString("image");
                Product product = new Product();
                product.setImage(image);
                product.setName(name);
                product.setBarcode(id);
                productList.add(product);

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

}