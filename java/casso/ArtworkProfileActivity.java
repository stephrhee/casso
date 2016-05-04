package casso;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import casso.http.OnStartFetchHandler;
import casso.model.Artwork;
import casso.model.SimpleTag;
import casso.model.Tag;
import casso.util.FontUtil;
import casso.util.StringUtil;
import casso.widget.CenterLockHorizontalScrollview;
import casso.widget.CenterLockHorizontalScrollviewAdapter;
import casso.widget.ImageViewAndLoadingScreen;

import casso.widget.RoundedBackgroundSpan;
import com.casso.R;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArtworkProfileActivity extends FragmentActivity implements
        DownloadImageAsyncTask.Callback,
        CenterLockHorizontalScrollviewAdapter.OnItemClickListener {

    public static final String ARTWORK_KEY = "ARTWORK_KEY";

    private Artwork mArtwork;

    private ImageViewAndLoadingScreen mImageViewAndLoadingScreen;
    private TextView mTitle;
    private TextView mArtist;
    private TextView mYear;
    private TextView mCategory;
    private TextView mGenre;
    private TextView mMaterials;
    private TextView mCuratorialComment;
    private TextView mTagsLabel;
    private TextView mTags;
    private CenterLockHorizontalScrollview mSuggestedArtworksScrollview;

    private CenterLockHorizontalScrollviewAdapter mSuggestedArtworksAdapter;
    private List<Bitmap> mSuggestedArtworksBitmaps = new ArrayList<>();
    private String mLastClickedEncodedTagName;
    private List<DownloadImageAsyncTask> mPendingSuggestedArtworkBitmapsDownloadTasks;

    private Typeface mGoudyStMRegularTypeface;
    private Typeface mGoudyStMItalicTypeface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.artwork_profile_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mArtwork = extras.getParcelable(ARTWORK_KEY);
        } else {
            // quit application
        }

        init();
        setViews();
        setTypefaces();
    }

    private void init() {
        mImageViewAndLoadingScreen = (ImageViewAndLoadingScreen) findViewById(R.id.artwork_profile_image);
        mTitle = (TextView) findViewById(R.id.artwork_profile_title);
        mArtist = (TextView) findViewById(R.id.artwork_profile_artist);
        mYear = (TextView) findViewById(R.id.artwork_profile_year);
        mCategory = (TextView) findViewById(R.id.artwork_profile_category);
        mGenre = (TextView) findViewById(R.id.artwork_profile_genre);
        mMaterials = (TextView) findViewById(R.id.artwork_profile_materials);
        mCuratorialComment = (TextView) findViewById(R.id.artwork_profile_curatorial_comment);
        mTagsLabel = (TextView) findViewById(R.id.artwork_profile_tags_label);
        mTags = (TextView) findViewById(R.id.artwork_profile_tags);
        mSuggestedArtworksScrollview = (CenterLockHorizontalScrollview) findViewById(R.id.suggested_artworks_scrollview);

        mSuggestedArtworksAdapter = new CenterLockHorizontalScrollviewAdapter(
                this,
                this,
                R.layout.suggested_artwork_view,
                mSuggestedArtworksBitmaps);
        mSuggestedArtworksScrollview.setAdapter(mSuggestedArtworksAdapter);

        mGoudyStMRegularTypeface = FontUtil.getTypeface(
                this,
                FontUtil.mGoudyStMRegularTypefaceString);
        mGoudyStMItalicTypeface = FontUtil.getTypeface(
                this,
                FontUtil.mGoudyStMItalicTypefaceString);
    }

    private void setViews() {
        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask(this);
        downloadImageAsyncTask.execute(mArtwork.mHighResImageUrl);
        mTitle.setText(mArtwork.mTitle);
        mArtist.setText(mArtwork.mArtist);

        if (mArtwork.getYearRange()!= null) {
            mYear.setText(mArtwork.getYearRange());
        } else {
            mYear.setVisibility(View.GONE);
        }

        if (mArtwork.mCategory!= null) {
            String string = getString(R.string.artwork_profile_category_string) + " " +
                    mArtwork.mCategory;
            mCategory.setText(string);
        } else {
            mCategory.setVisibility(View.GONE);
        }

        List<String> classificationGenreObjectTypeList = new ArrayList<>();
        if (mArtwork.mClassification != null) {
            classificationGenreObjectTypeList.add(mArtwork.mClassification);
        }
        if (mArtwork.mGenre != null) {
            classificationGenreObjectTypeList.add(mArtwork.mGenre);
        }
        if (mArtwork.mObjectTypes != null && mArtwork.mObjectTypes.size() > 0) {
            classificationGenreObjectTypeList.addAll(mArtwork.mObjectTypes);
        }
        String classificationGenreObjectTypeString =
                StringUtil.joinListIntoString(classificationGenreObjectTypeList, ", ");
        if (classificationGenreObjectTypeString != null) {
            String string = getString(R.string.artwork_profile_genre_string) + " " +
                    classificationGenreObjectTypeString;
            mGenre.setText(string);
        } else {
            mGenre.setVisibility(View.GONE);
        }

        List<String> materialsList = new ArrayList<>();
        if (mArtwork.mMaterials != null && mArtwork.mMaterials.size() > 0) {
            materialsList.addAll(mArtwork.mMaterials);
        }
        String materialsString = StringUtil.joinListIntoString(materialsList, ", ");
        if (materialsString!= null) {
            String string = getString(R.string.artwork_profile_materials_string) + " " +
                    materialsString;
            mMaterials.setText(string);
        } else {
            mMaterials.setVisibility(View.GONE);
        }

        if (mArtwork.mCuratorialComment!= null) {
            String string = mArtwork.mCuratorialComment;
            if (mArtwork.mCurator != null) {
                string += "\n-- " + mArtwork.mCurator;
            }
            mCuratorialComment.setText(string);
        } else {
            mCuratorialComment.setVisibility(View.GONE);
        }

        if (mArtwork.mTags != null) {
            mTags.setText(getSpannableStringOfTags());
            mTags.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mTags.setVisibility(View.GONE);
            mSuggestedArtworksScrollview.setVisibility(View.GONE);
        }

        mSuggestedArtworksScrollview.setVisibility(View.GONE);
    }

    private void setTypefaces() {
        mArtist.setTypeface(mGoudyStMRegularTypeface);
        mTitle.setTypeface(mGoudyStMItalicTypeface);
        mYear.setTypeface(mGoudyStMRegularTypeface);
        mCategory.setTypeface(mGoudyStMRegularTypeface);
        mGenre.setTypeface(mGoudyStMRegularTypeface);
        mMaterials.setTypeface(mGoudyStMRegularTypeface);
        mCuratorialComment.setTypeface(mGoudyStMRegularTypeface);
        mTagsLabel.setTypeface(mGoudyStMRegularTypeface);
        mTags.setTypeface(mGoudyStMRegularTypeface);
    }

    @Override
    public void onBitmapFetched(Bitmap bitmap, @Nullable String encodedTagName) {
        if (bitmap != null) {
            mImageViewAndLoadingScreen.setImageView(bitmap);
        } else {
            Log.e("ArtworkProfileActivity", "bitmap could not be fetched");
        }
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
        HashMap<String, SimpleTag> suggestedArtworksHashMap =
                ((OnStartFetchHandler) this.getApplication()).getSuggestedArtworkHashMap();
        int startIndex = 0;
        for (Tag tag : mArtwork.mTags) {
            String tagName = tag.mName;
            String shortenedTagName = StringUtil.shorten(tagName);
            String encodedTagName = StringUtil.getEncodedFirebasePath(tagName);
            ClickableSpan clickableSpan = getClickableSpan(encodedTagName, suggestedArtworksHashMap);
            spannableString.setSpan(
                    clickableSpan,
                    startIndex,
                    startIndex + shortenedTagName.length() + 4,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            RoundedBackgroundSpan roundedBackgroundSpan = getRoundedBackgroundSpan(
                    encodedTagName,
                    suggestedArtworksHashMap);
            spannableString.setSpan(
                    roundedBackgroundSpan,
                    startIndex,
                    startIndex + shortenedTagName.length() + 4,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(
                    new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    startIndex,
                    startIndex + shortenedTagName.length() + 4,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            startIndex += shortenedTagName.length() + 5;
        }
        return spannableString;
    }

    private ClickableSpan getClickableSpan(
            final String encodedTagName,
            final HashMap<String, SimpleTag> suggestedArtworksHashMap) {
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                if (suggestedArtworksHashMap == null || suggestedArtworksHashMap.get(encodedTagName) == null) {

                } else {
                    mLastClickedEncodedTagName = encodedTagName;
                    cancelPendingSuggestedArtworkBitmapDownloadTasks();

                    List<SimpleTag.SimpleArtwork> suggestedArtworks =
                            suggestedArtworksHashMap.get(encodedTagName).suggestedArtworks;
                    showSuggestedArtworksPlaceholder(suggestedArtworks);
                    for (int position = 0; position < suggestedArtworks.size(); position++) {
                        DownloadImageAsyncTask downloadImageAsyncTask = new DownloadImageAsyncTask(
                                getDownloadSuggestedArtworkImageCallback(position),
                                encodedTagName);
                        mPendingSuggestedArtworkBitmapsDownloadTasks.add(downloadImageAsyncTask);
                        downloadImageAsyncTask.execute(suggestedArtworks.get(position).thumbUrl);
                    }
                }
            }
        };
    }

    private void cancelPendingSuggestedArtworkBitmapDownloadTasks() {
        if (mPendingSuggestedArtworkBitmapsDownloadTasks != null) {
            for (DownloadImageAsyncTask task : mPendingSuggestedArtworkBitmapsDownloadTasks) {
                task.cancel(true);
            }
        }
        mPendingSuggestedArtworkBitmapsDownloadTasks = new ArrayList<>();
    }

    private DownloadImageAsyncTask.Callback getDownloadSuggestedArtworkImageCallback (final int position) {
        return new DownloadImageAsyncTask.Callback() {
            @Override
            public void onBitmapFetched(Bitmap bitmap, @Nullable String encodedTagName) {
                if (encodedTagName != null && encodedTagName.equals(mLastClickedEncodedTagName)) {
                    mSuggestedArtworksBitmaps.set(position, bitmap);
                    updateSuggestedArtworks();
                }
            }
        };
    }

    private void updateSuggestedArtworks() {
        mSuggestedArtworksAdapter.updateBitmaps(mSuggestedArtworksBitmaps);
        mSuggestedArtworksScrollview.setAdapter(mSuggestedArtworksAdapter);
    }

    private RoundedBackgroundSpan getRoundedBackgroundSpan(
            String encodedTagName,
            HashMap<String, SimpleTag> suggestedArtworksHashMap) {
        if (encodedTagName != null && suggestedArtworksHashMap != null
                && suggestedArtworksHashMap.get(encodedTagName) != null
                && suggestedArtworksHashMap.get(encodedTagName).suggestedArtworks != null) {
            List<SimpleTag.SimpleArtwork> suggestedArtworks =
                    suggestedArtworksHashMap.get(encodedTagName).suggestedArtworks;
            int suggestedArtworksCount = suggestedArtworks.size();
            if (suggestedArtworksCount < 3) {
                return new RoundedBackgroundSpan(this, R.color.indigo_50, R.color.grey_800);
            } else if (suggestedArtworksCount < 25) {
                return new RoundedBackgroundSpan(this, R.color.indigo_100, R.color.grey_800);
            } else if (suggestedArtworksCount < 50) {
                return new RoundedBackgroundSpan(this, R.color.indigo_200, R.color.grey_800);
            } else {
                return new RoundedBackgroundSpan(this, R.color.indigo_300, R.color.grey_800);
            }
        } else {
            return null;
        }
    }

    private void showSuggestedArtworksPlaceholder(List<SimpleTag.SimpleArtwork> suggestedArtworks) {
        List<Bitmap> dummySuggestedArtworksBitmaps = new ArrayList<>();
        for (SimpleTag.SimpleArtwork simpleArtwork : suggestedArtworks) {
            dummySuggestedArtworksBitmaps.add(null);
        }
        mSuggestedArtworksBitmaps = dummySuggestedArtworksBitmaps;
        updateSuggestedArtworks();
        mSuggestedArtworksScrollview.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClicked(int position) {
        HashMap<String, SimpleTag> suggestedArtworksHashMap =
                ((OnStartFetchHandler) this.getApplication()).getSuggestedArtworkHashMap();
        List<SimpleTag.SimpleArtwork> suggestedArtworks =
                suggestedArtworksHashMap.get(mLastClickedEncodedTagName).suggestedArtworks;
        int id = suggestedArtworks.get(position).id;

        HashMap<Integer, Artwork> artworks =
                ((OnStartFetchHandler) this.getApplication()).getArtworks();
        Artwork artwork = artworks.get(id);
        launchNewArtworkProfile(artwork);
    }

    private void launchNewArtworkProfile(Artwork artwork) {
        Bundle extras = new Bundle();
        extras.putParcelable(ArtworkProfileActivity.ARTWORK_KEY, artwork);
        Intent intent = new Intent(this, ArtworkProfileActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

}
