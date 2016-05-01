package casso.http;

import android.content.Context;
import android.util.Log;

import casso.model.Artwork;
import casso.model.Tag;
import casso.util.XmlUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class YCBAQueryAllHandler implements YCBARequestHandler.Callback {

    private final List<Integer> mObjectIds;
    private int mFetchedCount = 0;

    private final HashMap<String, Tag> mNameToTagHashMap = new HashMap<>();
    private final HashMap<Integer, String> mIdToThumbUrlHashMap = new HashMap<>();

    private YCBARequestHandler mYCBARequestHandler;
    private Context mContext;

    public YCBAQueryAllHandler(Context context, List<Integer> objectIds) {
        mContext = context;
        mObjectIds = objectIds;
    }

    public void execute() {
        for (int objectId : mObjectIds) {
            mYCBARequestHandler = new YCBARequestHandler(
                    mContext,
                    Integer.toString(objectId),
                    this);
            mYCBARequestHandler.execute();
        }
    }

    private void updateHashMaps(Artwork artwork) {
        List<Tag> tags = artwork.mTags;
        if (tags != null && tags.size() > 0) {
            for (Tag tag : tags) {
                Tag tagFromHashMap = mNameToTagHashMap.get(tag.mName);
                List<Integer> idsWithThisTag;
                if (tagFromHashMap != null && tagFromHashMap.mIdsWithThisTag != null) {
                    idsWithThisTag = tagFromHashMap.mIdsWithThisTag;
                } else {
                    idsWithThisTag = new ArrayList<>();
                }
                idsWithThisTag.add(artwork.mId);
                Tag newTag = new Tag.Builder()
                        .fromOld(tag)
                        .setCount(idsWithThisTag.size())
                        .setIdsWithThisTag(idsWithThisTag)
                        .build();
                mNameToTagHashMap.put(newTag.mName, newTag);
            }
        }

        String thumbUrl = mIdToThumbUrlHashMap.get(artwork.mId);
        if (thumbUrl == null) {
            thumbUrl = artwork.mLowResImageUrl;
            mIdToThumbUrlHashMap.put(artwork.mId, thumbUrl);
        }
    }

    private void updateFetchedCount() {
        mFetchedCount += 1;
        if (mFetchedCount == mObjectIds.size()) {
            FirebaseRequestHandler firebaseRequestHandler = new FirebaseRequestHandler(
                    mContext,
                    FirebaseRequestHandler.DATA_URL,
                    null);
            for (String tagName : mNameToTagHashMap.keySet()) {
                Log.d("YCBAQueryAllHandler", tagName + " " + mNameToTagHashMap.get(tagName).mIdsWithThisTag);
                firebaseRequestHandler.setTagAndListOfIds(
                        mNameToTagHashMap.get(tagName),
                        mIdToThumbUrlHashMap);
            }
        }
    }

    @Override
    public void onYCBAResponseFetched(YCBARequestHandler.Result result) {
        String xml = result.mString;
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        try {
            Artwork.Builder builder = new Artwork.Builder();
            XmlUtil.parse(stream, builder);
            Artwork artwork = builder.build();
            updateHashMaps(artwork);
        } catch (XmlPullParserException | IOException e) {
            Log.d("YCBAQueryAllHandler", e.toString());
        }
        updateFetchedCount();
    }

    @Override
    public void onYCBARequestFailed() {
        updateFetchedCount();
    }

}
