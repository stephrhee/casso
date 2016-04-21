package casso;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import casso.http.FirebaseRequestHandler;
import casso.http.SuggestedArtworksRequestHandler;
import casso.http.YCBARequestHandler;
import casso.model.Artwork;
import casso.model.Tag;
import casso.util.XmlUtil;
import casso.widget.CenterLockHorizontalScrollview;
import casso.widget.CenterLockHorizontalScrollviewAdapter;

import casso.widget.ImageViewAndLoadingScreen;
import com.casso.R;

import com.google.common.base.Preconditions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ArtworkProfileActivity extends FragmentActivity implements
        DownloadImageAsyncTask.Callback,
        FirebaseRequestHandler.GetObjectIdsCallback,
        YCBARequestHandler.Callback {

    private YCBARequestHandler mYCBARequestHandler;

    private final String mObjectId = "109";
    private Artwork mArtwork;

    private ImageViewAndLoadingScreen mImageViewAndLoadingScreen;
    private TextView mTitle;
    private TextView mArtist;
    private TextView mYear;
    private TextView mTags;
    private CenterLockHorizontalScrollview mSuggestedArtworksScrollview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.artwork_profile_layout);

        init();
        mYCBARequestHandler.execute();
    }

    private void init() {
        mYCBARequestHandler = new YCBARequestHandler(this, mObjectId, this);

        mImageViewAndLoadingScreen = (ImageViewAndLoadingScreen) findViewById(R.id.artwork_profile_image);
        mTitle = (TextView) findViewById(R.id.artwork_profile_title);
        mArtist = (TextView) findViewById(R.id.artwork_profile_artist);
        mYear = (TextView) findViewById(R.id.artwork_profile_year);
        mTags = (TextView) findViewById(R.id.artwork_profile_tags);
        mSuggestedArtworksScrollview = (CenterLockHorizontalScrollview) findViewById(R.id.suggested_artworks_scrollview);
    }

    private void setViews() {
        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask(this);
        downloadImageAsyncTask.execute(mArtwork.mHighResImageUrl);
        mTitle.setText(mArtwork.mTitle);
        mArtist.setText(mArtwork.mArtist);
        mYear.setText(mArtwork.getYearRange());
        if (mArtwork.mTags != null) {
            mTags.setText(getSpannableStringOfTags());
            mTags.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mTags.setVisibility(View.GONE);
            mSuggestedArtworksScrollview.setVisibility(View.GONE);
        }
        mSuggestedArtworksScrollview.setVisibility(View.GONE);
    }

    @Override
    public void onBitmapFetched(Bitmap bitmap) {
        if (bitmap != null) {
            mImageViewAndLoadingScreen.setImageView(bitmap);
        } else {
            Log.e("ArtworkProfileActivity", "bitmap could not be fetched");
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

    private SpannableString getSpannableStringOfTags() {
        Preconditions.checkNotNull(mArtwork.mTags);
        SpannableString spannableString = new SpannableString(mArtwork.getTagsAsOneString());
        int startIndex = 0;
        for (Tag tag : mArtwork.mTags) {
            String tagName = tag.mName;
            ClickableSpan clickableSpan = getClickableSpan(this, tagName);
            spannableString.setSpan(
                    clickableSpan,
                    startIndex,
                    startIndex + tagName.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            startIndex += tagName.length() + 1;
        }
        return spannableString;
    }

    private ClickableSpan getClickableSpan(final Context context, final String tagName) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                SuggestedArtworksRequestHandler suggestedArtworksRequestHandler =
                        new SuggestedArtworksRequestHandler(
                                context,
                                getSuggestedArtworkCallback(context));
                suggestedArtworksRequestHandler.showSuggestedArtworks(tagName);
            }
        };
    }

    private SuggestedArtworksRequestHandler.Callback getSuggestedArtworkCallback(
            final Context context) {
        return new SuggestedArtworksRequestHandler.Callback() {
            @Override
            public void onSuggestedArtworksImagesFetched(
                    List<Artwork> suggestedArtworks) {
                CenterLockHorizontalScrollviewAdapter adapter =
                        new CenterLockHorizontalScrollviewAdapter(
                                context,
                                R.layout.suggested_artworks_square_image_view,
                                suggestedArtworks);
                mSuggestedArtworksScrollview.setAdapter(adapter);
                mSuggestedArtworksScrollview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuggestedArtworksImagesFetchedFailed() {
                mSuggestedArtworksScrollview.setVisibility(View.GONE);
            }
        };
    }

}
