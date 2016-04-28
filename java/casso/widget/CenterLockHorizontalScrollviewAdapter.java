package casso.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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
        ImageViewAndLoadingScreen view = (ImageViewAndLoadingScreen) convertView;
        Holder holder;

        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = (ImageViewAndLoadingScreen) inflater.inflate(mRowViewResourceId, parent, false);
            holder = new Holder();
            holder.view = view;
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        /**
         * Not a placeholder. Has a real image.
         */
        if (mBitmaps.get(position) != null) {
            holder.view.setImageView(mBitmaps.get(position));
        }

        return view;
    }

    private class Holder {
        public ImageViewAndLoadingScreen view;
    }

}
