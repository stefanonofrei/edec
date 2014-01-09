package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.GroupsFragment;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsGroupsGridInfo extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();

        for (int i = 0; i < MainActivity.friendsGroups.size(); i++) {
            String id = MainActivity.friendsGroups.get(i).getId();
            String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject group = new JSONObject(jsonStr);

                    String name = group.getString("title");
                    MainActivity.friendsGroups.get(i).setName(name);
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
