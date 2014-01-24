package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.fragment.GroupsFragment;
import edu.uaic.fii.wad.edec.fragment.SearchFragment;
import edu.uaic.fii.wad.edec.fragment.StatisticsFragment;
import edu.uaic.fii.wad.edec.model.Company;
import edu.uaic.fii.wad.edec.model.Group;
import edu.uaic.fii.wad.edec.service.handler.ServiceHandler;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * User: irina
 * Date: 1/23/14
 * Time: 7:16 PM
 */
public class SearchGroup extends AsyncTask<Void, Void, Void> {

    private ArrayList<Group> searchResults = new ArrayList<Group>();
    private String searchInput;

    public SearchGroup(String input) {
        this.searchInput = input;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = null;
        try {
            jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + "/groups/" + URLEncoder.encode(searchInput, "ISO-8859-1")  + "/search.json", ServiceHandler.GET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (jsonStr != null) {
            try {
                JSONArray groups = new JSONArray(jsonStr);
                for (int i = 0; i < groups.length(); i++){
                    JSONObject group = groups.getJSONObject(i);
                    String id = group.getString("id");
                    addGroupInfo(id);
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
        SearchFragment.groupList = searchResults;
        SearchFragment.refreshGroupData();
        MainActivity.loading.dismiss();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.loading.show();
    }

    private void addGroupInfo(String id){
        ServiceHandler serviceHandler = new ServiceHandler();
        String jsonStr = serviceHandler.makeServiceCall(URLs.baseURL + id + ".json", ServiceHandler.GET);
        if (jsonStr != null) {
            try {
                JSONObject company = new JSONObject(jsonStr);

                String name = company.getString("title");
                String image = company.getString("logo");
                Group group = new Group();
                group.setId(id);
                group.setLogo(image);
                group.setName(name);

                searchResults.add(group);

            } catch (JSONException ex) {
                System.out.println(ex.getMessage());
            }
        }

    }

}