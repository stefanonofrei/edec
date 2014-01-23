package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.service.util.Reasons;
import edu.uaic.fii.wad.edec.service.util.Token;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SaveGroup extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        JSONObject group = new JSONObject();
        try {
            group.put("title", MainActivity.currentGroup.getName());
            group.put("logo", MainActivity.currentGroup.getLogo());
            group.put("description", MainActivity.currentGroup.getDescription());

            JSONArray rules = new JSONArray();

            for (int i = 0; i < MainActivity.currentGroup.getRules().size(); i++) {
                JSONObject rule = new JSONObject();

                rule.put("item_id", MainActivity.currentGroup.getRule(i).getId());
                rule.put("filter_reason_id", Reasons.getReasonURLFromId(MainActivity.currentGroup.getRule(i).getType(),
                        MainActivity.currentGroup.getRule(i).getReason()));
                rules.put(rule);

            }

            group.put("rules", rules);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;

        HttpPost httpPost = new HttpPost(URLs.baseURL + "/groups.json");


        try {
            StringEntity entity = new StringEntity(group.toString());
            entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httpPost.setEntity(entity);

            httpPost.setHeader("Authorization", Token.CURRENT);
            httpResponse = httpClient.execute(httpPost);

            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        MainActivity.loading.show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.loadAllGroups = false;
        new MyGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}
