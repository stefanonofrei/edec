package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Reason;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONException;
import org.json.JSONObject;

public class FilterReasonInfo extends AsyncTask<Void, Void, Void> {

    private String id;

    public FilterReasonInfo(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject product = new JSONObject(jsonStr);

                String description = product.getString("short_description");
                Reason reason = new Reason();
                reason.setName(description);
                MainActivity.currentProduct.addReason(reason);
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
        MainActivity.loading.show();
    }
}
