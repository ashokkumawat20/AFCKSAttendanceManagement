package in.afckstechnologies.attendancemanagement.utils;

import android.content.Context;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import org.apache.http.params.HttpConnectionParams;

import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

import java.io.IOException;

import java.io.InputStreamReader;


@SuppressWarnings({"deprecation", "deprecation"})
public class WebClient {
    Context context;

    String TAG = "ServiceAccess";
    String response = "";
    String baseURL = "";

    @SuppressWarnings({"deprecation", "resource"})
    public String SendHttpPost(String URL, JSONObject jsonObjSend) {

        try {
            HttpClient client = new DefaultHttpClient();

            HttpPost post = new HttpPost(URL);
            // HttpGet post = new HttpGet(URL);
            post.setHeader("Content-type", "application/json; charset=UTF-8");
            post.setHeader("Accept", "application/json");
            post.setEntity(new StringEntity(jsonObjSend.toString(), "UTF-8"));
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), 10 * 1000);
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10 * 1000);
            HttpResponse response = client.execute(post);
            Log.i(TAG, "resoponse" + response);
            HttpEntity entity = response.getEntity();

            return EntityUtils.toString(entity);

        } catch (Exception e) {
            // TODO: handle exception
            Log.i(TAG, "exception" + e);
        }
        Log.i(TAG, "response" + response);
        return response;
    }


    private String getResponse(HttpEntity entity) {
        String response = "";

        try {

            int length = (int) entity.getContentLength();

            StringBuffer sb = new StringBuffer(length);
            InputStreamReader isr = new InputStreamReader(entity.getContent(), "UTF-8");
            char buff[] = new char[length];
            int cnt;
            while ((cnt = isr.read(buff, 0, length - 1)) > 0) {
                sb.append(buff, 0, cnt);
            }

            response = sb.toString();
            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return response;
    }


}


