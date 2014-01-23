package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.GroupDetailsFragment;
import edu.uaic.fii.wad.edec.fragment.GroupsFragment;
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

public class GroupDetails extends AsyncTask<Void, Void, Void> {

    private String id;
    private int parent;

    public GroupDetails(String id, int parent) {
        this.id = id;
        this.parent = parent;
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
                JSONObject group = new JSONObject(jsonStr);

                String id = group.getString("id");
                String name = group.getString("title");
                String description = group.getString("description");
                String logo = group.getString("logo");
                JSONArray rules = group.getJSONArray("rules");

                MainActivity.currentGroup = new Group();
                MainActivity.currentGroup.setId(id);
                MainActivity.currentGroup.setName(name);
                MainActivity.currentGroup.setDescription(description);
                MainActivity.currentGroup.setLogo(logo);

                MainActivity.tasksNumber = rules.length();
                new RulesLoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                for (int i = 0; i < rules.length(); i++) {
                    JSONObject rule = rules.getJSONObject(i);

                    new RuleDetails(rule).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        MainActivity.loading.show();
    }

    private class RulesLoadTask extends AsyncTask<Void, Void, Void> {

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

            GroupDetailsFragment.parent = parent;
            if (parent == 1) {
                GroupsFragment.pageListener.onSwitchToNextFragment(1, 0);
            } else if (parent == 2) {
                StatisticsFragment.pageListener.onSwitchToNextFragment(2, 0);
            }
            MainActivity.loading.dismiss();
            MainActivity.tasksNumber = -1;
            MainActivity.completedTasks.set(0);
        }
    }
}
