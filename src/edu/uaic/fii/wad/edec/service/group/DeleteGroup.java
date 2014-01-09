package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;

public class DeleteGroup extends AsyncTask<Void, Void, Void> {

    private String id;

    public DeleteGroup(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.DELETE);

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
