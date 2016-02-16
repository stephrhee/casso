package com.example.casso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private Callback mCallback;

    public DownloadImageAsyncTask(Callback callback) {
        mCallback = callback;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        try {
            InputStream inputStream = new URL(url).openStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Bitmap bitmap) {
        mCallback.onBitmapFetched(bitmap);
    }

    public interface Callback {
        public void onBitmapFetched(Bitmap bitmap);
    }

}
