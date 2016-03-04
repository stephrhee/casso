package casso.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;

import casso.util.HttpUtil;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class YCBARequestHandler extends AsyncTask<String, Void, YCBARequestHandler.Result> {

    private final Context mContext;
    private final Callback mCallback;
    private final ConnectivityManager mConnectivityManager;
    private final Uri mUri;

    public enum ResultObjectType {
        JSON, XML;
    }

    public class Result {
        public ResultObjectType mResultObjectType;
        public String mString;

        public Result(ResultObjectType resultObjectType, String string) {
            mResultObjectType = resultObjectType;
            mString = string;
        }
    }

    public YCBARequestHandler(Context context, String objectId, Callback callback) {
        mContext = context;
        mCallback = callback;
        mConnectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mUri = getUri(objectId);
    }

    private Uri getUri (String objectId) {
        /*
        http://
        collections.britishart.yale.edu
        /oaicatmuseum
        /OAIHandler?
        verb=GetRecord&
        identifier=oai:tms.ycba.yale.edu:499&
        metadataPrefix=lido
        */
        return new Uri.Builder()
                .scheme("http")
                .authority("collections.britishart.yale.edu")
                .appendPath("oaicatmuseum")
                .appendPath("OAIHandler")
                .appendQueryParameter("verb", "GetRecord")
                .appendQueryParameter("identifier", "oai:tms.ycba.yale.edu:" + objectId)
                .appendQueryParameter("metadataPrefix", "lido")
                .build();
    }

    private Result getResponse() {
        Preconditions.checkNotNull(mUri);
        try {
            URL url = new URL(mUri.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if (httpURLConnection != null) {
                return new Result(
                        ResultObjectType.XML,
                        HttpUtil.convertInputStreamToString(httpURLConnection.getInputStream()));
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isOnline() {
        NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    @Override
    protected Result doInBackground(String... params) {
        if (isOnline()) {
            return getResponse();
        } else {
            cancel(true);
            return null;
        }
    }

    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (result == null) {
            mCallback.onYCBARequestFailed();
        } else {
            mCallback.onYCBAResponseFetched(result);
        }
    }

    @Override
    protected void onCancelled(Result result) {
        onCancelled();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mCallback.onYCBARequestFailed();
    }

    public interface Callback {
        public void onYCBAResponseFetched(Result result);
        public void onYCBARequestFailed();
    }

}
