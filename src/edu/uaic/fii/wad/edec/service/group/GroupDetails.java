package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.GroupsFragment;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupDetails extends AsyncTask<Void, Void, Void> {

    private String id;

    public GroupDetails(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();

        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject group = new JSONObject(jsonStr);

                String name = group.getString("title");
                String description = group.getString("description");
                String logo = group.getString("logo");
                JSONArray rules = group.getJSONArray("rules");

                MainActivity.currentGroup = new Group();
                MainActivity.currentGroup.setName(name);
                MainActivity.currentGroup.setDescription(description);

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

            GroupsFragment.pageListener.onSwitchToNextFragment(1, 0);
            MainActivity.loading.dismiss();
            MainActivity.tasksNumber = -1;
            MainActivity.completedTasks.set(0);
        }
    }
}
