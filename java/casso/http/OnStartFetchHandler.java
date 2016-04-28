package casso.http;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import casso.model.SimpleTag;

import java.util.HashMap;

/**
 * Created by stephrhee on 4/27/16.
 */

public class OnStartFetchHandler extends Application {

    private HashMap<String, SimpleTag> mSuggestedArtworksHashMap;

    public OnStartFetchHandler() {
    }

    public static void fetchSuggestedArtworks(final Context context, final Activity activity) {
        /**
         * Fetch list of all tags and their suggested artworks' ids and thumb image urls
         */
        FirebaseRequestHandler getSuggestedArtworksRequestHandler = new FirebaseRequestHandler(
                context,
                FirebaseRequestHandler.DATA_URL,
                new FirebaseRequestHandler.GetSuggestedArtworksCallback() {
                    @Override
                    public void onSuggestedArtworksFetched(HashMap<String, SimpleTag> firebaseKeyToSimpleTagHashMap) {
                        HashMap<String, SimpleTag> readableHashMap = new HashMap<>();
                        for (String firebaseKey : firebaseKeyToSimpleTagHashMap.keySet()) {
                            SimpleTag simpleTag = firebaseKeyToSimpleTagHashMap.get(firebaseKey);
                            if (simpleTag != null) {
                                readableHashMap.put(simpleTag.encodedString, simpleTag);
                            }
                        }
                        ((OnStartFetchHandler) activity.getApplication()).setSuggestedArtworkHashMap(readableHashMap);
                    }

                    @Override
                    public void onSuggestedArtworksFetchFailed() {
                    }

                });
        getSuggestedArtworksRequestHandler.getSuggestedArtworks();
    }

    public void setSuggestedArtworkHashMap(
            HashMap<String, SimpleTag> suggestedArtworksHashMap) {
        mSuggestedArtworksHashMap = suggestedArtworksHashMap;
    }

    public HashMap<String, SimpleTag> getSuggestedArtworkHashMap() {
        return mSuggestedArtworksHashMap;
    }

}
