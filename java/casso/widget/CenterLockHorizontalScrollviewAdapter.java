package casso.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import com.casso.R;

import java.util.List;

public class CenterLockHorizontalScrollviewAdapter extends ArrayAdapter<Bitmap> {

    private final Context mContext;
    private final OnItemClickListener mOnItemClickListener;
    private final int mRowViewResourceId;
    private List<Bitmap> mBitmaps;

    private int mMarginDim;

    public CenterLockHorizontalScrollviewAdapter(
            Context context,
            OnItemClickListener onItemClickListener,
            int rowViewResourceId,
            List<Bitmap> bitmaps) {
        super(context, rowViewResourceId, bitmaps);
        mContext = context;
        mOnItemClickListener = onItemClickListener;
        mRowViewResourceId = rowViewResourceId;
        mBitmaps = bitmaps;

        mMarginDim = mContext.getResources()
                .getDimensionPixelSize(R.dimen.artwork_profile_outer_box_margin);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageViewAndLoadingScreen view = (ImageViewAndLoadingScreen) convertView;
        Holder holder;

        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = (ImageViewAndLoadingScreen) inflater.inflate(mRowViewResourceId, parent, false);
            holder = new Holder();
            holder.view = view;

            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.view.getLayoutParams();
            if (position == mBitmaps.size() - 1) {
                layoutParams.setMargins(0, 0, 0, 0);
            } else {
                layoutParams.setMargins(0, 0, mMarginDim, 0);
            }
            holder.view.setLayoutParams(layoutParams);

            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClicked(position);
                }
            });

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

    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

}
