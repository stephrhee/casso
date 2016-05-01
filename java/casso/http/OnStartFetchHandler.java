package casso.http;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import casso.model.Artwork;
import casso.model.SimpleTag;

import java.util.HashMap;
import java.util.List;

/**
 * Created by stephrhee on 4/27/16.
 */

public class OnStartFetchHandler extends Application {

    private HashMap<String, SimpleTag> mSuggestedArtworksHashMap;

    private List<Integer> mObjectIds;

    private HashMap<Integer, Artwork> mArtworks;

    public static void fetchSuggestedArtworks(final Context context, final Activity activity) {
        /**
         * Fetch list of all tags and their suggested artworks' ids and thumb image urls
         */
        FirebaseRequestHandler getSuggestedArtworksRequestHandler = new FirebaseRequestHandler(
                context,
                FirebaseRequestHandler.DATA_URL,
                new FirebaseRequestHandler.GetSuggestedArtworksCallback() {
                    @Override
                    public void onSuggestedArtworksFetched(
                            HashMap<String, SimpleTag> firebaseKeyToSimpleTagHashMap) {
                        HashMap<String, SimpleTag> readableHashMap = new HashMap<>();
                        for (String firebaseKey : firebaseKeyToSimpleTagHashMap.keySet()) {
                            SimpleTag simpleTag = firebaseKeyToSimpleTagHashMap.get(firebaseKey);
                            if (simpleTag != null) {
                                readableHashMap.put(simpleTag.encodedString, simpleTag);
                            }
                        }
                        ((OnStartFetchHandler) activity.getApplication())
                                .setSuggestedArtworkHashMap(readableHashMap);
                    }

                    @Override
                    public void onSuggestedArtworksFetchFailed() {
                    }

                });
        getSuggestedArtworksRequestHandler.getSuggestedArtworks();
    }

    private void setSuggestedArtworkHashMap(HashMap<String, SimpleTag> suggestedArtworksHashMap) {
        mSuggestedArtworksHashMap = suggestedArtworksHashMap;
    }

    public HashMap<String, SimpleTag> getSuggestedArtworkHashMap() {
        return mSuggestedArtworksHashMap;
    }

    public static void fetchObjectIds(final Context context, final Activity activity) {
        /**
         * Fetch list of object ids for current collection
         */
        FirebaseRequestHandler getObjectIdsRequestHandler = new FirebaseRequestHandler(
                context,
                FirebaseRequestHandler.DATA_URL,
                new FirebaseRequestHandler.GetObjectIdsCallback() {
                    @Override
                    public void onObjectIdsFetched(List<Integer> objectIdsList) {
                        ((OnStartFetchHandler) activity.getApplication()).setObjectIds(objectIdsList);
                    }

                    @Override
                    public void onObjectIdsFetchFailed() {
                    }

                });
        getObjectIdsRequestHandler.getObjectIdsList();
    }

    private void setObjectIds(List<Integer> objectIds) {
        mObjectIds = objectIds;
    }

    public List<Integer> getObjectIds() {
        return mObjectIds;
    }

    public static void fetchArtworks(final Context context, final Activity activity) {
        /**
         * Fetch HashMap<Integer, Artwork> of <object id, artwork> for current collection
         */
        FirebaseRequestHandler getArtworksRequestHandler = new FirebaseRequestHandler(
                context,
                FirebaseRequestHandler.DATA_URL,
                new FirebaseRequestHandler.GetArtworksCallback() {
                    @Override
                    public void onArtworksFetched(HashMap<Integer, Artwork> artworks) {
                        ((OnStartFetchHandler) activity.getApplication()).setArtworks(artworks);
                    }

                    @Override
                    public void onArtworksFetchFailed() {
                    }

                });
        getArtworksRequestHandler.getArtworks();
    }

    private void setArtworks(HashMap<Integer, Artwork> artworks) {
        mArtworks = artworks;
    }

    public HashMap<Integer, Artwork> getArtworks() {
        return mArtworks;
    }

}
