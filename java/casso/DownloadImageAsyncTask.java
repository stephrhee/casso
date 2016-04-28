package casso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

public class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private Callback mCallback;
    @Nullable private String mEncodedTagName;

    public DownloadImageAsyncTask(Callback callback) {
        this(callback, null);
    }

    public DownloadImageAsyncTask(Callback callback, @Nullable String encodedTagName) {
        mCallback = callback;
        mEncodedTagName = encodedTagName;
    }

    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        if (url != null) {
            try {
                InputStream inputStream = new URL(url).openStream();
                return BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    protected void onPostExecute(Bitmap bitmap) {
        mCallback.onBitmapFetched(bitmap, mEncodedTagName);
    }

    public interface Callback {
        public void onBitmapFetched(Bitmap bitmap, @Nullable String encodedTagName);
    }

}
