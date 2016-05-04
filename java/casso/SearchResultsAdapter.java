package casso;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import casso.model.Artwork;
import casso.util.FontUtil;
import casso.util.StringUtil;

import com.casso.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SearchResultsAdapter extends ArrayAdapter<Artwork> {

    private String mArtistConstraint;
    private String mTitleConstraint;

    private static final int mRowViewLayoutResourceId = R.layout.search_result_row_view;

    private final Context mContext;
    private final SearchResultsFilter mFilter = new SearchResultsFilter();
    private final List<Artwork> mArtworks;
    private final HashMap<Artwork, List<String>> mSearchableArtistStrings = new HashMap<>();
    private final HashMap<Artwork, List<String>> mSearchableTitleStrings = new HashMap<>();
    private List<Artwork> mFilteredArtworks;
    private final Typeface mGoudyStMRegularTypeface;
    private final Typeface mGoudyStMItalicTypeface;

    public SearchResultsAdapter(
            Context context,
            List<Artwork> artworks) {
        super(context, mRowViewLayoutResourceId, artworks);
        mContext = context;
        mArtworks = artworks;
        for (Artwork artwork : mArtworks) {
            mSearchableArtistStrings.put(artwork, StringUtil.getSearchableStrings(artwork.mArtist));
            mSearchableTitleStrings.put(artwork, StringUtil.getSearchableStrings(artwork.mTitle));
        }
        mFilteredArtworks = artworks;

        mGoudyStMRegularTypeface = FontUtil.getTypeface(
                context,
                FontUtil.mGoudyStMRegularTypefaceString);
        mGoudyStMItalicTypeface = FontUtil.getTypeface(
                context,
                FontUtil.mGoudyStMItalicTypefaceString);
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
            searchResultHolder.artistTextView =
                    (TextView) view.findViewById(R.id.search_result_row_view_artist);
            searchResultHolder.titleTextView =
                    (TextView) view.findViewById(R.id.search_result_row_view_title);
            view.setTag(searchResultHolder);
        } else {
            searchResultHolder = (SearchResultHolder) view.getTag();
        }
        Artwork artwork = mFilteredArtworks.get(position);
        searchResultHolder.artistTextView.setText(artwork.mArtist);
        searchResultHolder.titleTextView.setText(artwork.mTitle);

        searchResultHolder.artistTextView.setTypeface(mGoudyStMRegularTypeface);
        searchResultHolder.titleTextView.setTypeface(mGoudyStMItalicTypeface);

        return view;
    }

    private static class SearchResultHolder {
        TextView artistTextView;
        TextView titleTextView;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public void setArtistConstraint(String artistConstraint) {
        mArtistConstraint = artistConstraint;
    }

    public void setTitleConstraint(String titleConstraint) {
        mTitleConstraint = titleConstraint;
    }

    private class SearchResultsFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if ((mArtistConstraint == null || mArtistConstraint.length() == 0)
                    && (mTitleConstraint == null || mTitleConstraint.length() == 0)) {
                results.values = mArtworks;
                results.count = mArtworks.size();
            } else {
                List<String> artistConstraints = Arrays.asList(
                        mArtistConstraint.toLowerCase().split("\\s+"));
                List<String> titleConstraints = Arrays.asList(
                        mTitleConstraint.toLowerCase().split("\\s+"));
                List<Artwork> filteredArtworks = getFilteredArtworks(
                        mArtworks,
                        artistConstraints,
                        mSearchableArtistStrings);
                filteredArtworks = getFilteredArtworks(
                        filteredArtworks,
                        titleConstraints,
                        mSearchableTitleStrings);
                results.values = filteredArtworks;
                results.count = filteredArtworks.size();
            }
            return results;
        }

        private List<Artwork> getFilteredArtworks(
                List<Artwork> artworks,
                List<String> constraints,
                HashMap<Artwork, List<String>> searchableStringsHashMap) {
            List<Artwork> filteredArtworks = new ArrayList<>();
            for (Artwork artwork : artworks) {
                List<String> searchableStrings = searchableStringsHashMap.get(artwork);
                boolean doesHaveAllConstraints = true;
                for (String constraint : constraints) {
                    boolean doesHaveThisConstraint = false;
                    for (String searchableStringWord : searchableStrings) {
                        if (searchableStringWord.toLowerCase().startsWith(constraint.toLowerCase())) {
                            doesHaveThisConstraint = true;
                            break;
                        }
                    }
                    doesHaveAllConstraints = doesHaveAllConstraints && doesHaveThisConstraint;
                }
                if (doesHaveAllConstraints) {
                    filteredArtworks.add(artwork);
                }
            }
            return filteredArtworks;
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
