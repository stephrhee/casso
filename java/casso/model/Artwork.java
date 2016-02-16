package casso.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.google.common.base.Preconditions;

public class Artwork implements Parcelable {

    public final String mTitle;
    public final String mArtist;
    public final @Nullable String mYear;
    public final @Nullable String mImageUrl;
    public final @Nullable Bitmap mImageBitmap;

    private Artwork(
            String title,
            String artist,
            @Nullable String year,
            @Nullable String imageUrl,
            @Nullable Bitmap imageBitmap) {
        Preconditions.checkArgument(title != null && artist != null);
        mTitle = title;
        mArtist = artist;
        mYear = year;
        mImageUrl = imageUrl;
        mImageBitmap = imageBitmap;
    }

    public static class Builder {
        private String mTitle;
        private String mArtist;
        private @Nullable String mYear;
        private @Nullable String mImageUrl;
        private @Nullable Bitmap mImageBitmap;

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

        public Builder setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
            return this;
        }

        public Builder setImageBitmap(Bitmap imageBitmap) {
            mImageBitmap = imageBitmap;
            return this;
        }

        public Artwork build() {
            return new Artwork(
                    mTitle,
                    mArtist,
                    mYear,
                    mImageUrl,
                    mImageBitmap);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mTitle);
        out.writeString(mArtist);
        out.writeString(mYear);
        out.writeString(mImageUrl);
        out.writeParcelable(mImageBitmap, flags);
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
        mTitle = in.readString();
        mArtist = in.readString();
        mYear = in.readString();
        mImageUrl = in.readString();
        mImageBitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

}
