package casso.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import casso.util.StringUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class Artwork implements Parcelable {

    public final Integer mId;
    public final String mTitle;
    public final String mArtist;
    public final @Nullable String mYear;
    public final @Nullable String mHighResImageUrl;
    public final @Nullable String mLowResImageUrl;
    public final @Nullable Bitmap mImageBitmap;
    public final @Nullable String mCategory;
    public final @Nullable List<String> mObjectTypes;
    public final @Nullable String mGenre;
    public final @Nullable String mClassification;
    public final @Nullable Integer mStartYear;
    public final @Nullable Integer mEndYear;
    public final @Nullable List<String> mMaterials;
    public final @Nullable String mCurator;
    public final @Nullable String mCuratorialComment;
    public final @Nullable List<Tag> mTags;

    private Artwork(
            Integer id,
            String title,
            String artist,
            @Nullable String year,
            @Nullable String highResImageUrl,
            @Nullable String lowResImageUrl,
            @Nullable Bitmap imageBitmap,
            @Nullable String category,
            @Nullable List<String> objectTypes,
            @Nullable String genre,
            @Nullable String classification,
            @Nullable Integer startYear,
            @Nullable Integer endYear,
            @Nullable List<String> materials,
            @Nullable String curator,
            @Nullable String curatorialComment,
            @Nullable List<Tag> tags) {
        Preconditions.checkArgument(id != null && title != null && artist != null);
        mId = id;
        mTitle = title;
        mArtist = artist;
        mYear = year;
        mHighResImageUrl = highResImageUrl;
        mLowResImageUrl = lowResImageUrl;
        mImageBitmap = imageBitmap;
        mCategory = category;
        mObjectTypes = objectTypes;
        mGenre = genre;
        mClassification = classification;
        mStartYear = startYear;
        mEndYear = endYear;
        mMaterials = materials;
        mCurator = curator;
        mCuratorialComment = curatorialComment;
        mTags = tags;
    }

    public String getTagsAsOneString() {
        if (mTags != null) {
            List<String> tagNames = Lists.transform(mTags, StringUtil.tagsToTagStrings);
            return Joiner.on(" ").join(tagNames);
        } else {
            return null;
        }
    }

    public String getYearRange() {
        if (mStartYear == null && mEndYear == null) {
            return null;
        } else if (mStartYear == null) {
            return mEndYear.toString();
        } else if (mEndYear == null) {
            return mStartYear.toString();
        } else if (mStartYear >= mEndYear) {
            return mStartYear.toString();
        } else {
            return mStartYear.toString() + " - " + mEndYear.toString();
        }
    }

    public static class Builder {
        private Integer mId;
        private String mTitle;
        private String mArtist;
        private @Nullable String mYear;
        private @Nullable String mHighResImageUrl;
        private @Nullable String mLowResImageUrl;
        private @Nullable Bitmap mImageBitmap;
        private @Nullable String mCategory;
        private @Nullable List<String> mObjectTypes;
        private @Nullable String mGenre;
        private @Nullable String mClassification;
        private @Nullable Integer mStartYear;
        private @Nullable Integer mEndYear;
        private @Nullable List<String> mMaterials;
        private @Nullable String mCurator;
        private @Nullable String mCuratorialComment;
        private @Nullable List<Tag> mTags;

        public Builder setId(Integer id) {
            mId = id;
            return this;
        }

        public Builder setTitle(String title) {
            mTitle = title;
            return this;
        }

        public Builder setArtist(String artist) {
            mArtist = artist;
            return this;
        }

        public Builder setYear(String year) {
            mYear = year;
            return this;
        }

        public Builder setHighResImageUrl(String highResImageUrl) {
            mHighResImageUrl = highResImageUrl;
            return this;
        }

        public Builder setLowResImageUrl(String lowResImageUrl) {
            mLowResImageUrl = lowResImageUrl;
            return this;
        }

        public Builder setImageBitmap(Bitmap imageBitmap) {
            mImageBitmap = imageBitmap;
            return this;
        }

        public Builder setCategory(String category) {
            mCategory = category;
            return this;
        }

        public Builder setObjectTypes(List<String> objectTypes) {
            mObjectTypes = objectTypes;
            return this;
        }

        public Builder setGenre(String genre) {
            mGenre = genre;
            return this;
        }

        public Builder setClassification(String classification) {
            mClassification = classification;
            return this;
        }

        public Builder setStartYear(Integer startYear) {
            mStartYear = startYear;
            return this;
        }

        public Builder setEndYear(Integer endYear) {
            mEndYear = endYear;
            return this;
        }

        public Builder setMaterials(List<String> materials) {
            mMaterials = materials;
            return this;
        }

        public Builder setCurator(String curator) {
            mCurator = curator;
            return this;
        }

        public Builder setCuratorialComment(String curatorialComment) {
            mCuratorialComment = curatorialComment;
            return this;
        }

        public Builder setTags(List<Tag> tags) {
            mTags = tags;
            return this;
        }

        public Builder fromOld(Artwork oldArtwork) {
            mId = oldArtwork.mId;
            mTitle = oldArtwork.mTitle;
            mArtist = oldArtwork.mArtist;
            mYear = oldArtwork.mYear;
            mHighResImageUrl = oldArtwork.mHighResImageUrl;
            mLowResImageUrl = oldArtwork.mLowResImageUrl;
            mImageBitmap = oldArtwork.mImageBitmap;
            mCategory = oldArtwork.mCategory;
            mObjectTypes = oldArtwork.mObjectTypes;
            mGenre = oldArtwork.mGenre;
            mClassification = oldArtwork.mClassification;
            mStartYear = oldArtwork.mStartYear;
            mEndYear = oldArtwork.mEndYear;
            mMaterials = oldArtwork.mMaterials;
            mCurator = oldArtwork.mCurator;
            mCuratorialComment = oldArtwork.mCuratorialComment;
            mTags = oldArtwork.mTags;
            return this;
        }

        public Artwork build() {
            return new Artwork(
                    mId,
                    mTitle,
                    mArtist,
                    mYear,
                    mHighResImageUrl,
                    mLowResImageUrl,
                    mImageBitmap,
                    mCategory,
                    mObjectTypes,
                    mGenre,
                    mClassification,
                    mStartYear,
                    mEndYear,
                    mMaterials,
                    mCurator,
                    mCuratorialComment,
                    mTags);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(mId);
        out.writeString(mTitle);
        out.writeString(mArtist);
        out.writeString(mYear);
        out.writeString(mHighResImageUrl);
        out.writeString(mLowResImageUrl);
        out.writeParcelable(mImageBitmap, flags);
        out.writeString(mCategory);
        out.writeList(mObjectTypes);
        out.writeString(mGenre);
        out.writeString(mClassification);
        out.writeInt(mStartYear == null ? -1 : mStartYear);
        out.writeInt(mEndYear == null ? -1 : mEndYear);
        out.writeList(mMaterials);
        out.writeString(mCurator);
        out.writeString(mCuratorialComment);
        out.writeList(mTags);
    }

    public static final Parcelable.Creator<Artwork> CREATOR = new Parcelable.Creator<Artwork>() {
        public Artwork createFromParcel(Parcel in) {
            return new Artwork(in);
        }

        public Artwork[] newArray(int size) {
            return new Artwork[size];
        }
    };

    private Artwork(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mArtist = in.readString();
        mYear = in.readString();
        mLowResImageUrl = in.readString();
        mHighResImageUrl = in.readString();
        mImageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
        mCategory = in.readString();
        mObjectTypes = new ArrayList<String>();
        in.readList(mObjectTypes, String.class.getClassLoader());
        mGenre = in.readString();
        mClassification = in.readString();
        int startYear = in.readInt();
        int endYear = in.readInt();
        mStartYear = startYear == -1 ? null : startYear;
        mEndYear = endYear == -1 ? null : endYear;
        mMaterials = new ArrayList<String>();
        in.readList(mMaterials, String.class.getClassLoader());
        mCurator = in.readString();
        mCuratorialComment = in.readString();
        mTags = new ArrayList<Tag>();
        in.readList(mTags, Tag.class.getClassLoader());
    }

}
