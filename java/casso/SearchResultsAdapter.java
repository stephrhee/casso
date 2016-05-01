package casso;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import casso.model.Artwork;
import casso.util.StringUtil;

import com.casso.R;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchResultsAdapter extends ArrayAdapter<Artwork> {

    public enum Type {
        ARTIST, TITLE;
    }

    private static final int mRowViewLayoutResourceId = R.layout.search_result_row_view;

    private final Context mContext;
    private final SearchResultsFilter mFilter = new SearchResultsFilter();
    private final List<Artwork> mArtworks;
    private final Type mType;
    private final HashMap<Artwork, List<String>> mSearchableStrings;
    private List<Artwork> mFilteredArtworks;

    public SearchResultsAdapter(
            Context context,
            List<Artwork> artworks,
            Type type) {
        super(context, mRowViewLayoutResourceId, artworks);
        mContext = context;
        mArtworks = artworks;
        mType = type;
        Preconditions.checkArgument(mType == Type.ARTIST || mType == Type.TITLE);
        mSearchableStrings = new HashMap<>();
        for (Artwork artwork : mArtworks) {
            if (mType == Type.ARTIST) {
                mSearchableStrings.put(artwork, StringUtil.getListOfSplitWords(artwork.mArtist));
            } else if (mType == Type.TITLE) {
                mSearchableStrings.put(artwork, StringUtil.getListOfSplitWords(artwork.mTitle));
            }
        }
        mFilteredArtworks = artworks;
    }

    @Override
    public int getCount() {
        return mFilteredArtworks.size();
    }

    @Override
    public Artwork getItem(int position) {
        return mFilteredArtworks.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SearchResultHolder searchResultHolder;
        if(view == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            view = inflater.inflate(mRowViewLayoutResourceId, parent, false);

            searchResultHolder = new SearchResultHolder();
            searchResultHolder.textView = (TextView) view;
            view.setTag(searchResultHolder);
        } else {
            searchResultHolder = (SearchResultHolder) view.getTag();
        }
        Artwork artwork = mFilteredArtworks.get(position);
        if (mType == Type.ARTIST) {
            searchResultHolder.textView.setText(artwork.mArtist);
        } else if (mType == Type.TITLE) {
            searchResultHolder.textView.setText(artwork.mTitle);
        }
        return view;
    }

    private static class SearchResultHolder {
        TextView textView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class SearchResultsFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = mArtworks;
                results.count = mArtworks.size();
            } else {
                String filter = constraint.toString().toLowerCase();
                List<Artwork> filteredArtworks = new ArrayList<>();

                for (Artwork artwork : mArtworks) {
                    List<String> searchableStrings = mSearchableStrings.get(artwork);
                    for (String string : searchableStrings) {
                        if (string.toLowerCase().startsWith(filter)) {
                            filteredArtworks.add(artwork);
                            break;
                        }
                    }
                }
                results.values = filteredArtworks;
                results.count = filteredArtworks.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mFilteredArtworks = convertObjectsToListOfArtworks(results.values);
            notifyDataSetChanged();
        }
    }

    private List<Artwork> convertObjectsToListOfArtworks(Object list) {
        if (!(list instanceof List)) {
            throw new RuntimeException("SearchResultsAdapter: object was not list");
        }
        List<Artwork> listOfArtworks = new ArrayList<>();
        for (Object object : (List) list) {
            if (!(object instanceof Artwork)) {
                throw new RuntimeException("SearchResultsAdapter: object was not Artwork");
            } else {
                listOfArtworks.add((Artwork) object);
            }
        }
        return listOfArtworks;
    }

}
