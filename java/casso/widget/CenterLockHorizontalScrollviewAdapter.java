package casso.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by stephrhee on 4/11/16.
 */

public class CenterLockHorizontalScrollviewAdapter extends ArrayAdapter<Drawable> {

    private final Context mContext;
    private final int mRowViewResourceId;
    private List<Drawable> mDrawableList;

    public CenterLockHorizontalScrollviewAdapter(
            Context context,
            int rowViewResourceId,
            List<Drawable> drawableList) {
        super(context, rowViewResourceId, drawableList);
        mContext = context;
        mRowViewResourceId = rowViewResourceId;
        mDrawableList = drawableList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        Holder holder;

        if (rowView == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            rowView = inflater.inflate(mRowViewResourceId, parent, false);
            holder = new Holder();
            holder.imageView = (ImageView) rowView;
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        holder.imageView.setImageDrawable(mDrawableList.get(position));

        return rowView;
    }

    private class Holder {
        public ImageView imageView;
    }

}
