package casso.widget;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class ColorPaletteAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private final int mRowViewResourceId;
    private List<Integer> mColors;

    private int mBlockWidth;

    public ColorPaletteAdapter(
            Context context,
            int rowViewResourceId,
            List<Integer> colors,
            int listViewWidthPixels) {
        super(context, rowViewResourceId, colors);
        mContext = context;
        mRowViewResourceId = rowViewResourceId;
        mColors = colors;
        mBlockWidth = listViewWidthPixels / mColors.size();
    }

    @Override
    public Integer getItem(int position) {
        return mColors.get(position);
    }

    @Override
    public int getCount() {
        return mColors.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder;
        if (view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(mRowViewResourceId, parent, false);
            holder = new Holder();
            holder.view = view;
            holder.view.setLayoutParams(new ViewGroup.LayoutParams(
                    mBlockWidth,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.view.setBackgroundColor(mColors.get(position));
        return view;
    }

    private class Holder {
        public View view;
    }

}
