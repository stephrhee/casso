package casso.http;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import casso.DownloadImageAsyncTask;
import casso.model.Artwork;
import casso.util.XmlUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stephrhee on 4/18/16.
 */
public class SuggestedArtworksRequestHandler {

    private Context mContext;
    private Callback mCallback;


    private List<Artwork> mSuggestedArtworks;
    private int mFetchedSuggestedArtworksCount = 0;

    public SuggestedArtworksRequestHandler(Context context, Callback callback) {
        mContext = context;
        mCallback = callback;
    }

    public void showSuggestedArtworks(String tagName) {
        FirebaseRequestHandler getIdsFromTagFirebaseRequestHandler = new FirebaseRequestHandler(
                mContext,
                FirebaseRequestHandler.DATA_URL,
                new FirebaseRequestHandler.GetIdsForTagCallback() {
                    @Override
                    public void onIdsFetched(List<Integer> ids) {
                        if (ids == null) {
                            onIdsFetchedFailed();
                        } else {
                            fetchSuggestedArtworks(ids);
                        }
                    }

                    @Override
                    public void onIdsFetchedFailed() {
                        mCallback.onSuggestedArtworksImagesFetchedFailed();
                    }
                });
        getIdsFromTagFirebaseRequestHandler.getIdsForTag(tagName);
    }

    private void fetchSuggestedArtworks(List<Integer> ids) {
        mSuggestedArtworks = new ArrayList<>();
        for (Integer id : ids) {
            YCBARequestHandler ycbaRequestHandler = new YCBARequestHandler(
                    mContext,
                    id.toString(),
                    new YCBARequestHandler.Callback() {
                        @Override
                        public void onYCBAResponseFetched(YCBARequestHandler.Result result) {
                            String xml = result.mString;
                            InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
                            try {
                                Artwork.Builder builder = new Artwork.Builder();
                                XmlUtil.parse(stream, builder);
                                Artwork suggestedArtwork = builder.build();
                                mSuggestedArtworks.add(suggestedArtwork);
                                DownloadImageAsyncTask downloadImageAsyncTask =
                                        new DownloadImageAsyncTask(getDownloadImageCallback(
                                                mSuggestedArtworks.size() - 1));
                                downloadImageAsyncTask.execute(suggestedArtwork.mLowResImageUrl);
                            } catch (XmlPullParserException | IOException e) {
                                Log.d("SuggestedArtworks", e.toString());
                                mCallback.onSuggestedArtworksImagesFetchedFailed();
                            }
                        }

                        @Override
                        public void onYCBARequestFailed() {
                            mCallback.onSuggestedArtworksImagesFetchedFailed();
                        }
                    });
            ycbaRequestHandler.execute();
        }
    }

    private DownloadImageAsyncTask.Callback getDownloadImageCallback(final int position) {
        return new DownloadImageAsyncTask.Callback() {
            @Override
            public void onBitmapFetched(Bitmap bitmap) {
                if (bitmap != null) {
                    Artwork oldArtwork = mSuggestedArtworks.get(position);
                    Artwork newArtwork = new Artwork.Builder()
                            .fromOld(oldArtwork)
                            .setImageBitmap(bitmap)
                            .build();
                    mSuggestedArtworks.set(position, newArtwork);
                    mFetchedSuggestedArtworksCount += 1;
                    if (mFetchedSuggestedArtworksCount == mSuggestedArtworks.size()) {
                        mCallback.onSuggestedArtworksImagesFetched(mSuggestedArtworks);
                    }
                } else {
                    Log.e("ArtworkProfileActivity", "bitmap could not be fetched");
                    mCallback.onSuggestedArtworksImagesFetchedFailed();
                }
            }
        };
    }

    public interface Callback {
        public void onSuggestedArtworksImagesFetched(List<Artwork> suggestedArtworks);
        public void onSuggestedArtworksImagesFetchedFailed();
    }

}
