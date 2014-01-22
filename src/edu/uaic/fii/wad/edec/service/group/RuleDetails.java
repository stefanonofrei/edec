package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Rule;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONException;
import org.json.JSONObject;

public class RuleDetails extends AsyncTask<Void, Void, Void> {

    private JSONObject rule;

    public RuleDetails(JSONObject rule) {
        this.rule = rule;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();

        try {
            String ruleName = "";
            int ruleType = 0;
            int ruleReason = 0;

            String item_id = rule.getString("item_id");
            String filter_reason_id = rule.getString("filter_reason_id");

            String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + item_id + ".json", ServiceHandler.GET);

            if (jsonStr != null) {
                JSONObject item = new JSONObject(jsonStr);

                ruleName = item.getString("name");
            }

            jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + filter_reason_id + ".json", ServiceHandler.GET);

            if (jsonStr != null) {
                JSONObject reason = new JSONObject(jsonStr);

                ruleType = getRuleType(reason.getString("for_resource"));
                ruleReason = getRuleReason(reason.getString("id"));
            }

            MainActivity.currentGroup.addRule(new Rule(ruleType, ruleName, ruleReason - 1, item_id));
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

    private int getRuleReason(String type) {
        return Integer.parseInt(type.substring(type.length() - 1));
    }
}
