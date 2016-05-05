package casso.http;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;

import casso.util.HttpUtil;
import casso.util.NetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephrhee on 5/4/16.
 */

public class ColorTagHandler extends AsyncTask<String, Void, List<Integer>> {

    private final Context mContext;
    private final Listener mListener;
    private final String mRequestURLString;

    public ColorTagHandler(Context context, Listener listener, String imageURLString) {
        mContext = context;
        mListener = listener;
        mRequestURLString = getURLString(imageURLString);
    }

    @Override
    protected List<Integer> doInBackground(String... params) {
        if (NetUtil.isOnline(mContext)) {
            return getHTTPResponse();
        } else {
            cancel(true);
            mListener.onRequestFailed();
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Integer> string) {
        super.onPostExecute(string);
        if (string == null || string.size() == 0) {
            mListener.onRequestFailed();
        } else {
            mListener.onColorsFetched(string);
        }
    }

    private List<Integer> getHTTPResponse() {
        try {
            URL url = new URL(mRequestURLString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(
                    "X-Mashape-Key",
                    "hK9TYJFHDumshUEz2cFxyN5G57Rhp1QysNcjsnK6MrCZJKsMu6");
            urlConnection.setRequestProperty("Accept", "application/json");
            String string = HttpUtil.convertInputStreamToString(urlConnection.getInputStream());
            JSONObject jsonObject = new JSONObject(string);
            JSONArray tagsJSONArray = jsonObject.getJSONArray("tags");
            List<Integer> colors = new ArrayList<>();
            for (int i = 0; i < tagsJSONArray.length(); i++) {
                JSONObject colorJSONObject = tagsJSONArray.getJSONObject(i);
                String hexCode = colorJSONObject.getString("color");
                colors.add(Color.parseColor(hexCode));
            }
            return colors;
        } catch (IOException | JSONException e) {
            return null;
        }
    }

    private String getURLString(String imageURL) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("apicloud-colortag.p.mashape.com")
                .appendPath("tag-url.json")
                .appendQueryParameter("palette", "simple")
                .appendQueryParameter("sort", "relevance")
                .appendQueryParameter("url", imageURL.replaceAll("\\s+", ""));
        return builder.build().toString();
    }

    public interface Listener {
        public void onColorsFetched(List<Integer> colors);
        public void onRequestFailed();
    }

}
