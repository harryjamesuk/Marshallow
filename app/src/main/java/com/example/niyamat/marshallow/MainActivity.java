package com.example.niyamat.marshallow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.R.id.content;

public class MainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    public static final String TAG = MainActivity.class.getSimpleName();
    public static final int TAKE_PHOTO_REQUEST = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri mMediaUri;
    public static final int PERMISSION_REQUEST_CODE = 2;
    private View mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkWriteExternalStorage();
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mMediaUri = getOutPutMediaFileUri(MEDIA_TYPE_IMAGE);
                if (mMediaUri == null) {
                    Log.d(TAG, "There is a problem accessing your device's external storage");
                } else {
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
                    startActivityForResult(takePhotoIntent, TAKE_PHOTO_REQUEST);
                }
            }

            private Uri getOutPutMediaFileUri(int mediaType) {
                if (isExternalStorageAvailable()) {
                    String appName = MainActivity.class.toString();
                    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);
                    if (!mediaStorageDir.exists()) {
                        if (!mediaStorageDir.mkdirs()) {
                            Log.d(TAG, "There was an problem creating dirctory");
                            return null;
                        }
                    }
                    Date date = new Date();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(date);
                    File mediaFile;
                    String path = mediaStorageDir.getPath() + File.separator;
                    if (mediaType == MEDIA_TYPE_IMAGE) {
                        mediaFile = new File(path + "IMG_" + timeStamp + ".jpg");
                    } else {
                        return null;
                    }
                    Log.d(TAG, "File" + Uri.fromFile(mediaFile));
                    return Uri.fromFile(mediaFile);
                } else {
                    return null;
                }
            }

            private boolean isExternalStorageAvailable() {
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private boolean checkWriteExternalStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //We don't have permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showWriteToStorageSnackbar();
            } else {
                requestWritePermissionWithCallback();
            }
            return false;
        }
        return true;
    }

    private void requestWritePermissionWithCallback() {
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
            PERMISSION_REQUEST_CODE);
    }

    private void showWriteToStorageSnackbar() {
        Snackbar.make(mLayout,"Write to storage is required to store and access photos/videos.",Snackbar.LENGTH_INDEFINITE)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requestWritePermissionWithCallback();
                    }
                }).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    showWriteToStorageSnackbar();
                }
                break;
                default:
                    Log.e(TAG, "Got request code: " + requestCode + " which is not used in switch.");
                    break;

        }

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


/*   fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                .setAction("Action", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        mMediaUri = getOutPutMediaFileUri(MEDIA_TYPE_IMAGE);
                                        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
                                    }

                                    private Uri getOutPutMediaFileUri(int mediaType) {
                                        if (isExternalStorageAvailable()) {
                                            return null;
                                        } else {
                                            return null;
                                        }
                                    }

                                    private boolean isExternalStorageAvailable() {
                                        String state = Environment.getExternalStorageState();
                                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                                            return true;
                                        } else {
                                            return false;
                                        }
                                    }
                                }).show();
                    }
                }); */


