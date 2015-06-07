package angelhack.nutritionfacts;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;


public class MainActivity extends ActionBarActivity implements SurfaceHolder.Callback {

    TextView testView;

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    private final String tag = "main";

    Button start, stop, capture, analysis;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        File fileholder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //File file = new File(fileholder, String.format("%d.jpg", System.currentTimeMillis()));
        File file = new File(fileholder, "last.jpg");
        boolean deleted = true;
        while(!deleted || file.exists()) {
            deleted = file.delete();
        }

        start = (Button)findViewById(R.id.btn_start);
        start.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                start_camera();
            }
        });
        stop = (Button)findViewById(R.id.btn_stop);
        capture = (Button) findViewById(R.id.capture);
        analysis = (Button) findViewById(R.id.analysis);
        stop.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                stop_camera();
            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                captureImage();
            }
        });
        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                analysis();
            }
        });

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };

        /** Handles data for jpeg picture */
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                String[] projection = new String[]{
                        MediaStore.Images.ImageColumns._ID,
                        MediaStore.Images.ImageColumns.DATA,
                        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Images.ImageColumns.DATE_TAKEN,
                        MediaStore.Images.ImageColumns.MIME_TYPE
                };
                final Cursor cursor = getContentResolver()
                        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
                                null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

                // Put it in the image view
                String filename = "";
                if (cursor.moveToFirst()) {
                    final ImageView imageView = (ImageView) findViewById(R.id.image);
                    filename = cursor.getString(1);
                }
                //    URL = imageLocation;
                File fileholder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File file = new File(fileholder, "last.jpg");
                filename = file.getPath();
                Log.e(tag, "newest filename "+ filename);
                FileOutputStream outStream = null;
                try {
                    file.createNewFile();
                    outStream = new FileOutputStream(file);
                    outStream.write(data);
                    outStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                    Toast.makeText(MainActivity.this, "Captured.", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Capture Failed.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Capture Failed.", Toast.LENGTH_SHORT).show();
                } finally {
                }
            }
        };
    }

    private void captureImage() {
        // TODO Auto-generated method stub
        if (camera == null) return;
        ((Button)findViewById(R.id.btn_start)).setText("      Resume      ");
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    private void start_camera()
    {
        if (camera != null) {
            camera.startPreview();
            return;
        }
        try{
            camera = Camera.open();
        }catch(RuntimeException e){
            Log.e(tag, "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        //modify parameter
        param.setPreviewFrameRate(20);
        param.setPreviewSize(176, 144);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setDisplayOrientation(90);
            camera.startPreview();
        } catch (Exception e) {
            Log.e(tag, "init_camera: " + e);
            return;
        }
    }

    private void stop_camera()
    {
        ((Button)findViewById(R.id.btn_start)).setText("Start Camera");
        if (camera == null) return;
        camera.stopPreview();
    }

    private void analysis()
    {
        //String path = getLastImagePath();
        Intent intent = new Intent(this, SecondActivity.class).putExtra(Intent.EXTRA_TEXT, "");
        startActivityForResult(intent, 0);
    }

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
       // CameraActivity activity = (CameraActivity)getActivity();
        //activity.setCurrentPhotoPath("file:" + image.getAbsolutePath());
        return image;
    }




    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


}

