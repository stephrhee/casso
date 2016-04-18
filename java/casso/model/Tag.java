package casso.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

public class Tag implements Parcelable {

    public final String mName;
    public final int mCount;
    public final List<Integer> mIdsWithThisTag;

    private Tag(
            String tag,
            int count,
            List<Integer> idsWithThisTag) {
        Preconditions.checkArgument(tag != null);
        mName = tag;
        mCount = count;
        mIdsWithThisTag = idsWithThisTag;
    }

    public static class Builder {
        private String mName;
        private int mCount;
        private List<Integer> mIdsWithThisTag;

        public Builder setName(String name) {
            mName = name;
            return this;
        }

        public Builder setCount(int count) {
            mCount = count;
            return this;
        }

        public Builder setIdsWithThisTag(List<Integer> idsWithThisTag) {
            mIdsWithThisTag = idsWithThisTag;
            return this;
        }

        public Builder fromOld(Tag oldTag) {
            mName = oldTag.mName;
            mCount = oldTag.mCount;
            mIdsWithThisTag = oldTag.mIdsWithThisTag;
            return this;
        }

        public Tag build() {
            return new Tag(mName, mCount, mIdsWithThisTag);
        }
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mName);
        out.writeInt(mCount);
        out.writeList(mIdsWithThisTag);
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
        mIdsWithThisTag = new ArrayList<>();
        in.readList(mIdsWithThisTag, Integer.class.getClassLoader());
    }

}
