package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsGroupsListing extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.friendsGroupsURL, ServiceHandler.GET);

        MainActivity.friendsGroups.clear();

        if (jsonStr != null) {
            try {
                JSONArray groups = new JSONArray(jsonStr);

                for (int i = 0; i < groups.length(); i++) {
                    JSONObject group = groups.getJSONObject(i);

                    String id = group.getString("id");
                    MainActivity.friendsGroups.add(new Group(id, null, null, null, null));
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

        new FriendsGroupsGridInfo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
