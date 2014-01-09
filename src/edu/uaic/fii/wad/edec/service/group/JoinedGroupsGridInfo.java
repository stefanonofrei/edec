package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONException;
import org.json.JSONObject;

public class JoinedGroupsGridInfo extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();

        for (int i = 0; i < MainActivity.joinedGroups.size(); i++) {
            String id = MainActivity.joinedGroups.get(i).getId();
            String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);

            if (jsonStr != null) {
                try {
                    JSONObject group = new JSONObject(jsonStr);

                    String name = group.getString("title");
                    MainActivity.myGroups.get(i).setName(name);
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

        MainActivity.completedTasks.incrementAndGet();
    }
}
