package angelhack.nutritionfacts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends ActionBarActivity {


    String URL = "https://api.idolondemand.com/1/api/sync/ocrdocument/v1";
    String result = "";
    final String tag = "Your Logcat tag: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final Button btnSearch = (Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String query = "asdfasdfkj";
                callWebService(query);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void callWebService(String query) {

        // query is what we want to send the async task
        WebService task = new WebService();
        task.execute(new String[]{query});
    }


    private class WebService extends AsyncTask<String, Void, String>{

        ProgressDialog dialog = new ProgressDialog(SecondActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending Data...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

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
                return "Fail";
            } catch (IOException e) {
                e.printStackTrace();
                return "Fail";
            } catch (Exception e){
                e.printStackTrace();
                return"Fail";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result){
            Toast.makeText(SecondActivity.this, result, Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    }





}
