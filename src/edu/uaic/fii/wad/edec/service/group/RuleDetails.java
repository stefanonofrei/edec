package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Rule;
import edu.uaic.fii.wad.edec.service.util.Reasons;
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

public class RuleDetails extends AsyncTask<Void, Void, Void> {

    private JSONObject rule;

    public RuleDetails(JSONObject rule) {
        this.rule = rule;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            String ruleName = "";
            int ruleType = 0;
            String ruleReason = "";

            String item_id = rule.getString("item_id");
            String filter_reason_id = rule.getString("filter_reason_id");

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse;
            HttpGet httpGet = new HttpGet(URLs.baseURL + item_id + ".json");

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
                JSONObject item = new JSONObject(jsonStr);

                ruleName = item.getString("name");
            }

            httpGet = new HttpGet(URLs.baseURL + filter_reason_id + ".json");

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
                JSONObject reason = new JSONObject(jsonStr);

                ruleType = getRuleType(reason.getString("for_resource"));
                ruleReason = reason.getString("id");
            }

            MainActivity.currentGroup.addRule(new Rule(ruleType, ruleName,
                    Reasons.getReasonIdFromURL(ruleType, ruleReason), item_id));
        } catch (JSONException ex) {
            System.out.println(ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        MainActivity.completedTasks.incrementAndGet();
    }

    private int getRuleType(String type) {
        if (type.equals("/companies")) {
            return 0;
        } else if (type.equals("/products")) {
            return 1;
        } else {
            return 2;
        }
    }
}
