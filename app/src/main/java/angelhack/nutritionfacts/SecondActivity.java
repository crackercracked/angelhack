package angelhack.nutritionfacts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;


public class SecondActivity extends ActionBarActivity {


    String URL = "http://www.google.com/?q=";
    String result = "";
    String deviceId = "xxxxx" ;
    final String tag = "Your Logcat tag: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final EditText txtSearch = (EditText)findViewById(R.id.txtSearch);
        txtSearch.setOnClickListener(new EditText.OnClickListener(){
            public void onClick(View v){txtSearch.setText("");}
        });

        final Button btnSearch = (Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                String query = txtSearch.getText().toString();
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
            HttpResponse response;
//            ResponseHandler<String> handler = new BasicResponseHandler();
            try {
                response = httpclient.execute(request);
                Toast.makeText(SecondActivity.this, response.toString(), Toast.LENGTH_LONG).show();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Toast.makeText(SecondActivity.this, "FAIL", Toast.LENGTH_LONG).show();
                return "Fail";
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(SecondActivity.this, "FAIL", Toast.LENGTH_LONG).show();
                return "Fail";
            } catch (Exception e){
                e.printStackTrace();
                return"Fail";
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String result){
            Toast.makeText(SecondActivity.this, result, Toast.LENGTH_LONG).show();
//            dialog.dismiss();
        }
    }





}
