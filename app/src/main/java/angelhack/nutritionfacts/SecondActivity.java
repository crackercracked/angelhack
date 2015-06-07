package angelhack.nutritionfacts;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;



public class SecondActivity extends ActionBarActivity {


 //   String URL = "https://api.idolondemand.com/1/api/sync/ocrdocument/v1";
    String URL = "";
    final String apiUrl = "https://api.idolondemand.com/1/api/sync/ocrdocument/v1";
    String result = "";
    final String tag = "Your Logcat tag: ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getLastImage();
       // ActionBar actionBar = getActionBar();
       // actionBar.setDisplayHomeAsUpEnabled(true);
        callWebService("");

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
        finish();

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


    private class WebService extends AsyncTask<String, Void, String> {

        ProgressDialog dialog = new ProgressDialog(SecondActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Sending Data...");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost request = new HttpPost(apiUrl);
            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            File file = new File(URL);
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
            } catch (Exception e) {
                e.printStackTrace();
                return "Fail";
            }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                ((TextView)findViewById(R.id.searchedResult)).setText(result);
             //   Toast.makeText(SecondActivity.this, result, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }

    private void getLastImage() {

//        String[] projection = new String[]{
//                MediaStore.Images.ImageColumns._ID,
//                MediaStore.Images.ImageColumns.DATA,
//                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
//                MediaStore.Images.ImageColumns.DATE_TAKEN,
//                MediaStore.Images.ImageColumns.MIME_TYPE
//        };
//        final Cursor cursor = getContentResolver()
//                .query(MediaStore.Images.Media., projection, null,
//                        null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
//
//        // Put it in the image view
//        if (cursor.moveToFirst()) {
//            final ImageView imageView = (ImageView) findViewById(R.id.image);
//            String imageLocation = cursor.getString(1);
//            URL = imageLocation;
//            Log.e(tag, "last photo path: "+imageLocation);
//            File imageFile = new File(imageLocation);
//            if (imageFile.exists()) {   // TODO: is there a better way to do this?
//                Bitmap bm = BitmapFactory.decodeFile(imageLocation);
//                imageView.setImageBitmap(bm);
//            }
//        }
        File fileholder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(fileholder, "last.jpg");
        final ImageView imageView = (ImageView) findViewById(R.id.image);
        String imageLocation = file.getPath();
        URL = imageLocation;
        Log.e(tag, "last photo path: " + imageLocation);
        if (file.exists()) {   // TODO: is there a better way to do this?
            Bitmap bm = BitmapFactory.decodeFile(imageLocation);
            imageView.setImageBitmap(bm);
        }

    }

}
