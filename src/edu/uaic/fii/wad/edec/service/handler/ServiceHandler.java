package edu.uaic.fii.wad.edec.service.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import edu.uaic.fii.wad.edec.service.util.Token;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ServiceHandler {

    static String response = null;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int PUT = 3;
    public final static int DELETE = 4;

    public ServiceHandler() {

    }

    public String makeServiceCall(String url, int method) {
        return this.makeServiceCall(url, method, null);
    }

    public String makeServiceCall(String url, int method, List<NameValuePair> params) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;

            switch (method) {
                case POST: {
                    HttpPost httpPost = new HttpPost(url);

                    if (params != null) {
                        httpPost.setEntity(new UrlEncodedFormEntity(params));
                    }

                    httpPost.setHeader("Authorization", Token.CURRENT);

                    httpResponse = httpClient.execute(httpPost);
                    break;
                }

                case GET: {
                    if (params != null) {
                        String paramString = URLEncodedUtils.format(params, "utf-8");
                        url += "?" + paramString;
                    }
                    HttpGet httpGet = new HttpGet(url);

                    httpGet.setHeader("Authorization", Token.CURRENT);

                    httpResponse = httpClient.execute(httpGet);
                    break;
                }

                case PUT: {
                    // TODO
                    break;
                }

                case DELETE: {
                    HttpDelete httpDelete = new HttpDelete(url);

                    httpDelete.setHeader("Authorization", Token.CURRENT);

                    httpResponse = httpClient.execute(httpDelete);
                    break;
                }

            }

            if (httpResponse != null) {
                httpEntity = httpResponse.getEntity();
            }
            response = EntityUtils.toString(httpEntity);

        } catch (UnsupportedEncodingException ex) {
            System.out.println(ex.getMessage());
        } catch (ClientProtocolException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return response;
    }
}