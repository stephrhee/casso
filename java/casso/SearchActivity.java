package casso;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import casso.http.OnStartFetchHandler;
import casso.model.Artwork;

import com.casso.R;
import com.google.common.base.Preconditions;

import java.util.*;

public class SearchActivity extends FragmentActivity {

    private List<Artwork> mArtworksSortedByArtists;
    private List<Artwork> mArtworksSortedByTitles;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_layout);

        OnStartFetchHandler.fetchSuggestedArtworks(this, this);
        OnStartFetchHandler.fetchObjectIds(this, this);
        OnStartFetchHandler.fetchArtworks(
                this,
                this,
                new OnStartFetchHandler.SetArtworksCallback() {
                    @Override
                    public void onArtworksSet() {
                        initSearchResults();
                        initViews();
                    }
                });
    }

    private void initSearchResults() {
        mArtworksSortedByArtists = new ArrayList<>(
                ((OnStartFetchHandler) getApplication()).getArtworks().values());
        mArtworksSortedByTitles = new ArrayList<>(
                ((OnStartFetchHandler) getApplication()).getArtworks().values());
        Collections.sort(mArtworksSortedByArtists, new Comparator<Artwork>() {
            @Override
            public int compare(Artwork lhs, Artwork rhs) {
                Preconditions.checkArgument(lhs.mArtist != null && rhs.mArtist != null);
                return lhs.mArtist.compareTo(rhs.mArtist);
            }
        });
        Collections.sort(mArtworksSortedByTitles, new Comparator<Artwork>() {
            @Override
            public int compare(Artwork lhs, Artwork rhs) {
                Preconditions.checkArgument(lhs.mTitle != null && rhs.mTitle != null);
                return lhs.mTitle.compareTo(rhs.mTitle);
            }
        });
    }

    private void initViews() {
        final EditText artistSearchField = (EditText) findViewById(R.id.search_artist_search_field);
        final EditText titleSearchField = (EditText) findViewById(R.id.search_title_search_field);
        final ListView artistsListView = (ListView) findViewById(R.id.search_artist_results_list_view);
        final ListView titlesListView = (ListView) findViewById(R.id.search_artwork_titles_list_view);

        artistSearchField.setHint(R.string.search_artist_question_string);
        titleSearchField.setHint(R.string.search_title_question_string);

        SearchResultsAdapter artistsAdapter = new SearchResultsAdapter(
                this, mArtworksSortedByArtists, SearchResultsAdapter.Type.ARTIST);
        SearchResultsAdapter titlesAdapter = new SearchResultsAdapter(
                this, mArtworksSortedByTitles, SearchResultsAdapter.Type.TITLE);

        artistsListView.setAdapter(artistsAdapter);
        titlesListView.setAdapter(titlesAdapter);

        artistSearchField.addTextChangedListener(getTextWatcher(
                artistSearchField,
                artistsListView,
                artistsAdapter));
        titleSearchField.addTextChangedListener(getTextWatcher(
                titleSearchField,
                titlesListView,
                titlesAdapter));

        artistSearchField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        titleSearchField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        artistSearchField.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            hideSearchResults(artistsListView);
                            titleSearchField.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
        titleSearchField.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            doSearch();
                            return true;
                        }
                        return false;
                    }
                });

        artistSearchField.setOnFocusChangeListener(getOnFocusChangeListener(
                artistsListView,
                titlesListView,
                artistSearchField));
        titleSearchField.setOnFocusChangeListener(getOnFocusChangeListener(
                titlesListView,
                artistsListView,
                titleSearchField));
    }

    private void doSearch() {
        Artwork artwork = ((OnStartFetchHandler) getApplication()).getArtworks().get(109);
        Bundle extras = new Bundle();
        extras.putParcelable(ArtworkProfileActivity.ARTWORK_KEY, artwork);
        Intent intent = new Intent(this, ArtworkProfileActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private TextWatcher getTextWatcher(
            final EditText editText,
            final ListView listView,
            final SearchResultsAdapter adapter) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showOrHideSearchResultsBasedOnEditText(editText, listView);
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void showOrHideSearchResultsBasedOnEditText(
            EditText editText,
            ListView listView) {
        if (editText.getText().length() > 0) {
            listView.setVisibility(View.VISIBLE);
        } else {
            hideSearchResults(listView);
        }
    }

    private void hideSearchResults(ListView listView) {
        listView.setVisibility(View.GONE);
    }

    private View.OnFocusChangeListener getOnFocusChangeListener(
            final ListView thisSearchResults,
            final ListView otherSearchResults,
            final EditText thisEditText) {
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                hideSearchResults(otherSearchResults);
                showOrHideSearchResultsBasedOnEditText(
                        thisEditText,
                        thisSearchResults);
            }
        };
    }

}
