package casso.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class CenterLockHorizontalScrollviewAdapter extends ArrayAdapter<Bitmap> {

    private final Context mContext;
    private final int mRowViewResourceId;
    private List<Bitmap> mBitmaps;

    public CenterLockHorizontalScrollviewAdapter(
            Context context,
            int rowViewResourceId,
            List<Bitmap> bitmaps) {
        super(context, rowViewResourceId, bitmaps);
        mContext = context;
        mRowViewResourceId = rowViewResourceId;
        mBitmaps = bitmaps;
    }

    public void updateBitmaps(List<Bitmap> bitmaps) {
        mBitmaps = bitmaps;
        notifyDataSetChanged();
    }

    @Override
    public Bitmap getItem(int position) {
        return mBitmaps.get(position);
    }

    @Override
    public int getCount() {
        return mBitmaps.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View imageView = convertView;
        Holder holder;

        if (imageView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            imageView = inflater.inflate(mRowViewResourceId, parent, false);
            holder = new Holder();
            holder.squareImageView = (SquareImageView) imageView;
            imageView.setTag(holder);
        } else {
            holder = (Holder) imageView.getTag();
        }

        /**
         * Not a placeholder. Has a real image.
         */
        if (mBitmaps.get(position) != null) {
            holder.squareImageView.setImageBitmap(mBitmaps.get(position));
        }

        return imageView;
    }

    private class Holder {
        public SquareImageView squareImageView;
    }

}
