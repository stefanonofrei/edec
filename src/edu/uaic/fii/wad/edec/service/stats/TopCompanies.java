package edu.uaic.fii.wad.edec.service.stats;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.StatisticsFragment;
import edu.uaic.fii.wad.edec.model.Company;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * User: irina
 * Date: 1/22/14
 * Time: 2:01 PM
 */


public class TopCompanies extends AsyncTask<Void, Void, Void>{

    private ArrayList<Company> companiesList = new ArrayList<Company>();
    private StatisticsFragment statisticsFragment;

    public TopCompanies(StatisticsFragment fragment){
        statisticsFragment = fragment;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.topURL + "companies.json", ServiceHandler.GET);
        System.out.println(jsonStr);
        if (jsonStr != null) {
            try {
                JSONArray companies = new JSONArray(jsonStr);
                for (int i = 0; i < companies.length(); i++){
                    JSONObject company = companies.getJSONObject(i);
                    String id = company.getString("id");
                    addCompanyInfo(id);
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
        StatisticsFragment.companiesList = companiesList;
        statisticsFragment.refreshCompaniesData();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    private void addCompanyInfo(String id){
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);
        if (jsonStr != null) {
            try {
                JSONObject company = new JSONObject(jsonStr);

                String name = company.getString("name");
                String image = company.getString("logo");
                companiesList.add(new Company(id, name, image));

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

}