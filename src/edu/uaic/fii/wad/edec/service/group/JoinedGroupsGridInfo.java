package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.GroupsFragment;
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

public class JoinedGroupsGridInfo extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        for (int i = 0; i < MainActivity.joinedGroups.size(); i++) {
            String id = MainActivity.joinedGroups.get(i).getId();

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
                    JSONObject group = new JSONObject(jsonStr);

                    String name = group.getString("title");
                    String logo = group.getString("logo");
                    MainActivity.joinedGroups.get(i).setName(name);
                    MainActivity.joinedGroups.get(i).setLogo(logo);
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (MainActivity.loadAllGroups) {
            MainActivity.completedTasks.incrementAndGet();
        } else {
            GroupsFragment.addGridViews();
            MainActivity.loading.dismiss();
        }
    }
}
