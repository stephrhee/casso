package casso;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import casso.http.FirebaseRequestHandler;
import casso.http.YCBARequestHandler;
import casso.model.Artwork;
import casso.util.XmlUtil;
import com.casso.R;

import com.google.common.base.Joiner;

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

    private ImageView mImage;
    private TextView mTitle;
    private TextView mArtist;
    private TextView mYear;
    private TextView mDescription;

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
        mDescription = (TextView) findViewById(R.id.artwork_profile_description);
    }

    private void setViews() {
        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask(this);
        downloadImageAsyncTask.execute(mArtwork.mImageUrl);
        mTitle.setText(mArtwork.mTitle);
        mArtist.setText(mArtwork.mArtist);
        mYear.setText(mArtwork.getYearRange());
        mDescription.setText(Joiner.on(" | ").join(mArtwork.mTags));
    }

    @Override
    public void onBitmapFetched(Bitmap bitmap) {
        if (bitmap != null) {
            mImage.setImageBitmap(bitmap);
        } else {
            Log.e("Error", "bitmap could not be fetched");
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
