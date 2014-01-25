package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
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

public class SimilarProducts extends AsyncTask<Void, Void, Void> {

    private String id;

    public SimilarProducts(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;
        HttpGet httpGet = new HttpGet(URLs.scanURL + id + "/similar.json");

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
                JSONArray similar = new JSONArray(jsonStr);

                MainActivity.tasksNumber += similar.length();

                for (int i = 0; i < similar.length(); i++) {
                    JSONObject product = similar.getJSONObject(i);
                    new SimilarProductInfo(product.getString("id")).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        MainActivity.completedTasks.incrementAndGet();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
