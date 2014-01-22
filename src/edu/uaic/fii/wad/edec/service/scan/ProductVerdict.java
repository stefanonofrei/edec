package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductVerdict extends AsyncTask<Void, Void, Void> {

    private String id;

    public ProductVerdict(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.scanURL + id + "/verdict.json", ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject verdict = new JSONObject(jsonStr);

                String status = verdict.getString("status");
                JSONArray reasons = verdict.getJSONArray("reasons");

                MainActivity.currentProduct.setStatus(status);
                MainActivity.tasksNumber += reasons.length();

                for (int i = 0; i < reasons.length(); i++) {
                    new FilterReasonInfo(reasons.getString(i)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

        MainActivity.completedTasks.incrementAndGet();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}
