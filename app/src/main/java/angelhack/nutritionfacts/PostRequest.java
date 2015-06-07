package angelhack.nutritionfacts;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;

public abstract class PostRequest extends AsyncTask<String, Void, String> {
    private final View rootView;
    String URL = "https://api.idolondemand.com/1/api/sync/ocrdocument/v1";
    String result = "";
    final String tag = "Your Logcat tag: ";

    PostRequest(View rootView) {
        this.rootView = rootView;
    }

    @Override
    protected String doInBackground(String... arg) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost request = new HttpPost(URL);
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        File file = new File(Environment.getExternalStorageDirectory(), "b.jpg");
        entityBuilder.addBinaryBody("file", file, ContentType.MULTIPART_FORM_DATA, file.getName());
        entityBuilder.addTextBody("apikey", "dd5e679c-3e9b-4ee6-ab4c-9db34501fb66");
        entityBuilder.addTextBody("mode", "document_scan");
        ResponseHandler<String> handler = new BasicResponseHandler();
        request.setEntity(entityBuilder.build());
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
