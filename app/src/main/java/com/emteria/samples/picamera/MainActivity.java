package com.emteria.samples.picamera;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity
{
    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView mImageView;
    private SnapshotTask mSnapshotTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button snapshotButton = (Button) findViewById(R.id.snapshotButton);
        snapshotButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mSnapshotTask != null && mSnapshotTask.getStatus() != AsyncTask.Status.FINISHED)
                {
                    Log.i(TAG, "Waiting for the task to finish...");
                    return;
                }

                mSnapshotTask = new SnapshotTask();
                mSnapshotTask.execute();
            }
        });

        mImageView = (ImageView) findViewById(R.id.imageView);
    }

    private class SnapshotTask extends AsyncTask<Void, Void, String>
    {
        private Exception mException;

        @Override
        protected String doInBackground(Void... params)
        {
            try
            {
                File temp = File.createTempFile("raspistill", ".jpg", getCacheDir());
                String path = temp.getAbsolutePath();

                Log.i(TAG, "Using temporary path: " + path);
                if (!RaspistillUtility.snapshot(path))
                {
                    Log.e(TAG, "Failed to create snapshot");
                    path = null;
                }

                return path;
            }
            catch (Exception ex)
            {
                mException = ex;
                Log.i(TAG, "Failed taking image", ex);

                return null;
            }
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);

            if (s != null)
            {
                Log.i("SnapshotTask", "Updating image view path");
                mImageView.setImageBitmap(BitmapFactory.decodeFile(s));
            }
            else if (mException != null)
            {
                Toast.makeText(MainActivity.this, mException.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
