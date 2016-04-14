package casso.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import casso.model.Artwork;

import java.util.List;

public class CenterLockHorizontalScrollviewAdapter extends ArrayAdapter<Artwork> {

    private final Context mContext;
    private final int mRowViewResourceId;
    private List<Artwork> mArtworks;

    public CenterLockHorizontalScrollviewAdapter(
            Context context,
            int rowViewResourceId,
            List<Artwork> artworks) {
        super(context, rowViewResourceId, artworks);
        mContext = context;
        mRowViewResourceId = rowViewResourceId;
        mArtworks = artworks;
    }

    @Override
    public Artwork getItem(int position) {
        return mArtworks.get(position);
    }

    @Override
    public int getCount() {
        return mArtworks.size();
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
        holder.squareImageView.setImageBitmap(mArtworks.get(position).mImageBitmap);

        return imageView;
    }

    private class Holder {
        public SquareImageView squareImageView;
    }

}
