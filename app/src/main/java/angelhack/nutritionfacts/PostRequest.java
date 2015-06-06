package angelhack.nutritionfacts;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.IOException;


public abstract class PostRequest extends AsyncTask<String, Void, String> {

    private final View rootView;
    String URL = "http://www.google.com/?q=";
    String result = "";
    String deviceId = "xxxxx" ;
    final String tag = "Your Logcat tag: ";

    PostRequest(View rootView) {
        this.rootView = rootView;
    }

    @Override
    protected String doInBackground(String... arg) {

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);
        request.addHeader("deviceId", deviceId);
        ResponseHandler<String> handler = new BasicResponseHandler();
        try {
            result = httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();

        Log.i(tag, result);

        return null;
    }

    @Override
    protected void onPostExecute(String result) {


        TextView resultSearch = (TextView)rootView.findViewById(R.id.resultSearch);
        resultSearch.setText(result);

    }
}
