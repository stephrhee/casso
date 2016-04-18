package casso;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import casso.http.FirebaseRequestHandler;
import casso.http.YCBARequestHandler;
import casso.model.Artwork;
import casso.util.StringUtil;
import casso.util.XmlUtil;
import casso.widget.CenterLockHorizontalScrollview;
import casso.widget.CenterLockHorizontalScrollviewAdapter;

import com.casso.R;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ArtworkProfileActivity extends FragmentActivity implements
        DownloadImageAsyncTask.Callback,
        FirebaseRequestHandler.Callback,
        YCBARequestHandler.Callback {

    private FirebaseRequestHandler mFirebaseRequestHandler;
    private YCBARequestHandler mYCBARequestHandler;

    private final String mObjectId = "499";
    private Artwork mArtwork;

    private int fetchedSuggestedArtworksCount = 0;

    private ImageView mImage;
    private TextView mTitle;
    private TextView mArtist;
    private TextView mYear;
    private TextView mTags;
    private CenterLockHorizontalScrollview mSuggestedArtworks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.artwork_profile_layout);

        init();
        mFirebaseRequestHandler.getObjectIdsList();
        mYCBARequestHandler.execute();
    }

    private void init() {
        mFirebaseRequestHandler = new FirebaseRequestHandler(
                this,
                FirebaseRequestHandler.DATA_URL,
                this);
        mYCBARequestHandler = new YCBARequestHandler(this, mObjectId, this);

        mImage = (ImageView) findViewById(R.id.artwork_profile_image);
        mTitle = (TextView) findViewById(R.id.artwork_profile_title);
        mArtist = (TextView) findViewById(R.id.artwork_profile_artist);
        mYear = (TextView) findViewById(R.id.artwork_profile_year);
        mTags = (TextView) findViewById(R.id.artwork_profile_tags);
        mSuggestedArtworks = (CenterLockHorizontalScrollview) findViewById(R.id.suggested_artworks_scrollview);
    }

    private void setViews() {
        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask(this);
        downloadImageAsyncTask.execute(mArtwork.mImageUrl);
        mTitle.setText(mArtwork.mTitle);
        mArtist.setText(mArtwork.mArtist);
        mYear.setText(mArtwork.getYearRange());
        mTags.setText(Joiner.on(" | ").join(Lists.transform(
                mArtwork.mTags,
                StringUtil.tagsToTagStrings)));
    }

    @Override
    public void onBitmapFetched(Bitmap bitmap) {
        if (bitmap != null) {
            mImage.setImageBitmap(bitmap);
        } else {
            Log.e("ArtworkProfileActivity", "bitmap could not be fetched");
        }
    }

    private void downloadSuggestedArtworksImages() {
        if (mArtwork.mSuggestedArtworks != null) {
            for (int position = 0; position < mArtwork.mSuggestedArtworks.size(); position++) {
                DownloadImageAsyncTask downloadImageAsyncTask =
                        new DownloadImageAsyncTask(getCallback(position));
                downloadImageAsyncTask.execute(mArtwork.mSuggestedArtworks.get(position).mImageUrl);
            }
        } else {
            mSuggestedArtworks.setVisibility(View.GONE);
        }
    }

    private DownloadImageAsyncTask.Callback getCallback(final int position) {
        return new DownloadImageAsyncTask.Callback() {
            @Override
            public void onBitmapFetched(Bitmap bitmap) {
                if (bitmap != null) {
                    Artwork newArtwork = new Artwork.Builder()
                            .fromOld(mArtwork.mSuggestedArtworks.get(position))
                            .setImageBitmap(bitmap)
                            .build();
                    mArtwork.mSuggestedArtworks.set(position, newArtwork);
                    fetchedSuggestedArtworksCount += 1;
                    if (fetchedSuggestedArtworksCount == mArtwork.mSuggestedArtworks.size()) {
                        renderSuggestedArtworks();
                    }
                } else {
                    Log.e("ArtworkProfileActivity", "bitmap could not be fetched");
                }
            }
        };
    }

    private void renderSuggestedArtworks() {
        if (mArtwork.mSuggestedArtworks != null && mArtwork.mSuggestedArtworks.size() > 0) {
            CenterLockHorizontalScrollviewAdapter adapter = new CenterLockHorizontalScrollviewAdapter(
                    this,
                    R.layout.suggested_artworks_square_image_view,
                    mArtwork.mSuggestedArtworks);
            mSuggestedArtworks.setAdapter(adapter);
        } else {
            mSuggestedArtworks.setVisibility(View.GONE);
        }
    }

    @Override
    public void onObjectIdsFetched(List<Integer> objectIdsList) {
    }

    @Override
    public void onObjectIdsFetchFailed() {
    }

    @Override
    public void onYCBAResponseFetched(YCBARequestHandler.Result result) {
        String xml = result.mString;
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        try {
            Artwork.Builder builder = new Artwork.Builder();
            XmlUtil.parse(stream, builder);
            mArtwork = builder.build();
            setViews();
            downloadSuggestedArtworksImages();
        } catch (XmlPullParserException | IOException e) {
            Log.d("ArtworkProfileActivity", e.toString());
        }
    }

    @Override
    public void onYCBARequestFailed() {
    }

    private void testPrint() {
        Log.d("ArtworkProfileActivity", "genre: " + mArtwork.mGenre);
        Log.d("ArtworkProfileActivity", "category: " + mArtwork.mCategory);
        Log.d("ArtworkProfileActivity", "classification: " + mArtwork.mClassification);
        if (mArtwork.mCurator != null) {
            Log.d("ArtworkProfileActivity", "curator: " + mArtwork.mCurator);
            Log.d("ArtworkProfileActivity", "curatorialComment: " + mArtwork.mCuratorialComment);
        }
        if (mArtwork.mObjectTypes != null && mArtwork.mObjectTypes.size() > 0) {
            for (String objectType : mArtwork.mObjectTypes) {
                Log.d("ArtworkProfileActivity", "objectType: " + objectType);
            }
        }
        if (mArtwork.mMaterials != null && mArtwork.mMaterials.size() > 0) {
            for (String materials : mArtwork.mMaterials) {
                Log.d("ArtworkProfileActivity", "materials: " + materials);
            }
        }
    }

}
