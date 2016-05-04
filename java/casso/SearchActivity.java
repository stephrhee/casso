package casso;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.*;

import casso.http.OnStartFetchHandler;
import casso.model.Artwork;

import casso.util.FontUtil;
import com.casso.R;
import com.google.common.base.Preconditions;

import java.util.*;

public class SearchActivity extends FragmentActivity {

    private List<Artwork> mArtworksSortedByArtists;

    private Artwork mClickedArtwork;

    private EditText mArtistSearchField;
    private EditText mTitleSearchField;
    private ListView mSearchResultsListView;

    private Typeface mGoudyStMRegularTypeface;

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

        mGoudyStMRegularTypeface = FontUtil.getTypeface(
                this,
                FontUtil.mGoudyStMRegularTypefaceString);
    }

    private void initSearchResults() {
        mArtworksSortedByArtists = new ArrayList<>(
                ((OnStartFetchHandler) getApplication()).getArtworks().values());
        Collections.sort(mArtworksSortedByArtists, new Comparator<Artwork>() {
            @Override
            public int compare(Artwork lhs, Artwork rhs) {
                Preconditions.checkArgument(lhs.mArtist != null && rhs.mArtist != null);
                return lhs.mArtist.compareTo(rhs.mArtist);
            }
        });
    }

    private void initViews() {
        mArtistSearchField = (EditText) findViewById(R.id.search_artist_search_field);
        mTitleSearchField = (EditText) findViewById(R.id.search_title_search_field);
        mArtistSearchField.setHint(R.string.search_artist_question_string);
        mTitleSearchField.setHint(R.string.search_title_question_string);
        mArtistSearchField.setTypeface(mGoudyStMRegularTypeface);
        mTitleSearchField.setTypeface(mGoudyStMRegularTypeface);

        mSearchResultsListView = (ListView) findViewById(R.id.search_results_list_view);
        showOrHideSearchResultsBasedOnEditText();

        final SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(
                this, mArtworksSortedByArtists);
        mSearchResultsListView.setAdapter(searchResultsAdapter);

        TextWatcher textWatcher = getTextWatcher(searchResultsAdapter);
        mArtistSearchField.addTextChangedListener(textWatcher);
        mTitleSearchField.addTextChangedListener(textWatcher);

        mArtistSearchField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        mTitleSearchField.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        mArtistSearchField.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_NEXT) {
                            mTitleSearchField.requestFocus();
                            return true;
                        }
                        return false;
                    }
                });
        mTitleSearchField.setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                            doSearch();
                            return true;
                        }
                        return false;
                    }
                });

        mSearchResultsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mClickedArtwork = searchResultsAdapter.getItem(position);
//                mArtistSearchField.setText(searchResultsAdapter.getItem(position).mArtist);
//                mTitleSearchField.setText(searchResultsAdapter.getItem(position).mTitle);
                doSearch();
            }
        });
    }

    private void doSearch() {
        if (mClickedArtwork != null) {
            Bundle extras = new Bundle();
            extras.putParcelable(ArtworkProfileActivity.ARTWORK_KEY, mClickedArtwork);
            Intent intent = new Intent(this, ArtworkProfileActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        } else {

        }
    }

    private TextWatcher getTextWatcher(final SearchResultsAdapter adapter) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                showOrHideSearchResultsBasedOnEditText();
                adapter.setArtistConstraint(mArtistSearchField.getText().toString());
                adapter.setTitleConstraint(mTitleSearchField.getText().toString());
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private void showOrHideSearchResultsBasedOnEditText() {
        if (mArtistSearchField.getText().length() > 0 || mTitleSearchField.getText().length() > 0) {
            mSearchResultsListView.setVisibility(View.VISIBLE);
        } else {
            mSearchResultsListView.setVisibility(View.GONE);
        }
    }

}
