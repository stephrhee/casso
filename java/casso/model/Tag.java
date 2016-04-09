package casso.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Tag implements Parcelable {

    public final String mName;
    public final int mCount;

    private Tag(
            String tag,
            int count) {
        mName = tag;
        mCount = count;
    }

    public static class Builder {
        private String mName;
        private int mCount;

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setCount(int count) {
            mCount = count;
            return this;
        }

        public Builder fromOld(Tag oldTag) {
            mName = oldTag.mName;
            mCount = oldTag.mCount;
            return this;
        }

        public Tag build() {
            return new Tag(mName, mCount);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeInt(mCount);
    }

    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    private Tag(Parcel in) {
        mName = in.readString();
        mCount = in.readInt();
    }

}
