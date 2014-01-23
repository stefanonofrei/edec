package edu.uaic.fii.wad.edec.service.group;

import android.os.AsyncTask;
import edu.uaic.fii.wad.edec.activity.MainActivity;
import edu.uaic.fii.wad.edec.service.util.Token;
import edu.uaic.fii.wad.edec.service.util.URLs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class DeleteGroup extends AsyncTask<Void, Void, Void> {

    private String id;

    public DeleteGroup(String id) {
        this.id = id;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpEntity httpEntity = null;
        HttpResponse httpResponse;

        HttpDelete httpDelete = new HttpDelete(URLs.baseURL + id + ".json");

        httpDelete.setHeader("Authorization", Token.CURRENT);

        try {
            httpResponse = httpClient.execute(httpDelete);

            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
            }

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

        MainActivity.loadAllGroups = false;
        new MyGroupsListing().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
