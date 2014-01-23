package edu.uaic.fii.wad.edec.service.stats;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.fragment.StatisticsFragment;
import edu.uaic.fii.wad.edec.model.Group;
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
 * Time: 5:51 PM
 */
public class TrendingGroups extends AsyncTask<Void, Void, Void> {

    private ArrayList<Group> groupList = new ArrayList<Group>();
    private StatisticsFragment statisticsFragment;


    public TrendingGroups(StatisticsFragment fragment){
        statisticsFragment = fragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;
        HttpGet httpGet = new HttpGet(URLs.trendingURL);

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
                JSONArray groups = new JSONArray(jsonStr);
                for (int i = 0; i < groups.length(); i++){
                    JSONObject group = groups.getJSONObject(i);
                    String id = group.getString("id");
                    addGroupInfo(id);
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
        StatisticsFragment.groupList = groupList;
        statisticsFragment.refreshGroupData();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private void addGroupInfo(String id){
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
                JSONObject company = new JSONObject(jsonStr);

                String name = company.getString("title");
                String image = company.getString("logo");
                Group group = new Group();
                group.setId(id);
                group.setLogo(image);
                group.setName(name);

                groupList.add(group);

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }


}