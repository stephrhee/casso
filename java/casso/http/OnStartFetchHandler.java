package casso.http;

import android.content.Context;
import android.util.Log;
import casso.model.SimpleTag;

import java.util.HashMap;

/**
 * Created by stephrhee on 4/27/16.
 */
public class OnStartFetchHandler {

    public static void fetch(Context context) {

        /**
         * Fetch list of all tags and their suggested artworks' ids and thumb image urls
         */
        FirebaseRequestHandler getSuggestedArtworksRequestHandler = new FirebaseRequestHandler(
                context,
                FirebaseRequestHandler.DATA_URL,
                new FirebaseRequestHandler.GetSuggestedArtworksCallback() {
                    @Override
                    public void onSuggestedArtworksFetched(
                            HashMap<String, SimpleTag> encodedStringToSimpleTagHashMap) {
                        Log.d("STEPH", encodedStringToSimpleTagHashMap.size() + " ");
                    }

                    @Override
                    public void onSuggestedArtworksFetchFailed() {
                    }

                });
        getSuggestedArtworksRequestHandler.getSuggestedArtworks();
    }

}
