package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.service.util.Token;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class GroupMigration extends AsyncTask<Void, Void, Void> {

    private String id;
    private String action;

    public GroupMigration(String id, String action) {
        this.id = id;
        this.action = action;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;
        HttpGet httpGet = new HttpGet(URLs.baseURL + id + action + ".json");

        httpGet.setHeader("Authorization", Token.CURRENT);

        String jsonStr = null;

        try {
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
            }

            jsonStr = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

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

        MainActivity.loadAllGroups = true;
        MainActivity.loadGroups();
    }
}
