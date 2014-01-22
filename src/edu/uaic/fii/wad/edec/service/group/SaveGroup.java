package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import android.util.Log;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.GroupsFragment;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.Reasons;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SaveGroup extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();

        JSONObject group = new JSONObject();
        try {
            group.put("title", MainActivity.currentGroup.getName());
            // TODO base64 image problem
            group.put("logo", "");
            group.put("description", MainActivity.currentGroup.getDescription());

            JSONArray rules = new JSONArray();

            for (int i = 0; i < MainActivity.currentGroup.getRules().size(); i++) {
                JSONObject rule = new JSONObject();

                    rule.put("item_id", MainActivity.currentGroup.getRule(i).getId());
                    rule.put("filter_reason_id", Reasons.getReasonURL(MainActivity.currentGroup.getRule(i).getType(),
                            MainActivity.currentGroup.getRule(i).getReason()));
                    rules.put(rule);

            }

            group.put("rules", rules);

        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        serviceHandler.makeServiceCall(URLs.baseURL + "/groups.json", ServiceHandler.POST, null, group);

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