package edu.uaic.fii.wad.edec.service.scan;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.model.Company;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CompanyInfo extends AsyncTask<Void, Void, Void> {

    private String id;

    public CompanyInfo(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);

        if (jsonStr != null) {
            try {
                JSONObject company = new JSONObject(jsonStr);

                String name = company.getString("name");
                String image = company.getString("logo");

                MainActivity.currentProduct.setCompany(new Company(id, name, image));

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
        //new MyGroupsGridInfo().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //MainActivity.loading.show();
    }


}
